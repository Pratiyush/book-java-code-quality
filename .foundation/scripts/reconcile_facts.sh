#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# reconcile_facts.sh  —  PIPELINE STEP 10 gate (HARD, cross-chapter)
# -----------------------------------------------------------------------------
# USED BY: ALL book types. The version-drift + LEDGER continuity-bible checks are
#          universal (any book pins a version/edition and keeps a bible). The GAV
#          and config-key token-class reports are TECHNICAL / REFERENCE profile
#          (see BOOK-TYPE-PROFILES.md); blank GAV_REGEX / KEY_REGEX in the CONFIG
#          block to disable them for a non-code book — version drift + the bible
#          cross-check still run.
#
# Cross-chapter fact reconciliation. Drafts and approved chapters must not
# contradict each other or the continuity bible in LEDGER.md. This script
# harvests canonical facts from every file under 03-drafts/ and 04-approved/
# and reports DISAGREEMENTS:
#
#   * VERSION DRIFT  — an x.y.z literal adjacent to a SUBJECT marker (the pinned
#                      subject's name/coordinates) that is NOT the pinned version
#                      is a contradiction; in an APPROVED chapter it HARD-fails.
#                      Every OTHER x.y.z (third-party deps, specs, runtimes) is a
#                      legitimate third-party version and only WARNs.
#   * GAV SET        — every dependency coordinate used, with the files that use
#                      it (so a renamed/typo'd artifact stands out). TECHNICAL.
#   * IDENTIFIER KEYS — every config-key-style identifier used across chapters,
#                      with files (so two spellings of the "same" key surface).
#                      TECHNICAL.
#   * LEDGER CHECK   — if LEDGER.md exists, its recorded pinned version is
#                      compared to the chapters'; keys/GAVs in chapters that the
#                      LEDGER continuity bible never mentions are listed for a
#                      human to fold into the bible.
#
# HEURISTIC LIMITS (commented, never false-FAIL):
#   - "Disagreement" here means the same token appears with differing spelling or
#     an unexpected version — it cannot judge semantic intent. Output is a REPORT
#     for the reconciler agent + human; we exit 0 with WARNINGS and only exit 1 on
#     an unambiguous version contradiction inside an APPROVED chapter.
#   - LEDGER.md and CHAPTER-TRACKER.md may not exist yet in a fresh worktree;
#     their absence is reported, not failed.
#
# Reads the pinned version from the book's SOURCE-PIN.md.
# Exit: 0 = no hard contradiction (warnings allowed). 1 = version contradiction
#       in an approved chapter.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
DEFAULT_VER="{{TAG}}"                  # e.g. 3.33.2 (fallback pinned version)
PIN_FILE_REL="{{PIN_FILE_REL}}"        # e.g. 00-strategy/SOURCE-PIN.md
DRAFTS_DIR_REL="{{DRAFTS_DIR_REL}}"    # e.g. 03-drafts
APPROVED_DIR_REL="{{APPROVED_DIR_REL}}" # e.g. 04-approved
LEDGER_REL="{{LEDGER_REL}}"            # e.g. LEDGER.md (continuity bible)
TAG_GREP='[0-9]+\.[0-9]+\.[0-9]+'      # x.y.z tag matcher in SOURCE-PIN.md
VER_REGEX='[0-9]+\.[0-9]+\.[0-9]+'     # version literal shape harvested from chapters
# SUBJECT_CTX: a line carrying ANY of these markers is "subject context", so a
# version on it is THE pinned subject's version (a non-pin value there is a real
# contradiction). e.g. for Quarkus: io.quarkus|quarkus-bom|quarkus.platform...|"Quarkus <ver>".
SUBJECT_CTX='io\.quarkus|quarkus-bom|quarkus\.platform\.version|[Qq]uarkus[[:space:]]+(version[[:space:]]+)?[0-9]'
# Dependency-coordinate matcher (TECHNICAL). Blank to disable the GAV report.
GAV_REGEX='io\.quarkus:[a-zA-Z0-9._-]+'
GAV_LABEL='Maven GAVs in use (io.quarkus:*)'
# Identifier-key matcher (TECHNICAL). Blank to disable the key report.
KEY_REGEX='quarkus\.[a-zA-Z0-9._-]*[a-zA-Z0-9]'
KEY_EXCLUDE='quarkus\.io'              # bare host that is never a config key (blank to skip)
KEY_LABEL='Config keys in use (quarkus.*)'
# Known third-party / spec versions — informational only, never reported. Extend
# as the book grows; absence here only downgrades a line to WARN.
THIRD_PARTY_ALLOW="$(cat <<'EOF'
21.3.10
17.0.0
17.0.9
21.0.0
3.0.0
10.0.0
1.12.0
EOF
)"
# =====================================================================

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
PIN_FILE="${REPO_ROOT}/${PIN_FILE_REL}"
DRAFTS_DIR="${REPO_ROOT}/${DRAFTS_DIR_REL}"
APPROVED_DIR="${REPO_ROOT}/${APPROVED_DIR_REL}"
LEDGER="${REPO_ROOT}/${LEDGER_REL}"

PIN_VER="$(grep -iE '\| *Pinned tag *\|' "${PIN_FILE}" 2>/dev/null \
            | grep -oE "${TAG_GREP}" | head -n1 || true)"
PIN_VER="${PIN_VER:-${DEFAULT_VER}}"

echo "reconcile_facts: pinned version is ${PIN_VER}"
echo "                 scanning ${DRAFTS_DIR_REL}/ and ${APPROVED_DIR_REL}/"
echo

# Collect target markdown files (chapters), skipping per-gate report files so we
# reconcile prose, not the gate logs themselves. We use a temp file + `while
# read` instead of `mapfile`/arrays so the script runs on the bash 3.2 that
# ships with macOS as well as on modern bash (true POSIX-portability target).
FILES_LIST="$(mktemp)"
GAV_TMP="$(mktemp)"
KEY_TMP="$(mktemp)"
trap 'rm -f "${FILES_LIST}" "${GAV_TMP}" "${KEY_TMP}"' EXIT

# `|| true` guards against `set -e` aborting when find/grep produce no lines
# (an empty repo is a valid state, not an error).
{ find "${DRAFTS_DIR}" "${APPROVED_DIR}" -type f -name '*.md' 2>/dev/null \
    | grep -vE '_(VERIFY|AUDIT|SCORE|RESEARCH)\.md$' || true ; } | sort > "${FILES_LIST}"

if [[ ! -s "${FILES_LIST}" ]]; then
  echo "No chapter files under ${DRAFTS_DIR_REL}/ or ${APPROVED_DIR_REL}/ yet — nothing to reconcile."
  exit 0
fi

HARD_CONTRA=0

# ---------------------------------------------------------------------------
# 1) Version drift
# ---------------------------------------------------------------------------
# The book is pinned to ONE subject version, but an approved chapter legitimately
# names many OTHER x.y.z versions — runtimes, specs, third-party deps. Hard-
# failing on every literal != pin is wrong (it would block any chapter that cites
# a third-party version). We only HARD-fail a literal when it sits in SUBJECT
# context — i.e. the version is adjacent (same line) to a subject marker AND
# differs from the pin. Every other x.y.z is third-party: WARN. An optional
# allowlist of well-known versions is suppressed entirely (not even a WARN).
echo "== Version drift (expected subject version: ${PIN_VER}) =="
DRIFT_MARK="$(mktemp)"; : > "${DRIFT_MARK}"
while IFS= read -r f; do
  [[ -z "${f}" ]] && continue
  rel="${f#${REPO_ROOT}/}"
  # (a) SUBJECT-CONTEXT versions: only lines carrying a subject marker. A non-pin
  #     version here is a real contradiction in an approved chapter.
  while IFS= read -r v; do
    [[ -z "${v}" || "${v}" == "${PIN_VER}" ]] && continue
    echo "x" >> "${DRIFT_MARK}"
    if [[ "${f}" == "${APPROVED_DIR}"* ]]; then
      echo "  CONTRADICTION ${v}  in APPROVED  ${rel}  (subject is pinned to ${PIN_VER})"
      HARD_CONTRA=$((HARD_CONTRA + 1))
    else
      echo "  WARN(subject) ${v}  in draft     ${rel}  (subject context; confirm intentional)"
    fi
  done < <(grep -E "${SUBJECT_CTX}" "${f}" 2>/dev/null \
             | grep -oE "${VER_REGEX}" | sort -u)
  # (b) NON-SUBJECT versions: every other x.y.z literal (lines without a subject
  #     marker). These are third-party — WARN only, never a hard fail. Allowlisted
  #     versions are suppressed entirely.
  while IFS= read -r v; do
    [[ -z "${v}" || "${v}" == "${PIN_VER}" ]] && continue
    grep -qxF "${v}" <<<"${THIRD_PARTY_ALLOW}" && continue
    echo "x" >> "${DRIFT_MARK}"
    echo "  WARN(3rd)     ${v}  in          ${rel}  (third-party version; not the subject pin)"
  done < <(grep -vE "${SUBJECT_CTX}" "${f}" 2>/dev/null \
             | grep -oE "${VER_REGEX}" | sort -u)
done < "${FILES_LIST}"
[[ -s "${DRIFT_MARK}" ]] || echo "  ok — no drifting version literals"
rm -f "${DRIFT_MARK}"
echo

# ---------------------------------------------------------------------------
# 2) GAV set across chapters (TECHNICAL / REFERENCE). Skipped if GAV_REGEX blank.
# ---------------------------------------------------------------------------
if [[ -n "${GAV_REGEX}" ]]; then
  echo "== ${GAV_LABEL} =="
  : > "${GAV_TMP}"
  while IFS= read -r f; do
    [[ -z "${f}" ]] && continue
    rel="${f#${REPO_ROOT}/}"
    grep -oE "${GAV_REGEX}" "${f}" 2>/dev/null | sort -u \
      | while IFS= read -r g; do echo "${g}|${rel}"; done >> "${GAV_TMP}"
  done < "${FILES_LIST}"
  if [[ -s "${GAV_TMP}" ]]; then
    cut -d'|' -f1 "${GAV_TMP}" | sort -u | while IFS= read -r g; do
      users="$(grep -F "${g}|" "${GAV_TMP}" | cut -d'|' -f2 | paste -sd',' -)"
      echo "  ${g}  <-  ${users}"
    done
  else
    echo "  (no GAVs found)"
  fi
  echo
fi

# ---------------------------------------------------------------------------
# 3) Identifier-key set across chapters (TECHNICAL / REFERENCE). Skip if blank.
# ---------------------------------------------------------------------------
if [[ -n "${KEY_REGEX}" ]]; then
  echo "== ${KEY_LABEL} =="
  : > "${KEY_TMP}"
  while IFS= read -r f; do
    [[ -z "${f}" ]] && continue
    rel="${f#${REPO_ROOT}/}"
    if [[ -n "${KEY_EXCLUDE}" ]]; then
      grep -oE "${KEY_REGEX}" "${f}" 2>/dev/null | grep -vxE "${KEY_EXCLUDE}" | sort -u \
        | while IFS= read -r k; do echo "${k}|${rel}"; done >> "${KEY_TMP}"
    else
      grep -oE "${KEY_REGEX}" "${f}" 2>/dev/null | sort -u \
        | while IFS= read -r k; do echo "${k}|${rel}"; done >> "${KEY_TMP}"
    fi
  done < "${FILES_LIST}"
  if [[ -s "${KEY_TMP}" ]]; then
    cut -d'|' -f1 "${KEY_TMP}" | sort -u | while IFS= read -r k; do
      users="$(grep -F "${k}|" "${KEY_TMP}" | cut -d'|' -f2 | paste -sd',' -)"
      echo "  ${k}  <-  ${users}"
    done
  else
    echo "  (no identifier keys found)"
  fi
  echo
fi

# ---------------------------------------------------------------------------
# 4) LEDGER continuity-bible cross-check
# ---------------------------------------------------------------------------
echo "== ${LEDGER_REL} continuity-bible cross-check =="
if [[ ! -f "${LEDGER}" ]]; then
  echo "  NOTE: ${LEDGER_REL} not found at this path. Skipping bible diff."
else
  # Version recorded in the ledger should match the pin.
  LEDGER_VER="$(grep -oE "\b${VER_REGEX}\b" "${LEDGER}" 2>/dev/null | sort -u || true)"
  if echo "${LEDGER_VER}" | grep -qx "${PIN_VER}"; then
    echo "  ok — ${LEDGER_REL} records the pinned version ${PIN_VER}"
  else
    echo "  WARN — ${LEDGER_REL} does not clearly record ${PIN_VER}; confirm the bible is current"
  fi
  # GAVs / keys used in chapters but never mentioned in the bible.
  if [[ -s "${GAV_TMP}" ]]; then
    cut -d'|' -f1 "${GAV_TMP}" | sort -u | while IFS= read -r g; do
      grep -qF "${g}" "${LEDGER}" 2>/dev/null || echo "  bible-gap: GAV ${g} used in a chapter but absent from ${LEDGER_REL}"
    done
  fi
  if [[ -s "${KEY_TMP}" ]]; then
    cut -d'|' -f1 "${KEY_TMP}" | sort -u | while IFS= read -r k; do
      grep -qF "${k}" "${LEDGER}" 2>/dev/null || echo "  bible-gap: key ${k} used in a chapter but absent from ${LEDGER_REL}"
    done
  fi
fi
echo

# ---------------------------------------------------------------------------
# Verdict
# ---------------------------------------------------------------------------
if [[ "${HARD_CONTRA}" -gt 0 ]]; then
  echo "RESULT: ${HARD_CONTRA} version contradiction(s) inside APPROVED chapters — must fix."
  exit 1
fi
echo "RESULT: no hard contradictions. Review the WARN / bible-gap lines above with the reconciler agent."
exit 0
