<!--
  Pull-request template for the Chapter 84 companion (the human side of quality). GitHub renders the body
  of this file into every new pull request's description, so the checklist below is in front of the author
  before they request review and in front of the reviewer when they open it. The chapter's point is that a
  short, maintained checklist improves review consistency over ad-hoc reading, and that the mechanical
  items belong to the automated gate, not the human. So this template asks the author to confirm the
  AUTOMATED gate is green (it does not re-ask a human to eyeball style or coverage) and reserves the human
  checklist for the SUBSTANTIVE questions a tool cannot answer.

  The displayed snippet is the tag region below — the real checklist section in the real template — so the
  printed checklist and the running template are one artifact.
-->

## What and why

<!-- One paragraph: what this change does, and why. Link the issue / ADR if there is one. -->

## Size

<!--
  Keep changes small. The largest code-review study (Cohen / SmartBear) found defect detection holds in a
  roughly 100-300 line zone and drops sharply past it as reviewer fatigue sets in. A large PR is not
  reviewed; it is skimmed. If this is large, say why it could not be split.
-->

- Lines changed (excluding generated / formatting-only): <!-- ~N -->
- If over ~300 lines: reason it could not be split:

## Author checklist — the mechanical is automated

<!--
  These are confirmations that the AUTOMATED gate ran and is green, not a request for a human to re-check
  them. Style, formatting, and Javadoc presence are enforced by `mvn -Pquality verify`; a human reviewer
  should never spend attention on them.
-->

- [ ] `mvn -Pquality verify` is green (formatter / Checkstyle / SpotBugs / tests).
- [ ] Public API changes carry their Javadoc contract (`@param` / `@return` / `@throws`).
- [ ] A decision that future readers will question is recorded as an ADR under `docs/adr/`.

## Reviewer focus — the substantive only

<!-- tag::pr-checklist-item[] -->
<!-- The one overarching standard (Google eng-practices): does this change improve overall code health? -->
- [ ] Design: is this the right change, and the right abstraction?
- [ ] Correctness: edge cases, error handling, and authorization paths.
- [ ] Tests: do they assert behaviour, not merely exist for coverage?
- [ ] The why is recorded where it cannot be read from the code (ADR / Javadoc contract).
<!-- end::pr-checklist-item[] -->

<!--
  Review the code, not the person. A "looks good to me" on a change nobody read is the failure mode this
  template exists to prevent — a required review confirms an approval was clicked, never that the change
  was understood. The small size above is the precondition that makes substantive review possible at all.
-->
