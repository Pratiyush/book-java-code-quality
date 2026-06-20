# Audit log — every action on the record

The audit trail lives at **`10-logs/audit.jsonl`** (append-only, one JSON object per line). It feeds
the **Audit** page of the HTML dashboard (`status.py` renders the tail).

## Two writers

| Writer | Captures | How |
|---|---|---|
| `.claude/hooks/post-tool-audit.sh` | **every tool call** (Bash / Edit / Write / Read / …) | a `PostToolUse` hook — fires automatically after each tool, appends `{ts, kind:"tool", tool, target, ok, session}` |
| `.claude/scripts/audit_log.sh` | **semantic milestones** ("capstone built", "auto-approved Ch 12", "report regenerated") | called explicitly; appends `{ts, kind:"milestone", actor, action, target, detail}` |

The milestone logger works now (no setup). The per-tool hook needs a one-time enable, below.

## Enable automatic per-tool capture (needs your OK once)

Installing a `PostToolUse` hook edits the agent's own startup config (`.claude/settings.json`), which
the harness gates behind explicit authorization — so I can't write it for you unsupervised. To turn on
"log **every** action", add this to **`.claude/settings.json`** (create the file if absent):

```json
{
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "*",
        "hooks": [
          { "type": "command", "command": "bash \"$CLAUDE_PROJECT_DIR/.claude/hooks/post-tool-audit.sh\"" }
        ]
      }
    ]
  }
}
```

Easiest path: run `/update-config` and ask it to add that PostToolUse hook, or just say
"enable the audit hook" and I'll apply it once you've authorized settings edits.

Until then, the milestone logger keeps the trail meaningful — every build, commit, gate, and
auto-approval is recorded.

## Entry shapes

```jsonc
// a tool call (from the hook)
{"ts":"2026-06-20T18:46:00Z","kind":"tool","tool":"Bash","target":"mvn -B -Pquality verify","ok":true,"session":"…"}
// a milestone (from audit_log.sh)
{"ts":"2026-06-20T18:46:00Z","kind":"milestone","actor":"claude","action":"build","target":"08-companion-code","detail":"BUILD SUCCESS, 17 modules"}
```

## Manual logging

```bash
bash .claude/scripts/audit_log.sh "<action>" "<target>" "[detail]"
# e.g.
bash .claude/scripts/audit_log.sh "auto-approve" "Ch 12" "score 46/50 (92%) + floors PASS + independent gates"
```
