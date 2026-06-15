#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# check_process.sh  —  PROCESS-CHECK step — the PROCESS-DRIFT guard
# -----------------------------------------------------------------------------
# USED BY: ALL book types. Process integrity is genre-independent — every book's
#          gate trail must be complete and in order, and its governing docs
#          (PIPELINE.md <-> CHAPTER-TRACKER.md <-> agent specs) must agree.
#
# The process equivalent of check_crossrefs.sh / check_snippets.sh: those guard
# the *manuscript* against drift; this one guards the *pipeline* against it. It
# is the script behind the PROCESS-CHECK step (owned by the production-manager),
# run alongside the MAINTAIN step. It asserts two invariants, exits non-zero on
# any drift:
#
#  (A) GATE-TRAIL completeness — parse the CHAPTER-TRACKER and assert no chapter
#      shows a LATER gate done while an EARLIER HARD gate is still pending /
#      blank / draft-raw. No HARD gate may be skipped, and nothing may be
#      advanced past a gate it did not pass — per the PIPELINE HARD-gate order,
#      projected onto the per-chapter tracker columns.
#
#  (B) DOC-CONSISTENCY — PIPELINE <-> CHAPTER-TRACKER <-> agent specs stay in
#      sync:
#        * every HARD gate named in the PIPELINE has an owning agent file under
#          the agents dir (or is an explicitly human/script-only gate), AND a
#          tracker column or legend entry;
#        * every agent spec names a PIPELINE step it owns (an agent that owns no
#          step is an orphan — flagged either way).
#
# Both checks are pure text scans over the governing docs — no toolchain, no
# network, no non-coreutils dependency. Greppable by design: every finding is a
# single "FAIL  <what>" / "WARN  <what>" line so the output can be grepped in CI.
#
# Portability: written for bash 3.2 (macOS default) — no mapfile, no `local -n`
# namerefs, no associative arrays, no ${var,,}. Lists are newline-delimited
# strings tested with grep -qxF.
#
# Usage:   check_process.sh
#          (reads the governing docs at the CONFIG paths; no arguments)
#
# Exit: 0 = no process drift (gate trails complete + in order; docs consistent).
#       1 = one or more drift findings (hard fail).
#       2 = a required governing doc is missing (cannot run the check).
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
PIPELINE_REL="00-strategy/PIPELINE.md"      # e.g. 00-strategy/PIPELINE.md
TRACKER_REL="01-index/CHAPTER-TRACKER.md"        # e.g. 01-index/CHAPTER-TRACKER.md
AGENTS_DIR_REL=".claude/agents"  # e.g. .claude/agents
# HARD gates owned by a HUMAN and/or a bare SCRIPT — they need NOT have an
# owning agent file. Space- or newline-delimited step ids. Typically the source
# pin, the cheap pre-check, and the human approval/release stops:
HUMAN_OR_SCRIPT_GATES="human-approve"   # e.g. "0 4a 12 16"
# The CANONICAL HARD-gate enumeration for THIS book — copied verbatim from this
# book's PIPELINE.md authoritative count ("N HARD gates … <list>"). PIPELINE.md
# is the source of truth; this list must equal it, and CHECK B1 below asserts the
# doc's harvested HARD set equals this list EXACTLY (drift either way fails). Count
# the figure gate ONCE as "9" (a PIPELINE that splits it as 9a + 9b is folded to
# 9); do NOT include a soft gate whose row text merely says "→ HARD at <later>"
# (e.g. an A11Y 9c). A non-code profile that turns the example/figure gates OFF
# (the build/compile gate turned off) drops 4b/4d/9 from this list. Keep this and PIPELINE.md in lockstep.
HARD_GATES="research|source-verify|example-build|code-review|repro|verify|clarity|audit|score|reconcile"   # e.g. "0 2 3 3b 4a 4b 4d 5 5b 6 6b 7 8 8a 8b 9 10 12 15 16"
# Agents that legitimately own NO pipeline step (pure support roles). Space- or
# newline-delimited basenames (no ".md"). Usually empty.
AGENT_STEP_EXEMPT="series-editor"           # e.g. ""
# Per-chapter HARD gate COLUMNS in pipeline order — the blocking trail this guard
# enforces row by row. A non-code book drops example/figure (its the build/compile gate turned off);
# keep only the columns that exist on THIS book's board, left-to-right.
HARD_COLS_CFG="Research|SourceVerify|ExampleBuild|CodeReview|Repro|Verify|Clarity|Audit|Score|Reconcile"        # e.g. "verify example clarity audit score figure reconcile approve"
# column:step pairs for the reverse-drift check (every board column maps to a
# step PIPELINE still defines). Space-delimited "col:step" tokens; the soft
# columns (research/draft) are included so a stray column is caught too.
COL_STEP_PAIRS="Research:research,Verify:source-verify,Draft:draft,Score:score"  # e.g. "research:1 verify:5 draft:4 example:4b clarity:6 audit:7 score:8 figure:9 reconcile:10 approve:12"
# RIDE-ALONG HARD gates that have NO own column but ride in-cell on a sibling
# column — validated by a NAMED mention in the tracker (the ride-along legend),
# not a column. Space-delimited "id:regex" tokens; the regex is matched case-
# insensitively against the tracker text. Leave empty for a book with no ride-
# along gates. e.g. the 2026-06 publishing-house gates on the technical profile:
RIDEALONG_TOKENS="TODO|TBD|FIXME|XXX"  # e.g. "4d:repro 5b:originality|verbatim 6b:copyedit 8a:reader-?sim 8b:red-?team|adversarial"
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
PIPELINE="$REPO_ROOT/$PIPELINE_REL"
TRACKER="$REPO_ROOT/$TRACKER_REL"
AGENTS_DIR="$REPO_ROOT/$AGENTS_DIR_REL"

# Normalize the space/newline-delimited config lists to newline lists.
HARD_COLS="$(printf '%s\n' $HARD_COLS_CFG)"
HUMAN_OR_SCRIPT_GATES="$(printf '%s\n' $HUMAN_OR_SCRIPT_GATES)"
AGENT_STEP_EXEMPT="$(printf '%s\n' $AGENT_STEP_EXEMPT)"
CANONICAL_HARD_IDS="$(printf '%s\n' $HARD_GATES)"
CANONICAL_HARD_N="$(printf '%s\n' $HARD_GATES | grep -c . || true)"

# =============================================================================
# Preconditions
# =============================================================================
for doc in "$PIPELINE" "$TRACKER"; do
  if [[ ! -f "$doc" ]]; then
    echo "check_process: required doc not found: $doc" >&2
    exit 2
  fi
done
if [[ ! -d "$AGENTS_DIR" ]]; then
  echo "check_process: agents dir not found: $AGENTS_DIR" >&2
  exit 2
fi

fail=0
warn=0
report_fail() { echo "FAIL  $1"; fail=$((fail+1)); }
report_warn() { echo "WARN  $1"; warn=$((warn+1)); }

# --- newline-list membership test --------------------------------------------
in_list() {  # in_list <needle> <newline-list>
  printf '%s\n' "$2" | grep -qxF "$1"
}

# A tracker cell "counts as passed" when it records a banked/passed verdict:
# "done" (with any decoration: done², done v4, done²✅green·CR✓), a ship verdict
# ("SHIP 44/50"), or a build/code-review token ("green", "CR✓"). Everything else
# — pending / in-prog / draft-raw / FLAG / blank / a bare marker — is NOT a pass.
# "n-a" is handled separately (not-applicable, never blocking).
cell_passed() {  # cell_passed <cell-text>
  printf '%s' "$1" | grep -qiE '(^|[^a-z])done([^a-z]|$)|SHIP[ ]*[0-9]|green|CR✓'
}
cell_na() {      # cell_na <cell-text>
  printf '%s' "$1" | grep -qiE '(^|[^a-z])n-?a([^a-z]|$)'
}

# =============================================================================
# CHECK A — GATE-TRAIL completeness (per-chapter, from the CHAPTER-TRACKER)
# -----------------------------------------------------------------------------
# The tracker is a set of Markdown tables whose header row names the per-chapter
# gate columns. We map each column name to its field index, then for every data
# row assert the HARD gates (HARD_COLS, in pipeline order) are a non-decreasing
# "passed" prefix: once a HARD gate is NOT passed, no LATER HARD gate may be
# passed. A data row is a "|"-line whose first cell is an integer chapter number.
# =============================================================================
gate_trail_check() {
  local line colname i
  local hdr_names="" hdr_idx=""

  while IFS= read -r line; do
    case "$line" in
      \|*) ;;
      *) continue ;;
    esac

    # Header row? It contains a "Ch" cell and a "research" cell.
    if printf '%s' "$line" | grep -qiE '\|[ ]*Ch[ ]*\|' \
       && printf '%s' "$line" | grep -qi 'research'; then
      hdr_names=""
      hdr_idx=""
      i=0
      while IFS= read -r colname; do
        i=$((i+1))
        colname="$(printf '%s' "$colname" | sed -E 's/^[[:space:]]+//; s/[[:space:]]+$//' | tr 'A-Z' 'a-z')"
        [[ -z "$colname" ]] && continue
        hdr_names="$hdr_names$colname"$'\n'
        hdr_idx="$hdr_idx$i"$'\n'
      done < <(printf '%s\n' "$line" | tr '|' '\n')
      continue
    fi

    # Separator row "|---|---|"? skip.
    if printf '%s' "$line" | grep -qE '^\|[ :|-]+\|$'; then
      continue
    fi

    [[ -z "$hdr_names" ]] && continue   # no header yet -> not a data row

    local first_cell
    first_cell="$(printf '%s\n' "$line" | tr '|' '\n' | sed -n '2p' \
                  | sed -E 's/^[[:space:]]+//; s/[[:space:]]+$//')"
    case "$first_cell" in
      ''|*[!0-9]*) continue ;;   # not a pure integer -> not a chapter data row
    esac
    local ch="$first_cell" nn
    nn="$(printf '%s\n' "$line" | tr '|' '\n' | sed -n '3p' \
          | sed -E 's/^[[:space:]]+//; s/[[:space:]]+$//')"

    local broken_at="" col cell pos
    for col in $HARD_COLS; do
      pos="$(paste -d'\t' <(printf '%s' "$hdr_names") <(printf '%s' "$hdr_idx") \
             | grep -iE "^${col}"$'\t' | head -1 | cut -f2)"
      [[ -z "$pos" ]] && continue   # column absent from this table — skip it
      cell="$(printf '%s\n' "$line" | tr '|' '\n' | sed -n "${pos}p" \
              | sed -E 's/^[[:space:]]+//; s/[[:space:]]+$//')"

      if cell_passed "$cell"; then
        if [[ -n "$broken_at" ]]; then
          report_fail "ch $ch (key $nn): '$col'=$cell is done, but earlier HARD gate '$broken_at' is not passed — skipped/out-of-order gate"
        fi
      elif cell_na "$cell"; then
        :   # not applicable — neither a pass nor a blocker
      else
        [[ -z "$broken_at" ]] && broken_at="$col"
      fi
    done
  done < "$TRACKER"
}
echo "== CHECK A — gate-trail completeness (CHAPTER-TRACKER) =="
gate_trail_check

# =============================================================================
# CHECK B — DOC-CONSISTENCY (PIPELINE <-> agents <-> tracker)
# =============================================================================
echo "== CHECK B — doc consistency (PIPELINE <-> agents <-> tracker) =="

# --- B1: enumerate the HARD gates declared in the PIPELINE --------------------
# The flow-at-a-glance block lists each step as "  <id>  NAME  ...  HARD".
PIPELINE_HARD_RAW="$(grep -nE '\bHARD\b' "$PIPELINE" \
  | grep -oE '^[0-9]+:[[:space:]]*[0-9]+[a-z]?[[:space:]]' \
  | sed -E 's/^[0-9]+:[[:space:]]*//; s/[[:space:]]*$//' \
  | sort -u || true)"
# Normalize to PIPELINE's canonical counting before comparing: a figure gate split
# as 9a + 9b (and a soft 9c whose row text reads "→ HARD at <later>") is over-
# collected by the \bHARD\b grep; collapse 9a/9b/9c to the single id "9".
PIPELINE_HARD_IDS="$(printf '%s\n' "$PIPELINE_HARD_RAW" \
  | sed -E 's/^9[abc]$/9/' \
  | grep -E '^[0-9]+[a-z]?$' | sort -u || true)"

# Reconcile EXACTLY with the configured canonical list (PIPELINE.md is
# authoritative; this script must mirror it). Any id in one set but not the other
# is hard drift — this is the check that keeps the script and PIPELINE.md from
# silently diverging. 9a/9b/9c already folded to 9.
for id in $PIPELINE_HARD_IDS; do
  if ! in_list "$id" "$CANONICAL_HARD_IDS"; then
    report_fail "PIPELINE marks step $id HARD, but it is not in this book's HARD_GATES — reconcile HARD_GATES with PIPELINE"
  fi
done
for id in $CANONICAL_HARD_IDS; do
  if ! in_list "$id" "$PIPELINE_HARD_IDS"; then
    report_fail "configured HARD gate $id is not marked HARD in PIPELINE's flow-at-a-glance — reconcile PIPELINE with HARD_GATES"
  fi
done
# Drive B3/B4 off the canonical set so the owner/tracker checks cover the exact
# gate set, independent of any harvest quirk (collapsed figure id, soft 9c, etc.).
PIPELINE_HARD_IDS="$CANONICAL_HARD_IDS"

# Per-step "### Step Nx" headings (catches HARD steps shown only in prose).
PIPELINE_STEP_IDS="$(grep -oE '^###[#]? Step [0-9]+[a-z]?' "$PIPELINE" \
  | grep -oE '[0-9]+[a-z]?' | sort -u || true)"
# Union of all step ids mentioned (raw harvest keeps 9a/9b/9c for B5's reverse map).
ALL_STEP_IDS="$(printf '%s\n%s\n%s\n' "$PIPELINE_HARD_RAW" "$PIPELINE_STEP_IDS" "$CANONICAL_HARD_IDS" \
  | grep -E '^[0-9]+[a-z]?$' | sort -u || true)"

# --- B2: which PIPELINE step does each agent file claim? ----------------------
CLAIMED_STEP_IDS=""
for af in "$AGENTS_DIR"/*.md; do
  [[ -e "$af" ]] || continue
  base="$(basename "$af" .md)"
  if in_list "$base" "$AGENT_STEP_EXEMPT"; then
    continue
  fi
  steps="$(grep -ioE '(PIPELINE )?[Ss]tep [0-9]+[a-z]?' "$af" \
           | grep -oE '[0-9]+[a-z]?' | sort -u || true)"
  if [[ -z "$steps" ]]; then
    report_fail "agent '$base' names no PIPELINE step it owns — orphan agent (add a 'PIPELINE step N' reference, or exempt it)"
    continue
  fi
  CLAIMED_STEP_IDS="$CLAIMED_STEP_IDS$steps"$'\n'
done
CLAIMED_STEP_IDS="$(printf '%s' "$CLAIMED_STEP_IDS" | grep -E '^[0-9]+[a-z]?$' | sort -u || true)"

# --- B3: every HARD gate has an owning agent (or is human/script-only) --------
# Figure-step parity: a PIPELINE that splits the figure gate into 9a + 9b is
# owned by one figure agent that claims the umbrella "step 9". Treat 9/9a/9b as
# one. (If your book uses no sub-lettered figure split this is a harmless no-op.)
owns_step() {  # owns_step <id>  — is this step claimed by some agent?
  local id="$1"
  if in_list "$id" "$CLAIMED_STEP_IDS"; then return 0; fi
  case "$id" in
    9|9a|9b)
      in_list 9  "$CLAIMED_STEP_IDS" && return 0
      in_list 9a "$CLAIMED_STEP_IDS" && return 0
      in_list 9b "$CLAIMED_STEP_IDS" && return 0
      ;;
  esac
  return 1
}
for id in $PIPELINE_HARD_IDS; do
  if in_list "$id" "$HUMAN_OR_SCRIPT_GATES"; then
    continue
  fi
  if ! owns_step "$id"; then
    report_fail "HARD gate step $id (PIPELINE) has no owning agent in $AGENTS_DIR_REL and is not a declared human/script gate"
  fi
done

# --- B4: every HARD gate maps to a tracker column OR a tracker mention ---------
# Per-chapter gates must surface as a real column (HARD_COLS_CFG names them).
# Book-level / not-yet-columned gates are satisfied by a named mention OR by the
# step id appearing in the tracker's HARD-gate enumeration. The column<->step
# map comes from COL_STEP_PAIRS so it tracks THIS book's board.
TRACKER_TEXT="$(cat "$TRACKER")"
tracker_has_token() {  # tracker_has_token <extended-regex>
  printf '%s' "$TRACKER_TEXT" | grep -qiE "$1"
}
# step -> column, derived by inverting COL_STEP_PAIRS (first column that maps to
# the step wins). Returns empty if no column owns the step (=> book-level gate).
col_for_step() {  # col_for_step <id>
  local id="$1" pair c s
  for pair in $COL_STEP_PAIRS; do
    c="${pair%%:*}"; s="${pair##*:}"
    if [[ "$s" == "$id" ]]; then printf '%s' "$c"; return 0; fi
  done
  # figure-step parity for the column map too (9 column owns 9a/9b)
  case "$id" in
    9a|9b)
      for pair in $COL_STEP_PAIRS; do
        c="${pair%%:*}"; s="${pair##*:}"
        if [[ "$s" == "9" ]]; then printf '%s' "$c"; return 0; fi
      done ;;
  esac
  return 1
}
# id -> ride-along regex from RIDEALONG_TOKENS (empty if this id rides nothing).
ridealong_regex() {  # ridealong_regex <id>
  local id="$1" pair k v
  for pair in $RIDEALONG_TOKENS; do
    k="${pair%%:*}"; v="${pair#*:}"
    if [[ "$k" == "$id" ]]; then printf '%s' "$v"; return 0; fi
  done
  return 1
}
for id in $PIPELINE_HARD_IDS; do
  in_list "$id" "$HUMAN_OR_SCRIPT_GATES" && continue   # handled by B3; may be column-less
  col="$(col_for_step "$id" || true)"
  if [[ -n "$col" ]]; then
    # Per-chapter gate — must have a real column on the board.
    if ! tracker_has_token "\\|[ ]*${col}[ ]*\\|"; then
      report_fail "HARD gate step $id has no matching '$col' column in the CHAPTER-TRACKER"
    fi
    continue
  fi
  # Ride-along (in-cell) gate — satisfied by its named token in the tracker.
  rx="$(ridealong_regex "$id" || true)"
  if [[ -n "$rx" ]]; then
    if tracker_has_token "$rx"; then continue; fi
  fi
  # Otherwise a book-level / not-yet-columned gate — accept a step id in the
  # HARD-gate enumeration, else WARN (ride-along with no token also lands here).
  id_listed='(HARD gate|HARD gates|gates:)[^.]*[^0-9]'"$id"'([^0-9a-z]|$)'
  if ! tracker_has_token "$id_listed"; then
    report_warn "HARD gate step $id is not named anywhere in the CHAPTER-TRACKER (column, ride-along token, or HARD-gate enumeration recommended)"
  fi
done

# --- B5: every tracker gate column maps back to a PIPELINE step ---------------
# Reverse drift: a column on the board that the PIPELINE no longer defines.
for pair in $COL_STEP_PAIRS; do
  col="${pair%%:*}"
  step="${pair##*:}"
  if tracker_has_token "\\|[ ]*${col}[ ]*\\|"; then
    if ! in_list "$step" "$ALL_STEP_IDS"; then
      report_fail "tracker column '$col' maps to PIPELINE step $step, which the PIPELINE does not define"
    fi
  fi
done

# =============================================================================
# Summary
# =============================================================================
# hard_n counts the reconciled canonical HARD set (B1 already asserted it equals
# PIPELINE.md exactly). Assert it also equals the configured count so a stale
# HARD_GATES list cannot drift from its own normalized count.
hard_n="$(printf '%s\n' "$PIPELINE_HARD_IDS" | grep -c . || true)"
if [[ "$hard_n" -ne "$CANONICAL_HARD_N" ]]; then
  report_fail "canonical HARD-gate count is $hard_n but normalized HARD_GATES count is $CANONICAL_HARD_N — fix HARD_GATES"
fi
agent_n="$(find "$AGENTS_DIR" -maxdepth 1 -name '*.md' | grep -c . || true)"
echo "----"
echo "check_process: PIPELINE HARD gates=$hard_n (canonical $CANONICAL_HARD_N); agents=$agent_n;"
echo "               $fail drift finding(s), $warn warning(s)."
if (( fail == 0 )); then
  echo "PASS — no process drift: gate trails complete + in order; docs consistent."
else
  echo "FAIL — process drift detected (see FAIL lines above)."
fi
(( fail == 0 ))
