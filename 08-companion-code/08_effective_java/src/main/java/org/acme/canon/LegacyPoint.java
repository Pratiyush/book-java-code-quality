package org.acme.canon;

import java.util.Objects;

/**
 * A transparent immutable data carrier written by hand in the pre-records style (Effective Java
 * Items 15, 16, 17 and the object contracts 10, 11, 12). Every line here states an intent the
 * language now states for the caller: private final fields (minimize mutability, Item 17), a
 * validating constructor, and hand-written {@code equals} / {@code hashCode} / {@code toString}.
 *
 * <p>The chapter's point is not that this form is wrong — it obeys the canon exactly — but that for
 * <em>transparent</em> data with no hidden representation, a Java 16+ {@code record} generates the
 * same behaviour from its components, so the ceremony below is what records retired. The principle
 * (the equals/hashCode contract must hold) stands; the boilerplate that expressed it is gone. The
 * twin {@link Point} carries the identical data and behaviour in one line, and a test proves the two
 * are observably equivalent for equal components.
 */
public final class LegacyPoint {

    private final int x;
    private final int y;
    private final String label;

    /**
     * Creates an immutable point, validating its components at the one place it comes into being.
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     * @param label the human-readable label, never {@code null}
     * @throws NullPointerException if {@code label} is {@code null}
     */
    public LegacyPoint(int x, int y, String label) {
        this.x = x;
        this.y = y;
        this.label = Objects.requireNonNull(label, "label");
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public String label() {
        return label;
    }

    /**
     * Obeys the {@code equals} contract (Item 10): reflexive, symmetric, transitive, consistent, and
     * {@code false} for {@code null}. Two points are equal when all three components are equal.
     */
    @Override
    // tag::handrolled-contract[]
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegacyPoint other)) {
            return false;
        }
        return x == other.x && y == other.y && label.equals(other.label);
    }
    // end::handrolled-contract[]

    /**
     * Always overridden together with {@code equals} (Item 11), over the same components, so equal
     * points share a hash code and behave correctly as keys in a hash-based collection.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, label);
    }

    /**
     * A self-describing string (Item 12) suitable for a log line, exposing every component.
     */
    @Override
    public String toString() {
        return "LegacyPoint[x=" + x + ", y=" + y + ", label=" + label + "]";
    }
}
