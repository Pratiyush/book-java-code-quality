#!/usr/bin/env python3
"""
status.py — the status MATRIX + auto-approval engine + capstone status + audit
report + anti-drift guard (purpose-built for this book). One regenerable report,
in Markdown AND a multi-page HTML dashboard.

Reads the authoritative artifacts and emits, on every run:

  Markdown (01-index/):
    STATUS-MATRIX.md      chapter x gate bubble grid + per-Part rollups + the
                          needs-human queue + ETA + capstone summary + drift.
    SCORING-APPROVAL.md   per-chapter score %, the 90%-auto-approve decision,
                          and the routing (auto-approve / lift / human gate).

  HTML (10-logs/, cross-linked, regenerable):
    dashboard.html        Overview (home) — headline counts + nav.
    chapters.html         the chapter x gate matrix + per-Part rollup + drift.
    scoring.html          the scoring + auto-approval table.
    capstones.html        the three capstone apps x their gates.
    audit.html            the audit trail (tail of 10-logs/audit.jsonl).

DRIFT CHECK (the anti-drift guard), exit 1 on drift:
  (A) gate-trail order — no HARD gate may be done while an EARLIER HARD gate is not-run.
  (B) evidence — every gate cell that claims done/self must have its report on disk.

AUTO-APPROVAL POLICY (the 90% rule):
  score >= 90% (>= 45/50) AND content floors PASS AND the INDEPENDENT gates have
  run (their reports on disk) -> AUTO-APPROVE. Otherwise -> LIFT (raise via the
  scorer's bounded loop); if lift cannot reach 90% -> HUMAN GATE. A self-score
  (main loop, no independent evidence) can only be "eligible", never auto-approved
  -- honest by construction.  Use --apply-approvals to actually approve the
  chapters that qualify (default: report only, mutates nothing).

Bubble legend:
  🟢 done   independent gate passed + evidence on disk  /  capstone gate green
  🟡 self/wip  main-loop self-pass / planned / in-lift — independent agent not run
  🔴 not-run  not started / blocked
  🔵 human  awaiting a human action (approval / sign-off / code-review)
  ⚪ n/a

Usage: python3 .claude/scripts/status.py [--check-only] [--apply-approvals]
Owner: production-manager.  No external deps (stdlib only).
"""
import os, re, sys, json, subprocess, datetime, shutil

ROOT = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
FINAL_INDEX = os.path.join(ROOT, "01-index", "FINAL_INDEX.md")
TRACKER     = os.path.join(ROOT, "01-index", "CHAPTER-TRACKER.md")
DRAFTS      = os.path.join(ROOT, "03-drafts")
APPROVED    = os.path.join(ROOT, "04-approved")
MATRIX_OUT  = os.path.join(ROOT, "01-index", "STATUS-MATRIX.md")
SCORING_OUT = os.path.join(ROOT, "01-index", "SCORING-APPROVAL.md")
CAPSTONES   = os.path.join(ROOT, "08-companion-code", "capstones", "CAPSTONE-STATUS.json")
AUDIT_LOG   = os.path.join(ROOT, "10-logs", "audit.jsonl")
HTML_DIR    = os.path.join(ROOT, "10-logs")
AUDIT_SH    = os.path.join(ROOT, ".claude", "scripts", "audit_log.sh")

APPROVE_THRESHOLD = 90          # percent; the "excellence" flag (45/50) — informational
SHIP_BAR = 80                   # ship bar (user-set): ≥80% (40/50) + floors → ready for the human gate
SCORE_MAX = 50

GATES = ["research", "verify", "draft", "example", "clarity", "audit",
         "score", "figure", "reconcile", "approve"]
GATE_IDX = {g: i + 4 for i, g in enumerate(GATES)}  # research→4 … approve→13
# Blocking trail for the anti-drift order check + the auto-approve path. The independent
# chapter-scorer (different model) is the decision gate; verify/clarity/audit/reconcile remain
# OPTIONAL deeper gates (shown in the matrix, never blocking auto-approval).
TRAIL = ["research", "draft", "score", "approve"]
EVIDENCE = {"research": None, "verify": "_VERIFY.md", "clarity": "_CLARITY.md",
            "audit": "_AUDIT.md", "score": "_SCORE.md", "reconcile": "_RECONCILE.md",
            "example": "_EXAMPLE.md"}
INDEP_GATES = ("verify", "clarity", "audit", "reconcile")   # optional deeper-review gates
SCORE_INDEP = "_SCORE_INDEP.md"   # an independent (different-model) score — the auto-approve authority

BUB = {"done": "🟢", "self": "🟡", "no": "🔴", "human": "🔵", "na": "⚪"}
HTML_COLOR = {"🟢": "#1a7f37", "🟡": "#bf8700", "🔴": "#cf222e", "🔵": "#0969da", "⚪": "#8c959f"}
PAGES = [("dashboard.html", "Overview"), ("chapters.html", "Chapters"),
         ("scoring.html", "Scoring & approval"), ("capstones.html", "Capstones"),
         ("audit.html", "Audit log")]


# ----------------------------------------------------------------------------- bubbles
def classify(gate, cell):
    """Map a raw tracker cell → an honest bubble (text-only; disk evidence applied later)."""
    c = cell.strip().lower().replace("*", "")
    if c in ("", "n-a", "n/a", "na", "n/a⁹", "—", "-"):
        return BUB["na"] if c in ("n-a", "n/a", "na", "n/a⁹") else BUB["no"]
    if c.startswith("flag") or "blocked" in c:
        return BUB["no"]
    if gate in ("research", "draft") and "done" in c:
        return BUB["done"]
    if gate == "approve":                       # overridden by the approval engine
        return BUB["done"] if "done" in c else BUB["human"]
    if gate in INDEP_GATES:
        return BUB["self"] if "done" in c else BUB["no"]
    if gate == "score":
        return BUB["self"] if (re.search(r"\d+\s*/\s*50", c) or "done" in c) else BUB["no"]
    if gate == "example":
        if "partial" in c:
            return BUB["self"]
        if "done" in c and "pend" not in c:
            return BUB["done"]
        return BUB["no"]
    if gate == "figure":
        if "done" in c:
            return BUB["done"]
        return BUB["self"] if "plan" in c else BUB["no"]
    return BUB["self"] if "done" in c else BUB["no"]


# ----------------------------------------------------------------------------- parsing
def parse_parts():
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


def slug_for(nn):
    if not os.path.isdir(DRAFTS):
        return None
    for d in sorted(os.listdir(DRAFTS)):
        if re.match(rf"^{re.escape(nn)}_", d):
            return d
    return None


def report_path(slug, suffix):
    return os.path.join(DRAFTS, slug, slug + suffix) if slug else None


def _verdict_token(cell):
    """The earliest PASS/FAIL/PENDING verdict in a table cell (emoji or word), or None."""
    found = []
    for needle, val, emoji in (("✅", "PASS", True), ("❌", "FAIL", True), ("⚠", "PENDING", True),
                               ("PASS", "PASS", False), ("FAIL", "FAIL", False), ("PENDING", "PENDING", False)):
        i = cell.find(needle) if emoji else cell.upper().find(needle)
        if i >= 0:
            found.append((i, val))
    return min(found)[1] if found else None


def parse_score(slug):
    """Read the score → {total, pct, floors, independent} or None.

    Prefers an independent (different-model) score file (_SCORE_INDEP.md) — its presence is what
    makes a score trustworthy enough to auto-approve. Falls back to the main-loop self-score
    (_SCORE.md), which is reported but can never auto-approve."""
    if not slug:
        return None
    indep_path = report_path(slug, SCORE_INDEP)
    if indep_path and os.path.exists(indep_path):
        p, independent = indep_path, True
    else:
        p = report_path(slug, "_SCORE.md")
        independent = False
        if not p or not os.path.exists(p):
            return None
    text = open(p, encoding="utf-8").read()
    # Match the labelled total in any of the forms the scorers use:
    # "**Aggregate 39/50**", "Cluster subtotal:** 38 / 50", "Aggregate score: 36/50".
    m = re.search(r"(?:Aggregate|Cluster subtotal)\D{0,10}(\d+)\s*/\s*50", text, re.I)
    total = int(m.group(1)) if m else None
    # Floors are read from the floor TABLE: a row whose label cell contains the floor name, with the
    # verdict in the next cell. Tolerates both "| A — NEUTRALITY | ✅ PASS |" and "| **NEUTRALITY** |
    # PASS |" formats; reads the verdict from the verdict cell only (evidence cells often mention other
    # floors' states, e.g. "COMPILE = PENDING", which must not bleed across). First matching row wins.
    floors = {"A": "?", "B": "?", "Csrc": "?", "Ccompile": "?"}
    names = (("A", "NEUTRAL"), ("B", "HONEST"), ("Csrc", "SOURCE-TRACE"), ("Ccompile", "COMPILE"))
    for ln in text.splitlines():
        s = ln.strip()
        if not s.startswith("|"):
            continue
        cells = [c.strip() for c in s.strip("|").split("|")]
        if len(cells) < 2:
            continue
        label = cells[0].upper()
        for key, name in names:
            if floors[key] != "?" or name not in label:
                continue
            # The verdict may sit in any cell after the label (3-col vs 4-col tables); take the first
            # cell that yields a verdict token, and the EARLIEST token in it (e.g. a combined
            # "PASS (SOURCE-TRACE) / PENDING (COMPILE)" cell resolves to PASS for SOURCE-TRACE).
            for c in cells[1:]:
                tok = _verdict_token(c)
                if tok:
                    floors[key] = tok
                    break
    pct = round(total / SCORE_MAX * 100) if total is not None else None
    return {"total": total, "pct": pct, "floors": floors, "independent": independent}


def approval_decision(score):
    """Routing policy (lift → human gate) → (decision, bubble_key, reason).

    A chapter is READY for the human approval gate (Step 12) when it has an INDEPENDENT
    (different-model) score that clears the book's ship bar (≥70% = 35/50) AND the editorial content
    floors (A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE) PASS. Below the bar or a floor not
    PASS → LIFT. A self-score (no independent gate yet) can only be NEEDS-INDEP. The COMPILE/
    example-build floor is tracked separately (the EXAMPLE-BUILD module suite is a later phase) and
    does NOT block reaching the human gate; ≥90% is reported as an excellence flag, not a separate
    route (final approval is the human's, per the chosen lift→human-gate plan)."""
    if not score or score["total"] is None:
        return ("UNSCORED", "no", "no score / no aggregate found")
    f = score["floors"]
    floors_pass = f.get("A") == "PASS" and f.get("B") == "PASS" and f.get("Csrc") == "PASS"
    pct = score["pct"]
    star = " ★≥90% excellence" if pct >= APPROVE_THRESHOLD else ""
    cnote = "" if f.get("Ccompile") == "PASS" else "; example-build pending (separate phase)"
    if not score["independent"]:
        if floors_pass and pct >= SHIP_BAR:
            return ("NEEDS-INDEP", "self",
                    f"{pct}% self ≥{SHIP_BAR}% — run the independent scorer to advance to the human gate")
        return ("LIFT", "self", f"{pct}% self-score — lift + independent-score toward the {SHIP_BAR}% bar")
    if not floors_pass:
        return ("LIFT", "self", f"{pct}% (independent) but a content floor is not PASS (A/B/C-src) → fix")
    if pct >= SHIP_BAR:
        return ("READY", "human",
                f"{pct}% (independent) ≥{SHIP_BAR}% + floors PASS → ready for human approval (Step 12){star}{cnote}")
    return ("LIFT", "self", f"{pct}% (independent) <{SHIP_BAR}% → lift loop (≤3) toward the bar")


def parse_tracker():
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
        if not re.fullmatch(r"\d+", ch):
            continue
        nn = f[2].strip()
        topic = re.sub(r"\s*\(Ch.*?\)\s*$", "", f[3].strip())
        cells = {g: f[GATE_IDX[g]].strip() for g in GATES}
        bubbles = {g: classify(g, cells[g]) for g in GATES}
        slug = slug_for(nn)
        # evidence upgrade: an independent gate with its report on disk is genuinely 🟢
        for g in INDEP_GATES:
            rp = report_path(slug, EVIDENCE[g])
            if rp and os.path.exists(rp):
                bubbles[g] = BUB["done"]
        # an independent (different-model) score upgrades the score gate to 🟢
        sip = report_path(slug, SCORE_INDEP)
        if sip and os.path.exists(sip):
            bubbles["score"] = BUB["done"]
        # approval engine drives the approve bubble
        score = parse_score(slug)
        decision, dkey, reason = approval_decision(score)
        already_approved = "done" in cells["approve"].lower() or (
            slug and os.path.exists(os.path.join(APPROVED, slug + ".md")))
        if already_approved:
            bubbles["approve"] = BUB["done"]
            decision = "APPROVED"
        else:
            bubbles["approve"] = BUB[dkey]
        rows.append({"ch": int(ch), "nn": nn, "topic": topic, "slug": slug,
                     "cells": cells, "bubbles": bubbles,
                     "score": score, "decision": decision, "reason": reason})
    return sorted(rows, key=lambda r: r["ch"])


def parse_capstones():
    if not os.path.exists(CAPSTONES):
        return None
    try:
        return json.load(open(CAPSTONES, encoding="utf-8"))
    except (ValueError, OSError):
        return None


def read_audit(limit=60):
    if not os.path.exists(AUDIT_LOG):
        return []
    out = []
    for ln in open(AUDIT_LOG, encoding="utf-8"):
        ln = ln.strip()
        if not ln:
            continue
        try:
            out.append(json.loads(ln))
        except ValueError:
            continue
    out.sort(key=lambda e: e.get("ts", ""), reverse=True)
    return out[:limit]


# ----------------------------------------------------------------------------- drift
def drift_check(rows):
    findings = []
    order = {g: i for i, g in enumerate(TRAIL)}
    for r in rows:
        b = r["bubbles"]
        for g in [g for g in TRAIL if b.get(g) == BUB["done"]]:
            for earlier in TRAIL[:order[g]]:
                if b.get(earlier) == BUB["no"]:
                    findings.append(f"Ch {r['ch']} (key {r['nn']}): '{g}' is 🟢 done but earlier '{earlier}' is 🔴 not-run")
        for g, suf in EVIDENCE.items():
            if suf is None:
                continue
            if b.get(g) in (BUB["done"], BUB["self"]):
                if not r["slug"]:
                    findings.append(f"Ch {r['ch']} (key {r['nn']}): '{g}' claims progress but no draft folder for key {r['nn']}")
                    break
                if not os.path.exists(report_path(r["slug"], suf)):
                    findings.append(f"Ch {r['ch']} (key {r['nn']}): '{g}' claims progress but {r['slug']}{suf} is missing on disk")
    return findings


def next_step(r):
    for g in TRAIL:
        bb = r["bubbles"][g]
        if bb == BUB["no"]:
            return g
        if bb == BUB["self"]:
            return g + (" (independent gate)" if g in INDEP_GATES else "")
        if bb == BUB["human"]:
            return g + " (HUMAN)"
    return "complete"


def summary(rows):
    n = len(rows)
    need_indep = sum(1 for r in rows if any(r["bubbles"][g] == BUB["self"] for g in INDEP_GATES + ("score",)))
    need_example = sum(1 for r in rows if r["bubbles"]["example"] in (BUB["no"], BUB["self"]))
    ready = sum(1 for r in rows if r["decision"] == "READY")
    needs_indep = sum(1 for r in rows if r["decision"] == "NEEDS-INDEP")
    lift = sum(1 for r in rows if r["decision"] == "LIFT")
    approved = sum(1 for r in rows if r["decision"] == "APPROVED")
    human = sum(1 for r in rows if r["bubbles"]["approve"] == BUB["human"])
    return dict(n=n, indep=need_indep, example=need_example,
                ready=ready, needs_indep=needs_indep, lift=lift, approved=approved, human=human)


# ----------------------------------------------------------------------------- markdown
def cap_bubble(v):
    return {"done": BUB["done"], "self": BUB["self"], "no": BUB["no"],
            "human": BUB["human"], "na": BUB["na"]}.get(v, BUB["no"])


def write_matrix(rows, ch2part, parts, findings, caps):
    today = datetime.date.today().isoformat()
    s = summary(rows)
    hdr = "| Ch | Key | Topic | " + " | ".join(g[:4] for g in GATES) + " | Next |"
    sep = "|" + "---|" * (4 + len(GATES))
    L = [
        "# STATUS MATRIX — Java Code Quality Book",
        "",
        f"> Generated by `.claude/scripts/status.py` · {today} · **the anti-drift + report tool** (regenerate any time).",
        "> Bubbles: 🟢 done (independent gate + evidence) · 🟡 self/wip (main-loop self-pass / in-lift — **independent agent not yet run**) · 🔴 not-run/blocked · 🔵 awaiting-human · ⚪ n/a.",
        "> Companion pages: `10-logs/dashboard.html` (HTML home) · `SCORING-APPROVAL.md` (the 90%-auto-approve engine) · capstones in `08-companion-code/capstones/`.",
        "",
        "## Summary",
        "",
        f"- **{s['n']}/47 chapters** drafted (🟢 `draft`).",
        f"- **{s['indep']}** await the **independent** gates (source-verify / clarity / audit / score / reconcile — agents on a *different model*).",
        f"- **{s['example']}** need EXAMPLE-BUILD (FLOOR-C compile).",
        f"- **Routing (ship bar {SHIP_BAR}% + floors → human gate):** {s['ready']} READY for human approval 🔵 · {s['lift']} in lift · {s['needs_indep']} need an independent score · {s['approved']} approved.",
        f"- **DRIFT: {'❌ ' + str(len(findings)) + ' finding(s)' if findings else '✅ none'}**.",
        "",
        "## Needs-human queue 🔵",
        "",
    ]
    hq = [r for r in rows if r["bubbles"]["approve"] == BUB["human"]]
    L.append(("Chapters at the human gate (Step 12): " + ", ".join(f"Ch {r['ch']}" for r in hq) + ".")
             if hq else "_None yet — chapters reach the human gate only after the lift loop cannot reach "
                        f"{APPROVE_THRESHOLD}%._")
    L += ["", "## Matrix", "", hdr, sep]
    cur = None
    for r in rows:
        p = ch2part.get(r["ch"], "—")
        if p != cur:
            L.append(f"| | | **{p}** | " + " | ".join([""] * len(GATES)) + " | |")
            cur = p
        bub = " | ".join(r["bubbles"][g] for g in GATES)
        topic = (r["topic"][:44] + "…") if len(r["topic"]) > 45 else r["topic"]
        L.append(f"| {r['ch']} | {r['nn']} | {topic} | {bub} | {next_step(r)} |")
    L += ["", "## Per-Part rollup", "", "| Part | Chapters | Drafted 🟢 | Await-independent 🟡 | At human 🔵 |", "|---|---|---|---|---|"]
    for p in parts:
        chs = [r for r in rows if ch2part.get(r["ch"]) == p]
        if not chs:
            continue
        drafted = sum(1 for r in chs if r["bubbles"]["draft"] == BUB["done"])
        awaiting = sum(1 for r in chs if r["bubbles"]["audit"] != BUB["done"])
        hum = sum(1 for r in chs if r["bubbles"]["approve"] == BUB["human"])
        L.append(f"| {p} | {len(chs)} | {drafted} | {awaiting} | {hum} |")
    # capstones
    if caps:
        L += ["", "## Capstones (companion microservice apps)", "",
              f"> {caps.get('as_of','')} — {caps.get('verified_by','')}", ""]
        gos = caps.get("gate_order", [])
        L.append("| Capstone | Domain | Services | " + " | ".join(gos) + " |")
        L.append("|" + "---|" * (3 + len(gos)))
        for c in caps.get("capstones", []):
            gg = " | ".join(cap_bubble(c["gates"].get(g, "no")) for g in gos)
            L.append(f"| {c['id']} | {c['domain']} | {len(c['services'])} ({c.get('tests','?')} tests) | {gg} |")
    # drift
    L += ["", "## Drift", ""]
    if findings:
        L.append("**❌ drift detected — records overstate or contradict the evidence:**")
        L += [f"- {x}" for x in findings]
    else:
        L.append("✅ No drift: every 🟢/🟡 gate has its evidence on disk and the gate-trail order holds.")
    L += ["", "---", "_Regenerate: `python3 .claude/scripts/status.py`._", ""]
    open(MATRIX_OUT, "w", encoding="utf-8").write("\n".join(L))


def write_scoring_md(rows):
    today = datetime.date.today().isoformat()
    L = ["# SCORING & APPROVAL ROUTING — Java Code Quality Book", "",
         f"> Generated by `status.py` · {today}. **Policy (lift → human gate):** an INDEPENDENT "
         f"(different-model) score **≥{SHIP_BAR}%** (≥{SHIP_BAR * SCORE_MAX // 100}/{SCORE_MAX}) **+ content floors "
         "PASS (A/B/C-source)** → **READY for the human approval gate (Step 12)**. Below the bar or a floor "
         "unresolved → **lift** (≤3 passes). The human gives final approval.",
         f"> A main-loop *self*-score must be independently re-scored before it advances. **≥{APPROVE_THRESHOLD}%** is "
         "flagged as excellence. COMPILE/example-build is tracked separately (a later phase), not a blocker here.",
         "",
         "| Ch | Key | Score | % | A | B | C-src | C-compile | Indep? | Decision | Why |",
         "|---|---|---|---|---|---|---|---|---|---|---|"]
    for r in rows:
        sc = r["score"]
        if not sc:
            L.append(f"| {r['ch']} | {r['nn']} | — | — | — | — | — | — | — | {r['decision']} | {r['reason']} |")
            continue
        f = sc["floors"]
        tot = f"{sc['total']}/{SCORE_MAX}" if sc["total"] is not None else "—"
        L.append(f"| {r['ch']} | {r['nn']} | {tot} | {sc['pct']}% | {f.get('A','?')} | {f.get('B','?')} | "
                 f"{f.get('Csrc','?')} | {f.get('Ccompile','?')} | {'yes' if sc['independent'] else 'no'} | "
                 f"**{r['decision']}** | {r['reason']} |")
    s = summary(rows)
    L += ["", "## Routing", "",
          f"- **{s['ready']}** READY for human approval — independent score ≥{SHIP_BAR}% + content floors PASS (Step 12 queue).",
          f"- **{s['needs_indep']}** need an independent score (self ≥{SHIP_BAR}%, run the independent scorer).",
          f"- **{s['lift']}** in the lift loop (below the {SHIP_BAR}% bar or a floor unresolved).",
          f"- **{s['approved']}** already human-approved (in 04-approved/).",
          "",
          "_Apply approvals for qualifying chapters: `python3 .claude/scripts/status.py --apply-approvals`._", ""]
    open(SCORING_OUT, "w", encoding="utf-8").write("\n".join(L))


# ----------------------------------------------------------------------------- html
CSS = ("body{font:14px/1.5 -apple-system,Segoe UI,Roboto,sans-serif;margin:0;color:#1f2328;background:#f6f8fa}"
       "header{background:#24292f;color:#fff;padding:14px 24px}header h1{margin:0;font-size:18px}"
       "nav{background:#2d333b;padding:0 16px}nav a{display:inline-block;color:#cdd9e5;text-decoration:none;"
       "padding:10px 14px;font-size:13px;border-bottom:3px solid transparent}nav a:hover{color:#fff}"
       "nav a.active{color:#fff;border-bottom-color:#fb8500;font-weight:600}main{padding:20px 24px;max-width:1200px}"
       "h2{font-weight:700;margin-top:1.6rem}table{border-collapse:collapse;margin:1rem 0;background:#fff;box-shadow:0 1px 3px #0002}"
       "td,th{border:1px solid #d0d7de;padding:4px 8px;font-size:13px}th{background:#24292f;color:#fff;position:sticky;top:0}"
       ".part td{background:#eaeef2;font-weight:700}.k{font-size:12px;color:#57606a}"
       ".box{display:inline-block;background:#fff;border:1px solid #d0d7de;border-radius:8px;padding:10px 16px;margin:4px 8px 4px 0;min-width:90px}"
       ".big{font-size:26px;font-weight:700}.q{background:#ddf4ff;border:1px solid #54aeff;border-radius:8px;padding:10px 14px;margin:1rem 0}"
       ".drift{background:#ffebe9;border:1px solid #ff8182;border-radius:8px;padding:10px 14px;margin:1rem 0}"
       ".ok{background:#dafbe1;border:1px solid #4ac26b;border-radius:8px;padding:10px 14px;margin:1rem 0}"
       "code{background:#eaeef2;padding:1px 5px;border-radius:4px;font-size:12px}.mut{color:#57606a;font-size:12px}")


def layout(title, active, body, today):
    nav = "".join(f'<a href="{href}" class="{"active" if label == active else ""}">{label}</a>'
                  for href, label in PAGES)
    return ("<!doctype html><meta charset=utf-8><title>Quality Book — " + title + "</title><style>" + CSS +
            "</style><header><h1>📘 Java Code Quality Book — control dashboard</h1>"
            f'<div class=mut style="color:#9da7b1">{title} · generated {today} by status.py</div></header>'
            "<nav>" + nav + "</nav><main>" + body + "</main>")


def htd(b):
    return f'<td style="background:{HTML_COLOR.get(b,"#fff")};color:#fff;text-align:center;font-weight:600">{b}</td>'


def write_html(rows, ch2part, parts, findings, caps, audit, today):
    os.makedirs(HTML_DIR, exist_ok=True)
    s = summary(rows)
    legend = ('<p class=mut>🟢 done (independent gate + evidence) · 🟡 self/wip (independent agent not yet run) · '
              '🔴 not-run · 🔵 awaiting-human · ⚪ n/a</p>')

    # --- Overview ---
    ov = ['<h2>Where the book stands</h2>', legend,
          f'<div class=box><div class=big>{s["n"]}/47</div>drafted</div>'
          f'<div class=box><div class=big>{s["ready"]}</div>READY for human 🔵</div>'
          f'<div class=box><div class=big>{s["lift"]}</div>in lift 🟡</div>'
          f'<div class=box><div class=big>{s["needs_indep"]}</div>need indep score</div>'
          f'<div class=box><div class=big>{s["example"]}</div>need example-build</div>'
          f'<div class=box><div class=big>{s["approved"]}</div>approved 🟢</div>']
    if caps:
        cg = sum(1 for c in caps["capstones"] for v in c["gates"].values() if v == "done")
        ov.append(f'<div class=box><div class=big>{len(caps["capstones"])}</div>capstone apps</div>')
    ov.append('<div class=q><b>Routing policy (lift → human gate):</b> an independent (different-model) score '
              '≥%d%% + content floors PASS → READY for human approval (Step 12). Below the bar or a floor unresolved '
              '→ lift. ≥%d%% is flagged as excellence. <a href="scoring.html">See scoring →</a></div>'
              % (SHIP_BAR, APPROVE_THRESHOLD))
    if findings:
        ov.append('<div class=drift><b>❌ Drift ({} finding{}).</b> <a href="chapters.html">See details →</a></div>'
                  .format(len(findings), "s" if len(findings) != 1 else ""))
    else:
        ov.append('<div class=ok><b>✅ No drift</b> — every 🟢/🟡 gate has evidence on disk; gate-trail order holds.</div>')
    ov.append('<p>Pages: <a href="chapters.html">Chapters matrix</a> · <a href="scoring.html">Scoring &amp; approval</a> · '
              '<a href="capstones.html">Capstones</a> · <a href="audit.html">Audit log</a>.</p>')
    write_page("dashboard.html", "Overview", "".join(ov), today)

    # --- Chapters matrix ---
    ch = [legend]
    ch.append("<h2>Per-Part rollup</h2><table><tr><th>Part</th><th>Chapters</th><th>Drafted 🟢</th><th>Await-independent 🟡</th><th>Human 🔵</th></tr>")
    for p in parts:
        chs = [r for r in rows if ch2part.get(r["ch"]) == p]
        if not chs:
            continue
        drafted = sum(1 for r in chs if r["bubbles"]["draft"] == BUB["done"])
        awaiting = sum(1 for r in chs if r["bubbles"]["audit"] != BUB["done"])
        hum = sum(1 for r in chs if r["bubbles"]["approve"] == BUB["human"])
        ch.append(f"<tr><td>{p}</td><td>{len(chs)}</td><td>{drafted}</td><td>{awaiting}</td><td>{hum}</td></tr>")
    ch.append("</table><h2>Matrix</h2><table><tr><th>Ch</th><th>Key</th><th>Topic</th>" +
              "".join(f"<th>{g[:4]}</th>" for g in GATES) + "<th>Next</th></tr>")
    cur = None
    for r in rows:
        p = ch2part.get(r["ch"], "—")
        if p != cur:
            ch.append(f'<tr class=part><td colspan="{4+len(GATES)}">{p}</td></tr>')
            cur = p
        ch.append(f"<tr><td>{r['ch']}</td><td class=k>{r['nn']}</td><td>{r['topic'][:50]}</td>" +
                  "".join(htd(r["bubbles"][g]) for g in GATES) + f"<td class=k>{next_step(r)}</td></tr>")
    ch.append("</table>")
    if findings:
        ch.append('<div class=drift><b>❌ Drift ({} finding{}):</b><ul>'.format(len(findings), "s" if len(findings) != 1 else "") +
                  "".join(f"<li>{x}</li>" for x in findings) + "</ul></div>")
    else:
        ch.append('<div class=ok><b>✅ No drift.</b></div>')
    write_page("chapters.html", "Chapters", "".join(ch), today)

    # --- Scoring & approval ---
    sc = ['<div class=q><b>Policy (lift → human gate):</b> an independent score ≥%d%% (≥%d/%d) + content floors PASS → '
          '<b>READY for human approval</b>. Else <b>lift</b> (≤3 passes) toward the bar. ≥%d%% flags excellence. '
          'A self-score must be independently re-scored before it advances.</div>'
          % (SHIP_BAR, SHIP_BAR * SCORE_MAX // 100, SCORE_MAX, APPROVE_THRESHOLD)]
    sc.append("<table><tr><th>Ch</th><th>Key</th><th>Score</th><th>%</th><th>A</th><th>B</th><th>C-src</th>"
              "<th>C-comp</th><th>Indep</th><th>Decision</th><th>Why</th></tr>")
    dcolor = {"AUTO-APPROVE": "#1a7f37", "APPROVED": "#1a7f37", "ELIGIBLE": "#bf8700",
              "LIFT": "#bf8700", "UNSCORED": "#cf222e", "HUMAN": "#0969da"}
    for r in rows:
        scd = r["score"]
        d = r["decision"]
        col = dcolor.get(d, "#57606a")
        if not scd:
            sc.append(f"<tr><td>{r['ch']}</td><td class=k>{r['nn']}</td><td colspan=7 class=k>no score</td>"
                      f'<td style="color:#fff;background:{col};font-weight:600">{d}</td><td class=mut>{r["reason"]}</td></tr>')
            continue
        f = scd["floors"]
        pctcell = f'<td style="font-weight:700;color:{"#1a7f37" if scd["pct"]>=APPROVE_THRESHOLD else "#bf8700"}">{scd["pct"]}%</td>'
        sc.append(f"<tr><td>{r['ch']}</td><td class=k>{r['nn']}</td><td>{scd['total']}/{SCORE_MAX}</td>{pctcell}"
                  f"<td>{f.get('A','?')}</td><td>{f.get('B','?')}</td><td>{f.get('Csrc','?')}</td><td>{f.get('Ccompile','?')}</td>"
                  f"<td>{'✅' if scd['independent'] else '—'}</td>"
                  f'<td style="color:#fff;background:{col};font-weight:600">{d}</td><td class=mut>{r["reason"]}</td></tr>')
    sc.append("</table>")
    write_page("scoring.html", "Scoring & approval", "".join(sc), today)

    # --- Capstones ---
    cp = []
    if not caps:
        cp.append("<p>No <code>CAPSTONE-STATUS.json</code> found.</p>")
    else:
        cp.append(f'<p class=mut>{caps.get("as_of","")} — {caps.get("verified_by","")}</p>')
        gos = caps.get("gate_order", [])
        labels = caps.get("gate_labels", {})
        cp.append("<table><tr><th>Capstone</th><th>Domain</th><th>Services</th><th>Tests</th>" +
                  "".join(f"<th>{labels.get(g,g)}</th>" for g in gos) + "</tr>")
        for c in caps["capstones"]:
            svc = "<br>".join(c["services"])
            cp.append(f"<tr><td><b>{c['id']}</b><div class=mut>{c.get('title','')}</div></td><td>{c['domain']}</td>"
                      f"<td class=k>{svc}</td><td style='text-align:center'>{c.get('tests','?')}</td>" +
                      "".join(htd(cap_bubble(c['gates'].get(g, 'no'))) for g in gos) + "</tr>")
        cp.append("</table>")
        cp.append('<p class=mut>Gate values are the last verified state (update <code>CAPSTONE-STATUS.json</code> after a build/review). '
                  'Code-review (FLOOR C) is the independent/human gate; Phase-4 assemble runs in Phase 4.</p>')
    write_page("capstones.html", "Capstones", "".join(cp), today)

    # --- Audit ---
    au = ['<p class=mut>The action trail — milestones from <code>audit_log.sh</code> and (once enabled) every tool '
          'call from the <code>PostToolUse</code> hook. Newest first; tail of <code>10-logs/audit.jsonl</code>.</p>']
    if not audit:
        au.append("<p>No audit entries yet.</p>")
    else:
        au.append("<table><tr><th>When (UTC)</th><th>Kind</th><th>Action / tool</th><th>Target</th><th>Detail</th></tr>")
        for e in audit:
            kind = e.get("kind", "")
            act = e.get("action") or e.get("tool") or ""
            tgt = (e.get("target") or "")[:90]
            det = (e.get("detail") or "")
            badge = "🔧" if kind == "tool" else "📌"
            au.append(f"<tr><td class=k>{e.get('ts','')}</td><td>{badge} {kind}</td><td>{act}</td>"
                      f"<td class=k>{tgt}</td><td class=mut>{det}</td></tr>")
        au.append("</table>")
    au.append('<p class=mut>Enable automatic per-tool capture: see <code>.claude/hooks/AUDIT-LOG.md</code>.</p>')
    write_page("audit.html", "Audit log", "".join(au), today)


def write_page(filename, active, body, today):
    title = next(label for href, label in PAGES if href == filename)
    open(os.path.join(HTML_DIR, filename), "w", encoding="utf-8").write(layout(title, active, body, today))


# ----------------------------------------------------------------------------- approvals
def apply_approvals(rows):
    """Copy qualifying (AUTO-APPROVE) drafts into 04-approved and log. Mutates the repo."""
    applied = []
    os.makedirs(APPROVED, exist_ok=True)
    for r in rows:
        if r["decision"] != "AUTO-APPROVE" or not r["slug"]:
            continue
        src = report_path(r["slug"], "_v1.md")
        dst = os.path.join(APPROVED, r["slug"] + ".md")
        if src and os.path.exists(src) and not os.path.exists(dst):
            shutil.copyfile(src, dst)
            applied.append(r)
            log_milestone("auto-approve", f"Ch {r['ch']} (key {r['nn']})",
                          f"{r['score']['pct']}% ≥{APPROVE_THRESHOLD}% + floors PASS + independent → {r['slug']}.md")
    return applied


def log_milestone(action, target, detail):
    try:
        subprocess.run(["bash", AUDIT_SH, action, target, detail],
                       cwd=ROOT, capture_output=True, timeout=10, check=False)
    except (OSError, subprocess.SubprocessError):
        pass


# ----------------------------------------------------------------------------- main
def main():
    check_only = "--check-only" in sys.argv
    do_apply = "--apply-approvals" in sys.argv
    ch2part, parts = parse_parts()
    rows = parse_tracker()
    caps = parse_capstones()
    findings = drift_check(rows)
    s = summary(rows)

    if do_apply and not check_only:
        applied = apply_approvals(rows)
        print(f"status: applied {len(applied)} auto-approval(s)" +
              (": " + ", ".join(f"Ch {r['ch']}" for r in applied) if applied else " (none qualified)"))
        rows = parse_tracker()  # re-read so the report reflects new approvals
        findings = drift_check(rows)
        s = summary(rows)

    if not check_only:
        audit = read_audit()
        today = datetime.date.today().isoformat()
        write_matrix(rows, ch2part, parts, findings, caps)
        write_scoring_md(rows)
        write_html(rows, ch2part, parts, findings, caps, audit, today)
        log_milestone("report", "status.py",
                      f"regenerated MD+HTML: {s['n']}/47 drafted, {s['ready']} ready-for-human, "
                      f"{s['lift']} in lift, drift={'none' if not findings else len(findings)}")
        print(f"status: wrote STATUS-MATRIX.md + SCORING-APPROVAL.md + 5 HTML pages in 10-logs/")

    print(f"status: {s['n']}/47 drafted · {s['example']} need example · "
          f"route[{s['ready']} READY-human / {s['lift']} lift / {s['needs_indep']} need-indep / {s['approved']} approved] (bar {SHIP_BAR}%)")
    if caps:
        print(f"status: capstones — {len(caps['capstones'])} apps (build/test/checkstyle/spotbugs green; code-review pending)")
    if findings:
        print(f"status: ❌ DRIFT — {len(findings)} finding(s):")
        for x in findings:
            print("  - " + x)
        sys.exit(1)
    print("status: ✅ no drift")
    sys.exit(0)


if __name__ == "__main__":
    main()
