#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# generate_toc.sh  —  ASSEMBLE-phase front-matter generator (Step: Assemble)
# -----------------------------------------------------------------------------
# USED BY: ALL book types. Every assembled manuscript needs navigation.
#
# Walks the assembled Markdown manuscript and emits, in reading order:
#
#   * Table of Contents     — every "# " chapter / appendix heading and its
#                             "## " / "### " sub-headings, indented by depth,
#                             each a GitHub-style anchor link.
#   * List of Figures       — every defined "Figure N.M …" caption, in order.
#   * List of Listings      — every defined "Listing N.M …" caption, in order.
#                             (code listings — TECHNICAL/REFERENCE profile;
#                              set LIST_LISTINGS="false" for non-code books)
#
# Output goes to STDOUT by default, or to <dir>/TOC.md with --write so the TOC
# becomes a committed front-matter file. Companion to check_crossrefs.sh: that
# gate asserts refs resolve; this emits the navigation that exercises them.
#
# Portability: written for bash 3.2 (macOS default) — no mapfile, no ${var,,};
# lowercasing for anchor slugs is done with tr.
#
# Usage:   generate_toc.sh [manuscript-dir] [--write]
#            manuscript-dir   default from CONFIG below
#            --write          write to <manuscript-dir>/TOC.md instead of stdout
#
# Exit: 0 = TOC emitted (even if empty).  2 = bad invocation / dir not found.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
ASSEMBLY_DIR_REL="{{ASSEMBLY_DIR_REL}}"   # default scan dir, e.g. 06-assembly
# Emit a "List of Listings" — code-snippet captions; TECHNICAL/REFERENCE only.
LIST_LISTINGS="{{LIST_LISTINGS}}"         # "true" (technical profile) | "false"
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"

# --- Parse args (dir + optional --write, order-independent) -------------------
MANUSCRIPT_DIR=""
WRITE=0
for arg in "$@"; do
  case "$arg" in
    --write) WRITE=1 ;;
    *)       MANUSCRIPT_DIR="$arg" ;;
  esac
done
MANUSCRIPT_DIR="${MANUSCRIPT_DIR:-$REPO_ROOT/$ASSEMBLY_DIR_REL}"

if [[ ! -d "$MANUSCRIPT_DIR" ]]; then
  echo "generate_toc: manuscript dir not found: $MANUSCRIPT_DIR" >&2
  exit 2
fi

# --- Collect manuscript files in reading order (skip an existing TOC) ---------
FILE_LIST="$(find "$MANUSCRIPT_DIR" -type f -name '*.md' ! -name 'TOC.md' 2>/dev/null | sort)"
FILE_COUNT="$(printf '%s\n' "$FILE_LIST" | grep -c . || true)"
if [[ "$FILE_COUNT" -eq 0 ]]; then
  echo "generate_toc: no *.md files under $MANUSCRIPT_DIR — nothing to walk." >&2
fi

# --- GitHub-style slug for an anchor link (lowercase, drop punctuation, dash) -
slugify() {  # slugify <heading text>
  printf '%s' "$1" \
    | tr '[:upper:]' '[:lower:]' \
    | sed -E 's/[^a-z0-9 _-]//g; s/ /-/g'
}

# --- Emit the document to stdout; caller redirects to TOC.md if --write -------
emit() {
  echo "# Table of Contents"
  echo

  # Walk every heading (# .. ###) across all files, in order, depth-indented.
  printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
    [[ -z "$f" ]] && continue
    grep -nE '^#{1,3} +' "$f" 2>/dev/null | while IFS= read -r hline; do
      heading="${hline#*:}"   # drop "<lineno>:" prefix
      hashes="$(printf '%s' "$heading" | grep -oE '^#{1,3}')"
      depth=${#hashes}
      text="$(printf '%s' "$heading" | sed -E 's/^#{1,3} +//; s/[[:space:]]+$//')"
      [[ -z "$text" ]] && continue
      slug="$(slugify "$text")"
      indent="$(printf '%*s' $(( (depth - 1) * 2 )) '')"
      printf '%s- [%s](#%s)\n' "$indent" "$text" "$slug"
    done
  done

  echo
  echo "# List of Figures"
  echo
  figs="$(printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
    [[ -z "$f" ]] && continue
    grep -hoE '(\*\*Figure [0-9]+\.[0-9]+[^*]*)|(\*Figure [0-9]+\.[0-9]+[^*]*)|(!\[Figure [0-9]+\.[0-9]+[^]]*)|(^Figure [0-9]+\.[0-9]+.*)' "$f" 2>/dev/null || true
  done | sed -E 's/^\*+//; s/^!\[//; s/[[:space:]]+$//' | awk 'NF && !seen[$0]++' || true)"
  if [[ -n "$figs" ]]; then
    printf '%s\n' "$figs" | sed -E 's/^/- /'
  else
    echo "_(none)_"
  fi

  # List of Listings — code-snippet captions; technical/reference profile only.
  if [[ "$LIST_LISTINGS" == "true" ]]; then
    echo
    echo "# List of Listings"
    echo
    lists="$(printf '%s\n' "$FILE_LIST" | while IFS= read -r f; do
      [[ -z "$f" ]] && continue
      grep -hoE '(\*\*Listing [0-9]+\.[0-9]+[^*]*)|(\*Listing [0-9]+\.[0-9]+[^*]*)|(^Listing [0-9]+\.[0-9]+.*)' "$f" 2>/dev/null || true
    done | sed -E 's/^\*+//; s/[[:space:]]+$//' | awk 'NF && !seen[$0]++' || true)"
    if [[ -n "$lists" ]]; then
      printf '%s\n' "$lists" | sed -E 's/^/- /'
    else
      echo "_(none)_"
    fi
  fi
}

if (( WRITE )); then
  OUT="$MANUSCRIPT_DIR/TOC.md"
  emit > "$OUT"
  echo "generate_toc: wrote $OUT" >&2
else
  emit
fi
