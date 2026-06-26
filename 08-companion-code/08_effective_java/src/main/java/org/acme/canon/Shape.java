package org.acme.canon;

/**
 * A closed set of shapes modelled as a sealed interface (JEP 409, final JDK 17). Declaring the
 * permitted implementations means the compiler — and the reader — knows the hierarchy is complete:
 * no fourth shape can appear without changing this declaration. This is the modern form of the
 * canon's advice to model a fixed set of variants with a type hierarchy (Items 20, 23).
 *
 * <p>The verdict the chapter assigns: <em>reinforced</em>. A type hierarchy was always the way to
 * model variants; sealing it and handling it with an exhaustive switch (see {@link Areas}) makes the
 * closure checkable at compile time, which the 3rd edition predates. The permitted types are records
 * so each variant is itself a transparent immutable carrier.
 */
// tag::sealed-types[]
public sealed interface Shape permits Shape.Circle, Shape.Rectangle, Shape.Square {

    record Circle(double radius) implements Shape { }

    record Rectangle(double width, double height) implements Shape { }

    record Square(double side) implements Shape { }
}
// end::sealed-types[]
