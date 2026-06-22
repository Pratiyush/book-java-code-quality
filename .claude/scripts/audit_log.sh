#!/usr/bin/env bash
# =============================================================================
# audit_log.sh "<action>" "<target>" "[detail]"  —  log a MILESTONE to the audit
# trail (10-logs/audit.jsonl). The PostToolUse hook (post-tool-audit.sh) records
# the mechanical tool calls; this records the semantic milestones a human cares
# about — "capstone 02 built green", "auto-approved Ch 12", "report regenerated".
#
# Each line: {ts, kind:"milestone", actor:"claude", action, target, detail}
#
# Examples:
#   audit_log.sh "build" "capstones/02-fintech-ledger" "mvn -Pquality verify → BUILD SUCCESS, 12 tests"
#   audit_log.sh "auto-approve" "Ch 12" "score 46/50 (92%) + floors PASS + independent gates"
# =============================================================================
set -uo pipefail
root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
log="$root/10-logs/audit.jsonl"
mkdir -p "$root/10-logs"
ts="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
actor="${AUDIT_ACTOR:-claude}"

if command -v jq >/dev/null 2>&1; then
  jq -nc --arg ts "$ts" --arg ac "$actor" --arg a "${1:-}" --arg t "${2:-}" --arg d "${3:-}" \
    '{ts:$ts, kind:"milestone", actor:$ac, action:$a, target:$t, detail:$d}' >> "$log"
else
  # jq-less fallback (no special-char escaping; milestones are plain text)
  printf '{"ts":"%s","kind":"milestone","actor":"%s","action":"%s","target":"%s","detail":"%s"}\n' \
    "$ts" "$actor" "${1:-}" "${2:-}" "${3:-}" >> "$log"
fi
echo "audit: logged milestone '${1:-}' → $(basename "$log")"
