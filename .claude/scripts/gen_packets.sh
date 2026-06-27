#!/usr/bin/env bash
# gen_packets.sh — regenerate the external-scoring packets from the current drafts.
# Each packet = REVIEW-PROMPT.md + the chapter draft, paste-ready, in FINAL_INDEX order.
# Re-run after any lift so the external reviewer always sees the current text.
# Usage: bash .claude/scripts/gen_packets.sh
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT"
PROMPT="09-flags/external-review/REVIEW-PROMPT.md"
OUT="09-flags/external-review/packets"
mkdir -p "$OUT"
[ -f "$PROMPT" ] || { echo "gen_packets: missing $PROMPT" >&2; exit 1; }
made=0; missing=0
while IFS=: read -r pp slug; do
  [ -z "${pp:-}" ] && continue
  draft="03-drafts/$slug/${slug}_v1.md"
  out="$OUT/ch-${pp}_${slug}.md"
  if [ ! -f "$draft" ]; then echo "  MISSING: $draft" >&2; missing=$((missing+1)); continue; fi
  {
    echo "# SCORING PACKET — Printed Chapter ${pp}  (dossier ${slug})"
    echo "# 1. Paste EVERYTHING below the line into a fresh chat in a DIFFERENT-VENDOR LLM (not Claude)."
    echo "# 2. Save its one-pager reply VERBATIM as: 03-drafts/${slug}/${slug}_SCORE_INDEP.md"
    echo "# 3. score >=88% (44/50) + floors A/B/C-source PASS auto-promotes the chapter."
    echo "# ====================================================================="
    echo
    cat "$PROMPT"
    echo
    echo "===================== CHAPTER DRAFT TO REVIEW ====================="
    echo
    cat "$draft"
  } > "$out"
  made=$((made+1))
done <<'MAP'
01:01_what_is_code_quality
02:03_readability_maintainability
03:05_java_quality_toolchain
04:06_quality_culture_ownership
05:08_effective_java
06:07_naming_structure_formatting
07:09_api_method_contracts
08:10_immutability_value_design
09:11_null_safety_optional
10:12_error_handling_exceptions
11:14_generics_type_safety
12:19_code_smells_antipatterns
13:20_thread_safety_jmm
14:22_virtual_threads_structured_concurrency
15:26_how_static_analysis_works
16:27_checkstyle
17:35_sonarqube_ide_layered_stack
18:38_custom_rules_codegen_lombok
19:39_managing_findings
20:41_testing_landscape_quality
21:42_unit_testing_assertions_mocking
22:45_integration_property_based_testing
23:48_coverage_mutation_effectiveness
24:50_contract_approval_testing
25:53_solid_coupling_cohesion_packages
26:55_enforcing_architecture_fitness_functions
27:62_build_dependency_hygiene
28:65_dependency_scanning_sbom_supply_chain
29:67_reproducible_builds_license_compliance
30:69_secure_coding_owasp
31:70_sast_secrets_detection
32:73_security_in_ci
33:75_ci_pipeline_quality_gates
34:80_coverage_pr_automation_platforms
35:81_branch_protection_precommit_parity
36:83_release_quality
37:84_code_review_standards_documentation
38:85_metrics_rollout_dashboards
39:91_refactoring_legacy_modernization
40:96_remediation_playbook_automated_change
41:97_ai_generated_code_quality
42:100_governing_ai_ai_review
43:101_performance_profiling_memory_benchmarking
44:105_performance_regression_gates
45:106_observability_logging_metrics_feedback
46:109_reference_quality_stack_gate
47:110_maturity_model_roadmap
MAP
echo "gen_packets: $made packet(s) written to $OUT (missing: $missing)"
