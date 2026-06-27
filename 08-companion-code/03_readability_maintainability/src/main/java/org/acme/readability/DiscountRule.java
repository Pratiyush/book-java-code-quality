package org.acme.readability;

/**
 * One pricing rule, written three ways behind a single contract — the spine of the Chapter 2 module.
 *
 * <p>{@link DiscountRulesNested}, {@link DiscountRulesFragmented}, and {@link DiscountRules} all
 * implement this interface and all compute the <em>same</em> discount for every input; they differ only
 * in <strong>shape</strong>. That separation is the chapter's measurement point made runnable: cognitive
 * complexity (Sonar {@code java:S3776}) measures how the code reads, not what it returns, so a rule can
 * score high or low while the {@link Money} it produces is identical. The behaviour-preservation test
 * drives all three through this one interface and asserts they agree.
 *
 * <p>Cyclomatic complexity (McCabe, 1976) counts a method's independent execution paths and answers
 * "how many tests do I need?"; cognitive complexity answers "how hard is this to read?" and increments
 * more for nesting. That increment for nesting is exactly where the two metrics part company: the deeply
 * nested form scores far higher on cognitive complexity than the balanced one, the divergence this module
 * is built to make visible.
 */
public interface DiscountRule {

    /**
     * Computes the discount for a cart, never returning {@code null} and never exceeding the cap.
     *
     * @param cart  the cart to price, never {@code null}
     * @param cap   the maximum discount allowed, in the cart's currency, never {@code null}
     * @param floor the minimum subtotal below which no discount applies, in minor units, never negative
     * @return the discount amount, in the cart's currency, never {@code null}
     * @throws NullPointerException if {@code cart} or {@code cap} is {@code null}
     * @throws PricingException     if {@code cap} is below {@code floor}
     */
    Money discountFor(Cart cart, Money cap, long floor);
}
