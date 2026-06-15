#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
set -euo pipefail
# =============================================================================
# approve_commit.sh <NN_slug>  —  PIPELINE STEP 13 (finalize & commit)
# -----------------------------------------------------------------------------
# USED BY: ALL book types. This is the universal "promote an approved draft into
#          the manuscript of record and commit it" step — it is book-type-
#          invariant (no code-specific logic).
#
# Runs ONLY after the human approval gate (step 12) has passed. It promotes an
# approved draft into the manuscript of record and records the commit:
#
#   1. GUARD: a .git dir must be present (the repo IS under git). If .git is
#      somehow absent this prints "not a git repository" and exits 1
#      (no partial moves).
#   2. Resolve the latest draft for <DRAFTS>/<NN_slug>/ (prefers _v3 > _v2 > _v1,
#      else the lone NN_slug*.md) and copy it to <APPROVED>/<NN_slug>.md.
#   3. Update the chapter tracker: mark the chapter APPROVED (created from a
#      minimal header if the tracker does not exist yet).
#   4. git add -A && git commit -m "ch NN approved — <topic>".
#      <topic> is derived from the draft's first H1 title; NN is the leading
#      two-digit dossier key in the slug. Pushing is left to the operator.
#
# Exit: 0 = committed.  1 = guard failure / missing inputs.  2 = usage.
# =============================================================================

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
DRAFTS_DIR_REL="{{DRAFTS_DIR_REL}}"      # e.g. 03-drafts
APPROVED_DIR_REL="{{APPROVED_DIR_REL}}"  # e.g. 04-approved
TRACKER_REL="{{TRACKER_REL}}"            # e.g. 01-index/CHAPTER-TRACKER.md
REMOTE_NAME="{{REPO_REMOTE}}"            # e.g. origin (push target reminder only)
# =====================================================================

if [[ $# -ne 1 ]]; then
  echo "usage: approve_commit.sh <NN_slug>   (e.g. 04_dev_mode)" >&2
  exit 2
fi
SLUG="$1"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
DRAFT_DIR="${REPO_ROOT}/${DRAFTS_DIR_REL}/${SLUG}"
APPROVED_DIR="${REPO_ROOT}/${APPROVED_DIR_REL}"
TRACKER="${REPO_ROOT}/${TRACKER_REL}"

# --- Validate slug shape: NN_short_name (two-digit zero-padded key) ----------
if [[ ! "${SLUG}" =~ ^[0-9]{2}_[a-z0-9_]+$ ]]; then
  echo "FAIL: slug must be NN_short_name (lowercase, underscores, two-digit key): got '${SLUG}'" >&2
  exit 1
fi
NN="${SLUG%%_*}"

# --- GUARD 1: inside a git repository ----------------------------------------
if [[ ! -d "${REPO_ROOT}/.git" ]]; then
  echo "not a git repository (expected .git at ${REPO_ROOT})"
  exit 1
fi

# --- Locate the draft to promote ---------------------------------------------
if [[ ! -d "${DRAFT_DIR}" ]]; then
  echo "FAIL: no draft directory at ${DRAFTS_DIR_REL}/${SLUG}/" >&2
  exit 1
fi

SRC=""
for v in v3 v2 v1; do
  cand="${DRAFT_DIR}/${SLUG}_${v}.md"
  if [[ -f "${cand}" ]]; then SRC="${cand}"; break; fi
done
# Fall back to a single non-gate-report markdown in the draft dir. We avoid
# `mapfile` (absent on bash 3.2 / macOS) and count matches portably.
if [[ -z "${SRC}" ]]; then
  LONE_LIST="$(find "${DRAFT_DIR}" -maxdepth 1 -type f -name "${SLUG}*.md" \
                ! -name '*_VERIFY.md' ! -name '*_AUDIT.md' ! -name '*_SCORE.md' 2>/dev/null | sort)"
  LONE_COUNT="$(printf '%s\n' "${LONE_LIST}" | grep -c . || true)"
  if [[ "${LONE_COUNT}" -eq 1 ]]; then SRC="$(printf '%s' "${LONE_LIST}" | head -n1)"; fi
fi
if [[ -z "${SRC}" ]]; then
  echo "FAIL: could not find a draft to promote in ${DRAFT_DIR}" >&2
  echo "      expected ${SLUG}_v3.md / _v2.md / _v1.md or a single ${SLUG}*.md" >&2
  exit 1
fi

# --- Derive the chapter topic from the draft's first H1 ----------------------
TOPIC="$(grep -m1 -E '^#[[:space:]]+' "${SRC}" | sed -E 's/^#[[:space:]]+//; s/[[:space:]]+$//' || true)"
TOPIC="${TOPIC:-${SLUG#*_}}"   # fall back to the slug's name portion

# --- Promote: copy draft -> <APPROVED>/<slug>.md -----------------------------
mkdir -p "${APPROVED_DIR}"
DEST="${APPROVED_DIR}/${SLUG}.md"
cp "${SRC}" "${DEST}"
echo "promoted ${SRC#${REPO_ROOT}/}  ->  ${DEST#${REPO_ROOT}/}"

# --- Update the chapter tracker ----------------------------------------------
TODAY="$(date +%F)"
mkdir -p "$(dirname "${TRACKER}")"
if [[ ! -f "${TRACKER}" ]]; then
  {
    echo "# CHAPTER-TRACKER — per-chapter status across every gate"
    echo
    echo "| Key | Slug | Status | Approved on |"
    echo "|---|---|---|---|"
  } > "${TRACKER}"
  echo "created ${TRACKER#${REPO_ROOT}/} (was absent)"
fi

if grep -qE "^\|[[:space:]]*${NN}[[:space:]]*\|" "${TRACKER}"; then
  # Update the existing row in place (portable BSD/GNU sed via a temp file).
  TMP="$(mktemp)"
  awk -v nn="${NN}" -v slug="${SLUG}" -v today="${TODAY}" '
    BEGIN { FS="|"; OFS="|" }
    {
      row=$0
      # match a table row whose first cell is the key NN
      if (row ~ ("^\\|[[:space:]]*" nn "[[:space:]]*\\|")) {
        print "| " nn " | " slug " | APPROVED | " today " |"
        next
      }
      print row
    }
  ' "${TRACKER}" > "${TMP}"
  mv "${TMP}" "${TRACKER}"
  echo "updated tracker row for key ${NN} -> APPROVED (${TODAY})"
else
  printf '| %s | %s | APPROVED | %s |\n' "${NN}" "${SLUG}" "${TODAY}" >> "${TRACKER}"
  echo "appended tracker row for key ${NN} -> APPROVED (${TODAY})"
fi

# --- Commit (one commit per approved chapter; push left to the operator) -----
COMMIT_MSG="ch ${NN} approved — ${TOPIC}"
git -C "${REPO_ROOT}" add -A
if git -C "${REPO_ROOT}" diff --cached --quiet; then
  echo "NOTE: nothing staged changed — chapter may already be committed. No commit made."
  exit 0
fi
git -C "${REPO_ROOT}" commit -m "${COMMIT_MSG}"
echo "committed: ${COMMIT_MSG}"
echo "reminder: push to ${REMOTE_NAME} when ready; any PUBLIC companion-repo push stays gated."
exit 0
