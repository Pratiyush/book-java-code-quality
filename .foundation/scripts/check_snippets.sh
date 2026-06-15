#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# =============================================================================
# check_snippets.sh — gate that every snippet include marker in the manuscript
# resolves to a real, <= MAX-line tag region in a compiling companion-code file.
# -----------------------------------------------------------------------------
# USED BY: TECHNICAL profile (and REFERENCE/cookbook when recipes are runnable).
#          Book types with {{GATES_OFF}}=example-build DROP this script — see
#          .foundation/BOOK-TYPE-PROFILES.md.
#
# The book prose carries stable markers (invisible in rendered Markdown):
#     <!-- include: NN_slug/path/File.ext#tag -->
# This gate asserts, for every such marker, that extract_snippet.sh resolves it
# (backing file exists, tag region exists, region is <= cap). A dangling marker
# or an over-length region is a FAIL. This is what makes the prose<->code "one
# artifact" anti-drift guarantee true rather than aspirational.
#
# Usage:   check_snippets.sh [path ...]   (default: the approved-chapters dir)
# Scans each given file, or every *.md under each given directory. Accepts
# multiple path args, so a v*.md glob from the source-verifier resolves every
# passed file.
# Exits 0 if all markers resolve; non-zero if any fails. Reports per-marker.
set -uo pipefail

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
APPROVED_DIR_REL="{{APPROVED_DIR_REL}}"  # e.g. 04-approved (default scan dir)
EXTRACT_REL="{{EXTRACT_REL}}"            # e.g. .claude/scripts/extract_snippet.sh
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
EXTRACT="$REPO_ROOT/$EXTRACT_REL"

# Default to the approved-chapters dir when no path args are given.
if [[ $# -eq 0 ]]; then
  set -- "$REPO_ROOT/$APPROVED_DIR_REL"
fi

files=()
for target in "$@"; do
  if [[ ! -e "$target" ]]; then
    echo "check_snippets: path not found: $target" >&2
    exit 2
  fi
  if [[ -f "$target" ]]; then
    files+=("$target")
  else
    while IFS= read -r line; do files+=("$line"); done < <(find "$target" -type f -name '*.md' | sort)
  fi
done

total=0; ok=0; fail=0
for f in "${files[@]:-}"; do
  [[ -z "${f:-}" ]] && continue
  # Pull each include spec out of <!-- include: SPEC --> markers.
  while IFS= read -r spec; do
    [[ -z "$spec" ]] && continue
    total=$((total+1))
    if msg="$("$EXTRACT" "$spec" 2>&1 >/dev/null)"; then
      ok=$((ok+1))
      echo "PASS  $spec  ($(basename "$f"))"
    else
      fail=$((fail+1))
      echo "FAIL  $spec  ($(basename "$f"))  — $msg"
    fi
  done < <(grep -oE '<!--[[:space:]]*include:[[:space:]]*[^[:space:]]+[[:space:]]*-->' "$f" \
             | sed -E 's/<!--[[:space:]]*include:[[:space:]]*//; s/[[:space:]]*-->//')
done

echo "----"
echo "check_snippets: $total marker(s); $ok pass, $fail fail."
if (( total == 0 )); then
  echo "(no include markers found — nothing to verify yet)"
fi
(( fail == 0 ))
