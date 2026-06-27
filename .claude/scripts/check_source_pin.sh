#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# check_source_pin.sh  —  PIPELINE STEP 0 gate (HARD)
# -----------------------------------------------------------------------------
# USED BY: ALL book types. This is the universal "are we reading the right,
#          frozen source of truth?" gate. For a code/docs repo it asserts the
#          clone is on the pinned SHA; for a frozen-corpus authority it asserts
#          the corpus directory is present (set EXPECT_GIT=false in CONFIG).
#
# Asserts that the pinned authority clone exists locally and is checked out at the
# EXACT commit SHA recorded in the book's SOURCE-PIN.md. No verification of any
# fact may proceed unless this gate passes. The clone may live in an ephemeral
# location, so it can vanish on reboot or a sweep — that is expected; this script
# is how the pipeline notices and refuses to read a wrong or missing source.
#
# Source of truth: SOURCE-PIN.md (NOT the clone on disk). This script READS the
# expected tag / SHA / clone path from that file so there is one place to re-pin;
# the CONFIG block only provides defensive defaults if the parse comes up empty.
#
# Exit: 0 = on-tag and present.  1 = absent or off-tag (hard fail, blocks
#       pipeline) and prints the exact re-clone command.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
AUTHORITY_REPO="{URL}"          # (this book is MULTI-AUTHORITY: no single repo — see SOURCE-PIN.md)
DEFAULT_TAG="multi-authority (see SOURCE-PIN.md; no single tag)"                  # fallback if SOURCE-PIN.md parse is empty
DEFAULT_SHA="n/a-multi-authority"                  # fallback if SOURCE-PIN.md parse is empty
DEFAULT_CLONE_PATH="${CLAUDE_JOB_DIR:-/tmp}/source-pin-clone"    # guarded (was $CLAUDE_JOB_DIR/tmp — unbound under set -u); unused in multi-authority mode
PIN_FILE_REL="00-strategy/SOURCE-PIN.md"        # e.g. 00-strategy/SOURCE-PIN.md (path under repo root)
# Regex fragments used to PARSE SOURCE-PIN.md table rows. Keep these matched to
# your SOURCE-PIN.md row labels. The clone-path matcher must match the kind of
# path you use (e.g. an absolute /tmp/... path, or a repo-relative dir).
PIN_PATH_GREP='/tmp/[^[:space:]`|]+'   # matcher for the "Clone path" cell value
TAG_GREP='[0-9]+\.[0-9]+\.[0-9]+'      # matcher for an x.y.z tag (edit for edition labels)
EXPECT_GIT="true"                      # "false" for a non-git frozen-corpus authority
# THIS BOOK: SOURCE-PIN.md is a MULTI-AUTHORITY version table (one row per tool/spec,
# each pinned to its own official channel) — there is NO single clone or SHA. In this
# mode the Step-0 gate is "the pin file is present and populated"; per-authority versions
# are verified at use (the companion-module green builds + the source-verifier pass).
MULTI_AUTHORITY="true"
# =====================================================================

# --- Locate the repo root (this script lives in <root>/.claude/scripts) -------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
PIN_FILE="${REPO_ROOT}/${PIN_FILE_REL}"

if [[ ! -f "${PIN_FILE}" ]]; then
  echo "FAIL: cannot find the pin file at ${PIN_FILE}" >&2
  echo "      The pin file IS the source of truth; without it nothing can verify." >&2
  exit 1
fi

# --- Multi-authority profile: the pin FILE is the authority (no single clone/SHA) ---
# For this book there is no one repo to check out; SOURCE-PIN.md is a version table of
# many authorities. The Step-0 gate therefore asserts the pin file is present and
# populated (version rows + pinned markers). Per-authority versions are verified at use.
if [[ "${MULTI_AUTHORITY}" == "true" ]]; then
  rows="$(grep -cE '\*\*[0-9][0-9.]*' "${PIN_FILE}" || true)"
  pinned="$(grep -cE '✅ *(re-)?pinned' "${PIN_FILE}" || true)"
  echo "check_source_pin: MULTI-AUTHORITY pin (no single clone) — ${PIN_FILE_REL}"
  if [[ "${rows}" -ge 5 && "${pinned}" -ge 3 ]]; then
    echo "PASS: SOURCE-PIN.md present and populated (${rows} version row(s), ${pinned} pinned marker(s));"
    echo "      it is the source of truth — per-authority versions verified at use."
    exit 0
  fi
  echo "FAIL: SOURCE-PIN.md present but looks unpopulated (version rows=${rows}, pinned markers=${pinned})." >&2
  echo "      Re-run /pin-source to populate the authority table." >&2
  exit 1
fi

# --- Parse pin fields from the markdown table rows ---------------------------
# Rows look like:  | Pinned tag | **3.33.2** ... |   | Commit SHA | **<sha>** |
# We pull the first 40-hex token for the SHA, the tag token, and the clone-path
# token. Robust to surrounding markdown/bold.
PIN_SHA="$(grep -iE '\| *Commit SHA *\|' "${PIN_FILE}" \
            | grep -oE '[0-9a-f]{40}' | head -n1 || true)"
PIN_TAG="$(grep -iE '\| *Pinned tag *\|' "${PIN_FILE}" \
            | grep -oE "${TAG_GREP}" | head -n1 || true)"
PIN_PATH="$(grep -iE '\| *Clone path *\|' "${PIN_FILE}" \
            | grep -oE "${PIN_PATH_GREP}" | head -n1 || true)"

# Fall back to CONFIG defaults if the table parse comes up empty (defensive).
PIN_SHA="${PIN_SHA:-${DEFAULT_SHA}}"
PIN_TAG="${PIN_TAG:-${DEFAULT_TAG}}"
PIN_PATH="${PIN_PATH:-${DEFAULT_CLONE_PATH}}"

RECLONE_CMD="git clone --branch ${PIN_TAG} --depth 1 ${AUTHORITY_REPO} ${PIN_PATH}"

echo "check_source_pin: expecting tag ${PIN_TAG} @ ${PIN_SHA}"
echo "                  clone path   ${PIN_PATH}"

# --- Gate 1: clone directory must exist --------------------------------------
if [[ ! -d "${PIN_PATH}" ]]; then
  echo "FAIL: pinned source is ABSENT at ${PIN_PATH}" >&2
  echo "      (an ephemeral clone is expected to vanish after a reboot)" >&2
  echo "Re-fetch it with exactly:" >&2
  echo "  ${RECLONE_CMD}" >&2
  exit 1
fi

# Non-git frozen corpus: presence is the whole gate.
if [[ "${EXPECT_GIT}" != "true" ]]; then
  echo "PASS: ${PIN_PATH} is present (non-git frozen corpus; SHA check skipped)."
  exit 0
fi

# --- Gate 2: it must be a git checkout we can interrogate --------------------
if ! git -C "${PIN_PATH}" rev-parse --git-dir >/dev/null 2>&1; then
  echo "FAIL: ${PIN_PATH} exists but is not a git checkout." >&2
  echo "      Remove it and re-fetch with:" >&2
  echo "  rm -rf ${PIN_PATH} && ${RECLONE_CMD}" >&2
  exit 1
fi

# --- Gate 3: HEAD must equal the pinned SHA ----------------------------------
HEAD_SHA="$(git -C "${PIN_PATH}" rev-parse HEAD)"
if [[ "${HEAD_SHA}" != "${PIN_SHA}" ]]; then
  echo "FAIL: clone is OFF-TAG." >&2
  echo "      HEAD     = ${HEAD_SHA}" >&2
  echo "      expected = ${PIN_SHA} (tag ${PIN_TAG})" >&2
  echo "      Verifying against the wrong source is forbidden. Re-fetch with:" >&2
  echo "  rm -rf ${PIN_PATH} && ${RECLONE_CMD}" >&2
  exit 1
fi

echo "PASS: ${PIN_PATH} is present and on-tag (${PIN_TAG} @ ${HEAD_SHA})."
exit 0
