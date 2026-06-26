package org.acme.canon;

/**
 * The same transparent immutable data as {@link LegacyPoint}, expressed as a record (JEP 395, final
 * JDK 16). The compiler generates the private final fields, the canonical constructor, the component
 * accessors, and {@code equals} / {@code hashCode} / {@code toString} from the components — the
 * boilerplate {@code LegacyPoint} writes by hand. This is the hook's one-liner.
 *
 * <p>The verdict the chapter assigns: <em>served by a feature</em>. Item 17's principle (minimize
 * mutability) and the object contracts (Items 10, 11, 12) still hold; a record is the canonical way
 * to satisfy them for plain data. A record is for transparent data whose components are its API, so
 * it fits here, where the point simply <em>is</em> its three components and nothing is hidden.
 *
 * @param x     the x coordinate
 * @param y     the y coordinate
 * @param label the human-readable label
 */
// tag::record-value[]
public record Point(int x, int y, String label) {
}
// end::record-value[]
