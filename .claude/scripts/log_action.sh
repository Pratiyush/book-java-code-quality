#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# =============================================================================
# log_action.sh — append ONE provenance line to the activity log (JSONL).
# Every gate run, every edit, every commit appends a line, so audit.sh can
# confirm "every agent used / file touched was logged" (the richer provenance
# layer on top of the artifact-derived audit).
#
# USED BY: ALL book types. The provenance layer is genre-independent — any gate
#          run / edit / commit on any book appends here.
#
# Usage:
#   log_action.sh <actor> <step> <key> <action> <verdict> [note] [files...]
#   e.g. log_action.sh source-verifier 5 03 gate-run PASS "12 facts traced" 03_slug_v1.md
# Exit: 0 appended / 2 usage.
# =============================================================================
set -euo pipefail

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
LOG_REL="10-logs/activity.jsonl"   # e.g. 10-logs/activity.jsonl (append-only provenance log)
# =====================================================================

[ $# -ge 5 ] || { echo "usage: log_action.sh <actor> <step> <key> <action> <verdict> [note] [files...]" >&2; exit 2; }
REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
LOG="$REPO_ROOT/$LOG_REL"
mkdir -p "$(dirname "$LOG")"
actor="$1"; step="$2"; key="$3"; action="$4"; verdict="$5"; note="${6:-}"; shift $(( $# < 6 ? $# : 6 ))
files="$*"
ts="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
esc(){ printf '%s' "$1" | sed 's/\\/\\\\/g; s/"/\\"/g'; }
printf '{"ts":"%s","actor":"%s","step":"%s","key":"%s","action":"%s","verdict":"%s","note":"%s","files":"%s"}\n' \
  "$ts" "$(esc "$actor")" "$(esc "$step")" "$(esc "$key")" "$(esc "$action")" "$(esc "$verdict")" "$(esc "$note")" "$(esc "$files")" \
  >> "$LOG"
