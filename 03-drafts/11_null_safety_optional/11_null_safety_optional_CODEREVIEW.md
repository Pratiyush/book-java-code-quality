# GATE REPORT — CODE-REVIEW — Chapter 11

> FLOOR-C second half (the judgment a green build cannot make). Senior-PR review of code readers copy
> into their own apps: correctness, idiomatic modern Java 21 (Optional + null discipline), security,
> simplicity, test quality, prose↔code fidelity, neutrality-in-code.

## Header

- **Gate:** CODE-REVIEW
- **Chapter key:** 11 (owner; folds 31, 32)
- **Slug:** `11_null_safety_optional`
- **Module:** `08-companion-code/11_null_safety_optional/`
- **Draft under review:** `03-drafts/11_null_safety_optional/11_null_safety_optional_v1.md`
- **Run date:** 2026-06-27
- **Reviewer:** `code-reviewer` agent
- **Build verified:** JDK 21.0.11 (Homebrew openjdk@21). `mvn -B -pl 11_null_safety_optional -am verify`
  and `mvn -B -Pquality ... verify` both **BUILD SUCCESS** — 16/16 tests pass, **0 Checkstyle
  violations**, **0 SpotBugs findings** (Max effort), no compiler warnings under the parent's
  `-Xlint:all`. Snippet gate: `check_snippets.sh` 7/7 markers resolve.
- **Verdict:** **PASS-WITH-FIXES**

---

## Verdict rationale

The module is correct, secure, well-tested, builds warning-clean under the strict parent, and every
displayed tag region is balanced and ≤9 lines (max 5). No BLOCKER. However, three idiom/fidelity
findings keep it from a clean PASS — and one is sharp: the chapter's prose states the *Effective Java*
Item 55 rule verbatim ("never use it as a field, parameter, or map value"), yet the code uses
`Optional<String>` as both an instance **field** and a method **parameter** in the very service the
chapter holds up as the discipline done right. For published code a reader pastes, teaching a shape the
same chapter forbids is a real defect. None blocks FLOOR C; all three must be resolved (or the prose
reconciled to them) before approval. The fixes are for the example-builder to apply; re-review after.

---

## Six-dimension scorecard

| # | Dimension | Result | Note |
|---|---|---|---|
| 1 | Correctness | **PASS** | Logic right; absence handled via `map`/`flatMap`/`orElse` with no bare `.get()`; records validate at construction; `Money.minus` clamps at zero; failure-path test is non-vacuous (asserts the JEP 358 message names `amountOffMinor`). |
| 2 | Idiomatic modern Java 21 | **FIX** | JSpecify `@NullMarked` package + type-use `@Nullable` exactly as the pin defines them; `System.Logger`, records, `ConcurrentHashMap`, `Optional.or`. BUT `Optional` used as field + parameter (F1), and a provably-dead null check (F2) that the chapter's own recommended checker would reject. |
| 3 | Security | **PASS** | No secrets/tokens/keys (grep clean); config externalized to `pricing.properties`; every public entry `requireNonNull`-guards its args; exceptions carry param names, not internals/stack traces. JEP-358 name-disclosure is honestly disclosed in the draft's limitations. |
| 4 | Simplicity & readability | **FIX** | Smallest-teaching-code, realistic `org.acme.storefront.pricing` names, every public type carries a purpose comment. One orphan tag region (F3) is dead weight. |
| 5 | Prose↔code fidelity | **FIX** | JSpecify 1.0.0 GAV, `@Target(TYPE_USE)`, the four annotations, `Objects.requireNonNull` family, and `Optional` API all verified against the resolved pin. The Optional-as-field/parameter shape (F1) directly contradicts the chapter's own Item-55 sentence; gate-engine versions trail the current pin (F4). |
| 6 | Neutrality in code | **PASS** | No banned phrasing anywhere in src/README/pom/config. NullAway vs Checker Framework named even-handedly in comments ("two points on one trade-off curve"); no crowning, no disparagement. |

---

## Findings

Severity: **BLOCKER** (blocks gate) · **MAJOR** (fix before approval) · **MINOR** (should fix) · **NOTE**.

| # | Item | Severity | Location | Fix |
|---|---|---|---|---|
| F1 | `Optional<String>` used as an instance **field** and as a method **parameter** — the exact shapes the chapter's prose forbids ("never use it as a field, parameter, or map value", draft line 65; Item 55). Self-contradiction in copyable code. | **MAJOR** | `DiscountService.java:31` (field `defaultCode`); `DiscountService.java:47` (ctor param `Optional<String> defaultCode`); `DiscountService.java:83` (param `Optional<String> requestedCode`); `PricingConfig.java:26` (field). | Pick one of: (a) hold the field as `@Nullable String` (resolved once at construction) and take a plain/`@Nullable String` parameter, normalizing to the default internally — the shape the prose actually prescribes; OR (b) if the field/param form is a deliberate, defensible exception, add an explicit carve-out sentence in the prose naming *why* (it currently reads as a flat prohibition), so code and prose agree. Do not leave the contradiction silent. |
| F2 | `isReady()` returns `catalog != null`, but `catalog` is a `final` field guaranteed non-null by the constructor `requireNonNull`, inside a `@NullMarked` package — the comparison is provably always `true`. The nullness checkers the chapter recommends (NullAway / Checker Framework) flag this as a redundant/dead null check. A null-safety chapter should not ship a null check its own tooling rejects. | **MAJOR** | `DiscountService.java:142-144` | Make the readiness signal real: either compute readiness from actual state (e.g. delegate to a catalog readiness/size probe) or `return true;` with a comment that the constructor's fail-fast guard already guarantees the port is wired. Remove the `!= null` on a value the package proves non-null. |
| F3 | Orphan tag region `// tag::optional-map[]` is defined in the source but referenced by no `<!-- include -->` in the draft (0 hits). The `map`/`orElse` idiom the prose describes (lines 73-76, README) is shown only via `guarded-fix`, so this region is dead. | **MINOR** | `DiscountService.java:87-91` (region); not referenced in `..._v1.md` | Either wire it into the prose where `priceWithDiscount`'s `flatMap`/`map`/`orElseGet` chain is discussed, or delete the markers. Every tag region in the module should have a home in the book (EXAMPLES-GUIDE §5). |
| F4 | Module's `quality` profile pins Checkstyle `10.26.1` and the SpotBugs **plugin** `4.9.3.0`, while `SOURCE-PIN.md` now records Checkstyle `13.6.0` and SpotBugs `4.10.2`. The draft front/back-matter cite `10.26.1` + SpotBugs `4.10.2`. Not an invented fact (these are the house-rule gate, Ch 16's subject, not a fact this chapter teaches) and not a FLOOR-C SOURCE-TRACE failure — but a cross-module version-consistency drift. | **NOTE** | `pom.xml:74-75` (checkstyle 10.26.1); `pom.xml:103-104` (spotbugs-maven-plugin 4.9.3.0); cf. `SOURCE-PIN.md:63,65` | Align to the pin at the next quality-tooling sweep across all modules (the "two-pin" lesson the pom comment already names). Reconcile the draft's cited SpotBugs version with whatever the engine resolves. Track at the chapter that owns ruleset tooling; not a Ch 11 blocker. |

---

## Blockers

**None.** Build is green and warning-clean; no security, neutrality, or invented-fact finding; no
duplicate/imbalanced tag. FLOOR C is not blocked by this gate. F1 and F2 are MAJOR (required before
approval) but do not by themselves fail FLOOR C.

---

## Tag-region audit (BLOCKER class — all clear)

8 `tag::` / 8 matching `end::`, no duplicate or imbalanced marker. Displayed-line counts (content
between markers, exclusive — what the extract tooling slices), all ≤ 9:

| Tag | File | Displayed in draft | Lines |
|---|---|---|---|
| `optional-return` | DiscountService.java | yes (L69) | 5 |
| `require-nonnull` | DiscountService.java | yes (L77) | 5 |
| `type-use-precision` | TypeUsePrecision.java | yes (L95) | 5 |
| `nullmarked-package` | package-info.java | yes (L101) | 4 |
| `nullable-return` | DiscountService.java | yes (L105) | 3 |
| `unguarded-deref` | BrokenCheckout.java | yes (L113) | 3 |
| `guarded-fix` | Checkout.java | yes (L117) | 4 |
| `optional-map` | DiscountService.java | **no (orphan — F3)** | 3 |

`check_snippets.sh` against the draft: 7 markers, 7 pass, 0 fail.

---

## Build / lint result

- `mvn -B -pl 11_null_safety_optional -am verify` → **BUILD SUCCESS**, Tests run: 16, Failures: 0,
  Errors: 0, Skipped: 0. No compiler warnings under parent `-Xlint:all,-processing`.
- `mvn -B -Pquality -pl 11_null_safety_optional -am verify` → **BUILD SUCCESS**, 16/16 tests,
  "0 Checkstyle violations", SpotBugs "Error size is 0 / No errors/warnings found" (effort=Max).
- Secret-pattern grep (password/secret/token/apikey/key/credential/BEGIN) across src + config + pom +
  resources: **no hits**.
- NEUTRALITY banned-phrase grep across the code tree: **no hits**.
- Pin spot-checks (resolved against `~/.m2`): JSpecify **1.0.0** `@Nullable` is `@Target(TYPE_USE)`;
  all four annotations present; `Objects.requireNonNull(T)/(T,String)/requireNonNullElse` and
  `Optional.of/ofNullable/map/flatMap/or/orElse/orElseGet` all confirmed on JDK 21.

> NOTE — no `mvnw` wrapper exists at the companion-tree root; the agent built with system Maven against
> a Homebrew JDK 21. EXAMPLES-GUIDE §4 calls for a committed wrapper. Out of scope for this code-review
> verdict (a tree-wide infra item), but worth recording for the example-builder / repo gate.

---

## Exemplary notes (keep these)

- **Failure-path test is the model the guide asks for.** `brokenCheckoutThrowsAHelpfulNpeOnThe-
  UnguardedDereference` drives the *real* runtime failure (`lookupOrNull` → null → `total`) and asserts
  the JEP-358 message names the exact null expression (`amountOffMinor`) — not a vacuous `isThrown`.
- **`BrokenCheckout` / `Checkout` side-by-side** makes the chapter's honest-limit point in code: the
  source/bytecode analyzers stay green on the unguarded `@Nullable` deref, which is precisely the gap a
  nullness checker closes. The contrast earns its place.
- **No bare `Optional.get()` / `isPresent()` anywhere in main** — `map`/`flatMap`/`orElse`/`or`
  throughout. The code walks the discipline it teaches (modulo F1).
- **Records validate at construction** (`Discount`, `Money`), `Money` carries currency next to amount
  and clamps `minus` at zero — immutability + boundary guards done well.
- **Neutrality is clean in comments** on the most comparison-sensitive topic in the book (NullAway vs
  Checker Framework, JSpecify vs JSR-305): trade-off framed as one axis, no winner.

---

## FLOOR-C disposition

**FLOOR C — CODE-REVIEW half: PASS-WITH-FIXES (does NOT block FLOOR C).**
Green warning-clean build + zero invented source atoms + no security/neutrality/originality finding =
FLOOR-C SOURCE-TRACE+COMPILE+CODE-REVIEW is satisfiable. The two MAJOR findings (F1, F2) are
exemplary-bar fixes required before the chapter clears the human approval gate, not FLOOR-C failures.
Recommended sequence: example-builder applies F1–F3 (and reconciles F4 at the tooling sweep), then
re-review to convert this to a clean PASS.

---

## Learnings & pipeline suggestions

- **Promote a check:** a chapter that states a rule in prose ("never use Optional as a field/parameter")
  and then violates it in the companion module is a fidelity failure a green build cannot catch. Worth a
  lightweight `reconcile_facts`/lint pass that greps the module for the anti-patterns the *same* draft
  names as forbidden (Optional field/param, bare `.get()`), keyed off the draft's own prohibition
  sentences. This is the highest-value automation this gate surfaced.
- **Idiom rule for null-safety modules:** inside a `@NullMarked` package, a `!= null` check on a
  constructor-guarded `final` field is dead code the recommended checker rejects — add it to the
  code-reviewer's null-discipline checklist so it is caught by eye every time.
- **Orphan-tag check:** `check_snippets.sh` verifies every *draft include* resolves to a region; it does
  not verify every *region* is included. Consider a reverse check (module tag with no draft reference)
  to catch dead tags like `optional-map`.
- Append these to `00-strategy/PIPELINE-LEARNINGS.md`; book-maintainer logs in `LEDGER.md`.
