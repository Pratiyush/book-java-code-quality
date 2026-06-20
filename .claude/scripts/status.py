#!/usr/bin/env python3
"""
status.py — the status MATRIX + ETA report + anti-drift guard (purpose-built for this book).

Reads the authoritative artifacts (01-index/FINAL_INDEX.md for chapter→Part, the
CHAPTER-TRACKER for per-gate marks, the on-disk gate reports for evidence) and emits:

  1. 01-index/STATUS-MATRIX.md   — chapter × gate bubble grid + per-Part rollups +
                                   the needs-human queue + an honest ETA + the drift section.
  2. 10-logs/dashboard.html      — the same, as a colour-celled HTML dashboard
                                   (needs-human queue on top, per-part rollups, progress).

And it runs a DRIFT CHECK (the anti-drift guard):
  (A) gate-trail order — no HARD gate may be 🟢 done while an EARLIER HARD gate isn't;
  (B) evidence — every gate cell that claims done/self must have its report file on disk.
Exit: 0 clean · 1 drift found · 2 bad inputs.

Bubble legend (honest semantics — distinguishes a main-loop self-pass from an
independent-agent gate):
  🟢 done        independent gate passed (or a genuinely-complete step) + evidence on disk
  🟡 self/wip    main-loop self-pass / planned / partial — the INDEPENDENT agent has not run
  🔴 not-run     not started / blocked
  🔵 human       awaiting a human action (approval, sign-off)
  ⚪ n/a         not applicable to this chapter

Usage: python3 .claude/scripts/status.py [--check-only]
Owner: production-manager (PROCESS-CHECK / SCHEDULE).  No external deps (stdlib only).
"""
import os, re, sys, datetime

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
FINAL_INDEX = os.path.join(ROOT, "01-index", "FINAL_INDEX.md")
TRACKER     = os.path.join(ROOT, "01-index", "CHAPTER-TRACKER.md")
DRAFTS      = os.path.join(ROOT, "03-drafts")
MATRIX_OUT  = os.path.join(ROOT, "01-index", "STATUS-MATRIX.md")
HTML_OUT    = os.path.join(ROOT, "10-logs", "dashboard.html")

# Tracker column order (1-based field index after splitting the row on '|'; a
# leading '|' makes f[0] empty so the first real cell is index 1):
#   1=Ch 2=NN 3=Topic 4=research 5=verify 6=draft 7=example 8=clarity 9=audit
#   10=score 11=figure 12=reconcile 13=approve
GATES = ["research", "verify", "draft", "example", "clarity", "audit",
         "score", "figure", "reconcile", "approve"]
GATE_IDX = {g: i + 4 for i, g in enumerate(GATES)}  # research→4 … approve→13

# HARD gate-trail order for the drift check (the blocking sequence; figure is
# plan-then-render, not on the linear trail; research/draft precede gating):
TRAIL = ["research", "draft", "verify", "clarity", "audit", "score", "reconcile", "approve"]

# Evidence: a gate that claims done/self must have produced this report suffix.
EVIDENCE = {"research": None, "verify": "_VERIFY.md", "clarity": "_CLARITY.md",
            "audit": "_AUDIT.md", "score": "_SCORE.md", "reconcile": "_RECONCILE.md",
            "example": "_EXAMPLE.md"}

BUB = {"done": "🟢", "self": "🟡", "no": "🔴", "human": "🔵", "na": "⚪"}
HTML_COLOR = {"🟢": "#1a7f37", "🟡": "#bf8700", "🔴": "#cf222e", "🔵": "#0969da", "⚪": "#8c959f"}


def classify(gate, cell):
    """Map a raw tracker cell → an honest bubble for this gate column."""
    c = cell.strip().lower().replace("*", "")
    if c in ("", "n-a", "n/a", "na", "n/a⁹", "—", "-"):
        return BUB["na"] if gate in ("example", "figure") and ("n/a" in c or "na" in c) else (
            BUB["na"] if c in ("n-a", "n/a", "na", "n/a⁹") else BUB["no"])
    if c.startswith("flag") or "blocked" in c:
        return BUB["no"]
    # research & draft: a plain 'done' is genuinely complete (dossier banked / v1 on disk)
    if gate in ("research", "draft") and "done" in c:
        return BUB["done"]
    # the HUMAN approval gate: pending → awaiting human; done → 🟢
    if gate == "approve":
        return BUB["done"] if "done" in c else BUB["human"]
    # independent-agent gates (verify/clarity/audit/reconcile): a main-loop 'done'
    # is a SELF-pass until the independent agent runs → 🟡, never 🟢 from text alone
    if gate in ("verify", "clarity", "audit", "reconcile"):
        if "done" in c:
            return BUB["self"]
        return BUB["no"]
    # score: a "NN/50" or 'done' is a main-loop self-score → 🟡
    if gate == "score":
        if re.search(r"\d+\s*/\s*50", c) or "done" in c:
            return BUB["self"]
        return BUB["no"]
    # example / repro (toolchain-gated): partial → 🟡, pend/rt → 🔴, done → 🟢
    if gate == "example":
        if "partial" in c:
            return BUB["self"]
        if "done" in c and "pend" not in c:
            return BUB["done"]
        return BUB["no"]
    # figure: plan-set/planned → 🟡, done → 🟢
    if gate == "figure":
        if "done" in c:
            return BUB["done"]
        if "plan" in c:
            return BUB["self"]
        return BUB["no"]
    if "done" in c:
        return BUB["self"]
    return BUB["no"]


def parse_parts():
    """FINAL_INDEX.md → {chapter:int -> part_name}, ordered part list."""
    ch2part, parts = {}, []
    if not os.path.exists(FINAL_INDEX):
        return ch2part, parts
    cur = "—"
    for ln in open(FINAL_INDEX, encoding="utf-8"):
        m = re.search(r"\*\*(Part [^*|]+?)\*\*", ln)
        if m and ln.lstrip().startswith("|"):
            cur = m.group(1).strip()
            if cur not in parts:
                parts.append(cur)
            continue
        cells = [c.strip() for c in ln.split("|")]
        if len(cells) > 2 and re.fullmatch(r"\d+", cells[1] or ""):
            ch2part[int(cells[1])] = cur
    return ch2part, parts


def parse_tracker():
    """CHAPTER-TRACKER.md → list of chapter dicts (only real numbered chapters)."""
    rows = []
    if not os.path.exists(TRACKER):
        print("status: CHAPTER-TRACKER.md missing", file=sys.stderr); sys.exit(2)
    for ln in open(TRACKER, encoding="utf-8"):
        if not ln.lstrip().startswith("|"):
            continue
        f = ln.rstrip("\n").split("|")
        if len(f) < 14:
            continue
        ch = f[1].strip()
        if not re.fullmatch(r"\d+", ch):       # skip header, folded (—) and rule rows
            continue
        nn = f[2].strip()
        topic = re.sub(r"\s*\(Ch.*?\)\s*$", "", f[3].strip())
        cells = {g: f[GATE_IDX[g]].strip() for g in GATES}
        bubbles = {g: classify(g, cells[g]) for g in GATES}
        rows.append({"ch": int(ch), "nn": nn, "topic": topic,
                     "cells": cells, "bubbles": bubbles})
    return sorted(rows, key=lambda r: r["ch"])


def slug_for(nn):
    """Find the draft folder slug owning dossier key NN (for evidence lookup)."""
    if not os.path.isdir(DRAFTS):
        return None
    for d in os.listdir(DRAFTS):
        if re.match(rf"^{re.escape(nn)}_", d):
            return d
    return None


def drift_check(rows):
    findings = []
    order = {g: i for i, g in enumerate(TRAIL)}
    for r in rows:
        b = r["bubbles"]
        # (A) gate-trail order — a 🟢 done gate must not sit after a not-done earlier HARD gate
        done_trail = [g for g in TRAIL if b.get(g) == BUB["done"]]
        for g in done_trail:
            for earlier in TRAIL[:order[g]]:
                if b.get(earlier) in (BUB["no"],):
                    findings.append(f"Ch {r['ch']} (key {r['nn']}): '{g}' is 🟢 done but earlier '{earlier}' is 🔴 not-run")
        # (B) evidence — a done/self gate with a defined report suffix must have the file
        slug = slug_for(r["nn"])
        for g, suf in EVIDENCE.items():
            if suf is None:
                continue
            if b.get(g) in (BUB["done"], BUB["self"]):
                if not slug:
                    findings.append(f"Ch {r['ch']} (key {r['nn']}): '{g}' claims progress but no draft folder for key {r['nn']}")
                    break
                rp = os.path.join(DRAFTS, slug, slug + suf)
                if not os.path.exists(rp):
                    findings.append(f"Ch {r['ch']} (key {r['nn']}): '{g}' claims progress but {slug}{suf} is missing on disk")
    return findings


def next_step(r):
    for g in TRAIL:
        bb = r["bubbles"][g]
        if bb == BUB["no"]:
            return g
        if bb == BUB["self"]:
            return g + " (independent gate)"
        if bb == BUB["human"]:
            return g + " (HUMAN)"
    return "complete"


def eta(rows):
    """Honest estimate — counts of remaining work, not fake precision."""
    n = len(rows)
    need_indep = sum(1 for r in rows if any(r["bubbles"][g] == BUB["self"]
                     for g in ("verify", "clarity", "audit", "score", "reconcile")))
    need_example = sum(1 for r in rows if r["bubbles"]["example"] in (BUB["no"], BUB["self"]))
    need_human = sum(1 for r in rows if r["bubbles"]["approve"] == BUB["human"])
    return n, need_indep, need_example, need_human


def write_matrix(rows, ch2part, parts, findings):
    today = datetime.date.today().isoformat()
    n, ni, ne, nh = eta(rows)
    hdr = "| Ch | Key | Topic | " + " | ".join(g[:4] for g in GATES) + " | Next | ETA-flags |"
    sep = "|" + "---|" * (4 + len(GATES) + 1)
    lines = [
        "# STATUS MATRIX — Java Code Quality Book",
        "",
        f"> Generated by `.claude/scripts/status.py` · {today} · **the anti-drift + report tool**.",
        "> Bubbles: 🟢 done (independent gate + evidence) · 🟡 self/wip (main-loop self-pass / planned — **independent agent not yet run**) · 🔴 not-run/blocked · 🔵 awaiting-human · ⚪ n/a.",
        "",
        "## Summary",
        "",
        f"- **{n}/47 chapters** have a v1 draft (🟢 on `draft`).",
        f"- **{ni}** chapters carry main-loop self-passes (🟡) awaiting the **independent** gates (source-verify / clarity / audit / score / reconcile, run by their agents — originality + red-team on a *different model*).",
        f"- **{ne}** chapters need their EXAMPLE-BUILD (FLOOR-C compile; toolchain-gated).",
        f"- **{nh}** chapters await **human approval** (Step 12) 🔵.",
        f"- **DRIFT: {'❌ ' + str(len(findings)) + ' finding(s)' if findings else '✅ none'}** (see §Drift).",
        "",
        "## Needs-human queue 🔵",
        "",
    ]
    hq = [r for r in rows if r["bubbles"]["approve"] == BUB["human"]]
    if hq:
        lines.append(f"{len(hq)} chapters await human approval (Step 12 per-chapter gate): " +
                     ", ".join(f"Ch {r['ch']}" for r in hq) + ".")
    else:
        lines.append("_None._")
    lines += ["", "## Matrix", "", hdr, sep]
    cur = None
    for r in rows:
        p = ch2part.get(r["ch"], "—")
        if p != cur:
            lines.append(f"| | | **{p}** | " + " | ".join([""] * len(GATES)) + " | | |")
            cur = p
        bub = " | ".join(r["bubbles"][g] for g in GATES)
        topic = (r["topic"][:46] + "…") if len(r["topic"]) > 47 else r["topic"]
        lines.append(f"| {r['ch']} | {r['nn']} | {topic} | {bub} | {next_step(r)} | |")
    # per-part rollup
    lines += ["", "## Per-Part rollup", "", "| Part | Chapters | Drafted 🟢 | Self/wip 🟡 | Awaiting-human 🔵 |", "|---|---|---|---|---|"]
    for p in parts:
        chs = [r for r in rows if ch2part.get(r["ch"]) == p]
        if not chs:
            continue
        drafted = sum(1 for r in chs if r["bubbles"]["draft"] == BUB["done"])
        selfwip = sum(1 for r in chs if r["bubbles"]["audit"] == BUB["self"])
        hum = sum(1 for r in chs if r["bubbles"]["approve"] == BUB["human"])
        lines.append(f"| {p} | {len(chs)} | {drafted} | {selfwip} | {hum} |")
    # drift
    lines += ["", "## Drift", ""]
    if findings:
        lines.append("**❌ drift detected — the records overstate or contradict the evidence:**")
        lines += [f"- {x}" for x in findings]
    else:
        lines.append("✅ No drift: every 🟢/🟡 gate has its evidence on disk and the gate-trail order holds.")
    lines += ["", "---", "_Re-run `python3 .claude/scripts/status.py` after any tracker change._", ""]
    open(MATRIX_OUT, "w", encoding="utf-8").write("\n".join(lines))


def write_html(rows, ch2part, parts, findings):
    today = datetime.date.today().isoformat()
    n, ni, ne, nh = eta(rows)
    os.makedirs(os.path.dirname(HTML_OUT), exist_ok=True)
    def cell(b):
        return f'<td style="background:{HTML_COLOR.get(b,"#fff")};color:#fff;text-align:center;font-weight:600">{b}</td>'
    h = ['<!doctype html><meta charset="utf-8"><title>Quality Book — status dashboard</title>',
         '<style>body{font:14px/1.5 -apple-system,Segoe UI,Roboto,sans-serif;margin:2rem;color:#1f2328;background:#f6f8fa}'
         'h1,h2{font-weight:700}table{border-collapse:collapse;margin:1rem 0;background:#fff;box-shadow:0 1px 3px #0002}'
         'td,th{border:1px solid #d0d7de;padding:4px 8px;font-size:13px}th{background:#24292f;color:#fff;position:sticky;top:0}'
         '.part td{background:#eaeef2;font-weight:700}.k{font-size:12px;color:#57606a}'
         '.box{display:inline-block;background:#fff;border:1px solid #d0d7de;border-radius:8px;padding:10px 16px;margin:4px 8px 4px 0}'
         '.big{font-size:26px;font-weight:700}.q{background:#ddf4ff;border:1px solid #54aeff;border-radius:8px;padding:10px 14px;margin:1rem 0}'
         '.drift{background:#ffebe9;border:1px solid #ff8182;border-radius:8px;padding:10px 14px;margin:1rem 0}'
         '.ok{background:#dafbe1;border:1px solid #4ac26b;border-radius:8px;padding:10px 14px;margin:1rem 0}</style>',
         f'<h1>Java Code Quality Book — status dashboard</h1><p class=k>Generated {today} by status.py · '
         '🟢 done · 🟡 self/wip (independent agent not yet run) · 🔴 not-run · 🔵 awaiting-human · ⚪ n/a</p>',
         f'<div class=box><div class=big>{n}/47</div>drafted</div>'
         f'<div class=box><div class=big>{ni}</div>await independent gates 🟡</div>'
         f'<div class=box><div class=big>{ne}</div>need example-build</div>'
         f'<div class=box><div class=big>{nh}</div>await human 🔵</div>']
    hq = [r for r in rows if r["bubbles"]["approve"] == BUB["human"]]
    h.append('<div class=q><b>🔵 Needs-human queue:</b> ' +
             (", ".join(f"Ch {r['ch']}" for r in hq) if hq else "none") +
             ' — human approval (Step 12).</div>')
    if findings:
        h.append('<div class=drift><b>❌ Drift ({} finding{}):</b><ul>'.format(len(findings), "s" if len(findings) != 1 else "") +
                 "".join(f"<li>{x}</li>" for x in findings) + "</ul></div>")
    else:
        h.append('<div class=ok><b>✅ No drift</b> — every 🟢/🟡 gate has evidence on disk; gate-trail order holds.</div>')
    # per-part rollup
    h.append("<h2>Per-Part rollup</h2><table><tr><th>Part</th><th>Chapters</th><th>Drafted 🟢</th><th>Self/wip 🟡</th><th>Await-human 🔵</th></tr>")
    for p in parts:
        chs = [r for r in rows if ch2part.get(r["ch"]) == p]
        if not chs:
            continue
        drafted = sum(1 for r in chs if r["bubbles"]["draft"] == BUB["done"])
        selfwip = sum(1 for r in chs if r["bubbles"]["audit"] == BUB["self"])
        hum = sum(1 for r in chs if r["bubbles"]["approve"] == BUB["human"])
        h.append(f"<tr><td>{p}</td><td>{len(chs)}</td><td>{drafted}</td><td>{selfwip}</td><td>{hum}</td></tr>")
    h.append("</table>")
    # matrix
    h.append("<h2>Matrix</h2><table><tr><th>Ch</th><th>Key</th><th>Topic</th>" +
             "".join(f"<th>{g[:4]}</th>" for g in GATES) + "<th>Next</th></tr>")
    cur = None
    for r in rows:
        p = ch2part.get(r["ch"], "—")
        if p != cur:
            h.append(f'<tr class=part><td colspan="{4+len(GATES)}">{p}</td></tr>')
            cur = p
        h.append(f"<tr><td>{r['ch']}</td><td class=k>{r['nn']}</td><td>{r['topic'][:50]}</td>" +
                 "".join(cell(r["bubbles"][g]) for g in GATES) +
                 f"<td class=k>{next_step(r)}</td></tr>")
    h.append("</table>")
    open(HTML_OUT, "w", encoding="utf-8").write("\n".join(h))


def main():
    check_only = "--check-only" in sys.argv
    ch2part, parts = parse_parts()
    rows = parse_tracker()
    findings = drift_check(rows)
    if not check_only:
        write_matrix(rows, ch2part, parts, findings)
        write_html(rows, ch2part, parts, findings)
        print(f"status: wrote {os.path.relpath(MATRIX_OUT, ROOT)} + {os.path.relpath(HTML_OUT, ROOT)}")
    n, ni, ne, nh = eta(rows)
    print(f"status: {n}/47 drafted · {ni} await independent gates · {ne} need example-build · {nh} await human")
    if findings:
        print(f"status: ❌ DRIFT — {len(findings)} finding(s):")
        for x in findings:
            print("  - " + x)
        sys.exit(1)
    print("status: ✅ no drift")
    sys.exit(0)


if __name__ == "__main__":
    main()
