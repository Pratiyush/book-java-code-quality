/**
 * One house invariant — <em>money is a dedicated value type, never a raw {@code double}/{@code float}</em>
 * — encoded five ways over five different artifacts, plus a codegen comparison for the boilerplate those
 * conventions create. This is Chapter 18's companion (dossier key 38, folding key 40).
 *
 * <p>Every custom rule shares one shape: <em>select</em> the elements of interest, <em>predicate</em>
 * whether each violates the invariant, <em>report</em> the violation, and <em>register/gate</em> it so a
 * violation fails the build. What changes between the realizations is the <em>artifact</em> each reasons
 * over, which fixes what the rule can see. The five forms here use only dependencies pinned in
 * {@code SOURCE-PIN.md}:
 *
 * <ul>
 *   <li>{@link org.acme.storefront.money.MoneyGuards} — a hand-written runtime guard (JDK only): the
 *       shape expressed directly, with no rule framework.</li>
 *   <li>{@link org.acme.storefront.money.Money} — a record compact constructor (JDK only): the
 *       type-level form, where the component type <em>is</em> {@link java.math.BigDecimal} so raw
 *       floating-point money cannot be constructed at all.</li>
 *   <li>{@link org.acme.storefront.money.LegacyMoneyFactory} — an Error Prone-style declarative fence
 *       built from the pinned {@code error_prone_annotations}: the banned floating-point factory is
 *       annotated so an Error Prone build flags every caller.</li>
 *   <li>{@code MoneyArchRules} (in the test source tree, since ArchUnit is a test dependency) — a custom
 *       ArchUnit {@code DescribedPredicate} + {@code ArchCondition}: the architectural form, asserting no
 *       domain type exposes floating-point money on its public API.</li>
 *   <li>{@link org.acme.storefront.money.MoneyApiInspector} — a reflective inspector that realizes the
 *       same select&rarr;predicate&rarr;report&rarr;gate shape without an unpinned analyzer SDK: the
 *       stand-in for a Checkstyle/PMD/SpotBugs custom check, and a concrete demonstration of the honest
 *       limit that each rule only sees its own artifact.</li>
 * </ul>
 *
 * <p>The codegen comparison ({@link org.acme.storefront.money.HandWrittenMoney} versus the record
 * {@link org.acme.storefront.money.Money}) shows the boilerplate a value type generates, written by hand
 * with its latent-bug risk and derived by the compiler with none. The new-file processors
 * (AutoValue, Immutables, MapStruct) and Lombok are different approaches to the same boilerplate problem;
 * none is a {@code SOURCE-PIN.md} row, so they are described in the chapter prose and not depended on here
 * (see {@code 09-flags/38_codegen_tools_not_pinned.md}). No realization is crowned — each states its
 * strongest case and its hardest limit, and the choice follows the artifact the invariant lives in,
 * which is Chapter 17's routing question, not this chapter's.
 */
package org.acme.storefront.money;
