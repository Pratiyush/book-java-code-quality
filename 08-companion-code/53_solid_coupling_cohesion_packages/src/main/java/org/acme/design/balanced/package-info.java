/**
 * The same order-pricing outcome as {@code org.acme.design.overengineered}, with abstraction kept to
 * where a real variation exists.
 *
 * <p>A {@code record} carries the order data directly, and a single {@link DiscountPolicy} interface
 * stays — but here it earns its place, because the package ships two genuinely different
 * implementations (a percentage policy and a no-op). There is no factory, because there is nothing to
 * vary in how a policy is constructed. The contrast with the over-abstracted package is the chapter's
 * point: same goal, less ceremony, the abstraction present only where it pays for itself.
 */
package org.acme.design.balanced;
