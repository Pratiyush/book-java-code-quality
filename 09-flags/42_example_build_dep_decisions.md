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

## 4. Residual verify-at-pin atoms — NOT resolvable by SOURCE-PIN or the companion build

> Appended 2026-06-27 by the deferred-verification resolution pass over
> `03-drafts/42_unit_testing_assertions_mocking/42_unit_testing_assertions_mocking_v1.md`. All version
> atoms (JUnit 6.1.0, AssertJ 3.27.7, Hamcrest 3.0, Truth 1.4.5, Mockito 5.23.0) and every idiom the
> companion module compiles (the `@ExtendWith(MockitoExtension.class)`+`@Mock`/`@InjectMocks`/
> `when().thenReturn`/`do*().when()`/`verify`/`never`/`verifyNoInteractions`/`InOrder`/`eq`/`any`/
> default-`STRICT_STUBS` set, the three compiled assertion styles, value-object-used-real, the
> over-mock `InOrder` failure path) were CONFIRMED and their markers removed. The atoms below remain
> marked `⚠ … verify-at-pin` in the draft because they are **neither pinned in `SOURCE-PIN.md` nor
> exercised by the green companion build**, so neither of this pass's two authorities can confirm
> them. They need a direct read of the named tool's docs/Javadoc at its pin (Step-5 SOURCE-VERIFY).

- **Mockito Java floor (claimed "11").** Not in `SOURCE-PIN.md` (the row pins only the Mockito
  version, 5.23.0); the module builds on JDK 21, which cannot witness an 11 floor. Verify against
  Mockito 5.23.0's own requirements page.
- **Per-version standalone (non-`MockitoExtension`) strictness default.** The module only exercises the
  *extension* default (`STRICT_STUBS`, confirmed). The plain `Mockito.mock(...)`/standalone default per
  version is not exercised — verify in the Mockito Javadoc/`Strictness` docs.
- **`@InjectMocks` precedence wording** (constructor vs setter vs field injection order). The module
  uses `@InjectMocks` (constructor path) but does not pin the documented precedence text.
- **`RETURNS_DEFAULTS` class name** and the exact default-answer return set (`0`/`false`/`null`/empty/
  `Optional.empty()`). Behaviour is relied on in prose; the class-name token is unverified.
- **`mockStatic`/`mockConstruction` scope wording** (`MockedStatic`/`MockedConstruction`, thread/scope
  bounds). Not used by the module (deliberately — the chapter calls them a sharp edge).
- **JUnit `@MethodSource`/`Assertions` signature changes 5→6 + the full 5→6 breaking-change list.** The
  module uses neither `@MethodSource` nor `@ParameterizedTest`; the 5→6 delta is a doc-read item.
- **Truth design-goal verbatim** ("deliberately small, consistent API" framing). Truth is prose-only
  (item 1 above), so no compiled evidence; the verbatim must be checked against `truth.dev` and, if it
  cannot be confirmed character-for-character, the quoted phrasing must be cut or de-quoted to a
  paraphrase. This is a quoted-span verbatim item, not just a version item.

Likewise the five-double / state-vs-behaviour **verbatim quotations** attributed to Fowler's *Mocks
Aren't Stubs* (and via him to Meszaros) on draft lines 95–101 and the `STATE-vs-behaviour` CONCEPT are
named-source verbatims that this pass did not open the source for; they remain to be confirmed
character-for-character at SOURCE-VERIFY (Fowler is not a `SOURCE-PIN.md` §7 row — see the existing
"SOURCE-PIN gaps" note in the draft front-matter).
