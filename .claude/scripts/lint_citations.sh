#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# lint_citations.sh <file>  —  PIPELINE STEP 5 gate (HARD, structural)
# -----------------------------------------------------------------------------
# USED BY: ALL book types (every type carries a back-matter "Sources" section).
#          The snippet-length cap (check 6) is TECHNICAL / REFERENCE profile
#          (see BOOK-TYPE-PROFILES.md): book types without runnable examples can
#          set MAX_SNIPPET_LINES very high or treat it as a no-op. The "no
#          academic residue" check (3) is profile-dependent — science / academic
#          books WANT DOI + author-year, so set CITATION_STYLE="academic" there.
#
# Structural linter for the back-matter "Sources" section of a dossier or draft,
# enforcing the house citation style from the book's GUIDELINES / templates:
#
#   1. TWO-TIER section present — a "Primary / Official" tier AND an
#      "Accessible / Further reading" tier must both appear.
#   2. PLAIN-TEXT URLs only — NO markdown hidden links of the form [text](url).
#      The house style is bare https://... URLs.
#   3. CITATION STYLE — for the "plain" house style, no DOI: / academic
#      author-year (e.g. "(Smith, 2020)") residue. Benign Tier-2 labels like
#      "(GraalVM, 2024)" / "(June 2026)" are NOT author-year citations and are
#      allowed. For CITATION_STYLE="academic", this check is SKIPPED.
#   4. EVERY source row carries a URL or a pinned source path.
#   5. EVERY source row carries a date-verified / "Verified" marker
#      (a YYYY-MM-DD date, a checkbox, or a "Verified" cell).
#   6. FENCED CODE snippets are <= N lines each (HARD cap; TECHNICAL profile).
#      Anchored to code fences only — the plain-text "Sources" back-matter fence
#      is excluded (it is legitimately long).
#
# This is a STRUCTURE check, not a fact check (verify_sources.sh does facts).
# Heuristic items are commented as such and WARN rather than fail where a false
# positive is likely.
#
# Exit: 0 = clean.  1 = at least one structural violation.  2 = usage.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
MAX_SNIPPET_LINES="9"                  # snippet ceiling (TECHNICAL); raise/ignore if no code
CITATION_STYLE="plain"                 # "plain" (bare URLs, no DOI) | "academic" (DOI/author-year OK)
# Matcher for a pinned local source PATH PREFIX inside a Sources row (besides http
# URLs). e.g. /tmp/ for a /tmp/<clone>... cloned repo; edit to your CLONE_PATH shape.
SOURCE_PATH_PREFIX='/tmp/'
# Full matcher for a source path token within a row (path prefix + non-delimiters).
ROW_SOURCE_PATH_GREP='/tmp/[^[:space:]|)]+'
# =====================================================================

if [[ $# -ne 1 ]]; then
  echo "usage: lint_citations.sh <dossier-or-draft.md>" >&2
  exit 2
fi
FILE="$1"
if [[ ! -f "${FILE}" ]]; then
  echo "FAIL: file not found: ${FILE}" >&2
  exit 2
fi

echo "lint_citations: ${FILE}"
VIOLATIONS=0
fail() { echo "  VIOLATION: $*"; VIOLATIONS=$((VIOLATIONS + 1)); }
warn() { echo "  WARN: $*"; }

# ---------------------------------------------------------------------------
# 6) CODE snippet length cap (<= N lines). TECHNICAL profile. The cap applies to
#    CODE fences only — NOT to the house "Sources" / back-matter, which is itself
#    a fenced plain-text block and is legitimately long. We therefore:
#      a) stop counting once we reach a "## Sources" (or deeper) heading, and
#      b) only flag a fence that looks like code: it carries a language hint on
#         the opening fence (```java, ```xml, ```properties, ...).
#    Bare/plain fences inside back-matter are skipped entirely.
# ---------------------------------------------------------------------------
echo "== Snippet length (<= ${MAX_SNIPPET_LINES} lines, CODE fences only) =="
LONG_BLOCKS="$(awk -v cap="${MAX_SNIPPET_LINES}" '
  # Reaching the Sources / back-matter heading ends code-fence checking: every
  # remaining fence is plain-text citation back-matter, not a code snippet.
  /^#{1,6}[[:space:]].*[Ss]ources/ { insources=1 }
  insources { next }
  /^[[:space:]]*```/ {
    if (inblock) {
      # only flag fences opened with a language hint (i.e. real code fences)
      if (iscode && cnt > cap) print "    code block ending ~line " NR " has " cnt " lines (cap " cap ")";
      inblock=0
    } else {
      inblock=1; cnt=0; start=NR
      # language hint = non-space text right after the opening ``` (```java etc.)
      fence=$0; sub(/^[[:space:]]*```/, "", fence); gsub(/[[:space:]]/, "", fence)
      iscode = (fence != "")
    }
    next
  }
  inblock { cnt++ }
  END { if (inblock && iscode) print "    UNCLOSED code fence opened ~line " start }
' "${FILE}")"
if [[ -n "${LONG_BLOCKS}" ]]; then
  while IFS= read -r line; do fail "over-length / unclosed code block:${line}"; done <<< "${LONG_BLOCKS}"
else
  echo "  ok — all fenced code blocks <= ${MAX_SNIPPET_LINES} lines and closed"
fi
echo

# ---------------------------------------------------------------------------
# Isolate the Sources section: from a heading matching /sources/i to the next
# top-level heading (or EOF). All remaining checks run on this slice.
# ---------------------------------------------------------------------------
SRC="$(mktemp)"; trap 'rm -f "${SRC}"' EXIT
awk '
  BEGIN { insec=0 }
  /^#{1,6}[[:space:]].*[Ss]ources/ { insec=1; print; next }
  insec && /^#{1,6}[[:space:]]/ {
    # a new heading of same-or-shallower depth ends the section, but keep
    # nested tier sub-headings (### Primary, ### Accessible) inside.
    if ($0 ~ /[Pp]rimary|[Oo]fficial|[Aa]ccessible|[Ff]urther|[Tt]ier/) { print; next }
    insec=0; next
  }
  insec { print }
' "${FILE}" > "${SRC}"

if [[ ! -s "${SRC}" ]]; then
  fail "no \"Sources\" section found (expected a heading containing 'Sources')."
  echo
  echo "RESULT: ${VIOLATIONS} violation(s)."
  exit 1
fi

# ---------------------------------------------------------------------------
# 1) Two-tier presence.
# ---------------------------------------------------------------------------
echo "== Two-tier structure =="
if grep -qiE 'primary|official' "${SRC}"; then
  echo "  ok — Primary / Official tier present"
else
  fail "missing the 'Primary / Official' tier."
fi
if grep -qiE 'accessible|further reading' "${SRC}"; then
  echo "  ok — Accessible / Further reading tier present"
else
  fail "missing the 'Accessible / Further reading' tier."
fi
echo

# ---------------------------------------------------------------------------
# 2) No markdown hidden links [text](url). Plain-text URLs only.
# ---------------------------------------------------------------------------
echo "== Plain-text URLs only (no hidden markdown links) =="
HIDDEN="$(grep -nE "\[[^]]+\]\((https?://|${SOURCE_PATH_PREFIX})[^)]*\)" "${SRC}" || true)"
if [[ -n "${HIDDEN}" ]]; then
  while IFS= read -r l; do fail "markdown hidden link (use a bare URL): ${l}"; done <<< "${HIDDEN}"
else
  echo "  ok — no [text](url) hidden links in Sources"
fi
echo

# ---------------------------------------------------------------------------
# 3) No academic-model residue (DOI / author-year) — "plain" style only.
#    For CITATION_STYLE="academic", DOI + author-year are the intended style.
# ---------------------------------------------------------------------------
echo "== Citation style (${CITATION_STYLE}) =="
if [[ "${CITATION_STYLE}" == "academic" ]]; then
  echo "  ok — academic style: DOI / author-year permitted (residue check skipped)"
else
  DOI="$(grep -niE 'doi:|doi\.org/' "${SRC}" || true)"
  # Author-year residue: catch the ACADEMIC "(Surname, 2020)" / "(Smith & Jones,
  # 2020)" / "(Doe et al., 2020)" shape, but NOT benign Tier-2 product/date labels
  # like "(GraalVM, 2024)" or "(June 2026)". We do this by:
  #   - requiring the comma form "(Name, YYYY)" (the academic separator), so a
  #     bare "(June 2026)" with no comma is never matched;
  #   - rejecting a surname token that runs straight into an uppercase letter
  #     (e.g. GraalVM, OpenJDK) — a real surname is a single cased word;
  #   - excluding month names, which are the common false positive.
  AY="$(grep -nE '\(([A-Z][a-z]+( &| et al\.)?,)[[:space:]]+(19|20)[0-9]{2}\)' "${SRC}" \
        | grep -vE '\((Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)[a-z]*,' \
        | grep -vE '\([A-Z][a-z]+[A-Z]' \
        || true)"
  if [[ -n "${DOI}" ]]; then
    while IFS= read -r l; do fail "DOI / academic reference (banned by house style): ${l}"; done <<< "${DOI}"
  fi
  if [[ -n "${AY}" ]]; then
    while IFS= read -r l; do fail "author-year citation (banned by house style): ${l}"; done <<< "${AY}"
  fi
  [[ -z "${DOI}${AY}" ]] && echo "  ok — no DOI / author-year residue"
fi
echo

# ---------------------------------------------------------------------------
# 4 & 5) Per-row URL + date-verified. We inspect "source rows": markdown table
# rows (| ... |) and bullet list items (- / *) inside the Sources section.
# Heuristic: a row that carries no URL and no source path is treated as a content
# row only if it contains a source label; pure separators / headers are skipped.
# ---------------------------------------------------------------------------
echo "== Per-row URL + date-verified =="
ROWS_CHECKED=0
while IFS= read -r row; do
  # candidate source rows: table rows or bullets
  case "${row}" in
    \|*\|*|-\ *|\*\ *) : ;;
    *) continue ;;
  esac
  # skip markdown table header / separator rows ( |---|---| ) and header labels
  case "${row}" in
    *---*) continue ;;
  esac
  # skip rows that are clearly column headers (contain 'URL' AND 'Title' etc.)
  if echo "${row}" | grep -qiE '\b(title|verified)\b.*\b(url|path)\b|\b(url|path)\b.*\b(title|verified)\b'; then
    # header row of the table — skip, it defines columns not data
    if ! echo "${row}" | grep -qE "https?://|${SOURCE_PATH_PREFIX}"; then continue; fi
  fi
  # an empty template stub row ( | | | | ) carries no info — warn, do not fail
  stripped="$(echo "${row}" | tr -d '|*- ')"
  [[ -z "${stripped}" ]] && continue

  ROWS_CHECKED=$((ROWS_CHECKED + 1))
  has_url=0; has_date=0
  if echo "${row}" | grep -qE "https?://[^[:space:]|)]+|${ROW_SOURCE_PATH_GREP}"; then has_url=1; fi
  # date-verified marker: a YYYY-MM-DD, a checked/empty box, or the word Verified
  if echo "${row}" | grep -qE '[0-9]{4}-[0-9]{2}-[0-9]{2}|☑|☒|✔|\[x\]|\[X\]|[Vv]erified'; then has_date=1; fi

  if [[ "${has_url}" -eq 0 ]]; then
    fail "source row has no plain-text URL / source path: ${row}"
  fi
  if [[ "${has_date}" -eq 0 ]]; then
    # Unchecked template box (☐ / [ ]) means "not yet verified" — that is a real
    # gate violation for a draft going to approval, so fail it.
    fail "source row has no date-verified / Verified marker: ${row}"
  fi
done < "${SRC}"

if [[ "${ROWS_CHECKED}" -eq 0 ]]; then
  warn "no concrete source rows detected — the Sources section may be an empty template (template stubs are tolerated; a real draft must have rows)."
else
  echo "  inspected ${ROWS_CHECKED} source row(s)"
fi
echo

# ---------------------------------------------------------------------------
# Verdict
# ---------------------------------------------------------------------------
if [[ "${VIOLATIONS}" -gt 0 ]]; then
  echo "RESULT: ${VIOLATIONS} structural violation(s) in citations."
  exit 1
fi
echo "RESULT: citations are structurally clean (two-tier, plain URLs, dated, snippets <= ${MAX_SNIPPET_LINES} lines)."
exit 0
