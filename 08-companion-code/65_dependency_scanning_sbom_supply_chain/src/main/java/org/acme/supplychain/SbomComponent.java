package org.acme.supplychain;

import java.util.Objects;

/**
 * One component in a Software Bill of Materials: a name, a pinned version, and a Package URL (purl) that
 * identifies it unambiguously. Transitive dependencies are components too, so a complete inventory lists
 * them all. An immutable record — a component, once recorded, does not change under the inventory.
 *
 * <p>The version is required and must be concrete: an inventory built over a moving version ({@code LATEST},
 * a range) would describe a build that may never ship, which is exactly why the previous chapter's
 * deterministic tree is this chapter's precondition.
 *
 * @param name    the component's artifact name (e.g. {@code commons-lang3})
 * @param version the concrete, pinned version (no ranges, no {@code LATEST})
 * @param purl    the Package URL identifying the component
 *                (e.g. {@code pkg:maven/org.apache.commons/commons-lang3@3.18.0})
 */
public record SbomComponent(String name, String version, String purl) {

    /** Compact constructor: a component must be fully and concretely identified to be inventoried. */
    public SbomComponent {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(version, "version");
        Objects.requireNonNull(purl, "purl");
        if (name.isBlank()) {
            throw new IllegalArgumentException("component name must not be blank");
        }
        if (version.isBlank()) {
            throw new IllegalArgumentException("component version must not be blank");
        }
    }
}
