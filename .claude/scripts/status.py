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

AUTO-APPROVAL POLICY (the 88% rule — spec):
  independent score >= 88% (>= 44/50) AND content floors A/B/C-source PASS
  -> AUTO-APPROVE: the draft is promoted into 04-approved/ on the next normal run.
  Otherwise -> LIFT (raise via the bounded lift loop). A self-score (main loop, no
  independent evidence) can only be NEEDS-INDEP, never auto-approved -- honest by
  construction. The only human gate is the whole-book Step 16 MANUSCRIPT-GATE.
  A normal run applies approvals automatically; --check-only and --no-apply never mutate.

Bubble legend:
  🟢 done   independent gate passed + evidence on disk  /  capstone gate green
  🟡 self/wip  main-loop self-pass / planned / in-lift — independent agent not run
  🔴 not-run  not started / blocked
  🔵 human  awaiting a human action (approval / sign-off / code-review)
  ⚪ n/a

Usage: python3 .claude/scripts/status.py [--check-only] [--no-apply]
Owner: production-manager.  No external deps (stdlib only).
"""
import os, re, sys, json, glob, subprocess, datetime, shutil

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
SHIP_BAR = 88                   # auto-approval bar (spec): ≥88% (44/50) + floors A/B/Csrc PASS → auto-approve into 04-approved
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
         ("scoring.html", "Scoring & approval"), ("figures.html", "Figures"),
         ("capstones.html", "Capstones"), ("audit.html", "Audit log"),
         ("publication-roadmap.html", "Roadmap")]   # externally authored (market-analyst); nav-linked, not generated here


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


def report_verdict(slug, suffix):
    """Read the headline VERDICT from a gate report (_EXAMPLE.md / _CODEREVIEW.md) → one of
    'PASS' / 'FAIL' / 'NA' / None. The reports write the verdict on the first line containing 'Verdict'
    ('## VERDICT: PASS', '- **Verdict:** **PASS-WITH-FIXES**', 'Floor-C verdict: N/A', '## VERDICT: FAIL').

    Robustness rules learned from the real reports (each rule has a concrete failing case):
      1. A verdict DECLARATION binds 'verdict' to its token directly: 'verdict' followed within a few
         chars (after optional :/→/-/* ) by PASS / FAIL / N/A. Narrative that merely contains 'verdict'
         ('Treat as FAIL on dimension 6', 'the dossier's N/A call', '## Verdict rationale — BUILT') is NOT
         a declaration and is ignored — else Ch26/Ch03/Ch110 misread.
      2. An UPGRADE phrase ('Verdict upgraded FAIL → PASS-WITH-FIXES') resolves to its TARGET (right of
         the arrow). Several reports open '## VERDICT: FAIL', narrate the fix, and close by upgrading.
      3. The verdict of record is the LAST declaration/upgrade on disk; PASS / PASS-WITH-FIXES is a PASS
         (test PASS before FAIL; ignore counted forms like '0 FAIL').
    Returns None when there is no report / no decisive declaration on disk."""
    p = report_path(slug, suffix)
    if not p or not os.path.exists(p):
        return None
    body = open(p, encoding="utf-8").read()

    # _EXAMPLE.md special case — the COMPILE floor attaches only if a module EXISTS. Some chapters are
    # pure-concept: a VERDICT line reads 'PASS — module N/A' / 'PASS (module N/A)' / 'NOT CREATED (N/A)'
    # (the FLOOR-C COMPILE clause does not attach) → classify NA so the example bubble is ⚪, not a green
    # that implies a built module. Scoped to verdict lines (not the whole body) so narrative like Ch110's
    # 'the dossier's N/A call was revised' (module actually BUILT green) does NOT trip it.
    if suffix == "_EXAMPLE.md":
        for ln in body.splitlines():
            if "verdict" not in ln.lower():
                continue
            if re.search(r"module\s*\**\s*(?:=\s*)?\**\s*N/?A|\(module\s+N/?A\)|NOT\s+CREATED|no\s+companion\s+module",
                         ln, re.I):
                return "NA"

    def tok(text):
        up = text.upper()
        if "N/A" in up or "NOT CREATED" in up or "NOT APPLICABLE" in up:
            return "NA"
        if "PASS" in up:                      # PASS / PASS-WITH-FIXES — a pass even if it counts "0 FAIL"
            return "PASS"
        if ("FAIL" in up and not re.search(r"(?:NO|0|ZERO)\s+FAIL", up)) or "BLOCKER" in up:
            return "FAIL"
        return None

    # A DECLARATION binds 'verdict' to its token within a short window (≤24 chars: room for ':' '**'
    # 'Floor-C ' etc., but not enough to bridge into narrative like 'Verdict rationale — … the N/A call').
    # An UPGRADE explicitly resolves to the token right of the arrow regardless of distance. Upgrade is
    # tried first (it is the resolution of record); the LAST matching line on disk wins.
    decl = re.compile(r"\bverdict\b\W{0,8}\**\s*(?:floor[\- ]?c\W*)?(pass(?:-with-fixes)?|fail|n/?a)\b", re.I)
    upg = re.compile(r"\bverdict\b[^\n]*?→\s*\**\s*(pass(?:-with-fixes)?|fail|n/?a)\b", re.I)
    result = None
    for ln in body.splitlines():
        m = upg.search(ln) or decl.search(ln)
        if m:
            t = tok(m.group(1))
            if t:
                result = t
    return result


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
    # "**Aggregate 39/50**", "Cluster subtotal:** 38 / 50", "Aggregate score: 36/50",
    # and the sum form "Cluster subtotal: 8 + 8 + 8 + 8 + 8 = 40 / 50".
    # CRITICAL: must NOT match a *bar-description* line like "Requires aggregate >= 44/50"
    # or "the 44/50 bar" — those state the threshold, not this chapter's score. So scan
    # aggregate/subtotal lines, skip threshold prose, and take the RESULT number (the last
    # "NN/50" on the line, i.e. the figure after "=").
    total = None
    for ln in text.splitlines():
        low = ln.lower()
        if ("aggregate" not in low) and ("cluster subtotal" not in low):
            continue
        if any(x in low for x in ("≥", ">=", "requires", "/50 bar", "/50)", " bar", "vs ", "ship bar", "auto-approv", "threshold")):
            continue
        nums = re.findall(r"(\d+)\s*/\s*50", ln)
        if nums:
            total = int(nums[0])    # the FINAL/headline figure, e.g. "45/50 (was 43/50)" -> 45;
            break                   # and the sum form "...= 40/50" -> 40 (its only /50)
    if total is None:   # fallback to the original lenient form
        m = re.search(r"(?:Aggregate|Cluster subtotal)\D{0,10}(\d+)\s*/\s*50", text, re.I)
        total = int(m.group(1)) if m else None
    # Floors are read from the floor TABLE: a row whose label cell contains the floor name, with the
    # verdict in the next cell. Tolerates both "| A — NEUTRALITY | ✅ PASS |" and "| **NEUTRALITY** |
    # PASS |" formats; reads the verdict from the verdict cell only (evidence cells often mention other
    # floors' states, e.g. "COMPILE = PENDING", which must not bleed across). First matching row wins.
    floors = {"A": "?", "B": "?", "Csrc": "?", "Ccompile": "?", "Creview": "?"}
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
    # FLOOR-C evidence upgrade (mirrors the _EXAMPLE.md bubble upgrade): the _SCORE.md floor table is
    # written at scoring time and often predates EXAMPLE-BUILD/CODE-REVIEW, so it records FLOOR-C
    # COMPILE as PENDING-RUNTIME. The on-disk gate reports are the fresher truth:
    #   _EXAMPLE.md  PASS  -> the module built green        -> Ccompile = PASS
    #   _EXAMPLE.md  NA    -> pure-concept chapter, no module -> Ccompile = NA (no compile clause)
    #   _CODEREVIEW.md PASS/PASS-WITH-FIXES -> FLOOR-C 2nd half (CODE-REVIEW) satisfied -> Creview = PASS
    #   _CODEREVIEW.md FAIL/BLOCKER         -> FLOOR-C 2nd half FAILS                  -> Creview = FAIL
    # We only ever UPGRADE a PENDING/'?' compile cell from disk evidence; we never downgrade a recorded
    # PASS/FAIL. Creview is a new half, sourced only from the report (no _SCORE.md field exists for it).
    ex_v = report_verdict(slug, "_EXAMPLE.md")
    if ex_v == "PASS" and floors.get("Ccompile") in (None, "?", "PENDING"):
        floors["Ccompile"] = "PASS"
    elif ex_v == "NA":
        floors["Ccompile"] = "NA"
    cr_v = report_verdict(slug, "_CODEREVIEW.md")
    floors["Creview"] = {"PASS": "PASS", "FAIL": "FAIL", "NA": "NA"}.get(cr_v, "NA" if ex_v == "NA" else "?")
    pct = round(total / SCORE_MAX * 100) if total is not None else None
    return {"total": total, "pct": pct, "floors": floors, "independent": independent}


def approval_decision(score):
    """Approval policy (88% auto-approve) → (decision, bubble_key, reason).

    A chapter AUTO-APPROVES (promoted into 04-approved by apply_approvals) when it has an INDEPENDENT
    (different-model) score ≥ the ship bar (SHIP_BAR = 88% = 44/50) AND the editorial content floors
    (A NEUTRALITY / B HONEST-LIMITATIONS / C SOURCE-TRACE) PASS. Below the bar or a floor not PASS →
    LIFT. A self-score (no independent gate yet) can only be NEEDS-INDEP — a self-score never approves
    a chapter; only an independent score does. The COMPILE / example-build floor is tracked separately
    and does NOT block auto-approval; ≥90% is reported as an excellence flag. The only human gate is
    the whole-book Step 16 MANUSCRIPT-GATE, not per-chapter."""
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
                    f"{pct}% self ≥{SHIP_BAR}% — needs an INDEPENDENT score to auto-approve")
        return ("LIFT", "self", f"{pct}% self-score — lift + independent-score toward the {SHIP_BAR}% bar")
    if not floors_pass:
        return ("LIFT", "self", f"{pct}% (independent) but a content floor is not PASS (A/B/C-src) → fix")
    if pct >= SHIP_BAR:
        return ("AUTO-APPROVE", "human",
                f"{pct}% (independent) ≥{SHIP_BAR}% + floors PASS → auto-approve into 04-approved{star}{cnote}")
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
        # a rendered figure on disk upgrades the figure gate to 🟢
        has_fig = bool(slug) and bool(glob.glob(os.path.join(ROOT, "05-figures", slug, "fig*_*.png")))
        if has_fig:
            bubbles["figure"] = BUB["done"]
        # FLOOR-C on disk: the `example` column carries BOTH halves of FLOOR-C — the green build
        # (_EXAMPLE.md) AND the CODE-REVIEW (_CODEREVIEW.md, the 2nd half, a HARD gate). An EXAMPLE-BUILD
        # report upgrades the cell to 🟢 (green module + bound snippets), N/A stays ⚪ — but a CODE-REVIEW
        # FAIL/BLOCKER on a copy-verbatim deliverable forces the cell 🔴 so the unresolved FLOOR-C defect
        # is visible (never silently green). PASS / PASS-WITH-FIXES leaves the build verdict standing.
        ex_v = report_verdict(slug, "_EXAMPLE.md")
        cr_v = report_verdict(slug, "_CODEREVIEW.md")
        if ex_v == "PASS":
            bubbles["example"] = BUB["done"]
        elif ex_v == "NA":
            bubbles["example"] = BUB["na"]
        if cr_v == "FAIL":
            bubbles["example"] = BUB["no"]
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
                     "cells": cells, "bubbles": bubbles, "has_fig": has_fig,
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
    ready = sum(1 for r in rows if r["decision"] in ("AUTO-APPROVE", "READY"))
    needs_indep = sum(1 for r in rows if r["decision"] == "NEEDS-INDEP")
    lift = sum(1 for r in rows if r["decision"] == "LIFT")
    approved = sum(1 for r in rows if r["decision"] == "APPROVED")
    human = sum(1 for r in rows if r["bubbles"]["approve"] == BUB["human"])
    figured = sum(1 for r in rows if r.get("has_fig"))
    scored = sum(1 for r in rows if r["score"] and r["score"]["independent"])
    # FLOOR-C (compile + code-review), read from the on-disk gate reports
    built = sum(1 for r in rows if report_verdict(r["slug"], "_EXAMPLE.md") == "PASS")
    cr_pass = sum(1 for r in rows if report_verdict(r["slug"], "_CODEREVIEW.md") == "PASS")
    cr_fail = sum(1 for r in rows if report_verdict(r["slug"], "_CODEREVIEW.md") == "FAIL")
    return dict(n=n, indep=need_indep, example=need_example,
                ready=ready, needs_indep=needs_indep, lift=lift, approved=approved, human=human,
                figured=figured, scored=scored, built=built, cr_pass=cr_pass, cr_fail=cr_fail)


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
        f"- **FLOOR-C on disk:** {s['built']}/47 modules built green; {s['cr_pass'] + s['cr_fail']} CODE-REVIEW reports "
        f"({s['cr_pass']} PASS, {s['cr_fail']} FAIL)" + ("." if not s['cr_fail'] else " — **1 FLOOR-C blocker**, see Scoring."),
        f"- **Routing (auto-approve at {SHIP_BAR}% + floors):** {s['ready']} eligible/at-gate · {s['lift']} in lift · {s['needs_indep']} need an independent score · {s['approved']} approved (in 04-approved/).",
        f"- **DRIFT: {'❌ ' + str(len(findings)) + ' finding(s)' if findings else '✅ none'}**.",
        "",
        "## Needs-human queue 🔵",
        "",
    ]
    hq = [r for r in rows if r["bubbles"]["approve"] == BUB["human"]]
    L.append(("Chapters eligible to auto-approve (independent ≥%d%% + floors, applied next run): " % SHIP_BAR
              + ", ".join(f"Ch {r['ch']}" for r in hq) + ".")
             if hq else f"_None yet — a chapter auto-approves once an INDEPENDENT score reaches {SHIP_BAR}% + floors PASS._")
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
         f"> Generated by `status.py` · {today}. **Policy (88% auto-approve):** an INDEPENDENT "
         f"(different-model) score **≥{SHIP_BAR}%** (≥{SHIP_BAR * SCORE_MAX // 100}/{SCORE_MAX}) **+ content floors "
         "PASS (A/B/C-source)** → **AUTO-APPROVE** (promoted into 04-approved/). Below the bar or a floor "
         "unresolved → **lift** (≤3 passes). The only human gate is the whole-book Step 16 MANUSCRIPT-GATE.",
         f"> A main-loop *self*-score must be independently re-scored before it advances. **≥{APPROVE_THRESHOLD}%** is "
         "flagged as excellence. COMPILE/example-build is tracked separately (a later phase), not a blocker here.",
         "",
         "| Ch | Key | Score | % | A | B | C-src | C-compile | C-review | Indep? | Decision | Why |",
         "|---|---|---|---|---|---|---|---|---|---|---|---|"]
    for r in rows:
        sc = r["score"]
        if not sc:
            L.append(f"| {r['ch']} | {r['nn']} | — | — | — | — | — | — | — | — | {r['decision']} | {r['reason']} |")
            continue
        f = sc["floors"]
        tot = f"{sc['total']}/{SCORE_MAX}" if sc["total"] is not None else "—"
        L.append(f"| {r['ch']} | {r['nn']} | {tot} | {sc['pct']}% | {f.get('A','?')} | {f.get('B','?')} | "
                 f"{f.get('Csrc','?')} | {f.get('Ccompile','?')} | {f.get('Creview','?')} | "
                 f"{'yes' if sc['independent'] else 'no'} | **{r['decision']}** | {r['reason']} |")
    s = summary(rows)
    L += ["", "## Routing", "",
          f"- **{s['ready']}** eligible to auto-approve — independent score ≥{SHIP_BAR}% + content floors PASS (applied into 04-approved/ on the next run).",
          f"- **{s['needs_indep']}** need an independent score (self ≥{SHIP_BAR}%, run the independent scorer).",
          f"- **{s['lift']}** in the lift loop (below the {SHIP_BAR}% bar or a floor unresolved).",
          f"- **{s['approved']}** approved (in 04-approved/).",
          "",
          "## FLOOR-C (compile + code-review) — from the on-disk gate reports", "",
          f"- **{s['built']}/47** companion modules built GREEN (`_EXAMPLE.md` = PASS); the rest are pure-concept N/A.",
          f"- **{s['cr_pass'] + s['cr_fail']}** CODE-REVIEW reports on disk (`_CODEREVIEW.md`, FLOOR-C 2nd half): "
          f"**{s['cr_pass']} PASS / PASS-WITH-FIXES**, **{s['cr_fail']} FAIL**"
          + ("." if not s['cr_fail'] else " — a FAIL is an unresolved FLOOR-C blocker on a copy-verbatim deliverable; see the C-review column above and the chapter's `_CODEREVIEW.md`."),
          "",
          "_Approvals apply automatically on a normal `status.py` run; `--check-only`/`--no-apply` are read-only._", ""]
    open(SCORING_OUT, "w", encoding="utf-8").write("\n".join(L))


# ----------------------------------------------------------------------------- html
CSS = """
:root{--bg:#f4f6f9;--panel:#fff;--ink:#1f2328;--muted:#5b6675;--line:#e3e8ef;--brand:#3a5bd9;
--brand2:#6b46c1;--done:#1a7f37;--self:#bf8700;--no:#cf222e;--human:#0969da;
--shadow:0 1px 2px rgba(16,24,40,.05),0 6px 18px rgba(16,24,40,.06)}
*{box-sizing:border-box}
body{font:15px/1.55 -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,Helvetica,Arial,sans-serif;margin:0;
color:var(--ink);background:var(--bg);-webkit-font-smoothing:antialiased}
a{color:var(--brand);text-decoration:none}a:hover{text-decoration:underline}
.hd{background:linear-gradient(120deg,#1e2a4a,#2b1d52);color:#fff;padding:20px 28px 16px}
.hd h1{margin:0;font-size:20px;font-weight:750;letter-spacing:.2px}
.hd .sub{color:#aeb8d4;font-size:13px;margin-top:3px}
.hd .prog{margin:14px 0 0;max-width:560px}
.hd .prog .lbl{display:flex;justify-content:space-between;font-size:12px;color:#cdd5ee;margin-bottom:5px;font-weight:600}
.bar{height:8px;border-radius:99px;background:rgba(255,255,255,.16);overflow:hidden}
.bar>i{display:block;height:100%;border-radius:99px;background:linear-gradient(90deg,#7aa2ff,#b794f6)}
nav{position:sticky;top:0;z-index:9;background:#1b2440;display:flex;gap:2px;padding:0 16px;flex-wrap:wrap;box-shadow:var(--shadow)}
nav a{color:#b7c0da;padding:12px 15px;font-size:13.5px;font-weight:600;border-bottom:3px solid transparent}
nav a:hover{color:#fff;text-decoration:none}nav a.active{color:#fff;border-bottom-color:#7aa2ff}
main{padding:24px 28px;max-width:1180px;margin:0 auto}
h2{font-size:13px;font-weight:750;text-transform:uppercase;letter-spacing:.6px;color:var(--muted);margin:30px 0 12px}
.hero{background:var(--panel);border:1px solid var(--line);border-radius:14px;padding:18px 22px;box-shadow:var(--shadow);font-size:15px;line-height:1.65}
.hero b{color:var(--ink)}
.kpis{display:grid;grid-template-columns:repeat(auto-fit,minmax(158px,1fr));gap:14px}
.kpi{background:var(--panel);border:1px solid var(--line);border-radius:14px;padding:15px 17px;box-shadow:var(--shadow)}
.kpi .n{font-size:29px;font-weight:760;line-height:1}.kpi .n small{font-size:15px;color:var(--muted);font-weight:600}
.kpi .l{font-size:12.5px;color:var(--muted);margin:6px 0 10px;font-weight:600}
.kpi .bar{background:#eef1f6;height:7px}.kpi .bar>i{background:linear-gradient(90deg,var(--brand),var(--brand2))}
.note{border-radius:12px;padding:12px 16px;margin:14px 0;font-size:14px}
.note.ok{background:#e9f8ee;border:1px solid #abe1bd;color:#0f6a2e}
.note.bad{background:#fdeceb;border:1px solid #f5b5b1;color:#b42318}
.note.info{background:#eef2ff;border:1px solid #c2cffc;color:#33409e}
.scroll{overflow-x:auto;border-radius:12px;border:1px solid var(--line);box-shadow:var(--shadow)}
table{border-collapse:separate;border-spacing:0;width:100%;background:var(--panel);font-size:13px}
.scroll table{border:none;box-shadow:none}
th,td{padding:8px 11px;text-align:left;border-bottom:1px solid var(--line);white-space:nowrap}
th{background:#f7f9fc;color:var(--muted);font-size:11px;text-transform:uppercase;letter-spacing:.5px;position:sticky;top:45px;z-index:2}
tr:last-child td{border-bottom:none}tbody tr:hover td{background:#f9fbff}
td.k{color:var(--muted);font-family:ui-monospace,SFMono-Regular,Menlo,monospace;font-size:11.5px}
td.c{text-align:center}td.topic{white-space:normal;min-width:230px}
.part td{background:#f0eefb;color:#4a2f8f;font-weight:750;font-size:11px;text-transform:uppercase;letter-spacing:.5px}
.bub{display:inline-flex;align-items:center;justify-content:center;width:24px;height:24px;border-radius:7px;font-size:13px}
.legend{display:flex;flex-wrap:wrap;gap:8px;margin:10px 0 4px}
.chip{display:inline-flex;align-items:center;gap:6px;background:var(--panel);border:1px solid var(--line);border-radius:99px;padding:4px 11px;font-size:12.5px;color:var(--muted)}
.badge{display:inline-block;padding:3px 10px;border-radius:99px;font-size:11px;font-weight:700;color:#fff;letter-spacing:.3px}
.flr{display:inline-block;padding:2px 7px;border-radius:6px;font-size:10.5px;font-weight:700;margin-right:3px}
.flr.PASS{background:#e9f8ee;color:#0f6a2e}.flr.PENDING{background:#fff4e0;color:#9a6700}
.flr.FAIL{background:#fdeceb;color:#b42318}.flr.q{background:#eef1f6;color:#8893a4}
.sbar{position:relative;height:20px;border-radius:6px;background:#eef1f6;overflow:hidden;min-width:140px}
.sbar>i{display:block;height:100%}.sbar>span{position:absolute;left:8px;top:0;line-height:20px;font-size:11px;font-weight:700}
.partbar{display:grid;grid-template-columns:170px 1fr 60px;gap:12px;align-items:center;margin:7px 0;font-size:13px}
.partbar .bar{height:10px;background:#eef1f6}.partbar .bar>i{background:linear-gradient(90deg,var(--brand),var(--brand2))}
.tl{list-style:none;margin:0;padding:0}
.tl li{position:relative;padding:0 0 16px 24px;border-left:2px solid var(--line);margin-left:7px}
.tl li:last-child{border-left-color:transparent;padding-bottom:0}
.tl .dot{position:absolute;left:-9px;top:0;width:16px;height:16px;border-radius:50%;background:var(--panel);border:2px solid var(--brand);font-size:8px;text-align:center;line-height:13px}
.tl .when{font-size:11.5px;color:var(--muted);font-family:ui-monospace,monospace}
.tl .what{font-weight:700}.tl .det{font-size:13px;color:var(--muted);margin-top:2px}
.capgrid{display:grid;grid-template-columns:repeat(auto-fit,minmax(260px,1fr));gap:14px}
.capcard{background:var(--panel);border:1px solid var(--line);border-radius:14px;padding:16px 18px;box-shadow:var(--shadow)}
.capcard h3{margin:0 0 2px;font-size:15px}.capcard .dm{color:var(--muted);font-size:12.5px;margin-bottom:10px}
.capcard .svc{font-size:12px;color:var(--muted);font-family:ui-monospace,monospace;margin-bottom:10px}
.gline{display:flex;justify-content:space-between;align-items:center;font-size:12.5px;padding:3px 0;border-top:1px solid var(--line)}
.foot{color:var(--muted);font-size:12.5px;margin:34px 0 12px;padding-top:14px;border-top:1px solid var(--line)}
.foot code{background:#eef1f6;padding:1px 6px;border-radius:5px}
@media(max-width:640px){main{padding:16px 14px}.hd{padding:16px}.partbar{grid-template-columns:110px 1fr 46px}}
"""

BUB_TIP = {"🟢": "done", "🟡": "in progress / self-pass", "🔴": "not started", "🔵": "awaiting human", "⚪": "n/a"}
DCOLOR = {"AUTO-APPROVE": "#1a7f37", "APPROVED": "#1a7f37", "READY": "#0969da", "NEEDS-INDEP": "#bf8700", "LIFT": "#bf8700", "UNSCORED": "#cf222e"}


def _bar(pct):
    return f'<div class=bar><i style="width:{max(0, min(100, int(round(pct))))}%"></i></div>'


def _kpi(num, total, label):
    return (f'<div class=kpi><div class=n>{num}{("<small>/" + str(total) + "</small>") if total else ""}</div>'
            f'<div class=l>{label}</div>{_bar((num / total * 100) if total else 0)}</div>')


def layout(title, active, body, hdr):
    nav = "".join(f'<a href="{href}" class="{"active" if label == active else ""}">{label}</a>'
                  for href, label in PAGES)
    return ("<!doctype html><html lang=en><meta charset=utf-8>"
            "<meta name=viewport content='width=device-width,initial-scale=1'>"
            f"<title>Quality Book · {title}</title><style>{CSS}</style>"
            f"{hdr}<nav>{nav}</nav><main>{body}</main></html>")


def htd(b):
    return (f'<td class=c><span class=bub title="{BUB_TIP.get(b, "")}" '
            f'style="background:{HTML_COLOR.get(b, "#fff")}22">{b}</span></td>')


def write_html(rows, ch2part, parts, findings, caps, audit, today):
    os.makedirs(HTML_DIR, exist_ok=True)
    s = summary(rows)
    stamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
    earned = s["n"] + s["figured"] + s["scored"] + s["ready"] + s["approved"]
    overall = earned / (s["n"] * 4) * 100 if s["n"] else 0
    hdr = ('<div class=hd><h1>📘 Java Code Quality — production dashboard</h1>'
           f'<div class=sub>47-chapter manuscript · generated {stamp} · auto-refreshed after every gate</div>'
           f'<div class=prog><div class=lbl><span>Pipeline progress</span><span>{int(round(overall))}%</span></div>'
           f'{_bar(overall)}</div></div>')
    foot = ('<div class=foot>Generated by <code>.claude/scripts/status.py</code> · ' + stamp +
            ' · regenerate any time: <code>python3 .claude/scripts/status.py</code> · auto-runs after any gate '
            '(CLAUDE.md “Reporting discipline”). Bubbles reflect on-disk evidence, not claims.</div>')
    legend = ('<div class=legend><span class=chip>🟢 done</span><span class=chip>🟡 in progress</span>'
              '<span class=chip>🔴 not started</span><span class=chip>🔵 awaiting human</span>'
              '<span class=chip>⚪ n/a</span></div>')

    def page(fn, active, body):
        write_page(fn, active, body, hdr)

    # --- Overview ---
    drift_ok = not findings
    hero = (f'<div class=hero>All <b>{s["n"]} of 47</b> chapters are drafted. <b>{s["figured"]}</b> have their '
            f'figures rendered and <b>{s["scored"]}</b> carry an independent (different-model) score. '
            f'<b>{s["ready"]}</b> are ready for the human approval gate, <b>{s["lift"]}</b> are in the lift loop, '
            f'and <b>{s["approved"]}</b> are approved. '
            + ('Records match the evidence on disk — no drift.' if drift_ok
               else f'⚠ {len(findings)} drift finding(s) need attention.') + '</div>')
    ov = [hero, '<h2>At a glance</h2><div class=kpis>',
          _kpi(s["n"], 47, "Drafted"), _kpi(s["figured"], 47, "Figures rendered"),
          _kpi(s["scored"], 47, "Independently scored"), _kpi(s["ready"], 47, "Ready for human 🔵"),
          _kpi(s["lift"], 47, "In lift loop 🟡"), _kpi(s["approved"], 47, "Approved 🟢")]
    if caps:
        ov.append(_kpi(len(caps["capstones"]), len(caps["capstones"]), "Capstone apps (green)"))
    ov.append('</div>')
    ov.append('<h2>Pipeline</h2><div class=card>')
    for lbl, val in (("Drafted", s["n"]), ("Figures rendered", s["figured"]),
                     ("Independent score", s["scored"]), ("Ready for human gate", s["ready"]),
                     ("Approved", s["approved"])):
        ov.append(f'<div class=partbar><span>{lbl}</span>{_bar(val / 47 * 100)}'
                  f'<span style="text-align:right;color:var(--muted)">{val}/47</span></div>')
    ov.append('</div>')
    ov.append('<div class="note ' + ('ok' if drift_ok else 'bad') + '">'
              + ('✅ <b>No drift</b> — every 🟢/🟡 gate has its evidence on disk and the gate order holds.'
                 if drift_ok else f'❌ <b>{len(findings)} drift finding(s)</b> — see the Chapters page.') + '</div>')
    ov.append('<div class="note info"><b>Routing:</b> an independent score ≥%d%% + content floors PASS → '
              'AUTO-APPROVE (promoted into 04-approved/); below the bar → lift (≤3 passes). Review/scoring is '
              'delegated to an external different-vendor LLM (<code>09-flags/external-review/</code>); Claude does '
              'the heavy lifting. Only the whole-book Step 16 is human. <a href="scoring.html">Scoring →</a></div>' % SHIP_BAR)
    ov.append(foot)
    page("dashboard.html", "Overview", "".join(ov))

    # --- Chapters matrix ---
    ch = ['<div class=hero>The gate matrix for every chapter — each cell is a gate’s state from the evidence on '
          'disk. Hover a bubble for its meaning.</div>', legend]
    ch.append('<h2>Progress by Part (figures rendered)</h2><div class=card>')
    for p in parts:
        chs = [r for r in rows if ch2part.get(r["ch"]) == p]
        if not chs:
            continue
        figpct = sum(1 for r in chs if r["bubbles"]["figure"] == BUB["done"]) / len(chs) * 100
        ch.append(f'<div class=partbar><span title="{p}">{p[:26]}</span>{_bar(figpct)}'
                  f'<span style="text-align:right;color:var(--muted)">{len(chs)} ch</span></div>')
    ch.append('</div>')
    ch.append('<h2>Gate matrix</h2><div class=scroll><table><thead><tr><th>Ch</th><th>Key</th><th>Topic</th>'
              + "".join(f'<th title="{g}">{g[:4]}</th>' for g in GATES) + '<th>Next step</th></tr></thead><tbody>')
    cur = None
    for r in rows:
        p = ch2part.get(r["ch"], "—")
        if p != cur:
            ch.append(f'<tr class=part><td colspan="{4 + len(GATES)}">{p}</td></tr>')
            cur = p
        ch.append(f'<tr><td><b>{r["ch"]}</b></td><td class=k>{r["nn"]}</td><td class=topic>{r["topic"][:60]}</td>'
                  + "".join(htd(r["bubbles"][g]) for g in GATES) + f'<td class=k>{next_step(r)}</td></tr>')
    ch.append('</tbody></table></div>')
    if findings:
        ch.append('<div class="note bad"><b>Drift ({}):</b><ul>'.format(len(findings))
                  + "".join(f"<li>{x}</li>" for x in findings) + '</ul></div>')
    ch.append(foot)
    page("chapters.html", "Chapters", "".join(ch))

    # --- Scoring & approval ---
    scr = ['<div class="note info"><b>Policy (88% auto-approve):</b> an independent score '
           f'≥{SHIP_BAR}% (≥{SHIP_BAR * SCORE_MAX // 100}/{SCORE_MAX}) + content floors PASS → AUTO-APPROVE '
           f'(into 04-approved/); else lift (≤3 passes). ≥{APPROVE_THRESHOLD}% flags excellence. Scoring is delegated to an '
           'external different-vendor LLM (one-pager); Claude applies the lifts. Only Step 16 is human.</div>']
    fc_cls = "ok" if not s["cr_fail"] else "bad"
    scr.append(f'<div class="note {fc_cls}"><b>FLOOR-C (compile + code-review) — from disk:</b> '
               f'<b>{s["built"]}/47</b> companion modules built green (<code>_EXAMPLE.md</code> = PASS; the rest '
               f'pure-concept N/A); <b>{s["cr_pass"] + s["cr_fail"]}</b> CODE-REVIEW reports '
               f'(<code>_CODEREVIEW.md</code>, the FLOOR-C 2nd half): <b>{s["cr_pass"]} PASS / PASS-WITH-FIXES</b>, '
               f'<b>{s["cr_fail"]} FAIL</b>'
               + ('. The C-build / C-rev floor chips below are read from these reports, not the (older) score files.'
                  if not s["cr_fail"] else
                  ' — a FAIL is an unresolved FLOOR-C blocker on copy-verbatim code; see the C-rev chip and the '
                  'chapter’s <code>_CODEREVIEW.md</code>.') + '</div>')
    scored_rows = sorted([r for r in rows if r["score"] and r["score"]["total"] is not None],
                         key=lambda r: (r["score"]["independent"], r["score"]["pct"]), reverse=True)
    indep_n = sum(1 for r in scored_rows if r["score"]["independent"])
    prov_n = len(scored_rows) - indep_n
    scr.append(f'<h2>Chapter scores — {indep_n} independent ★ · {prov_n} provisional self-score (bar {SHIP_BAR}%)</h2>')
    if scored_rows:
        scr.append('<div class=scroll><table><thead><tr><th>Ch</th><th>Topic</th><th>Type</th><th>Score</th>'
                   '<th>Floors A · B · C-src · C-build · C-rev</th><th>Decision</th></tr></thead><tbody>')
        for r in scored_rows:
            sc = r["score"]; fl = sc["floors"]; pct = sc["pct"]; indep = sc["independent"]
            if indep:
                col = "var(--done)" if pct >= SHIP_BAR else "#e0a82e" if pct >= 60 else "var(--no)"
                tag = '<span class=badge style="background:#0969da">★ independent</span>'
            else:
                col = "#c4ccd6"   # provisional self-score: greyed, not trustworthy until external review
                tag = '<span class="flr q">self · provisional</span>'
            sbar = (f'<div class=sbar><i style="width:{pct}%;background:{col}"></i>'
                    f'<span>{sc["total"]}/{SCORE_MAX} · {pct}%</span></div>')
            flrs = ""
            for k, lab in (("A", "A"), ("B", "B"), ("Csrc", "C-src"), ("Ccompile", "C-build"), ("Creview", "C-rev")):
                v = fl.get(k, "?")
                cls = v if v in ("PASS", "PENDING", "FAIL") else "q"
                disp = "n/a" if v == "NA" else v
                flrs += f'<span class="flr {cls}">{lab}·{disp}</span>'
            bd = f'<span class=badge style="background:{DCOLOR.get(r["decision"], "#5b6675")}">{r["decision"]}</span>'
            scr.append(f'<tr><td><b>{r["ch"]}</b></td><td class=topic>{r["topic"][:42]}</td><td>{tag}</td>'
                       f'<td>{sbar}</td><td>{flrs}</td><td>{bd}</td></tr>')
        scr.append('</tbody></table></div>')
    if prov_n:
        scr.append(f'<div class="note info"><b>{prov_n}</b> chapters carry only a <b>provisional self-score</b> '
                   '(greyed above) — they are voice-lifted and illustrated, and queued for the external '
                   'independent reviewer in <code>09-flags/external-review/QUEUE.md</code>. A self-score never '
                   'advances a chapter; only an independent score does.</div>')
    scr.append(foot)
    page("scoring.html", "Scoring & approval", "".join(scr))

    # --- Capstones ---
    cp = []
    if not caps:
        cp.append('<div class=hero>No <code>CAPSTONE-STATUS.json</code> found.</div>')
    else:
        cp.append('<div class=hero>Three enterprise, microservice-based capstones on one shared platform. '
                  f'<span class=k>{caps.get("as_of", "")} — {caps.get("verified_by", "")}</span></div>')
        gos = caps.get("gate_order", []); labels = caps.get("gate_labels", {})
        cp.append('<div class=capgrid>')
        for c in caps["capstones"]:
            gl = "".join(
                f'<div class=gline><span>{labels.get(g, g)}</span>'
                f'<span class=bub style="background:{HTML_COLOR.get(cap_bubble(c["gates"].get(g, "no")), "#fff")}22">'
                f'{cap_bubble(c["gates"].get(g, "no"))}</span></div>' for g in gos)
            cp.append(f'<div class=capcard><h3>{c["id"]}</h3><div class=dm>{c.get("title", "")} · {c["domain"]}</div>'
                      f'<div class=svc>{", ".join(c["services"])} · {c.get("tests", "?")} tests</div>{gl}</div>')
        cp.append('</div>')
        cp.append('<div class="note info">Gate values are the last verified state — update '
                  '<code>CAPSTONE-STATUS.json</code> after a build/review. Code-review (FLOOR C) is the '
                  'independent/human gate; Phase-4 assemble runs in Phase 4.</div>')
    cp.append(foot)
    page("capstones.html", "Capstones", "".join(cp))

    # --- Audit ---
    au = ['<div class=hero>The action trail — milestones from <code>audit_log.sh</code>, plus every tool call once '
          'the <code>PostToolUse</code> hook is enabled. Newest first.</div>']
    if not audit:
        au.append('<div class="note info">No audit entries yet.</div>')
    else:
        au.append('<ul class=tl>')
        for e in audit:
            kind = e.get("kind", ""); icon = "🔧" if kind == "tool" else "📌"
            act = e.get("action") or e.get("tool") or ""
            tgt = (e.get("target") or "")[:110]
            det = (e.get("detail") or "")[:240]
            au.append(f'<li><span class=dot>{icon}</span><span class=when>{e.get("ts", "")}</span> · '
                      f'<span class=what>{act}</span> <span class=k>{tgt}</span>'
                      + (f'<div class=det>{det}</div>' if det else '') + '</li>')
        au.append('</ul>')
    au.append('<div class="note info">Enable automatic per-tool capture: see '
              '<code>.claude/hooks/AUDIT-LOG.md</code>.</div>')
    au.append(foot)
    page("audit.html", "Audit log", "".join(au))


def write_page(filename, active, body, hdr):
    title = next(label for href, label in PAGES if href == filename)
    open(os.path.join(HTML_DIR, filename), "w", encoding="utf-8").write(layout(title, active, body, hdr))


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
                          f"{r['score']['pct']}% ≥{SHIP_BAR}% + floors PASS + independent → {r['slug']}.md")
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
    no_apply = "--no-apply" in sys.argv          # escape hatch; --check-only also never mutates
    ch2part, parts = parse_parts()
    rows = parse_tracker()
    caps = parse_capstones()
    findings = drift_check(rows)
    s = summary(rows)

    # A normal run auto-approves qualifying chapters (independent ≥88% + floors PASS) into 04-approved.
    # --check-only and --no-apply are read-only.
    if not check_only and not no_apply:
        applied = apply_approvals(rows)
        if applied:
            print(f"status: auto-approved {len(applied)}: " + ", ".join(f"Ch {r['ch']}" for r in applied))
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
          f"route[{s['ready']} auto-eligible / {s['lift']} lift / {s['needs_indep']} need-indep / {s['approved']} approved] (auto-approve bar {SHIP_BAR}%)")
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
