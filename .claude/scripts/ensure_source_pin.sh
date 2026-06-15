#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# ensure_source_pin.sh — self-heal for the (often EPHEMERAL) pinned authority clone.
#
# USED BY: ALL book types whose AUTHORITY_SOURCE is a clonable git repo (technical /
#          reference / any book that pins a code or docs *repository*). Book types
#          whose authority is a frozen bibliography / corpus / interview log (science,
#          business, narrative) instead point CLONE_PATH at a local source-of-truth
#          directory and may set AUTHORITY_REPO empty — see notes at the CONFIG block.
#
# The pinned source can live in a scratch/ephemeral location: a reboot, a /tmp sweep,
# or a fresh environment deletes it, and then every source-verification step has
# nothing to read. This script makes the outage self-healing instead of relying on a
# human remembering.
#
#   check (default) : fast — confirm the clone exists AND is on the pinned SHA.
#                     On miss, print a LOUD warning + the one-line fix and exit 1.
#                     Wire to a SessionStart hook so every session is told the moment
#                     the clone is gone.
#   --heal          : actually (re-)clone the pinned tag, then re-verify the SHA.
#
# Pin values MUST stay in sync with the book's SOURCE-PIN.md (the source of truth).
set -euo pipefail

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
AUTHORITY_REPO="{URL}"     # e.g. https://github.com/quarkusio/quarkus.git
AUTHORITY_TAG="multi-authority (see SOURCE-PIN.md; no single tag)"           # e.g. 3.33.2 (release tag / edition label)
AUTHORITY_SHA="n/a-multi-authority"           # e.g. 2f9a60b84d3338f5b81cd83a2b4b853c41071a0f
CLONE_PATH="$CLAUDE_JOB_DIR/tmp"       # e.g. /tmp/quarkus-3.33.2 (local pinned-source dir)
DOCS_SUBDIR="per-tool (see SOURCE-PIN.md)"     # e.g. docs/src/main/asciidoc (file count sanity-check; "." if flat)
# Non-repo book types (science/business/narrative): set AUTHORITY_REPO="" and keep
# CLONE_PATH pointing at the local frozen corpus; --heal then becomes a no-op guard.
# =====================================================================

on_tag() {
  [ -d "$CLONE_PATH/.git" ] || return 1
  [ "$(git -C "$CLONE_PATH" rev-parse HEAD 2>/dev/null)" = "$AUTHORITY_SHA" ]
}

heal() {
  if [ -z "$AUTHORITY_REPO" ]; then
    echo "ensure_source_pin: AUTHORITY_REPO is empty (non-repo authority) — nothing to clone." >&2
    echo "  Restore the frozen corpus at $CLONE_PATH by hand, then re-run 'check'." >&2
    exit 1
  fi
  echo "ensure_source_pin: (re-)cloning $AUTHORITY_TAG into $CLONE_PATH …"
  rm -rf "$CLONE_PATH"
  git clone --branch "$AUTHORITY_TAG" --depth 1 "$AUTHORITY_REPO" "$CLONE_PATH"
  if on_tag; then
    docs_dir="$CLONE_PATH/$DOCS_SUBDIR"
    if [ -d "$docs_dir" ]; then
      echo "ensure_source_pin: OK — $CLONE_PATH on $AUTHORITY_SHA ($(ls "$docs_dir" | wc -l | tr -d ' ') doc files in $DOCS_SUBDIR)."
    else
      echo "ensure_source_pin: OK — $CLONE_PATH on $AUTHORITY_SHA."
    fi
  else
    echo "ensure_source_pin: FAILED — clone is not on $AUTHORITY_SHA. Investigate before any verification read." >&2
    exit 1
  fi
}

case "${1:-check}" in
  --heal|heal)
    heal
    ;;
  check|*)
    if on_tag; then
      echo "ensure_source_pin: OK — pinned clone present and on-tag ($AUTHORITY_SHA)."
    else
      echo "============================================================" >&2
      echo "ensure_source_pin: ! PINNED SOURCE CLONE MISSING OR OFF-TAG." >&2
      echo "  Path : $CLONE_PATH (may be ephemeral — vanishes on reboot)" >&2
      echo "  Need : tag $AUTHORITY_TAG @ $AUTHORITY_SHA" >&2
      echo "  FIX  : bash .claude/scripts/ensure_source_pin.sh --heal" >&2
      echo "  No fact may be verified until this is healed." >&2
      echo "============================================================" >&2
      exit 1
    fi
    ;;
esac
