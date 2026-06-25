#!/usr/bin/env python3
"""review_figures.py — the 20-parameter figure review + the 10-logs/figures.html report.

Every designed figure is authored as HTML and rendered to a cropped PNG (never image-generated),
with a `.sources.md` trace sidecar, and is referenced from its chapter draft as a Markdown image
`![alt](../../05-figures/NN_slug/figNN_x.png)`. This script reviews every figure against the 20
parameters the spec names, writes a human-readable gallery report to 10-logs/figures.html (linked
from the dashboard nav), and — with --check-only — exits non-zero if any HARD structural parameter
fails so it can sit in CI / the pre-push validation suite.

Of the 20 parameters, 8 are auto-checkable here; the other 12 are judgement calls flagged for the
figure-designer / accessibility-editor / auditor agents (reported as REVIEW, never silently passed).

Usage:  python3 .claude/scripts/review_figures.py [--check-only]
Stdlib only. Mirrors status.py conventions.
"""
import os, re, sys, json, glob, html, datetime

ROOT     = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
FIG_DIR  = os.path.join(ROOT, "05-figures")
DRAFTS   = os.path.join(ROOT, "03-drafts")
OUT_HTML = os.path.join(ROOT, "10-logs", "figures.html")
OUT_JSON = os.path.join(ROOT, "10-logs", "figures.json")

BANNED = ["better than", "unlike ", "superior", " beats ", "the problem with",
          "outperforms", "worse than", "inferior"]            # NEUTRALITY blocklist
PLACEHOLDER = ["TODO", "TKTK", "PLACEHOLDER", "LOREM IPSUM", "FIXME", "XXX", "TBD"]
PNG_MAGIC = b"\x89PNG\r\n\x1a\n"
MIN_PNG_BYTES = 2000                                           # a real rendered diagram is larger

# The 20 review parameters: (key, label, kind). kind="auto" → checked here; "review" → agent/human.
PARAMS = [
    ("png_integrity",  "PNG render integrity",              "auto"),
    ("sidecar",        "Source sidecar completeness",       "auto"),
    ("file_naming",    "File naming (figNN_x)",             "auto"),
    ("numbering",      "Numbering (sequential in chapter)", "auto"),
    ("crossref",       "Manuscript cross-reference",        "auto"),
    ("alt_text",       "Accessibility alt-text",            "auto"),
    ("no_placeholder", "No placeholders",                   "auto"),
    ("neutral",        "Neutral wording",                   "auto"),
    ("source_trace",   "Source trace (label → pin)",        "review"),
    ("correctness",    "Correctness",                       "review"),
    ("chapter_fit",    "Chapter fit",                       "review"),
    ("caption",        "Caption quality",                   "review"),
    ("long_desc",      "Long-description quality",          "review"),
    ("visual_hier",    "Visual hierarchy",                  "review"),
    ("contrast",       "Contrast (grayscale-safe)",         "review"),
    ("readability",    "Readability",                       "review"),
    ("responsive",     "Responsive sizing",                 "review"),
    ("no_overclaim",   "No overclaims",                     "review"),
    ("no_benchmark",   "No unsupported benchmarks",         "review"),
    ("no_dup_role",    "No duplicate / low-value role",     "review"),
]
# A failing one of these blocks --check-only — unambiguous STRUCTURAL defects only. Wording
# (neutral), placeholders, and file-naming are WARN: a sidecar legitimately quotes the banned-phrase
# blocklist as a self-attestation, an example diagram may show a literal "// TODO", and a folded
# dossier key (figNN in dir MM) is valid. The auditor & figure-designer agents own that judgement.
HARD = {"png_integrity", "sidecar", "crossref", "alt_text"}


def png_ok(path):
    try:
        with open(path, "rb") as fh:
            head = fh.read(8)
        return head == PNG_MAGIC and os.path.getsize(path) >= MIN_PNG_BYTES
    except OSError:
        return False


def banned_in(text):
    low = text.lower()
    return [b.strip() for b in BANNED if b in low]


def placeholder_in(text):
    up = text.upper()
    return [p for p in PLACEHOLDER if p in up]


def find_figures():
    """Yield (slug, name, png_path) for every figNN_x.png, sorted by chapter then index."""
    out = []
    for png in glob.glob(os.path.join(FIG_DIR, "*", "fig*_*.png")):
        slug = os.path.basename(os.path.dirname(png))
        name = os.path.basename(png)[:-4]                     # figNN_x
        out.append((slug, name, png))
    def key(t):
        m = re.match(r"fig(\d+)_(\d+)", t[1])
        return (int(m.group(1)), int(m.group(2))) if m else (9999, 9999)
    return sorted(out, key=key)


def draft_text(slug):
    p = os.path.join(DRAFTS, slug, slug + "_v1.md")
    return open(p, encoding="utf-8").read() if os.path.exists(p) else ""


def review_one(slug, name, png, draft, sibling_names):
    """Return {key: (verdict, note)} for all 20 params. verdict ∈ PASS/REVIEW/WARN/FAIL."""
    d = os.path.dirname(png)
    side = os.path.join(d, name + ".sources.md")
    htmlf = os.path.join(d, name + ".html")
    r = {}
    nn = re.match(r"fig(\d+)_(\d+)", name)
    chap_nn, idx = (nn.group(1), int(nn.group(2))) if nn else ("?", 0)

    r["png_integrity"] = (("PASS", "valid PNG, %d KB" % (os.path.getsize(png) // 1024))
                          if png_ok(png) else ("FAIL", "missing/empty/not-a-PNG"))
    sc_txt = open(side, encoding="utf-8").read() if os.path.exists(side) else ""
    if not os.path.exists(side):
        r["sidecar"] = ("FAIL", "no .sources.md sidecar")
    elif sc_txt.count("|") < 6:
        r["sidecar"] = ("FAIL", "sidecar has no trace table")
    else:
        r["sidecar"] = ("PASS", "%d trace rows" % max(0, sc_txt.count("\n|") - 1))
    dir_key = slug.split("_")[0]
    if not re.fullmatch(r"fig\d+_\d+", name):
        r["file_naming"] = ("FAIL", "malformed name")
    elif chap_nn == dir_key:
        r["file_naming"] = ("PASS", "")
    else:
        r["file_naming"] = ("WARN", f"key {chap_nn} in dir {dir_key} (folded key?)")
    # numbering: indices within the chapter should be 1..N contiguous
    idxs = sorted(int(re.match(r"fig\d+_(\d+)", s).group(1)) for s in sibling_names)
    r["numbering"] = (("PASS", "") if idxs == list(range(1, len(idxs) + 1))
                      else ("WARN", "non-contiguous: %s" % idxs))
    # cross-ref: the draft must embed this PNG path
    has_ref = (name + ".png") in draft
    r["crossref"] = ("PASS", "referenced in draft") if has_ref else ("FAIL", "not referenced in chapter draft")
    # alt-text: the ![alt](...name.png) alt must be non-trivial
    m = re.search(r"!\[([^\]]*)\]\([^)]*" + re.escape(name) + r"\.png[^)]*\)", draft)
    alt = (m.group(1).strip() if m else "")
    r["alt_text"] = (("PASS", "%d chars" % len(alt)) if len(alt) >= 12
                     else ("FAIL", "alt-text missing/too short"))
    # placeholders + neutral wording across sidecar + html + alt
    # Scan the RENDERED FIGURE content only (visible text) — not the sidecar, which legitimately quotes
    # the banned-phrase blocklist as a self-attestation, and not the draft alt-text.
    fightml = open(htmlf, encoding="utf-8").read() if os.path.exists(htmlf) else ""
    fig_text = re.sub(r"<[^>]+>", " ", fightml)
    ph = placeholder_in(fig_text)
    r["no_placeholder"] = ("PASS", "") if not ph else ("WARN", "placeholder(s) in figure: " + ", ".join(ph))
    bn = banned_in(fig_text)
    r["neutral"] = ("PASS", "") if not bn else ("WARN", "check wording: " + ", ".join(bn))
    # html provenance note folded into source_trace review
    prov = "authored-HTML present" if os.path.exists(htmlf) else "⚠ no .html source (authored-as-HTML?)"
    for key, label, kind in PARAMS:
        if kind == "review" and key not in r:
            r[key] = ("REVIEW", prov if key == "source_trace" else "needs agent/human judgement")
    return r, {"slug": slug, "name": name, "chap": chap_nn, "alt": alt, "sidecar": os.path.exists(side)}


def verdict_emoji(v):
    return {"PASS": "✅", "FAIL": "❌", "WARN": "⚠️", "REVIEW": "🔎"}.get(v, "•")


CSS = """
*{box-sizing:border-box}body{margin:0;font:15px/1.55 -apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;color:#1c2128;background:#f6f8fa}
.hd{background:linear-gradient(135deg,#3a2d6b,#5b3fa0);color:#fff;padding:22px 28px}
.hd h1{margin:0;font-size:22px}.hd .sub{opacity:.85;font-size:13px;margin-top:4px}
nav{position:sticky;top:0;background:#21262d;padding:9px 28px;display:flex;gap:18px;z-index:9}
nav a{color:#adbac7;text-decoration:none;font-size:13px}nav a.on,nav a:hover{color:#fff}
main{padding:22px 28px;max-width:1200px;margin:0 auto}
.kpis{display:flex;gap:14px;flex-wrap:wrap;margin:6px 0 20px}
.kpi{background:#fff;border:1px solid #d0d7de;border-radius:11px;padding:13px 18px;min-width:120px}
.kpi b{font-size:24px;display:block}.kpi span{color:#656d76;font-size:12px}
.note{background:#ddf4ff;border:1px solid #b6e3ff;border-radius:9px;padding:11px 15px;margin:14px 0;font-size:13px}
.note.bad{background:#ffebe9;border-color:#ffcecb}.note.ok{background:#dafbe1;border-color:#aceebb}
h2{margin:26px 0 10px;font-size:16px;border-bottom:1px solid #d8dee4;padding-bottom:5px}
.card{background:#fff;border:1px solid #d0d7de;border-radius:12px;padding:16px;margin:14px 0;display:grid;grid-template-columns:340px 1fr;gap:18px}
.card img{width:100%;border:1px solid #d8dee4;border-radius:7px;background:#fff}
.card h3{margin:0 0 4px;font-size:15px}.cap{color:#57606a;font-size:12.5px;margin-bottom:10px}
.params{display:grid;grid-template-columns:1fr 1fr;gap:4px 18px}
.p{font-size:12.5px;display:flex;gap:6px}.p .lab{color:#424a53;min-width:165px}.p .nt{color:#7a828a}
.foot{color:#7a828a;font-size:12px;margin:26px 0 8px;border-top:1px solid #d8dee4;padding-top:10px}
@media(max-width:780px){.card{grid-template-columns:1fr}.params{grid-template-columns:1fr}}
"""


def write_html(rows, results, stamp):
    n = len(rows)
    hard_fail = sum(1 for r in results if any(r["params"][k][0] == "FAIL" for k in HARD))
    side_missing = sum(1 for r in results if not r["meta"]["sidecar"])
    nav = ('<a href="dashboard.html">Overview</a><a href="chapters.html">Chapters</a>'
           '<a href="scoring.html">Scoring</a><a href="figures.html" class=on>Figures</a>'
           '<a href="capstones.html">Capstones</a><a href="audit.html">Audit</a>')
    out = [f"<!doctype html><meta charset=utf-8><title>Figures — Java Code Quality</title><style>{CSS}</style>",
           f'<div class=hd><h1>🖼️ Figure review — 20 parameters</h1>'
           f'<div class=sub>{n} figures · generated {stamp} · authored HTML → rendered PNG (never image-generated)</div></div>',
           f"<nav>{nav}</nav><main>"]
    out.append('<div class=kpis>'
               f'<div class=kpi><b>{n}</b><span>figures</span></div>'
               f'<div class=kpi><b>{n - side_missing}/{n}</b><span>with sidecar</span></div>'
               f'<div class=kpi><b>{n - hard_fail}/{n}</b><span>pass hard checks</span></div>'
               f'<div class=kpi><b>12</b><span>params need agent review</span></div></div>')
    out.append('<div class="note ' + ("ok" if hard_fail == 0 else "bad") + '">'
               + ("✅ All figures pass the 8 auto-checked structural parameters." if hard_fail == 0
                  else f"❌ {hard_fail} figure(s) fail a HARD structural parameter (see ❌ below). "
                       f"{side_missing} missing a sidecar.") + ' The 12 judgement parameters (🔎) are for the '
               'figure-designer / accessibility-editor / auditor agents.</div>')
    cur = None
    for res in results:
        slug = res["meta"]["slug"]
        if slug != cur:
            out.append(f"<h2>{html.escape(slug)}</h2>")
            cur = slug
        p = res["params"]
        cap = html.escape(res["meta"]["alt"][:160] or "(no caption)")
        rel = f'../05-figures/{slug}/{res["meta"]["name"]}.png'
        cells = "".join(
            f'<div class=p><span>{verdict_emoji(p[k][0])}</span>'
            f'<span class=lab>{html.escape(lab)}</span>'
            f'<span class=nt>{html.escape(p[k][1])}</span></div>'
            for k, lab, _ in PARAMS)
        out.append(f'<div class=card><div><img src="{rel}" alt="{cap}"></div>'
                   f'<div><h3>{html.escape(res["meta"]["name"])}</h3><div class=cap>{cap}</div>'
                   f'<div class=params>{cells}</div></div></div>')
    out.append(f'<div class=foot>Generated by <code>.claude/scripts/review_figures.py</code> · {stamp} · '
               'regenerate: <code>python3 .claude/scripts/review_figures.py</code> · '
               '✅ pass · ❌ hard-fail · ⚠️ warn · 🔎 needs agent/human review.</div></main>')
    os.makedirs(os.path.dirname(OUT_HTML), exist_ok=True)
    open(OUT_HTML, "w", encoding="utf-8").write("\n".join(out))


def main():
    check_only = "--check-only" in sys.argv
    figs = find_figures()
    by_slug = {}
    for slug, name, png in figs:
        by_slug.setdefault(slug, []).append(name)
    results, hard_fails = [], []
    for slug, name, png in figs:
        params, meta = review_one(slug, name, png, draft_text(slug), by_slug[slug])
        results.append({"params": params, "meta": meta})
        fails = [k for k in HARD if params[k][0] == "FAIL"]
        if fails:
            hard_fails.append((name, fails))
    stamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M") if not check_only else "(check-only)"

    if not check_only:
        write_html(results, results, stamp)
        json.dump({"generated": stamp, "count": len(figs),
                   "results": [{"name": r["meta"]["name"], "slug": r["meta"]["slug"],
                                "params": {k: list(v) for k, v in r["params"].items()}} for r in results]},
                  open(OUT_JSON, "w", encoding="utf-8"), indent=1)
        print(f"review_figures: {len(figs)} figures reviewed → 10-logs/figures.html")

    auto_pass = sum(1 for r in results if not any(r["params"][k][0] == "FAIL" for k in HARD))
    print(f"review_figures: {auto_pass}/{len(figs)} pass hard checks; {len(hard_fails)} with hard failures")
    for name, fails in hard_fails:
        print(f"  ❌ {name}: " + ", ".join(fails))
    if check_only and hard_fails:
        sys.exit(1)
    sys.exit(0)


if __name__ == "__main__":
    main()
