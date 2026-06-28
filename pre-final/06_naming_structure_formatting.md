# Three Things You Should Never Argue About Twice

*Naming, structure, formatting — and the one of them a tool cannot do for you · Part II*

> A formatter settles where the braces go. Nothing settles whether the name is true.

## Hook

Two reviewers open the same pull request. The first leaves nine comments: a brace on the wrong line, a 130-character line, an import out of order, two-space versus four-space indentation in a new method, a constant in `lowerCamelCase`. The second reviewer reads the same diff, ignores all of that, and leaves one comment: *"`data` — data about what? This holds the customer's unsettled invoices. Call it that."*

The first reviewer spent their whole budget on things a machine decides more reliably than any human, and never reached the one question that mattered. The second reviewer let the machine handle the typography and spent their attention on the part no machine can reach: whether the name tells the truth.

The division of labour matters. Naming, structure, and formatting are the lowest-level, highest-frequency readability controls in Java. A developer hits them on *every* line, which is why they repay attention more often than any architectural decision. The move that makes them cheap is to sort them into what a tool can settle once and for all, and what only a human can judge, then to stop re-deciding the settled part on every commit.

## Overview

**What this chapter covers**

- The three layers (**naming**, **structure**, **formatting**) and the single axis that separates them: *typography a tool can check* versus *meaning only a human can judge*.
- The typographical conventions that are genuinely settled (`UpperCamelCase`/`lowerCamelCase`/`CONSTANT_CASE`), where they come from, and how the linters encode them.
- **Deterministic formatters** (Spotless driving google-java-format or palantir-java-format) that *rewrite* the file instead of merely flagging it (the format/lint split).
- **Comments and Javadoc**: the near-consensus (a published API is documented) and the genuinely contested part (how much to comment implementation code), presented as two schools, neither crowned.

**What this chapter does NOT cover.** The deep tool mechanics: how to author a Checkstyle ruleset (Chapter 16), PMD (Chapter 16), the Sonar engine (Chapter 17), suppression and baselines (Chapter 18), pre-commit parity (Part XI). This chapter teaches the *practice and the conventions*; the tools appear here as the enforcement surface.

One axis runs under all of it: a tool checks typography (regex-enforceable); a human checks meaning (un-enforceable). Selling "we enforce naming with a linter" as "our names are good" is the category error this chapter exists to prevent.

## How it works

One axis carries this chapter. Figure 6.1 sets it out: on one side, the part a tool settles once and for all; on the other, the part only a human with domain knowledge can judge. Every concern below sorts onto that axis.

![Figure 6.1 — the typography / meaning axis: what a tool settles once versus what only a human with domain knowledge can judge, the spine of Chapter 6](figures/fig07_1.png)

*Figure 6.1 — the typography / meaning axis: what a tool settles once versus what only a human with domain knowledge can judge.*

### The three layers, and where each is enforced

Three concerns that feel like taste are actually three different *kinds* of decision, and they get enforced in three different places.

| Layer | What it is | How much is mechanical | Enforced by |
|---|---|---|---|
| **Naming** | identifiers that carry intent | the *case* is mechanical; the *meaning* is not | linter checks case; human checks meaning |
| **Structure** | source-file order; member order within a class | overload-contiguity is checkable; the rest is judgment | linter checks file layout; human explains member order |
| **Formatting** | whitespace, indentation, braces, line length, import order | almost entirely mechanical | a deterministic formatter *rewrites* it |

Read top to bottom, the column that matters is "how much is mechanical." A *linter* here is a static-analysis tool that reads source and reports rule violations; a *formatter* is one that rewrites the source into a canonical shape. Formatting is fully decidable, so a formatter can *produce* the right answer rather than merely detect a wrong one. Naming is half-decidable: a regex confirms `customerId` is `lowerCamelCase`, but no tool knows whether `customerId` actually holds an *order* id. Structure sits in between.

> **CONCEPT** *Convention vs meaning.* Every naming/style decision splits into a typographical part (a tool checks it with a regex) and a semantic part (only a person can judge it). The settled-feeling parts of "readability" are exactly the typographical ones, which is *why* they are safe to automate, and why automating them reveals nothing about the semantic ones.

### Layer 1 — Naming: the settled part and the hard part

The typographical conventions are near-universal. *Effective Java* Item 68 ("Adhere to generally accepted naming conventions", Bloch, 2018) sorts Java naming into two kinds of rule and is explicit that they are not equally firm: the *typographical* conventions (which case a package, class, method, field, or constant takes) are tight and rarely in dispute, whereas the *grammatical* conventions (the part-of-speech shape of a good name — noun phrases for classes, verb phrases for methods) are softer and admit more judgement. The Google Java Style Guide states the typographical half precisely, and they line up with the JLS §6.1 naming guidance (⚠ confirm exact SE 21/25 wording @pin):

| Element | Convention (Google Java Style §5) | Example |
|---|---|---|
| Package | lowercase letters and digits, no underscores; words concatenated | `com.acme.orderpricing` |
| Class / interface / enum / annotation | `UpperCamelCase` | `OrderValidator` |
| Method | `lowerCamelCase` | `computeTotal` |
| Constant (`static final`, deeply immutable, side-effect-free) | `UPPER_SNAKE_CASE` | `MAX_RETRIES` |
| Non-constant field | `lowerCamelCase` | `retryCount` |
| Parameter / local variable | `lowerCamelCase` | `customerId`, `subtotal` |
| Type variable | single capital `±` numeral, or class-form + `T` | `T`, `RequestT` |

There is a Java-concrete trap hiding in that "constant" row. Google Java Style §5.2.4 defines a *constant* as a `static final` field "whose contents are deeply immutable and whose methods have no detectable side effects", not every `static final`. So `static final ImmutableList<String> NAMES` is `CONSTANT_CASE`, but `static final MutableThing thing` is `lowerCamelCase`. A linter rule that naively maps "`static final` ⇒ uppercase" gets this wrong; the convention is about *deep immutability*, not the modifier.

These are encoded as regexes by every major linter, and the regexes are *not identical across tools*, which is the first place neutrality bites:

| Tool | Rule | Default pattern | Status |
|---|---|---|---|
| Checkstyle | `ConstantName` | `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$` | verified |
| Checkstyle | `MethodName` | `^[a-z][a-zA-Z0-9]*$` | verified |
| PMD | `ClassNamingConventions.classPattern` | `[A-Z][a-zA-Z0-9]*` | verified |
| PMD | `FieldNamingConventions.constantPattern` | `[A-Z][A-Z_0-9]*` | verified |
| SonarQube | `java:S101` (class), `java:S100` (method), `java:S115` (constant), `java:S116` (field), `java:S117` (local/param) | per-rule regex | ⚠ default @pin |

Checkstyle, PMD, and SonarQube each ship their *own* naming defaults; they overlap heavily but the exact regexes differ. State each from its own source; do not present one as *the* canonical default. (Which to run, and how to stop them fighting, is Chapter 17.)

In the companion module the naming layer is a curated block of Checkstyle modules. Each settles one element's *case*; none of them reaches its meaning:

```xml
        <module name="TypeName"/>            <module name="MethodName"/>          <module name="ConstantName"/>        <module name="MemberName"/>          <module name="ParameterName"/>       <module name="LocalVariableName"/>
```

The deterministic part of even naming is the **camel-case algorithm** (Google Java Style §5.3): take the prose phrase, transliterate to ASCII, split into words, lowercase everything, then uppercase the first letter of each word. It settles the case that trips everyone: `XMLHTTPRequest` becomes `XmlHttpRequest`, not `XMLHTTPRequest`, by treating an acronym as one word.

The hard part, which no algorithm reaches, is whether the name is *true*. A linter confirms `data` matches `lowerCamelCase`; it cannot flag `data` as a uselessly vague name for the customer's unsettled invoices. The grammatical and semantic part of naming — the softer half Item 68 separates out — is where the value is, and it is unenforceable. Enforcing naming with a tool does not give good names.

### Layer 2 — Structure: order with a reason

Structure is the order of things, and Google Java Style is deliberately uneven about how much of it is a rule.

- **Source-file structure (§3):** exactly this order: license/copyright if present, `package` statement, imports, then *exactly one* top-level class, with one blank line between sections. Fully checkable.
- **Import discipline (§3.3):** no wildcard imports; static imports in one group, non-static in another; ASCII sort within a group. Fully checkable (and a formatter will *do* it automatically).
- **Member ordering (§3.4.2):** here the guide explicitly declines a single rule — "there's no single correct recipe… what is important is that each class uses *some* logical order, which its maintainer could explain if asked." Not alphabetical, not chronological-by-date-added. The *one* crisp rule is overload contiguity: methods sharing a name "appear in a single contiguous group with no other members in between," even when modifiers differ.

That unevenness is the teaching point. A team that tries to gate member order mechanically is fighting the guide's own intent. Only overload-contiguity is cleanly checkable; the rest is judgment a reviewer can ask about ("why is this method here?") but a regex cannot.

### Layer 3 — Formatting: decidable, so let the machine do it

Formatting is the one quality dimension that is *purely mechanical and fully decidable*: under a given style, there is exactly one in-format rendering of a given program. Because the mapping from code to formatted-code is a function, a tool can **produce** the answer. This is the **format/lint split**: a style *linter* reports a whitespace-rule violation and leaves the fix to the developer; an *auto-formatter* rewrites the file into canonical form.

Google Java Style §4 fixes the values teams argue about: indentation "+2 spaces" (§4.2), a 100-character column limit (§4.4), K&R braces (§4.1), one statement per line (§4.3). A deterministic formatter takes these out of human hands entirely. The four tools in this space occupy three roles:

- **Engines** that do the reflow. **google-java-format** renders Google Java Style and states its defining choice in its own README: "There is no configurability as to the formatter's algorithm for formatting. This is a deliberate design decision to unify our code formatting on a single format." Its one documented variation is `--aosp` (4-space). **palantir-java-format** describes itself as "a modern, lambda-friendly, 120 character Java formatter," "based on the excellent google-java-format": a *different* fixed opinion (120 columns, chain-tuned wrapping), not a tunable one.
- **An orchestrator** (**Spotless**), which defines no style of its own but wires an engine plus auxiliary steps (`importOrder`, `removeUnusedImports`, `licenseHeader`, `trimTrailingWhitespace`) into Maven (`spotless:check` / `spotless:apply`) and Gradle (`spotlessCheck` / `spotlessApply`). Its `ratchetFrom` step ("only format files which have changed since `origin/main`") is the documented answer to the "10,000-line reformat PR" problem.
- **An author-time baseline** (**EditorConfig**): a `.editorconfig` file that carries a few whitespace settings (`indent_style`, `indent_size`, `end_of_line`, `charset`, `trim_trailing_whitespace`, `insert_final_newline`) to every contributor's editor *as they type*, with "closer files take precedence" and `root=true` to stop the upward search.

The companion module wires the orchestrator and the engine together, Spotless driving google-java-format, with `ratchetFrom` so only files changed since trunk are touched:

```xml
  <groupId>com.diffplug.spotless</groupId>
  <artifactId>spotless-maven-plugin</artifactId>
  <version>${spotless.maven.plugin.version}</version>   <configuration>
    <ratchetFrom>origin/main</ratchetFrom>     <java>
      <googleJavaFormat><version>1.35.0</version></googleJavaFormat>
    </java>
  </configuration>
```

and carries the author-time baseline as a `.editorconfig` at the module root:

```
root = true

[*.java]
indent_style = space
indent_size = 2
end_of_line = lf
charset = utf-8
trim_trailing_whitespace = true
insert_final_newline = true
```

> **CONCEPT** *Format/lint split.* A linter *detects* a deviation and asks a human to fix it; a formatter *computes* the canonical rendering and writes it. Formatting is decidable, so it belongs to the formatter; once it does, the whitespace-checking linter rules can be deleted so two tools do not fight over the same bytes (Chapter 17's layering call).

The practice lesson is one sentence: pick one deterministic formatter, run its `check` in CI, run its `apply` locally or pre-commit, and stop reviewing whitespace forever.

### The fourth thing: comments (a contested practice)

Comments split into two populations that must be reasoned about separately, because the field agrees about one and argues about the other.

**Javadoc as a contract: near-consensus.** For code other people call, an API doc comment is part of the deliverable. *Effective Java* Item 56 ("Write doc comments for all exposed API elements", Bloch, 2018) states the strong form: precede every exported class, interface, constructor, method, and field with a doc comment describing the *contract* (preconditions often via `@throws`, postconditions, side effects), not the implementation. The JDK 21 doc-comment spec defines the grammar: a `/** … */` block recognized only immediately before a declaration (comments in a method body are ignored), a main description whose first sentence becomes the summary, then block tags (`@param`, `@return`, `@throws`, `@see`, `@since`, `@deprecated`) and inline tags (`{@code}`, `{@link}`, `{@inheritDoc}`).

Two platform features make Javadoc more than prose. `{@snippet}` (JEP 413, GA in JDK 18) makes example code *compiler-discoverable and validatable*: an example in a doc comment can be compiled in CI instead of rotting. And `-Xdoclint` (on by default since JDK 8; the maven-javadoc-plugin `<doclint>` element) can fail the build on comment problems across categories `accessibility`, `html`, `missing`, `reference`, `syntax`. The documented middle path is `<doclint>all,-missing</doclint>`, which validates the *shape* of comments without forcing one onto every member.

> **Past the anchor (Java 23).** Markdown documentation comments (`///`, JEP 467) ship in **JDK 23**, past the Java 21 anchor and ahead of the Java 25 forward LTS line. Treat them as a Java 23-era delta, not anchor fact; formatter support has lagged (google-java-format issue #1193). Do not adopt `///` while pinned to 21.

**Implementation comments: genuinely contested.** How much to comment code *inside* method bodies is not settled, and this book does not pretend it is. Two reputable schools:

| School | Position | Hardest objection against it |
|---|---|---|
| **A — *Clean Code*** (Martin, 2008) | A comment is at best a necessary evil and usually a *failure* to express intent in code — "a comment is an apology" (⚠ verbatim verify @pin). Prefer self-documenting code: good names, small functions. | Critics (e.g. qntm, "stop recommending *Clean Code*") argue the prescription is dogmatic and over-fragments code; suppressing comments can lose the *why*. |
| **B — *A Philosophy of Software Design*** (Ousterhout, 2018, ch. 13) | Comments capture information that was in the designer's mind but cannot be represented in code; "self-documenting code" is a myth for design rationale. Document *what* and *why*, not *how*. | More comments means more to maintain and more that can rot; over-commenting obvious code adds noise. |

The trade-off axis has no winner: names and structure express *what* cheaply and stay in sync with the code; comments express the *why*, the contract, the non-obvious constraint that code structurally cannot, at a maintenance cost. A team chooses where to sit on that axis *per surface*: a public API earns Javadoc, a private helper is often self-documenting, a subtle algorithm earns a `// why` comment. Both schools agree on the real core: a comment that *restates* the code is bad, and the right order is to rename before commenting.

## Deep dive: the readability pass, end to end

Put the layers together on one small class and the division of labour becomes concrete.

Start with a file a reviewer would stall on:

```java
public class orderthing {
  static final int maxRetries = 3;          // mis-cased "constant"
  private java.util.List data;              // vague name, wildcard-prone import
  public int calc(int X){return X*data.size();} // bad case, cramped, unclear
}
```

The same before-state lives in the companion module, kept inside a comment so the naming gate it would otherwise fail can stay green:

```java
     * public class orderthing {                 // type not UpperCamelCase
     *   static final int maxRetries = 3;        // a real constant, not CONSTANT_CASE
     *   private java.util.List data;            // legal case, but a name that says nothing
     *   public int calc(int X){return X*data.size();} // cramped, X is not lowerCamelCase
     * }
```

Run the **formatter** (`spotless:apply`) and the typography is gone as a topic: indentation, braces, spacing, import order are now canonical, computed not argued. Run the **linter** and it flags what the formatter does not own: `orderthing` fails `java:S101`/Checkstyle `TypeName` (class is not `UpperCamelCase`), `maxRetries` fails `ConstantName` (a real constant must be `CONSTANT_CASE`), `X` and `calc` and `data` are legal-but-poor. The machine has now done everything it can.

What remains is the part only a person does. `orderthing` → `OutstandingInvoices`. `data` → `invoices`. `calc` → `totalOutstanding`. `X` → `taxRatePercent`. None of those renames is something a regex could have produced; each is a claim about what the code *means*, checked by a human who understands the domain. And the result reads as a sentence:

```java
/** The customer's unsettled invoices. */
public final class OutstandingInvoices {
  static final int MAX_RETRIES = 3;
  private final List<Invoice> invoices;
  // ...
  public Money totalOutstanding(BigDecimal taxRatePercent) { ... }
}
```

The companion module carries the conventionally-named, conventionally-formatted result. It is an order line whose constant is `static final` *and* deeply immutable (the only kind that earns `CONSTANT_CASE`), and whose method name reads as what it returns:

```java
    /** The largest quantity a single order line accepts (a genuine, deeply-immutable constant). */
    public static final int MAX_QUANTITY_PER_LINE = 999;

    /** Returns this line's total price: the unit price taken {@code quantity} times. */
    public Money lineTotal() {
        return unitPrice.times(quantity);
    }
```

The formatter made it *uniform*. The linter made the *case* correct. The human made it *true*. Three layers, three enforcers, in that order. Only the last one required understanding the business.

Adoption cost is real: introducing a formatter to an existing codebase rewrites many files at once, which buries real changes in the diff and wrecks `git blame`. Two mitigations exist: Spotless `ratchetFrom` to format only changed-since-trunk files, or a single recorded "format everything" commit added to `.git-blame-ignore-revs`. Neither is free; both are cheaper than reviewing whitespace forever.

## Limitations & when NOT to reach for it

- **Tools check typography, not meaning.** Passing every naming linter says nothing about whether the names are good. Do not report "Checkstyle is green" as "our code is readable."
- **Member order is judgment, not a rule.** Google Java Style declines a single recipe on purpose. Gating member order mechanically (beyond overload contiguity) fights the guide and generates noise.
- **Style values are choices, not truths.** Two-space vs four-space, 80 vs 100 vs 120 columns: Google uses 2/100, AOSP 4, palantir 120, Checkstyle `LineLength` defaults to 80. There is no *correct* value; the value is in picking one. Stating any single value as "right" is the neutrality landmine of this whole topic.
- **Over-strict naming regexes cause false positives.** `java:S101`'s default can reject legitimate names; a rigid `ShortVariable` flags idiomatic `i`/`x` loop and lambda names. Naming rules need per-project tuning and suppression discipline (Chapter 18), or developers learn to ignore the linter. (The unnamed variable `_` interacts directly with short-name rules: Java 21 *previewed* it as JEP 443 ("Unnamed Patterns and Variables (Preview)"), and it was finalized in Java 22 as JEP 456 ("Unnamed Variables & Patterns") — past the anchor, so treat `_` as a Java 22-era delta, not anchor fact, and configure naming rules to exempt it only once on 22+.)
- **Over-enforced Javadoc breeds vacuous comments.** Forcing `@param`/`@return` on every no-logic getter (`-Xdoclint:all` with `missing`, or PMD `CommentRequired` everywhere) produces `@param name the name`, which satisfies the linter and informs no one. Use `all,-missing`.
- **Formatter output shifts between versions, and couples to the JDK.** A formatter's rendering can change across its own versions, so pin the formatter GAV (do not float it). Because the engines parse Java source, a given formatter version is coupled to a JDK range: the *same* `spotless:check` can pass on one JDK and fail to launch on another (⚠ exact version↔JDK matrix and any `--add-exports` args verify @pin). 
- **`.editorconfig` is a baseline, not a formatter.** It carries whitespace settings to editors but has no concept of line-wrapping or import order, and `max_line_length` is widely supported but not in the core spec's listed properties. Do not rely on it as a hard column gate.
- **When not to invest at all.** A throwaway script, a spike, a two-week prototype slated for deletion does not earn a formatter plus three linters' naming rules in CI. The investment pays back over a file's *lifetime*; short-lived code has none.

## Alternatives & adjacent approaches

- **IDE formatter profiles** (the older approach): each developer's IDE applies a shared profile. Works, but drifts between IDEs and versions and is not enforced at the gate; the field has largely moved to a deterministic formatter in CI.
- **The configurable formatter** (Eclipse JDT formatter, selectable as a Spotless `<eclipse>` step): for teams that genuinely need tunable rules rather than a fixed opinion. It trades the "no arguments" benefit for configurability, a different point on the same axis, not a winner.
- **Naming linters without a formatter** (Checkstyle/PMD/Sonar naming rules alone): catches case violations but leaves whitespace to humans. Complementary to a formatter, not a substitute; pairing them requires turning off the overlapping whitespace rules (Chapter 17).
- **No tooling, code review only:** viable for a tiny team, but it spends scarce review attention on exactly the mechanical things a tool does better, which is the failure mode in this chapter's hook.

These are layers, not rivals: EditorConfig (author-time) + a formatter (reflow) + naming linters (case) + human review (meaning) compose into one stack, each owning the layer it can actually enforce.

## When to use what

- **Always, on any code with a lifetime:** a deterministic formatter (`check` in CI, `apply` locally) + an `.editorconfig` baseline. This is the cheapest, highest-return quality investment in the book; it removes a whole category from review.
- **On a team that argues about naming:** a naming linter with the *team's chosen* regexes (not the default if the default fights the team's house style), plus suppression discipline.
- **On a public or shared API:** Javadoc as contract (Item 56), `-Xdoclint` shape-checking (`all,-missing`), and `{@snippet}` for examples that must not rot.
- **On implementation code:** pick the right spot on the comment axis per surface. Rename before commenting, comment the *why* where the code cannot carry it, and delete commented-out code (version control is the history).
- **When adopting on a legacy codebase:** `ratchetFrom` or a single ignored-revs reformat commit; never a silent global rewrite that buries real changes.

## Hand-off to the next chapter

The readability fundamentals now have clear ownership: a formatter owns typography, a linter owns case, a human owns meaning and the *why*. That division (*the tool does what is decidable, the human does what is not*) is the same division the rest of Part II keeps applying at higher levels: to control flow and error handling, to immutability, to API design. The naming and structure judgments made by hand here are the raw material the next layers build on. Get the names true and the structure explainable, and every subsequent layer has cleaner ground to work with.

## Back matter — sources & traceability

- **Google Java Style Guide** — §3 source-file structure, §4 formatting (+2 indent, 100-col, K&R), §5 naming (the case table, §5.2.4 constant definition, §5.3 camel-case algorithm). *(Living web doc — re-confirm § numbers at pin.)*
- **Effective Java 3e** (Bloch, 2018) — Item 68 "Adhere to generally accepted naming conventions" (the typographical-vs-grammatical split: typographical case rules tight, grammatical name-shape rules looser — paraphrased, not quoted), Item 56 "Write doc comments for all exposed API elements" (doc comments as contract). *(Item#+title web-confirmed against the published 3e TOC; positions paraphrased — no verbatim asserted.)*
- **JLS SE 21 / SE 25 §6.1** — language naming guidance. *(⚠ exact wording/section verify @pin.)*
- **Checkstyle 13.6.0** — `ConstantName` `^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$`, `MethodName` `^[a-z][a-zA-Z0-9]*$` (verified); `MissingJavadocMethod`, `SummaryJavadoc`, `LineLength` (default 80) ⚠ verify @pin.
- **PMD 7.25.0** — `ClassNamingConventions.classPattern` `[A-Z][a-zA-Z0-9]*`, `FieldNamingConventions.constantPattern` `[A-Z][A-Z_0-9]*` (verified); `CommentRequired`, `CommentSize`.
- **SonarQube 2026.1 LTA** — `java:S100/S101/S115/S116/S117` (naming), `java:S125` (commented-out code). *(⚠ default regexes verify @pin.)*
- **Spotless** (`spotless-maven-plugin` 3.6.0) — `spotless:check`/`apply`, `<googleJavaFormat>`/`<palantirJavaFormat>` steps, `<ratchetFrom>` (verbatim "only format files which have changed since origin/main").
- **google-java-format 1.35.0** — "no configurability… deliberate design decision" (verbatim README); `--aosp`.
- **palantir-java-format** — "modern, lambda-friendly, 120 character Java formatter," "based on the excellent google-java-format" (verbatim README).
- **EditorConfig spec** — `root`, `indent_style`, `indent_size`, `end_of_line`, `charset`, `trim_trailing_whitespace`, `insert_final_newline`; "closer files take precedence." *(⚠ EditorConfig not yet a SOURCE-PIN row; `max_line_length` not in core spec list.)*
- **JDK 21 doc-comment spec** — `/** */` grammar, summary sentence, block/inline tags. **JEP 413** — `{@snippet}`, GA JDK 18 (available at the anchor). **JEP 467** — `///` Markdown comments, JDK 23 (past the Java 21 anchor; see 09-flags/09_jep467_markdown_doccomments_ahead_of_pin.md). `-Xdoclint` categories; maven-javadoc-plugin `<doclint>all,-missing>`.
- **Clean Code** (Martin, 2008) — School A on comments. **A Philosophy of Software Design** (Ousterhout, 2018, ch. 13) — School B. *(⚠ verbatim verify @pin; APoSD to be added as a SOURCE-PIN canon row.)*

## Next chapter teaser

Names and structure make code *readable*; the next chapter makes it *correct under stress*. Control flow, exceptions, and the failure paths are next. A clear name on a method that swallows an exception is a clear name on a lie.
