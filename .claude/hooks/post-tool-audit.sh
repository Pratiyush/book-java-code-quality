#!/usr/bin/env bash
# =============================================================================
# post-tool-audit.sh — the AUDIT-LOG hook. Wired as a PostToolUse hook in
# .claude/settings.json, it fires after EVERY tool call and appends one compact
# JSONL line to 10-logs/audit.jsonl — so "each and every action" is on the
# record without anyone remembering to log it. Fast (one jq pass), never blocks
# a tool (always exits 0), and truncates long targets so the log stays skimmable.
#
# Each line: {ts, kind:"tool", tool, target, ok, session}
#   ts      ISO-8601 UTC
#   tool    the tool name (Bash / Edit / Write / Read / …)
#   target  the salient argument (file_path / command / url / pattern / desc), ≤160 chars
#   ok      whether the tool reported success (best-effort from tool_response)
#
# Read by status.py to render the Audit page of the dashboard.
# =============================================================================
set -uo pipefail
root="${CLAUDE_PROJECT_DIR:-$(git rev-parse --show-toplevel 2>/dev/null || true)}"
[ -n "$root" ] || exit 0
log="$root/10-logs/audit.jsonl"
mkdir -p "$root/10-logs" 2>/dev/null || exit 0

ts="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
input="$(cat)"

command -v jq >/dev/null 2>&1 || exit 0
printf '%s' "$input" | jq -c --arg ts "$ts" '{
  ts: $ts,
  kind: "tool",
  tool: (.tool_name // "?"),
  target: ((.tool_input.file_path // .tool_input.command // .tool_input.url
            // .tool_input.pattern // .tool_input.description // .tool_input.prompt // "")
           | tostring | gsub("[\n\r]+"; " ") | .[0:160]),
  ok: ((.tool_response.success // (if (.tool_response.error // "") != "" then false else true end)) // true),
  session: (.session_id // "")
}' >> "$log" 2>/dev/null || true
exit 0
