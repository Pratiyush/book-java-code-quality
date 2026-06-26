package org.acme.supplychain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The in-code analogue of an SBOM: a complete component inventory that turns "am I affected by CVE-X?"
 * into a query rather than an archaeology project across build files. This is the data structure a
 * generated {@code bom.json} (see the CycloneDX plugin in {@code pom.xml}) carries; the chapter's value
 * is response speed, so the load-bearing method is {@link #findByName(String)} — the query an on-call
 * engineer runs during the next Log4Shell.
 *
 * <p>The honest edge the chapter names is encoded here as a fact, not a slogan: an inventory enables a
 * fast response; it fixes nothing (see {@link #isInventoryNotDefense()}). A generated-and-never-queried
 * inventory is theatre — the value is realised only when something queries it.
 */
public final class ComponentInventory {

    private final List<SbomComponent> components = new ArrayList<>();

    /**
     * Records a component in the inventory. Adding the same purl twice is idempotent: an inventory lists
     * each component once, so a re-declaration does not duplicate it.
     *
     * @param component the component to inventory; must not be {@code null}
     */
    public void record(SbomComponent component) {
        Objects.requireNonNull(component, "component");
        boolean alreadyPresent = components.stream().anyMatch(c -> c.purl().equals(component.purl()));
        if (!alreadyPresent) {
            components.add(component);
        }
    }

    // tag::inventory-not-defense[]
    /**
     * Answers "are we affected by CVE-X?" — the query the SBOM exists to make fast. Returns the matching
     * component if it is in what ships, else empty. The inventory ENABLES this answer; it does not fix it.
     */
    public Optional<SbomComponent> findByName(String name) {
        return components.stream().filter(c -> c.name().equals(name)).findFirst();
    }
    // end::inventory-not-defense[]

    /**
     * States the chapter's honest limit in code: an SBOM is an inventory, not a defense. Always
     * {@code true} — it is a property of what this type is, surfaced so a caller (or a reader) cannot
     * mistake "we have an SBOM" for "we are safe".
     *
     * @return {@code true} — an inventory makes a team fast to respond, it does not make them safe
     */
    public boolean isInventoryNotDefense() {
        return true;
    }

    /**
     * Observability surface: the number of components currently inventoried — the size of "what ships",
     * the metric a dashboard or a completeness check reads. An inventory that silently omits a shaded
     * component manufactures false confidence (the chapter's accuracy-gap warning), so the count is worth
     * watching, not assuming.
     *
     * @return the count of distinct components recorded
     */
    public int componentCount() {
        return components.size();
    }

    /**
     * Readiness probe: an inventory is a usable Log4Shell answer only once it actually lists components.
     * An empty inventory answers every "are we affected?" query with a false "no".
     *
     * @return {@code true} once at least one component is recorded
     */
    public boolean isReady() {
        return !components.isEmpty();
    }

    /**
     * An immutable snapshot of the inventory — what a generated SBOM serialises. Returning a copy keeps
     * the inventory's internal list from leaking to a caller.
     *
     * @return an unmodifiable list of the recorded components
     */
    public List<SbomComponent> components() {
        return List.copyOf(components);
    }
}
