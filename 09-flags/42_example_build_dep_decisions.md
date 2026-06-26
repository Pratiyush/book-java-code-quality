# FLAG — key 42 EXAMPLE-BUILD dependency decisions (human review)

> Raised by `example-builder` on 2026-06-26 while building
> `08-companion-code/42_unit_testing_assertions_mocking/`. Neither item blocks the Floor-C PASS; both
> are deliberate, in-bounds decisions that the human may wish to change at the aggregator level.

## 1. Truth (4th assertion style) shown in prose, not compiled

- **Context.** The chapter contrasts FOUR assertion styles (JUnit built-in, AssertJ, Hamcrest, Truth).
  The module compiles three of them in `AssertionStylesTest.java`; **Truth** is shown in the module
  README and the chapter prose for comparison, not compiled.
- **Why.** The example brief authorized only JUnit (BOM), AssertJ 3.27.7, and Mockito 5.23.0 as deps
  to use, and said "flag any needed dep that is not pinned." Truth (1.4.5) IS pinned in `SOURCE-PIN.md`
  §3, but it is (a) outside the brief's authorized list, (b) absent from the local `.m2` cache, and
  (c) pulls a heavy transitive tree (Guava + failureaccess + checker-qual + error_prone_annotations).
  Compiling only the cleanly-available subset keeps the build green and in-bounds while the chapter's
  comparison table still presents all four (no library is crowned — NEUTRALITY held).
- **Decision needed.** If a compiled 4th style is wanted: add `com.google.truth:truth:1.4.5` test-scope
  (its own version literal, traced to SOURCE-PIN §3) to the module pom, add a `truth-style` tag in
  `AssertionStylesTest.java`, and bind it into the prose. Requires network to fetch Truth's tree.

## 2. JUnit resolves to 6.0.3 (aggregator pin), not 6.1.0 (brief / SOURCE-PIN row)

- **Context.** The brief named JUnit 6.1.0; `08-companion-code/pom.xml` manages `junit-bom` at
  `junit.version` = **6.0.3**. The module correctly inherits the parent and adds NO JUnit version
  literal, so JUnit Jupiter/Platform resolve to **6.0.3**. Both are the pinned JUnit 6 line; the module
  could not pin 6.1.0 without either adding a forbidden local literal or editing the aggregator (the
  brief forbade editing `08-companion-code/pom.xml`).
- **SOURCE-PIN.md** §3 row pins JUnit **6.1.0** (GA 2026-05). The aggregator trails it at 6.0.3.
- **Decision needed.** A one-time aggregator-level bump of `junit.version` 6.0.3 → 6.1.0 would move all
  child modules together and should re-test every companion module (Re-pin runbook step 4). This is the
  right place to close the gap — not in a single child module. Until then, this module (and every other
  companion module) builds on 6.0.3.

## 3. Transitive Byte Buddy version note (informational)

- Mockito 5.23.0 pins `net.bytebuddy:byte-buddy` + `byte-buddy-agent` at **1.17.7**. In the resolved
  tree, `byte-buddy-agent` is 1.17.7 but `byte-buddy` (core) resolves to **1.18.3** (nearest-wins from
  another managed dep). Tolerated by Mockito at runtime; build green, 13 tests pass. Recorded for
  provenance; no action unless a future Mockito run misbehaves.
