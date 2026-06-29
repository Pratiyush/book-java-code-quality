#!/usr/bin/env python3
"""build_assembly.py — Phase 4: assemble the reader MANUSCRIPT from APPROVED chapters.

Reads 04-approved/<slug>.md for every chapter in FINAL_INDEX printed order, resolves code
`<!-- include: -->` markers to compiled snippets (via extract_snippet.sh), strips internal
pipeline metadata (HTML comment blocks, build/figure/routing/audit lines — the same hardened
stripper as build_prefinal), copies referenced figures into 06-assembly/figures/, and writes:

  06-assembly/MANUSCRIPT.md  — front matter + 47 chapters + AI-disclosure + provenance appendix
  06-assembly/TOC.md         — reader table of contents (chapter titles, in reading order)

Only APPROVED chapters are eligible (all 47 are approved). Source of truth is 04-approved/, NOT
03-drafts/ (build_prefinal's source) — so this reflects the latest approved edits. Nothing is
invented; this only re-packages approved chapters + figures + companion code.
Re-run after any approved-chapter edit: python3 .claude/scripts/build_assembly.py
"""
import os, re, subprocess, shutil, sys

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
APPROVED = os.path.join(ROOT, "04-approved")
FIGS = os.path.join(ROOT, "05-figures")
ASM = os.path.join(ROOT, "06-assembly")
OUTFIG = os.path.join(ASM, "figures")
EXTRACT = os.path.join(ROOT, ".claude", "scripts", "extract_snippet.sh")

# printed-chapter# -> dossier slug (FINAL_INDEX reading order); same order as build_prefinal
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
        return m.group(0)
    text = re.sub(r"<!--\s*include:\s*(\S+?)\s*-->", repl, text)
    return text, n

def copy_figs(text, slugdir):
    copied = 0
    def repl(m):
        nonlocal copied
        alt, path = m.group(1), m.group(2)
        base = os.path.basename(path)
        src = os.path.join(FIGS, slugdir, base)
        if os.path.exists(src):
            shutil.copy2(src, os.path.join(OUTFIG, base))
            html = src[:-4] + ".html"
            if os.path.exists(html):
                shutil.copy2(html, os.path.join(OUTFIG, os.path.basename(html)))
            copied += 1
            return f"![{alt}](figures/{base})"
        return m.group(0)
    text = re.sub(r"!\[([^\]]*)\]\(([^)]+\.png)\)", repl, text)
    return text, copied

def strip_internal_comments(text):
    # 1) remove HTML comment blocks (dossier/provenance header, figure-plan, spec footer)
    text = re.sub(r"<!--.*?-->\n?", "", text, flags=re.DOTALL)
    # 2) drop any blank-line-separated block CONTAINING pipeline-internal metadata
    INTERNAL = re.compile(
        r"EXAMPLE-BUILD|FIGURE PLAN|RUNNABLE EXAMPLE|BUILD STATUS:|"
        r"withdrawn proposal|adjudicated N/A|Snippet tags:|"
        r"\*\*[A-Za-z][\w /-]*(modules?|artifacts?)\b|Trace it back\.",
        re.IGNORECASE)
    blocks = re.split(r"\n\s*\n", text)
    kept = [b for b in blocks if not INTERNAL.search(b)]
    text = "\n\n".join(kept)
    # 3) drop pure-scaffolding lines: **Routing** dossier-key bullets + ⚠/✅ audit-status lines
    out = []
    for ln in text.split("\n"):
        head = re.sub(r"^[\s>]*(?:[-*]\s+)?", "", ln)
        if head.startswith("**Routing"):
            continue
        if head[:1] in ("⚠", "✅"):
            continue
        out.append(ln)
    return "\n".join(out)

def chapter_title(stripped_text):
    m = re.search(r"^#\s+(.+)$", stripped_text, re.M)
    return m.group(1).strip() if m else "(untitled)"

def main():
    if os.path.isdir(OUTFIG):
        shutil.rmtree(OUTFIG)
    os.makedirs(OUTFIG)
    inc_total = fig_total = 0
    parts, toc = [], ["# Table of Contents", ""]
    # front matter (strip the internal "Working notes (not for print)" tail)
    fm_path = os.path.join(ASM, "00_front-matter.md")
    if os.path.exists(fm_path):
        fm = open(fm_path).read()
        fm = re.split(r"\n#{1,6}\s+Working notes \(not for print\)", fm)[0].rstrip()
        parts.append(fm)
    for i, slug in enumerate(ORDER, 1):
        p = os.path.join(APPROVED, f"{slug}.md")
        if not os.path.exists(p):
            print(f"  MISSING approved {slug}", file=sys.stderr); continue
        text = open(p).read()
        text, ni = resolve_includes(text)
        text = strip_internal_comments(text)
        text, nf = copy_figs(text, slug)
        inc_total += ni; fig_total += nf
        toc.append(f"{i}. {chapter_title(text)}")
        parts.append(f"\n\n---\n\n<!-- Chapter {i} -->\n\n" + text.strip())
    # back matter
    for extra in ("AI-DISCLOSURE.md", "APPENDIX-example-provenance.md"):
        ep = os.path.join(ASM, extra)
        if os.path.exists(ep):
            parts.append("\n\n---\n\n" + open(ep).read().strip())
    open(os.path.join(ASM, "MANUSCRIPT.md"), "w").write("\n".join(parts) + "\n")
    open(os.path.join(ASM, "TOC.md"), "w").write("\n".join(toc) + "\n")
    print(f"assembled: 47 chapters -> 06-assembly/MANUSCRIPT.md, {inc_total} snippets resolved, {fig_total} figures copied")

if __name__ == "__main__":
    main()
