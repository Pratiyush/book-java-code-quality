#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# verify_sources.sh <file>  —  PIPELINE STEPS 2 & 5 gate (HARD, heuristic)
# -----------------------------------------------------------------------------
# USED BY: ALL book types as a SOURCE-TRACE gate (FLOOR C without the compile
#          clause). The fenced-code-block report and the GAV/config-key token
#          classes are TECHNICAL / REFERENCE profile (see BOOK-TYPE-PROFILES.md);
#          book types without {{GATES_OFF}}=example-build keep them, others can
#          blank out those token-class regexes in the CONFIG block and rely on
#          the version/identifier search plus the human source-verifier.
#
# Static source-trace audit for a dossier (02-research/.../*_RESEARCH.md) or a
# draft (03-drafts/.../*_v?.md). Extracts the load-bearing factual tokens from
# the file and greps each one against the pinned authority clone (docs + source):
#
#   * identifier keys   — e.g. config-key names ({{INVENT_UNITS}})
#   * version strings    — x.y.z literals (must be the pinned version, not drift)
#   * dependency coords  — e.g. Maven GAVs ({{INVENT_UNITS}})
#   * fenced code blocks  — reported for human/snippet-length review
#
# Each token is printed as FOUND or MISSING. A MISSING identifier / coordinate is
# a hard signal the fact may be invented or off-version -> nonzero exit.
#
# HEURISTIC LIMITS (important, by design — we never false-FAIL on these):
#   - This is a STATIC text search. It cannot compile code, resolve a dependency
#     graph, or confirm a signature is callable. It only confirms the literal
#     token appears SOMEWHERE under the pinned tree.
#   - A token can legitimately be absent from the clone (e.g. documented only in
#     release notes, a third-party coordinate, an illustrative value). Such
#     tokens are reported MISSING so a human/source-verifier agent can adjudicate.
#   - Version strings that are NOT the pinned version are flagged as WARN (a
#     chapter may legitimately mention an older deprecation), not hard-failed.
#
# Reads the clone path + version from the book's SOURCE-PIN.md.
# Exit: 0 = no MISSING identifiers/coords.  1 = at least one MISSING. 2 = usage.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
DEFAULT_CLONE_PATH="{{CLONE_PATH}}"    # e.g. /tmp/quarkus-3.33.2
DEFAULT_VER="{{TAG}}"                  # e.g. 3.33.2 (the pinned version literal)
DEFAULT_SHA="{{SHA}}"                  # e.g. 2f9a60b84d3338f5b81cd83a2b4b853c41071a0f
PIN_FILE_REL="{{PIN_FILE_REL}}"        # e.g. 00-strategy/SOURCE-PIN.md
PIN_PATH_GREP='/tmp/[^[:space:]`|]+'   # matcher for the "Clone path" cell value
TAG_GREP='[0-9]+\.[0-9]+\.[0-9]+'      # matcher for an x.y.z tag
# --- Token classes to extract & trace (TECHNICAL / REFERENCE profile) --------
# Identifier keys: e.g. config keys "quarkus.*". Blank to disable that class.
KEY_REGEX='quarkus\.[a-zA-Z0-9._-]*[a-zA-Z0-9]'
KEY_EXCLUDE='quarkus\.io'              # bare host that is never a config key (blank to skip)
KEY_LABEL='Config keys'
# Dependency coordinates: e.g. Maven GAVs "io.quarkus:artifact". Blank to disable.
GAV_REGEX='io\.quarkus:[a-zA-Z0-9._-]+'
GAV_PREFIX='io.quarkus:'              # stripped to get the searchable artifact name
GAV_LABEL='Maven GAVs (io.quarkus:*)'
# Version literal shape (re-used for the version-drift report).
VER_REGEX='\b[0-9]+\.[0-9]+\.[0-9]+\b'
# camelCase->kebab normalization: ON for frameworks whose @Mapping keys differ
# between source (camelCase) and runtime (kebab-case). Set "false" to disable.
KEBAB_NORMALIZE="true"
# File extensions searched inside the clone (the grep --include set).
SEARCH_INCLUDES=(--include='*.adoc' --include='*.java' --include='*.properties'
                 --include='*.yml' --include='*.yaml' --include='*.xml' --include='*.md'
                 --include='*.kt' --include='*.json')
# =====================================================================

if [[ $# -ne 1 ]]; then
  echo "usage: verify_sources.sh <dossier-or-draft.md>" >&2
  exit 2
fi
TARGET="$1"
if [[ ! -f "${TARGET}" ]]; then
  echo "FAIL: file not found: ${TARGET}" >&2
  exit 2
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
PIN_FILE="${REPO_ROOT}/${PIN_FILE_REL}"

PIN_PATH="$(grep -iE '\| *Clone path *\|' "${PIN_FILE}" 2>/dev/null \
            | grep -oE "${PIN_PATH_GREP}" | head -n1 || true)"
PIN_VER="$(grep -iE '\| *Pinned tag *\|' "${PIN_FILE}" 2>/dev/null \
            | grep -oE "${TAG_GREP}" | head -n1 || true)"
PIN_PATH="${PIN_PATH:-${DEFAULT_CLONE_PATH}}"
PIN_VER="${PIN_VER:-${DEFAULT_VER}}"
# Pinned commit SHA (see SOURCE-PIN.md). The clone may be EPHEMERAL, so we verify
# the tree is present AND on this exact SHA before trusting any FOUND result.
PIN_SHA="${DEFAULT_SHA}"

# ---------------------------------------------------------------------------
# SHA GUARD: the clone may be absent OR present on the WRONG tag/SHA. Either case
# silently produces false-PASS (FOUND) or false-FAIL (MISSING) results against
# the wrong tree. Hard-exit BEFORE any token is searched. Prefer the canonical
# check_source_pin.sh if present; otherwise fall back to an inline
# `git rev-parse HEAD` == PIN_SHA assertion.
# ---------------------------------------------------------------------------
if [[ ! -d "${PIN_PATH}" ]]; then
  echo "FAIL: pinned clone absent at ${PIN_PATH} — run check_source_pin.sh first." >&2
  exit 1
fi
if [[ -x "${SCRIPT_DIR}/check_source_pin.sh" ]]; then
  if ! "${SCRIPT_DIR}/check_source_pin.sh" >/dev/null 2>&1; then
    echo "FAIL: pinned clone at ${PIN_PATH} failed check_source_pin.sh (off-tag or dirty)." >&2
    echo "      Re-pin to ${PIN_VER} (SHA ${PIN_SHA}) before verifying." >&2
    exit 1
  fi
else
  HEAD_SHA="$(git -C "${PIN_PATH}" rev-parse HEAD 2>/dev/null || true)"
  if [[ "${HEAD_SHA}" != "${PIN_SHA}" ]]; then
    echo "FAIL: pinned clone at ${PIN_PATH} is off-tag." >&2
    echo "      HEAD=${HEAD_SHA:-<none>} expected=${PIN_SHA} (tag ${PIN_VER})." >&2
    echo "      Re-clone at the pinned tag before verifying — see SOURCE-PIN.md." >&2
    exit 1
  fi
fi

echo "verify_sources: ${TARGET}"
echo "                against ${PIN_PATH} (pinned ${PIN_VER})"
echo "                NOTE: static text search only — cannot compile or resolve deps."
echo

MISSING=0

# Helper: grep a literal token anywhere under the pinned tree (docs + source).
# -r recursive, -F fixed-string, -q quiet. We restrict to text-ish files for speed.
search_clone() {
  local token="$1"
  grep -rqsF "${SEARCH_INCLUDES[@]}" -- "${token}" "${PIN_PATH}" 2>/dev/null
}

# Normalize a camelCase identifier to kebab-case: some frameworks spell mapping
# keys camelCase in source but the generated/runtime property is kebab-case
# (e.g. quarkus.http.readTimeout -> quarkus.http.read-timeout). A literal search
# for one spelling false-MISSES the other. We insert '-' before each interior
# uppercase letter and lowercase it, so callers can search BOTH and treat
# FOUND-in-either as FOUND.
to_kebab() {
  printf '%s' "$1" | sed -E 's/([a-z0-9])([A-Z])/\1-\2/g' | tr '[:upper:]' '[:lower:]'
}

# Strip fenced code blocks into a temp file so identifiers inside prose AND code
# are both considered; also capture the blocks themselves for the length report.
WORK="$(mktemp)"
BLOCKS="$(mktemp)"
trap 'rm -f "${WORK}" "${BLOCKS}"' EXIT
cp "${TARGET}" "${WORK}"

# ---------------------------------------------------------------------------
# 1) Fenced code blocks — count and length-report (snippets must be <=9 lines;
#    that hard cap is enforced structurally by lint_citations.sh, here we only
#    surface them for the source-verifier agent). TECHNICAL / REFERENCE profile.
# ---------------------------------------------------------------------------
echo "== Fenced code blocks =="
awk '
  /^[[:space:]]*```/ {
    if (inblock) { print "  block #" n " : " (cnt) " line(s)"; inblock=0 }
    else { inblock=1; n++; cnt=0 }
    next
  }
  inblock { cnt++ }
  END { if (inblock) print "  block #" n " : UNCLOSED fence (check the file)" }
' "${TARGET}" | tee "${BLOCKS}"
[[ -s "${BLOCKS}" ]] || echo "  (none)"
echo

# ---------------------------------------------------------------------------
# 2) Identifier keys (e.g. config keys). TECHNICAL / REFERENCE profile.
#    Disabled when KEY_REGEX is blank.
# ---------------------------------------------------------------------------
if [[ -n "${KEY_REGEX}" ]]; then
  echo "== ${KEY_LABEL} =="
  if [[ -n "${KEY_EXCLUDE}" ]]; then
    KEYS="$(grep -oE "${KEY_REGEX}" "${WORK}" 2>/dev/null \
            | grep -vxE "${KEY_EXCLUDE}" | sort -u || true)"
  else
    KEYS="$(grep -oE "${KEY_REGEX}" "${WORK}" 2>/dev/null | sort -u || true)"
  fi
  if [[ -z "${KEYS}" ]]; then
    echo "  (none found)"
  else
    while IFS= read -r key; do
      [[ -z "${key}" ]] && continue
      kebab="${key}"
      [[ "${KEBAB_NORMALIZE}" == "true" ]] && kebab="$(to_kebab "${key}")"
      if search_clone "${key}" || { [[ "${kebab}" != "${key}" ]] && search_clone "${kebab}"; }; then
        echo "  FOUND   ${key}"
      else
        echo "  MISSING ${key}"
        MISSING=$((MISSING + 1))
      fi
    done <<< "${KEYS}"
  fi
  echo
fi

# ---------------------------------------------------------------------------
# 3) Dependency coordinates (e.g. Maven GAVs). TECHNICAL / REFERENCE profile.
#    Disabled when GAV_REGEX is blank.
# ---------------------------------------------------------------------------
if [[ -n "${GAV_REGEX}" ]]; then
  echo "== ${GAV_LABEL} =="
  GAVS="$(grep -oE "${GAV_REGEX}" "${WORK}" 2>/dev/null | sort -u || true)"
  if [[ -z "${GAVS}" ]]; then
    echo "  (none found)"
  else
    while IFS= read -r gav; do
      [[ -z "${gav}" ]] && continue
      artifact="${gav#${GAV_PREFIX}}"
      # A coordinate maps to a module dir or appears in build files.
      if search_clone "${artifact}"; then
        echo "  FOUND   ${gav}"
      else
        echo "  MISSING ${gav}"
        MISSING=$((MISSING + 1))
      fi
    done <<< "${GAVS}"
  fi
  echo
fi

# ---------------------------------------------------------------------------
# 4) Version strings — any x.y.z literal. The pinned version must be present;
#    any OTHER version is a WARN (could be a legit deprecation note) not a fail.
# ---------------------------------------------------------------------------
echo "== Version strings (x.y.z) =="
VERS="$(grep -oE "${VER_REGEX}" "${WORK}" 2>/dev/null | sort -u || true)"
if [[ -z "${VERS}" ]]; then
  echo "  (none found)"
else
  while IFS= read -r ver; do
    [[ -z "${ver}" ]] && continue
    if [[ "${ver}" == "${PIN_VER}" ]]; then
      echo "  OK      ${ver} (matches pin)"
    else
      echo "  WARN    ${ver} (not the pinned ${PIN_VER} — confirm it is an intentional historical reference, not version drift)"
    fi
  done <<< "${VERS}"
fi
echo

# ---------------------------------------------------------------------------
# Verdict
# ---------------------------------------------------------------------------
if [[ "${MISSING}" -gt 0 ]]; then
  echo "RESULT: ${MISSING} token(s) MISSING from ${PIN_PATH}."
  echo "        Each MISSING identifier / coordinate must be traced by hand or cut/flagged"
  echo "        to 09-flags/ (it may be invented, off-version, or third-party)."
  exit 1
fi
echo "RESULT: all extracted identifiers & coordinates trace to the pinned clone."
echo "        (Reminder: this is a static check — a human/source-verifier still"
echo "         confirms signatures and snippet correctness.)"
exit 0
