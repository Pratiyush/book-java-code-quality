#!/usr/bin/env python3
"""relabel_figures.py — fix every figure's baked title from the dossier-key number
("Fig 06.1") to the printed chapter number in the spelled-out form ("Figure 4.1"),
then re-render the PNG via render.mjs. Resolves the Fig->Figure + key->printed drift
that was baked into the rendered images (the draft captions were already normalized).

Usage: node must be available; run from repo root. Re-render uses 05-figures/_assets/render.mjs.
"""
import os, re, subprocess, sys

ROOT = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
FIGS = os.path.join(ROOT, "05-figures")
RENDER = os.path.join(FIGS, "_assets", "render.mjs")

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
PRINTED = {slug: i for i, slug in enumerate(ORDER, 1)}

def main():
    relabeled = rendered = failed = 0
    for slug, printed in PRINTED.items():
        d = os.path.join(FIGS, slug)
        if not os.path.isdir(d):
            continue
        for fn in sorted(os.listdir(d)):
            if not re.fullmatch(r"fig\d+_\d+\.html", fn):
                continue
            hp = os.path.join(d, fn)
            html = open(hp, encoding="utf-8").read()
            # the fig-title carries "Fig <N>.<m>" or "Figure <N>.<m>"; force printed + spelled-out
            new = re.sub(r"\bFig(?:ure)?\s+\d+\.(\d+)\b", lambda m: f"Figure {printed}.{m.group(1)}", html)
            if new != html:
                open(hp, "w", encoding="utf-8").write(new)
                relabeled += 1
            png = hp[:-5] + ".png"
            r = subprocess.run(["node", RENDER, hp, png], cwd=ROOT, capture_output=True, text=True, timeout=120)
            if r.returncode == 0 and os.path.exists(png):
                rendered += 1
            else:
                failed += 1
                print(f"  RENDER FAIL {fn}: {(r.stderr or r.stdout).strip()[:120]}", file=sys.stderr)
    print(f"relabel_figures: {relabeled} HTML relabeled, {rendered} PNG re-rendered, {failed} failed")

if __name__ == "__main__":
    main()
