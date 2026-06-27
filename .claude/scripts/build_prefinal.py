#!/usr/bin/env python3
"""build_prefinal.py — assemble the PRE-FINAL review copy from the lifted drafts.

Output: pre-final/  — the whole book for an external-LLM review (NOT gated on approval):
  00_front-matter.md, NN_<slug>.md (x47, FINAL_INDEX printed order), zz_AI-DISCLOSURE.md,
  MANUSCRIPT.md (everything concatenated), figures/ (png + html), README.md.

Per chapter: code `<!-- include: -->` markers are RESOLVED to real fenced snippets
(via extract_snippet.sh), figure image paths are rewritten to the local figures/ copy,
and the internal provenance/spec HTML comments are stripped. Nothing is invented; this
only re-packages the on-disk drafts + figures + companion code.
Re-run after any lift. Usage: python3 .claude/scripts/build_prefinal.py
"""
import os, re, subprocess, shutil, sys

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
DRAFTS = os.path.join(ROOT, "03-drafts")
FIGS = os.path.join(ROOT, "05-figures")
OUT = os.path.join(ROOT, "pre-final")
EXTRACT = os.path.join(ROOT, ".claude", "scripts", "extract_snippet.sh")

# printed-chapter# -> dossier slug (FINAL_INDEX reading order)
ORDER = [
 "01_what_is_code_quality","03_readability_maintainability","05_java_quality_toolchain",
 "06_quality_culture_ownership","08_effective_java","07_naming_structure_formatting",
 "09_api_method_contracts","10_immutability_value_design","11_null_safety_optional",
 "12_error_handling_exceptions","14_generics_type_safety","19_code_smells_antipatterns",
 "20_thread_safety_jmm","22_virtual_threads_structured_concurrency","26_how_static_analysis_works",
 "27_checkstyle","35_sonarqube_ide_layered_stack","38_custom_rules_codegen_lombok",
 "39_managing_findings","41_testing_landscape_quality","42_unit_testing_assertions_mocking",
 "45_integration_property_based_testing","48_coverage_mutation_effectiveness","50_contract_approval_testing",
 "53_solid_coupling_cohesion_packages","55_enforcing_architecture_fitness_functions","62_build_dependency_hygiene",
 "65_dependency_scanning_sbom_supply_chain","67_reproducible_builds_license_compliance","69_secure_coding_owasp",
 "70_sast_secrets_detection","73_security_in_ci","75_ci_pipeline_quality_gates",
 "80_coverage_pr_automation_platforms","81_branch_protection_precommit_parity","83_release_quality",
 "84_code_review_standards_documentation","85_metrics_rollout_dashboards","91_refactoring_legacy_modernization",
 "96_remediation_playbook_automated_change","97_ai_generated_code_quality","100_governing_ai_ai_review",
 "101_performance_profiling_memory_benchmarking","105_performance_regression_gates",
 "106_observability_logging_metrics_feedback","109_reference_quality_stack_gate","110_maturity_model_roadmap",
]

def resolve_includes(text):
    n = 0
    def repl(m):
        nonlocal n
        spec = m.group(1).strip()
        try:
            out = subprocess.run(["bash", EXTRACT, spec], cwd=ROOT,
                                 capture_output=True, text=True, timeout=30)
            if out.returncode == 0 and out.stdout.strip():
                n += 1
                return out.stdout.rstrip("\n")
        except Exception:
            pass
        return m.group(0)  # leave marker if it cannot resolve
    text = re.sub(r"<!--\s*include:\s*(\S+?)\s*-->", repl, text)
    return text, n

def copy_figs(text, slugdir):
    """Copy referenced PNGs (+ sibling HTML) into figures/, rewrite paths to figures/<base>."""
    copied = 0
    def repl(m):
        nonlocal copied
        alt, path = m.group(1), m.group(2)
        base = os.path.basename(path)
        src = os.path.join(FIGS, slugdir, base)
        if os.path.exists(src):
            shutil.copy2(src, os.path.join(OUT, "figures", base))
            html = src[:-4] + ".html"
            if os.path.exists(html):
                shutil.copy2(html, os.path.join(OUT, "figures", os.path.basename(html)))
            copied += 1
            return f"![{alt}](figures/{base})"
        return m.group(0)
    text = re.sub(r"!\[([^\]]*)\]\(([^)]+\.png)\)", repl, text)
    return text, copied

def strip_internal_comments(text):
    # 1) remove HTML comment blocks (dossier/provenance header, figure-plan, spec footer)
    text = re.sub(r"<!--.*?-->\n?", "", text, flags=re.DOTALL)
    # 2) drop any blank-line-separated block CONTAINING pipeline-internal metadata.
    #    Substring match (not prefix) so it also catches the companion-module build-status
    #    notes AND fragments orphaned by a malformed comment that embeds a literal "-->"
    #    (e.g. a back-matter comment quoting "<!-- include: -->"). Markers are unambiguous
    #    pipeline jargon, so legit prose + "Sources and further reading" are preserved.
    #    (09-flags/ paths deliberately NOT a marker — they appear in legit source-trace rows.)
    INTERNAL = re.compile(
        r"EXAMPLE-BUILD|FIGURE PLAN|RUNNABLE EXAMPLE|BUILD STATUS:|"
        r"withdrawn proposal|adjudicated N/A|Snippet tags:|"
        r"\*\*[A-Za-z][\w /-]*(modules?|artifacts?)\b|Trace it back\.",
        re.IGNORECASE)
    blocks = re.split(r"\n\s*\n", text)
    kept = [b for b in blocks if not INTERNAL.search(b)]
    return "\n\n".join(kept)

README = """# PRE-FINAL review copy — Java Code Quality

The whole book assembled for an independent (external-LLM) review, built from the lifted drafts by
`.claude/scripts/build_prefinal.py`. NOT the gated `06-assembly/` manuscript (which assembles only
*approved* chapters) — this is the complete 47-chapter book in reading order so a reviewer can read it
end to end **before** the per-chapter approval scores.

## What's here
- `00_front-matter.md` — preface, how-to-use, conventions, colophon (draft fields marked).
- `NN_<slug>.md` (x47) — chapters numbered + ordered by FINAL_INDEX printed chapter, code
  `<!-- include -->` markers RESOLVED inline to real compiled snippets, figures pointing at `figures/`.
- `figures/` — every referenced diagram as PNG (rendered) + HTML (source).
- `zz_AI-DISCLOSURE.md` — the AI-authorship disclosure.
- `MANUSCRIPT.md` — everything concatenated for a single-pass whole-book read.

## Workflow
**pre-final (this) -> external LLM reviews -> prefinal-edit (Claude applies it) -> prefinal-draft
(human read).** Approval scoring is deferred to last. Paste `MANUSCRIPT.md` (one pass) or the per-chapter
files (chunked) into a different-vendor LLM with this prompt:

```
You are an independent editor reviewing a pre-final technical book on Java code quality.
Read it as a skeptical senior engineer. For each chapter (or the whole book), report:
- the 3-6 most important concrete fixes (location + suggested change),
- any factual error, broken explanation, or claim that looks unverifiable,
- neutrality slips (any tool crowned; banned: "better than / unlike X / superior /
  beats / the problem with X / outperforms / inferior"),
- voice slips (second person in narration, narration contractions, filler
  "simply/just/obviously/easy", over-used em-dashes),
- continuity issues across chapters (terminology drift, duplicated content,
  cross-reference or figure-numbering errors).
Be specific and prioritize. Do not rewrite; list what to change.
```
Hand the review back to Claude (the prefinal-edit pass), then read the prefinal-draft.

## Known WIP (already tracked — do not re-flag)
- 0/47 approved — needs the independent per-chapter scores (deferred to last); the book is
  drafted · source-verified · code-reviewed · voice-lifted.
- Back-matter "Sources" not yet in the final two-tier format (a scheduled CLARITY pass); a few carry
  dossier-key labels (e.g. "key 62") that will be trimmed.
- ~180 `@pin` residuals (copyrighted-book verbatims, live-SaaS rule defaults, per-rule analyzer defaults,
  JLS/JEP spec text) are dated-at-use + flagged in `09-flags/`, pending a networked `/pin-source`.

Regenerate after any lift: `python3 .claude/scripts/build_prefinal.py`.
"""

def main():
    if os.path.isdir(OUT):
        shutil.rmtree(OUT)
    os.makedirs(os.path.join(OUT, "figures"))
    inc_total = fig_total = 0
    manifest = []
    book_parts = []
    # front matter
    fm = os.path.join(ROOT, "06-assembly", "00_front-matter.md")
    if os.path.exists(fm):
        shutil.copy2(fm, os.path.join(OUT, "00_front-matter.md"))
        book_parts.append(open(fm).read())
    for i, slug in enumerate(ORDER, 1):
        draft = os.path.join(DRAFTS, slug, f"{slug}_v1.md")
        if not os.path.exists(draft):
            print(f"  MISSING {draft}", file=sys.stderr); continue
        text = open(draft).read()
        text, ni = resolve_includes(text)
        text = strip_internal_comments(text)
        text, nf = copy_figs(text, slug)
        inc_total += ni; fig_total += nf
        pp = f"{i:02d}"
        outname = f"{pp}_{slug.split('_',1)[1]}.md"
        open(os.path.join(OUT, outname), "w").write(text.strip() + "\n")
        manifest.append(f"{pp}. {outname}  (key {slug.split('_')[0]}; {ni} snippets, {nf} figures)")
        book_parts.append(f"\n\n---\n\n<!-- Chapter {i} -->\n\n" + text.strip())
    # AI disclosure
    ad = os.path.join(ROOT, "06-assembly", "AI-DISCLOSURE.md")
    if os.path.exists(ad):
        shutil.copy2(ad, os.path.join(OUT, "zz_AI-DISCLOSURE.md"))
        book_parts.append("\n\n---\n\n" + open(ad).read())
    open(os.path.join(OUT, "MANUSCRIPT.md"), "w").write("\n".join(book_parts))
    open(os.path.join(OUT, "README.md"), "w").write(README)
    print(f"pre-final built: 47 chapters, {inc_total} snippets resolved, {fig_total} figures copied")
    return manifest

if __name__ == "__main__":
    m = main()
    print("\n".join(m[:5]) + f"\n   ... ({len(m)} chapters)")
