---
description: "Pipeline step 4b — build the chapter's runnable enterprise-grade NN_slug module, tag-include the displayed snippet, run {{BUILD_CMD}}, and record the EXAMPLE gate report. Inserted between Step 4 (draft) and Step 5 (VERIFY). HARD gate. (technical profile — see BOOK-TYPE-PROFILES.md; book types with {{GATES_OFF}} = example-build/code-review drop this command and FLOOR C's compile clause.)"
argument-hint: "<N>  (chapter dossier key from FINAL_INDEX.md)"
---

# /example <N> — Step 4b: build the runnable companion module

> **(technical profile — see BOOK-TYPE-PROFILES.md.)** This command realizes `{{EXAMPLE_POLICY}}` as a **built-green** artifact. A book type whose profile lists example-build / code-review under `{{GATES_OFF}}` (science, business, narrative) removes this command entirely and strikes FLOOR C's compile clause, keeping only source-trace. A reference/cookbook turns it on only if its recipes are runnable.

You are the conductor for **pipeline step 4b — EXAMPLE-BUILD** for chapter key `$ARGUMENTS`. Run the `example-builder` agent to produce ONE self-contained, enterprise-grade, runnable module for this chapter, prove it builds green with `{{BUILD_CMD}}`, wire the book's displayed snippet to the compiled file through tag-includes, and write the gate report. Step 4b sits between Step 4 (draft) and Step 5 (VERIFY); Step 5 then checks the prose against this compiled code. This is the ninth HARD gate.

## Hard preconditions (verify before building)
1. The draft `03-drafts/NN_slug/NN_slug_v1.md` exists (Step 4 has run). The module realizes the draft's listings; it does not invent new behavior the chapter never describes.
2. The dossier `02-research/NN_slug/NN_slug_RESEARCH.md` is banked and its source-trace audit passed @ {{AUTHORITY_PIN}} (`NN_slug_VERIFY.md`). Every {{INVENT_UNITS}} the module uses traces to {{AUTHORITY_SOURCE}} at the pin.
3. Step 0 holds: `{{AUTHORITY_CLONE_PATH}}` is on-pin (run `/pin-source` if unsure). The pinned source is the single source of truth for every {{SUBJECT_SHORT}} fact.
4. The slug `NN_slug` is frozen for this key. The module directory and the snippet `tag::name[]` regions are keyed by it so the printed listing and the runnable code stay one artifact.

## What to do
Run the `example-builder` agent, reading whole through the `book-law` and `{{SUBJECT_SHORT}}-source-verify` skills, to:
1. **Build the module** under `08-companion-code/NN_slug/` per `00-strategy/templates/EXAMPLES-GUIDE-{{SUBJECT_SHORT}}.md`. Enterprise-grade means: dependencies pinned through one inherited parent-pom property (`{{AUTHORITY_PIN}}`); externalized config profiles; at least one automated test for the topic; an observability/health surface where the topic touches it; and an explicit failure path (a fault-tolerance policy, graceful-shutdown, or error response) so the module demonstrates the honest-limitations floor in code.
2. **Tag-include the displayed snippet.** The book's bounded displayed listing is a `// tag::name[]` / `// end::name[]` region inside the full compilable file (`COMPANION-REPO.md` §2.5), so the printed listing and the runnable code are one artifact and cannot drift. Confirm the tagged region renders within the snippet length bound.
3. **Build green.** Run `{{BUILD_CMD}}` for the module with all compiler lint warnings clean at the pin ({{LANG_RUNTIME}}). A red build blocks the chapter; record the exact command and its result.
4. **CODE-REVIEW.** Run the `code-reviewer` agent over the module and write `03-drafts/NN_slug/NN_slug_CODEREVIEW.md` per `00-strategy/templates/GATE-REPORT-TEMPLATE.md`; a CODE-REVIEW FAIL blocks FLOOR C exactly as a red build does.
5. **Step 4c — CAPTURE (when the topic has a {{SUBJECT_SHORT}}-native UI surface).** Run the green module locally and capture the chapter's {{SUBJECT_SHORT}}-native UI screenshots — the framework's own dev/admin/health/API-explorer/test consoles — to `05-figures/NN_slug/` per `00-strategy/templates/FIGURE-GUIDE-{{SUBJECT_SHORT}}.md` (SCREENSHOTS). Each capture ships with a source sidecar — the capture manifest recording `module + exact command + URL/route + what it shows + redactions` plus the {{AUTHORITY_PIN}} pin — so the image traces to a real run and cannot drift; secrets and hostnames redacted, cropped to the load-bearing region. Third-party-tool UIs are banned ({{NEUTRALITY_STANCE}}). Skip only when the topic exposes no {{SUBJECT_SHORT}}-native UI (pure-API chapters).
6. **Record the gate report** at `03-drafts/NN_slug/NN_slug_EXAMPLE.md` following `00-strategy/templates/GATE-REPORT-TEMPLATE.md` (Gate: EXAMPLE): the module path, the `{{BUILD_CMD}}` outcome, the CODE-REVIEW verdict and report path, the tag-include names and their resolved line counts, any captured screenshots and their source sidecars, the externalized-config / automated-test / observability / failure-path checklist, and every source path each fact traces to.

## Hard constraints
- **Verify against {{AUTHORITY_PIN}}, never invent.** No {{INVENT_UNITS}} that is not present at the pin. A fact the module needs but the pin lacks is flagged to `09-flags/`, not guessed.
- **Snippet within the length bound.** The tagged region the book displays is at most the bounded length, verified against the compiled file.
- **{{NEUTRALITY_STANCE}} in comments.** Code comments and READMEs keep the locked neutral voice — {{BOOK_SUBJECT}} is the subject; no banned phrasings ({{NEUTRALITY_STANCE}}). Shipped first-party components are {{BOOK_SUBJECT}}, never rivals.
- **Local only.** The module is built locally under `08-companion-code/`. No public repo, no `git`-remote, no `gh`, no push — the public-facing push stays gated on the human/legal sign-off in `COMPANION-REPO.md` §5.

## Floor C — SOURCE-TRACE / COMPILE / CODE-REVIEW (PASS/FAIL)
The gate carries scoring **Floor C**: zero invented {{INVENT_UNITS}} — everything traces to {{AUTHORITY_SOURCE}} at {{AUTHORITY_PIN}} per `SOURCE-PIN.md` or is flagged to `09-flags/` — AND the companion module builds green via `{{BUILD_CMD}}` with all compiler lint warnings clean at the pin AND the module passes the CODE-REVIEW gate. A red build, a CODE-REVIEW FAIL, or any invented detail is fatal regardless of the aggregate — the chapter cannot advance to the Step-12 human gate until all three hold. (Book types with `{{GATES_OFF}}` = example-build/code-review keep only the source-trace clause of Floor C.)

## Tooling honesty
The `/example` command and the `example-builder` agent are newly authored and mirror `/draft` + `drafter`. This gate runs as a documented manual procedure until the one-chapter pilot lands; mark the build-state `[MANUAL — tooling pending]` in the report until the pilot clears. The CI matrix that runs `{{BUILD_CMD}}` across the `NN_slug` modules on the verified {{LANG_RUNTIME}} baseline is specified but not yet wired; say so where it applies. Nothing here is battle-tested.

## Report
Return: the module path under `08-companion-code/NN_slug/`, the `{{BUILD_CMD}}` result, the CODE-REVIEW verdict and `NN_slug_CODEREVIEW.md` path, the tag-include name(s) and resolved snippet line count, any Step-4c captured screenshots and their source sidecars, the Floor-C verdict (PASS/FAIL), the gate-report path, and any `09-flags/` gaps raised. Close with **"Learnings & pipeline suggestions"** and have the maintainer log it.
