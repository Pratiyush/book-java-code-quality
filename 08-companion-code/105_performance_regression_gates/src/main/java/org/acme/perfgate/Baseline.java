package org.acme.perfgate;

/**
 * The committed baseline a current measurement is compared against. The gate is
 * <em>relative-to-baseline</em>, not absolute: an absolute threshold ("fail if p99 &gt; 200ms") flaps
 * on shared-runner noise, while a baseline-relative comparison rides the environment up and down with
 * the measurement (Chapter 43). The baseline is a stored value per metric — checked in beside the
 * code, the way a coverage floor is — and it is moved <em>deliberately</em>, never silently.
 *
 * <p>A green gate means "no regression past this baseline," not "fast enough": if the baseline itself
 * does not meet the real target (the p99 users actually need, Chapter 43), the gate is faithfully
 * protecting an inadequate status quo. Measuring and fixing performance to a real target comes first;
 * the gate only keeps that hard-won performance from eroding.
 *
 * @param metric the metric this baseline is for, never {@code null}
 * @param value  the committed baseline value, in the metric's unit
 */
public record Baseline(String metric, double value) {

    // tag::baseline-model[]
    /**
     * Returns a new baseline at {@code newValue} — a baseline is moved by replacement, never mutated.
     * A sanctioned performance change (an intentional trade-off, a feature with a known cost) ratchets
     * the baseline deliberately, exactly like a coverage ratchet (Chapter 34); a baseline that is
     * never moved drifts out of meaning, and one moved carelessly hides regressions.
     */
    public Baseline movedTo(double newValue) {
        return new Baseline(metric, newValue);
    }
    // end::baseline-model[]
}
