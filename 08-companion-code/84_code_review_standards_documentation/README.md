# Chapter 84 — The Part the Machine Can't Do (`code-review-standards-documentation`)

The human side of quality made into runnable, reviewable artifacts: a pull-request template, a
`CODEOWNERS` routing file, a review checklist, a sample ADR, and one well-documented Java class that is the
exemplar of the chapter's documentation standard. The chapter is mostly process and documentation, so the
displayed snippets are tag regions inside the artifacts that *do* the work — the PR template section, the
`CODEOWNERS` rule, the review-checklist item, the Javadoc-as-contract, and the Checkstyle rule that
enforces Javadoc presence on the public API. The printed artifact and the running/checked artifact are one
file.

It is a child module of the companion-code reactor; it adds no version literals beyond the one pinned
SpotBugs-annotations GAV and inherits the runtime and test-library pins from the aggregator.

## What it demonstrates

| Concept (Chapter 84) | Where in the module | Tag |
|---|---|---|
| Review checklist — the substantive only, "improve code health?" | `.github/pull_request_template.md` | `pr-checklist-item` |
| Who reviews what, routed automatically | `.github/CODEOWNERS` | `codeowners-rule` |
| The team's review checklist (docs-as-code) | `docs/CODE_REVIEW_GUIDELINES.md` | `review-checklist-item` |
| Javadoc as contract on the public API | `RefundPolicy#refundCents` | `javadoc-contract` |
| The mechanical floor: Checkstyle enforces Javadoc *presence* | `config/checkstyle/checkstyle.xml` | `javadoc-presence-rule` |
| Observability — review size stays visible | `ReviewThroughputHealth#report` | (surface) |
| The why, recorded once | `docs/adr/0001-keep-the-companion-modules-jdk-only.md` | (ADR) |

## The honest edge, made executable

The chapter's standing limitation is that **automation enforces presence, never quality**. This module is
built to make that true rather than merely stated:

- The `quality` profile's Checkstyle Javadoc rules (`MissingJavadocType`, `MissingJavadocMethod`,
  `JavadocMethod`, `SummaryJavadoc`) confirm that the public API carries a Javadoc that is present,
  summarised, and well-formed (its `@param`/`@return`/`@throws` match the signature). They cannot confirm
  the prose is true.
- That the documented contract is *actually* true is asserted by `RefundPolicyTest` and judged by a human
  reviewer reading the checklist — the substantive half the tool cannot reach.
- The same green module is held to a SpotBugs gate too, so a Javadoc-clean module is shown to be
  consistently documented, never thereby correct.

## Build and run

```
# fast build (compile + tests)
mvn -B -f ../pom.xml -pl 84_code_review_standards_documentation -am verify

# with the documentation + correctness gates (Checkstyle Javadoc rules + SpotBugs)
mvn -B -Pquality -f ../pom.xml -pl 84_code_review_standards_documentation -am verify

# or standalone, from this module directory
mvn -B -Pquality -f pom.xml verify
```

A green run reports tests passing, **0 Checkstyle violations** (the public API meets the Javadoc standard),
and **0 SpotBugs findings reported**.

> This module is intentionally **not yet** listed in `08-companion-code/pom.xml`'s `<modules>`. Per the
> companion-code policy, a module joins the reactor only after a green build **and** the CODE-REVIEW gate
> passes; until then it is built standalone with the commands above.

## TRY-IT — watch the documentation gate work

- Run `mvn -Pquality verify`: green, with the public API documented to the standard.
- **The presence rule is load-bearing.** Delete the Javadoc block above `RefundPolicy#refundCents` and
  re-run: `MissingJavadocMethod` fails the build. (Restore it.)
- **Well-formedness is checked, truth is not.** Change the Javadoc summary to a lie (e.g. document a
  refund of *double* the price) but leave the tags well-formed: the Checkstyle gate stays **green** while
  `RefundPolicyTest` goes **red**. That gap — a present, well-formed, *false* Javadoc passing the tool and
  failing the test — is the chapter's whole point about presence versus quality. (Restore it.)

## The failure path

`RefundPolicy#refundCents` returns `0` once the refund window has closed — a defined, benign outcome a
caller handles, not a thrown control-flow signal for an ordinary case. The construction-time guards
(`RefundPolicy` negative-price rejection, `refundCents` and `ReviewThroughputHealth#report` negative-input
rejection) are the other half: invalid input fails fast and loud. Both halves are exercised by
`RefundPolicyTest`.

## Neutrality note

Code review practices, the specific coding standard, and how much to document are all contested. The
artifacts here record *one team's* agreement (a 120-column limit, a review SLA, a JDK-only runtime
decision) and say so; they crown no doctrine. The cited figures (the ~100-300-line effective-review zone)
are the Cohen / SmartBear study's and are labelled as such.
