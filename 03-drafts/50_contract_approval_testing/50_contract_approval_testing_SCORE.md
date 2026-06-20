# SCORECARD — Ch 24 "Contract & approval testing" (key 50 + 52)

> Part V CLOSER (Ch 20-24 complete). Two merged dossiers (contract/API leads + approval/snapshot section).
> Main-loop; gates = manual passes. two-jobs-on-one-boundary + four-stage-pipeline + verifies-unchanged-not
> -correct + golden-master-for-legacy shapes. Draft: `50_contract_approval_testing_v1.md`. Pin 2026-06-20.
> Hand-off opens Part VI — Architecture & Design Governance.

| Floor | Verdict | Evidence |
|---|---|---|
| A — NEUTRALITY | ✅ PASS | sweep = 0 first pass; Pact vs REST-assured framed as two-jobs-on-one-boundary (complementary not rivals, "crowning none"); Pact vs JSON-schema as different approaches; the field-rename failure path shows each catches what the other misses (no winner); each tool cited to its own docs. |
| B — HONEST-LIMITATIONS | ✅ PASS | §Limitations (Pact-not-public/3rd-party; contract-not-functional/perf/load; consumer-pact-without-provider-verify=false-confidence; operational overhead; REST-assured-needs-endpoint + GPath; **approval verifies-unchanged-not-correct/rubber-stamp**; churn/flake; says-what-not-why; boundary-not-whole-system) + the whole "when the reference is wrong" deep dive + §When to use. |
| C — SOURCE-TRACE | ✅ PASS | Pact definition + suitability limits + matrix verbatim from docs.pact.io; pipeline annotations from pact-jvm junit5 guides; REST-assured DSL + GPath-not-Jayway verbatim; ApprovalTests verify/scrubber model; all versions (Pact consumer 4.4.x/provider 4.6.x independent; REST-assured 5.5.x; ApprovalTests) + pactVersion-default + selector-set + CLI flags + matcher names carried ⚠ @pin; Docker-gating → REPRO PENDING-RUNTIME; Cohn/Feathers §7 canon gap flagged. |
| C — COMPILE | ⚠ PENDING (toolchain READY; provider/API tests Docker-gated) | Pact consumer+provider pair + REST-assured endpoint test + ApprovalTests golden-file module spec'd; provider/API tests = REPRO PENDING-RUNTIME where Docker/Boot absent; not yet authored/built. |

| Cluster | Score | Note |
|---|---|---|
| CLARITY | 9 | the consumer-break hook + the assert-against-external-reference framing unifies 3 techniques; the two-jobs-on-one-boundary table + the four-stage Pact pipeline land cleanly; three CONCEPT callouts (contract-honest-only-if-both-halves-run, two-jobs-not-rivals, + the reference-wrong center) anchor it. |
| ACCURACY | 8 | Pact definition/suitability/matrix + REST-assured DSL + ApprovalTests model all sourced; −2 for the broad verify-at-pin surface (Pact consumer/provider independent versions, pactVersion default, selector set, CLI flags, matcher names, REST-assured + ApprovalTests versions) — all flagged; GPath-not-Jayway + consumer/provider-version-independence handled precisely. |
| UTILITY | 9 | actionable: the question-each-answers decision, run-the-whole-Pact-pipeline, can-i-deploy gate, REST-assured for running endpoints, approval-only-where-diffs-are-read, scrub-non-determinism, golden-master-for-legacy; the field-rename demo is a runnable proof of contract-vs-API. |
| DEPTH | 8 | assert-against-an-external-reference as the unifying lens + the consumer-mock-held-honest-by-provider-verify (over-mocking instance) + verifies-unchanged-not-correct + the "every test is only as good as its reference" Part-V close is senior material; −2 vs 9s as the approval half draws on a concise dossier. |
| READABILITY | 8 | strong concrete hook, one comparison table + one code snippet, three callouts, the "when the reference is wrong" synthesis closing Part V on its humility theme; 3742w — right for one rich + one concise dossier; clean Part V→VI hand-off. |

**Aggregate 42/50**, none < 6. Floors A/B/C-source ✅; FLOOR-C COMPILE = PENDING (toolchain READY; provider/API
Docker-gated). New shapes: two-jobs-on-one-boundary + four-stage-pipeline + verifies-unchanged-not-correct +
golden-master-for-legacy. **CLOSES Part V (Testing, Ch 20-24 — 5 chapters, all drafted).** Hands off to Ch 25
(Part VI — Architecture & Design Governance; SOLID/coupling/cohesion/package structure, keys 53+54+57). The
assert-against-external-reference framing + "a test is a signal not a proof" is Part V's closing thread.
