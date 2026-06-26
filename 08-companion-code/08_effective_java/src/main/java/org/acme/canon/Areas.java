package org.acme.canon;

import org.acme.canon.Shape.Circle;
import org.acme.canon.Shape.Rectangle;
import org.acme.canon.Shape.Square;

/**
 * Computes the area of a {@link Shape} with an exhaustive pattern-matching switch (JEP 441, final
 * JDK 21) using record patterns (JEP 440, final JDK 21) to deconstruct each variant in place. This
 * replaces the nested {@code instanceof}-and-cast ladder the canon predates: the handling is flat,
 * each case binds the variant's components directly, and there is no {@code default}.
 *
 * <p>Because {@link Shape} is sealed and every permitted type has a case, the switch is exhaustive
 * and the compiler verifies it. Adding a fourth permitted shape to {@code Shape} without adding a
 * case here is a compile error, not a bug that surfaces at run time — the chapter's compiler-checked
 * canon. The absence of a {@code default} branch is deliberate: a {@code default} would silence that
 * exhaustiveness check and let a missing case slip through.
 */
public final class Areas {

    private Areas() {
    }

    /**
     * Returns the area of the given shape.
     *
     * @param shape the shape to measure, never {@code null}
     * @return the shape's area
     */
    // tag::pattern-switch[]
    public static double of(Shape shape) {
        return switch (shape) {
            case Circle(double radius) -> Math.PI * radius * radius;
            case Rectangle(double width, double height) -> width * height;
            case Square(double side) -> side * side;
        };
    }
    // end::pattern-switch[]
}
