package org.acme.repro;

import java.util.Objects;

/**
 * A dependency's declared license, normalised to its SPDX identifier and tagged with its obligation
 * category. This is the license field the SBOM (Chapter 28) already carries — license compliance reads it
 * off the inventory rather than building a second one. An immutable record: a component's declared license
 * does not change under the policy that evaluates it.
 *
 * <p>The {@code spdxId} is whatever the metadata <em>declared</em>; the chapter is explicit that a
 * POM-declared identifier is sometimes not the actual license, so a declared id is a starting point for a
 * policy decision, not a legal conclusion (detection is imperfect — missing, ambiguous, multi-licensed,
 * relicensed, and dual-licensed components confuse scanners).
 *
 * @param component the component the license belongs to (e.g. {@code commons-lang3})
 * @param spdxId    the declared SPDX license identifier (e.g. {@code Apache-2.0}, {@code GPL-3.0-only})
 * @param category  the obligation band this license falls into (factual; not legal advice)
 */
public record License(String component, String spdxId, LicenseCategory category) {

    /** Compact constructor: a license entry must name a component and a non-blank SPDX identifier. */
    public License {
        Objects.requireNonNull(component, "component");
        Objects.requireNonNull(spdxId, "spdxId");
        Objects.requireNonNull(category, "category");
        if (component.isBlank()) {
            throw new IllegalArgumentException("component must not be blank");
        }
        if (spdxId.isBlank()) {
            throw new IllegalArgumentException("spdxId must not be blank");
        }
    }
}
