#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# =============================================================================
# coverage.sh — pipeline TOOLING usage coverage + dead-tooling report.
# Crosses three views to find what is healthy, unexercised, or dead:
#   INVENTORY  — every agent / command / skill / script that exists on disk.
#   WIRING     — is it referenced by the pipeline / contract docs?  (not wired => ORPHAN/dead)
#   USAGE      — has it actually run? evidence = a line in the activity log
#                OR its on-disk artifact (gate reports), so past runs count even
#                if they predate the activity log.
# Output: a coverage summary + DEAD (exists, not wired), GHOST (wired, file
# missing), and NOT-YET-EXERCISED (wired, no evidence — expected for later-phase
# tools). Run any time; bundled into /stage-report at each phase boundary.
#
# USED BY: ALL book types. Tooling health is genre-independent. Per book, edit
# the CONFIG block so the artifact map names ONLY the agents this book's profile
# keeps (a non-code book drops example-builder/code-reviewer/repro-proofer).
#
# Exit: 0 always (informational) unless --strict (then 1 if any DEAD/GHOST).
# =============================================================================
set -uo pipefail   # NOT -e: this is a report script; per-tool test "failures" are normal control flow

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
PIPELINE="00-strategy/PIPELINE.md"          # e.g. 00-strategy/PIPELINE.md (the step authority; scanned for ghost refs)
# WIRED_DOCS — every doc that, if it NAMES a tool, counts that tool as wired
# (it owns or is named by a step or the operating contract). Space-delimited.
WIRED_DOCS="AGENTS.md CLAUDE.md 00-strategy/PIPELINE.md 00-strategy/GUIDELINES-JAVA-QUALITY.md"          # e.g. "00-strategy/PIPELINE.md CLAUDE.md AGENTS.md 00-strategy/READ-MAP.md"
LOG="10-logs/activity.jsonl"                    # e.g. 10-logs/activity.jsonl  (USAGE evidence for agents)
# Tool-directory globs (where each kind of tool lives). Adjust only if your repo
# nests .claude/ differently.
AGENTS_GLOB=".claude/agents/*.md"        # e.g. .claude/agents/*.md
COMMANDS_GLOB=".claude/commands/*.md"    # e.g. .claude/commands/*.md
SKILLS_GLOB=".claude/skills/*"        # e.g. .claude/skills/*/SKILL.md
SCRIPTS_GLOB=".claude/scripts/*.sh"      # e.g. .claude/scripts/*.sh
# Tool DIRECTORIES (for the type-aware wiring scan: a script/agent named by a
# COMMAND or AGENT file counts as wired). Default to each glob's parent dir.
COMMANDS_DIR_REL=".claude/commands"  # e.g. .claude/commands
AGENTS_DIR_REL=".claude/agents"      # e.g. .claude/agents
# ARTIFACT_MAP — for each GATE agent, the on-disk artifact(s) that prove it ran
# (so a past run counts as USAGE even with no log line). One token per agent:
#   agent=glob[;glob...]   (semicolon-separated globs within a token, no spaces).
# Agents that only edit shared state (no signature artifact) are listed with an
# empty value (agent=) and rely on the activity log alone. Newline-separated;
# keep ONLY the agents this book's profile carries.
ARTIFACT_MAP='research:02-research draft:03-drafts approved:04-approved figures:05-figures code:08-companion-code'
# e.g.:
# ARTIFACT_MAP='
# researcher=02-research/*/*_RESEARCH.md
# source-verifier=02-research/*/*_VERIFY.md;03-drafts/*/*_VERIFY.md
# drafter=03-drafts/*/*_v*.md
# example-builder=03-drafts/*/*_EXAMPLE.md
# code-reviewer=03-drafts/*/*_CODEREVIEW.md
# tech-clarity-reviewer=03-drafts/*/*_CLARITY.md
# auditor=03-drafts/*/*_AUDIT.md
# chapter-scorer=03-drafts/*/*_SCORE.md
# reconciler=03-drafts/*/*_RECONCILE.md
# figure-designer=05-figures/*/*.html
# book-maintainer='
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$REPO_ROOT"
STRICT=no; [ "${1:-}" = "--strict" ] && STRICT=yes
dead=0

# wired? = the tool's name is referenced by any WIRED_DOC (it owns or is named by
# a step or the operating contract), and (for scripts/agents/skills) by a command
# or agent that invokes it. Not referenced anywhere => ORPHAN.
# Type-aware to avoid a self-reference false-positive: a command/agent isn't "wired"
# just because its own file names it, so the commands dir is excluded for commands.
wired(){ local name="$1" type="${2:-}" targets="$WIRED_DOCS"
  [ "$type" != command ] && targets="$targets $COMMANDS_DIR_REL"
  [ "$type" = script ] && targets="$targets $AGENTS_DIR_REL"
  grep -rqF -- "$name" $targets 2>/dev/null; }
# logged? = appears as an actor in the activity log
logged(){ [ -f "$LOG" ] && grep -q "\"actor\":\"$1\"" "$LOG" 2>/dev/null; }
# artifact glob(s) for a gate agent (empty => no artifact signal; rely on log/wiring).
# Looks the agent up in ARTIFACT_MAP and prints its globs space-separated.
artifact_glob(){
  printf '%s\n' "$ARTIFACT_MAP" | while IFS= read -r line; do
    line="${line# }"; line="${line% }"; [ -z "$line" ] && continue
    case "$line" in
      "$1="*) printf '%s' "${line#*=}" | tr ';' ' '; return 0;;
    esac
  done
}
has_artifact(){ for g in $(artifact_glob "$1"); do for f in $g; do [ -e "$f" ] && return 0; done; done; return 1; }

report_group(){ # $1=label $2=dir-glob $3=type(agent|command|skill|script)
  local label="$1" glob="$2" type="$3" total=0 wired_n=0 used_n=0 deadl="" unex=""
  for f in $glob; do
    [ -e "$f" ] || continue
    local name; case "$type" in
      skill) name="$(basename "$(dirname "$f")")";;
      *) name="$(basename "$f")"; name="${name%.md}"; name="${name%.sh}";;
    esac
    total=$((total+1))
    local w=no u=no
    if wired "$name" "$type"; then w=yes; wired_n=$((wired_n+1)); fi
    if [ "$type" = "agent" ]; then
      if logged "$name" || has_artifact "$name"; then u=yes; used_n=$((used_n+1)); fi
    fi
    [ "$w" = no ] && deadl="$deadl $name"
    { [ "$w" = yes ] && [ "$u" = no ] && [ "$type" = agent ]; } && unex="$unex $name"
  done
  printf "%-9s total=%-3s wired=%-3s" "$label" "$total" "$wired_n"
  [ "$type" = agent ] && printf " exercised=%-3s" "$used_n"
  printf "\n"
  [ -n "$deadl" ] && { echo "   DEAD/ORPHAN (exists, not referenced by the pipeline/contract docs):$deadl"; dead=1; }
  [ -n "$unex" ] && echo "   not-yet-exercised (wired, no run evidence — expected for later-phase tools):$unex"
}

echo "== COVERAGE — pipeline tooling (inventory x wiring x usage) =="
report_group "agents"   "$AGENTS_GLOB"   agent
report_group "commands" "$COMMANDS_GLOB" command
report_group "skills"   "$SKILLS_GLOB"   skill
report_group "scripts"  "$SCRIPTS_GLOB"  script
echo ""

# GHOST: a script name referenced by the pipeline that has no file on disk.
echo "== GHOST refs (named in the pipeline doc but no matching file) =="
ghost=0
scripts_dir="${SCRIPTS_GLOB%/*}"
for n in $(grep -oE '[a-z][a-z0-9_-]+\.sh' "$PIPELINE" 2>/dev/null | sort -u); do
  [ -f "$scripts_dir/$n" ] || { echo "   ! script referenced but missing: $n"; ghost=1; dead=1; }
done
[ "$ghost" = 0 ] && echo "   none"
echo ""

if [ "$dead" = 0 ]; then echo "COVERAGE: clean — no dead/orphan tooling."
else echo "COVERAGE: dead/orphan or ghost tooling listed above (informational; prune or wire)."; fi
[ "$STRICT" = yes ] && [ "$dead" != 0 ] && exit 1
exit 0
