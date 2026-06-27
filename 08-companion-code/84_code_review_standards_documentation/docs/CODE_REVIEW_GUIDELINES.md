# Code review guidelines

The team's shared agreement on how review is done here. It is kept in the repository, versioned with the
code, and changed by pull request like anything else — the same docs-as-code discipline the documentation
standard asks for. It is deliberately short: a checklist a team will actually keep, rather than an
exhaustive one it ignores.

The one organizing idea: **automate the mechanical, reserve human attention for the substantive.** The
formatter, Checkstyle, SpotBugs, and the test suite run in `mvn -Pquality verify` and in CI. A reviewer
never comments on formatting, import order, or Javadoc presence — those are not opinions here, they are
applied and checked. Human attention goes only to what a tool cannot judge.

## The precondition: small and fast

The largest code-review study (Cohen / SmartBear, *Best Kept Secrets of Peer Code Review*) found defect
detection holds in a roughly **100-300 line** zone and **30-60 minutes** of attention, and drops sharply
past it as reviewer fatigue sets in. So a small change is a precondition for review, not a courtesy. A
2,000-line pull request is not reviewed; it is skimmed and approved.

## Review SLA (so review does not become a bottleneck)

Slow review stalls delivery as surely as a missing review ships a defect. The team's agreement, not a
universal truth:

| Change size | First response | Notes |
|---|---|---|
| Small (under ~300 lines) | within 1 business day | the common case; keep it the common case |
| Larger | within 2 business days, or ask the author to split | size is the first review comment, before content |

## The reviewer checklist — the substantive only

The order is roughly Google's published engineering practices: design, functionality, complexity, tests,
naming, comments, documentation, then style — and style is last because the formatter already owns it. The
overarching standard each item serves: *does this change improve the overall code health of the system?*

<!-- tag::review-checklist-item[] -->
- [ ] Design: is this the right change, and is the abstraction the right one?
- [ ] Correctness: edge cases, error handling, null/empty inputs, authorization.
- [ ] Tests: do they assert behaviour, not merely raise the coverage number?
- [ ] Contracts: public API changes carry their Javadoc (`@param`/`@return`/`@throws`).
- [ ] The why: a non-obvious decision is recorded in an ADR, not only in the diff.
<!-- end::review-checklist-item[] -->

## Tone

Review the code, not the person. The goal is a better change and a shared understanding of it, not a
verdict on the author. A review is a learning act for both sides.

## The honest limits

- A required review confirms an approval was clicked, never that the change was understood. The small-size
  precondition above is what makes a substantive review possible; the checklist does not enforce that it
  happened.
- Static analysis covers only a slice of what manual review finds (and the reverse). They are
  complementary; a green automated gate is not a substitute for review, and review is not a substitute for
  the gate.
- These practices are contested — pair programming instead of after-the-fact review, synchronous versus
  asynchronous, mandatory versus optional. This file records *one team's* agreement; it crowns no doctrine.
