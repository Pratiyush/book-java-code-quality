#!/usr/bin/env bash
# install_hooks.sh — copy the in-repo hooks into .git/hooks (idempotent).
set -euo pipefail
REPO="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"; cd "$REPO"
for h in .claude/hooks/*; do
  n="$(basename "$h")"; cp "$h" ".git/hooks/$n"; chmod +x ".git/hooks/$n"
  echo "installed .git/hooks/$n"
done
