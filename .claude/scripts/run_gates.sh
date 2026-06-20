#!/usr/bin/env bash
# =============================================================================
# run_gates.sh <chapter-number | dossier-key>  —  the SCRIPTED-ENFORCER runner.
# -----------------------------------------------------------------------------
# Runs the cheap, mechanical, single-file enforcers (the PIPELINE Step-4a
# PRE-CHECK line of defence) on ONE chapter's v1 draft and writes a dated
# _GATELOG.md beside the draft:
#     check_neutrality   (banned comparative/superlative phrasings)   FAIL-hard
#     lint_citations     (citation structure)                         FAIL-hard
#     verify_sources     (MISSING source identifiers/coords)          FAIL-hard
#     check_snippets     (snippet tag-marker integrity)               FLAG
#
# These are the SCRIPTED enforcers — NOT the independent-agent gates
# (source-verifier / tech-clarity / auditor / originality / red-team), which
# must run as their agents on a DIFFERENT model and produce _VERIFY/_CLARITY/
# _AUDIT reports. run_gates proves the mechanical floor; the agents prove the
# rest. A green _GATELOG is necessary, not sufficient.
#
# Usage:  run_gates.sh 17        (chapter number)   or   run_gates.sh 35  (key)
#         run_gates.sh --all     (every drafted chapter)
# Exit:   0 all enforcers clean · 1 at least one FAIL · 2 bad input.
# =============================================================================
set -uo pipefail
REPO="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"; cd "$REPO"
SC=".claude/scripts"; DR="03-drafts"; TR="01-index/CHAPTER-TRACKER.md"

resolve_slug() {  # arg: chapter-number or dossier-key -> prints the draft slug
  local arg="$1" key=""
  # if it matches a chapter-number column in the tracker, map to its dossier key
  key="$(awk -F'|' -v c="$arg" '
      $2+0==c && $2 ~ /^[0-9 ]+$/ {gsub(/ /,"",$3); print $3; exit}' "$TR" 2>/dev/null)"
  [ -z "$key" ] && key="$arg"                       # else treat arg as the key
  key="$(printf '%s' "$key" | tr -d ' ')"
  ls -d "$DR"/${key}_* 2>/dev/null | head -1 | xargs -n1 basename 2>/dev/null
}

run_one() {  # arg: slug
  local slug="$1" v="$DR/$1/$1_v1.md" log="$DR/$1/$1_GATELOG.md" fails=0
  [ -f "$v" ] || { echo "  no v1 draft for $slug — skip"; return 0; }
  local rows=()
  _gate() {  # name script-cmd hard?
    local name="$1"; shift; local hard="$1"; shift
    if [ ! -x "$SC/$1.sh" ] && [ ! -f "$SC/$1.sh" ]; then rows+=("| $name | ⚪ n/a | script absent |"); return; fi
    local out rc
    out="$(bash "$SC/$1.sh" "${@:2}" "$v" 2>&1)"; rc=$?
    if [ "$rc" -eq 0 ]; then rows+=("| $name | 🟢 pass | clean |")
    elif [ "$hard" = "hard" ]; then rows+=("| $name | 🔴 FAIL | $(printf '%s' "$out" | head -1 | cut -c1-90) |"); fails=$((fails+1))
    else rows+=("| $name | 🟡 flag | $(printf '%s' "$out" | head -1 | cut -c1-90) |"); fi
  }
  _gate "neutrality"   hard check_neutrality
  _gate "citations"    flag lint_citations
  _gate "source-trace" flag verify_sources
  _gate "snippets"     flag check_snippets
  { echo "# SCRIPTED-ENFORCER GATELOG — $slug"
    echo ""
    echo "> \`run_gates.sh\` · $(date -u +%Y-%m-%dT%H:%MZ) · the mechanical Step-4a PRE-CHECK."
    echo "> 🟢 pass · 🟡 flag (review) · 🔴 FAIL (blocks) · ⚪ n/a. **Scripted floor only — NOT the independent-agent gates.**"
    echo ""
    echo "| Enforcer | Result | Note |"
    echo "|---|---|---|"
    printf '%s\n' "${rows[@]}"
    echo ""
    echo "_Verdict: $([ $fails -eq 0 ] && echo '🟢 scripted floor clean (independent gates still pending)' || echo "🔴 $fails FAIL — fix before gating")_"
  } > "$log"
  echo "  $slug → $([ $fails -eq 0 ] && echo '🟢 clean' || echo "🔴 $fails FAIL") ($(basename "$log"))"
  return $((fails>0))
}

[ $# -ge 1 ] || { echo "usage: run_gates.sh <chapter-number | dossier-key | --all>" >&2; exit 2; }

total_fail=0
if [ "$1" = "--all" ]; then
  for d in "$DR"/*/; do
    s="$(basename "$d")"; [ -f "$DR/$s/${s}_v1.md" ] || continue
    run_one "$s" || total_fail=$((total_fail+1))
  done
else
  slug="$(resolve_slug "$1")"
  [ -n "$slug" ] || { echo "run_gates: no draft folder for '$1'" >&2; exit 2; }
  run_one "$slug" || total_fail=1
fi
echo "run_gates: $([ $total_fail -eq 0 ] && echo '✅ all clean' || echo "❌ $total_fail chapter(s) with FAILs")"
exit $((total_fail>0))
