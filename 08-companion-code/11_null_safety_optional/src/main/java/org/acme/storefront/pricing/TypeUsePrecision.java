package org.acme.storefront.pricing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Shows the precision a type-use annotation buys over a declaration one — the distinction that
 * separates the JSpecify and Checker Framework families from the legacy declaration set.
 *
 * <p>Because JSpecify's {@code @Nullable} targets {@code TYPE_USE} (JSR 308, Java 8), it attaches to
 * any use of a type, so it can say different things about the container and its elements. A
 * declaration annotation can only sit on the field as a whole and cannot reach inside the generic, so
 * it cannot express either distinction below. The two fields here are different contracts, and the
 * annotation placement is the only thing that tells them apart.
 */
public final class TypeUsePrecision {

    // tag::type-use-precision[]
    /** A non-null list whose elements may each be null: the list is always present. */
    private final List<@Nullable String> codesWithNullableElements;

    /** A list that may be absent, but whose elements are each non-null when it is present. */
    private final @Nullable List<String> maybeAbsentListOfCodes;
    // end::type-use-precision[]

    /**
     * Creates the holder. The {@code maybeAbsentListOfCodes} argument is allowed to be null, which the
     * {@code @Nullable} on its type declares; the other list is required.
     *
     * @param codesWithNullableElements a present list that may contain null elements, never
     *     {@code null} itself
     * @param maybeAbsentListOfCodes    a list that may itself be {@code null}
     */
    public TypeUsePrecision(
            List<@Nullable String> codesWithNullableElements,
            @Nullable List<String> maybeAbsentListOfCodes) {
        Objects.requireNonNull(codesWithNullableElements, "codesWithNullableElements");
        // A defensive copy that tolerates null ELEMENTS — List.copyOf would reject them, which is the
        // whole distinction this type illustrates: the list is non-null, its elements need not be.
        this.codesWithNullableElements =
            Collections.unmodifiableList(new ArrayList<>(codesWithNullableElements));
        this.maybeAbsentListOfCodes =
            maybeAbsentListOfCodes == null ? null : List.copyOf(maybeAbsentListOfCodes);
    }

    /**
     * Counts the non-null elements in the always-present list, tolerating the null elements its type
     * declares are possible.
     *
     * @return the number of non-null promo codes present
     */
    public long presentCodeCount() {
        return codesWithNullableElements.stream().filter(code -> code != null).count();
    }

    /**
     * Reports whether the optional list of codes is present.
     *
     * @return {@code true} when {@code maybeAbsentListOfCodes} was supplied
     */
    public boolean hasOptionalList() {
        return maybeAbsentListOfCodes != null;
    }
}
