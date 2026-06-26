/**
 * Chapter 48 companion — coverage versus mutation effectiveness on one method.
 *
 * <p>{@link org.acme.effectiveness.Discount} is a behaviour-rich method (a quantity boundary, an
 * arithmetic expression, and an early return). Two test classes act on it to make the chapter's
 * thesis tactile: {@code DiscountWeakTest} executes every line yet asserts almost nothing — full
 * line coverage, surviving mutants — while {@code DiscountTest} asserts the boundary and the
 * computed value, killing those mutants without moving line coverage at all. The build wires JaCoCo
 * (coverage) into {@code verify} and PITest (mutation) behind the {@code pitest} profile, so the two
 * measures sit on the same code.
 */
package org.acme.effectiveness;
