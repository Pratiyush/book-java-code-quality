#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# check_crossrefs.sh  —  ASSEMBLE-phase integrity gate (Step: Assemble the book)
# -----------------------------------------------------------------------------
# USED BY: ALL book types. Cross-reference integrity is genre-independent — a
#          manuscript may never promise a chapter, figure, listing, table, or
#          appendix it does not contain, nor mis-number one.
#
# Scans an assembled Markdown manuscript and asserts that every internal
# cross-reference RESOLVES to a real, correctly-numbered target:
#
#   "Chapter N"      -> the Nth chapter exists (a top-level "# " heading)
#   "Figure N.M"     -> a Figure N.M caption / image is DEFINED in the book
#   "Listing N.M"    -> a Listing N.M caption is DEFINED   (technical profile)
#   "Table N.M"      -> a Table N.M caption is DEFINED
#   "Appendix X"     -> an "Appendix X" heading exists
#   "see §N[.M…]"    -> a §-numbered section heading exists (soft WARN when the
#                       book uses named, un-numbered sections)
#
# A reference pointing at a target the manuscript does not contain is a DANGLING
# ref. A defined Figure/Listing/Table whose per-chapter ordinals skip or do not
# start at 1 is MIS-NUMBERED. Both fail the gate. This is the assembly-time
# sibling of check_snippets.sh (which guarantees prose<->code markers resolve;
# that one is technical-profile only — this one applies to every book type).
#
# Portability: written for bash 3.2 (macOS default) — no mapfile, no `local -n`
# namerefs, no ${var,,}. Inventories are newline-delimited strings.
#
# Usage:   check_crossrefs.sh [manuscript-dir]   (default from CONFIG)
# Scans every *.md under the dir (manuscript + appendix + glossary) as ONE
# corpus — refs may point across files.
#
# Exit: 0 = every reference resolves and is correctly numbered.
#       1 = one or more dangling or mis-numbered references (hard fail).
#       2 = bad invocation / manuscript dir not found.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
ASSEMBLY_DIR_REL="{{ASSEMBLY_DIR_REL}}"   # default scan dir, e.g. 06-assembly
# "Listing N.M" is a code-snippet caption — TECHNICAL/REFERENCE profiles only.
# Set CHECK_LISTINGS="false" for non-code books (science, business, narrative)
# that carry no code listings, so a stray "Listing" word is not over-policed.
CHECK_LISTINGS="{{CHECK_LISTINGS}}"       # "true" (technical profile) | "false"
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

# --- Resolve the manuscript directory ----------------------------------------
MANUSCRIPT_DIR="${1:-$REPO_ROOT/$ASSEMBLY_DIR_REL}"
if [[ ! -d "$MANUSCRIPT_DIR" ]]; then
  echo "check_crossrefs: manuscript dir not found: $MANUSCRIPT_DIR" >&2
  exit 2
fi

# --- Collect the manuscript files in reading order (skip an existing TOC) -----
FILE_LIST="$(find "$MANUSCRIPT_DIR" -type f -name '*.md' ! -name 'TOC.md' 2>/dev/null | sort)"
FILE_COUNT="$(printf '%s\n' "$FILE_LIST" | grep -c . || true)"
if [[ "$FILE_COUNT" -eq 0 ]]; then
  echo "check_crossrefs: no *.md files under $MANUSCRIPT_DIR — nothing to check."
  exit 0
fi

# grep helpers across every manuscript file (file list has no spaces in the
# standard NN_slug layout). mgrep -> "<file>:<lineno>:<text>"; ogrep -> matches.
mgrep() {  # mgrep <extended-regex>
  printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
    [[ -z "$f" ]] && continue
    grep -nHE "$1" "$f" 2>/dev/null || true
  done
}
ogrep() {  # ogrep <extended-regex>
  printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
    [[ -z "$f" ]] && continue
    grep -hoE "$1" "$f" 2>/dev/null || true
  done
}

# --- newline-list membership test --------------------------------------------
in_list() {  # in_list <needle> <newline-list>
  printf '%s\n' "$2" | grep -qxF "$1"
}

# =============================================================================
# Build the TARGET inventories — what actually EXISTS in the manuscript.
# =============================================================================

# Chapters: top-level "# " headings (not "##"+), excluding "# Appendix …".
CHAPTER_HEADINGS="$(printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
  [[ -z "$f" ]] && continue
  grep -hE '^# [^#]' "$f" 2>/dev/null | grep -ivE '^# *Appendix ' || true
done)"
CHAPTER_COUNT="$(printf '%s\n' "$CHAPTER_HEADINGS" | grep -c . || true)"

# A Figure/Listing/Table "exists" only if it is DEFINED (a caption / image), not
# merely mentioned. Definition shapes: bold "**Figure N.M …", italic "*Listing
# N.M.", image "![Figure …", or a line STARTING with the kind ("Listing 7.1. …").
defined_ids() {  # defined_ids <Figure|Listing|Table>
  local kind="$1"
  ogrep "(\*\*${kind} [0-9]+\.[0-9]+)|(\*${kind} [0-9]+\.[0-9]+)|(!\[${kind} [0-9]+\.[0-9]+)|(^${kind} [0-9]+\.[0-9]+)" \
    | grep -oE "${kind} [0-9]+\.[0-9]+" | sort -u
}
FIG_DEFINED="$(defined_ids Figure || true)"
TABLE_DEFINED="$(defined_ids Table || true)"
if [[ "$CHECK_LISTINGS" == "true" ]]; then
  LIST_DEFINED="$(defined_ids Listing || true)"
else
  LIST_DEFINED=""
fi

# Appendices that EXIST: headings "# Appendix X" / "## Appendix X".
APPENDIX_DEFINED="$(printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
  [[ -z "$f" ]] && continue
  grep -hoE '^#{1,3} *Appendix [A-Z]' "$f" 2>/dev/null || true
done | grep -oE 'Appendix [A-Z]' | sort -u || true)"

# Numbered sections that EXIST (for "§" refs): headings beginning with a number.
SECTION_DEFINED="$(printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
  [[ -z "$f" ]] && continue
  grep -hoE '^#{2,4} +[0-9]+(\.[0-9]+)*' "$f" 2>/dev/null || true
done | grep -oE '[0-9]+(\.[0-9]+)*' | sort -u || true)"
SECTION_COUNT="$(printf '%s\n' "$SECTION_DEFINED" | grep -c . || true)"

fail=0
warn=0
checked=0

report_fail() { echo "FAIL  $1"; fail=$((fail+1)); }
report_warn() { echo "WARN  $1"; warn=$((warn+1)); }

# =============================================================================
# Check 1 — "Chapter N" references resolve to an existing chapter.
# =============================================================================
while IFS= read -r line; do
  [[ -z "$line" ]] && continue
  where="$(printf '%s' "$line" | cut -d: -f1)"
  ln="$(printf '%s' "$line" | cut -d: -f2)"
  for num in $(printf '%s' "$line" | grep -oE 'Chapter [0-9]+' | grep -oE '[0-9]+'); do
    checked=$((checked+1))
    if (( num < 1 || num > CHAPTER_COUNT )); then
      report_fail "$(basename "$where"):$ln  dangling \"Chapter $num\" — manuscript has $CHAPTER_COUNT chapter(s)"
    fi
  done
done < <(mgrep 'Chapter [0-9]+')

# =============================================================================
# Check 2 — "Figure / Listing / Table N.M" references resolve to a DEFINED one.
# =============================================================================
check_numbered_refs() {  # check_numbered_refs <kind> <defined-list>
  local kind="$1" defined="$2" line where ln id
  while IFS= read -r line; do
    [[ -z "$line" ]] && continue
    where="$(printf '%s' "$line" | cut -d: -f1)"
    ln="$(printf '%s' "$line" | cut -d: -f2)"
    for id in $(printf '%s' "$line" | grep -oE "${kind} [0-9]+\.[0-9]+" | tr ' ' '@'); do
      id="$(printf '%s' "$id" | tr '@' ' ')"   # restore "Figure 6.1"
      checked=$((checked+1))
      if ! in_list "$id" "$defined"; then
        report_fail "$(basename "$where"):$ln  dangling \"$id\" — no such $kind is defined in the manuscript"
      fi
    done
  done < <(mgrep "${kind} [0-9]+\.[0-9]+")
}
check_numbered_refs Figure "$FIG_DEFINED"
check_numbered_refs Table  "$TABLE_DEFINED"
if [[ "$CHECK_LISTINGS" == "true" ]]; then
  check_numbered_refs Listing "$LIST_DEFINED"   # code listings — technical profile
fi

# =============================================================================
# Check 3 — defined Figures/Listings/Tables are correctly numbered: per-chapter
#           ordinals start at 1 and do not skip.
# =============================================================================
check_ordinals() {  # check_ordinals <kind> <defined-list>
  local kind="$1" defined="$2" prefixes ch ords expect o
  prefixes="$(printf '%s\n' "$defined" | grep -oE '[0-9]+\.[0-9]+' \
              | sed -E 's/\..*$//' | sort -un || true)"
  for ch in $prefixes; do
    [[ -z "$ch" ]] && continue
    ords="$(printf '%s\n' "$defined" | grep -E " ${ch}\.[0-9]+$" \
            | grep -oE '[0-9]+$' | sort -un || true)"
    expect=1
    for o in $ords; do
      if (( o != expect )); then
        report_fail "$kind numbering in chapter $ch skips: expected $kind ${ch}.${expect}, found $kind ${ch}.${o}"
        expect=$o
      fi
      expect=$((expect+1))
    done
  done
}
check_ordinals Figure "$FIG_DEFINED"
check_ordinals Table  "$TABLE_DEFINED"
if [[ "$CHECK_LISTINGS" == "true" ]]; then
  check_ordinals Listing "$LIST_DEFINED"        # code listings — technical profile
fi

# =============================================================================
# Check 4 — "Appendix X" references resolve to an existing appendix heading.
# =============================================================================
while IFS= read -r line; do
  [[ -z "$line" ]] && continue
  text="$(printf '%s' "$line" | cut -d: -f3-)"
  printf '%s' "$text" | grep -qE '^#{1,3} *Appendix ' && continue
  where="$(printf '%s' "$line" | cut -d: -f1)"
  ln="$(printf '%s' "$line" | cut -d: -f2)"
  for ap in $(printf '%s' "$line" | grep -oE 'Appendix [A-Z]' | tr ' ' '@'); do
    ap="$(printf '%s' "$ap" | tr '@' ' ')"
    checked=$((checked+1))
    if ! in_list "$ap" "$APPENDIX_DEFINED"; then
      report_fail "$(basename "$where"):$ln  dangling \"$ap\" — no such appendix heading exists"
    fi
  done
done < <(mgrep 'Appendix [A-Z]')

# =============================================================================
# Check 5 — "see §N[.M…]" references. Numbered-section books must resolve them;
#           named-section books get a soft WARN.
# =============================================================================
while IFS= read -r line; do
  [[ -z "$line" ]] && continue
  where="$(printf '%s' "$line" | cut -d: -f1)"
  ln="$(printf '%s' "$line" | cut -d: -f2)"
  for sec in $(printf '%s' "$line" | grep -oE '§ *[0-9]+(\.[0-9]+)*' | grep -oE '[0-9]+(\.[0-9]+)*'); do
    checked=$((checked+1))
    if [[ "$SECTION_COUNT" -eq 0 ]]; then
      report_warn "$(basename "$where"):$ln  \"§$sec\" — manuscript has no numbered section headings (named-section book); verify by hand"
    elif ! in_list "$sec" "$SECTION_DEFINED"; then
      report_fail "$(basename "$where"):$ln  dangling \"§$sec\" — no numbered section $sec exists"
    fi
  done
done < <(mgrep '§ *[0-9]+')

# =============================================================================
# Summary
# =============================================================================
fig_n="$(printf '%s\n' "$FIG_DEFINED" | grep -c . || true)"
list_n="$(printf '%s\n' "$LIST_DEFINED" | grep -c . || true)"
table_n="$(printf '%s\n' "$TABLE_DEFINED" | grep -c . || true)"
appx_n="$(printf '%s\n' "$APPENDIX_DEFINED" | grep -c . || true)"
echo "----"
echo "check_crossrefs: scanned $FILE_COUNT file(s); chapters=$CHAPTER_COUNT,"
echo "                 figures=$fig_n listings=$list_n tables=$table_n appendices=$appx_n"
echo "                 $checked reference(s) checked; $fail dangling/mis-numbered, $warn warning(s)."
if (( fail == 0 && warn == 0 && checked == 0 )); then
  echo "(no cross-references found — nothing to resolve yet)"
fi
(( fail == 0 ))
