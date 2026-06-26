package org.acme.layered;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A composed static-analysis stack: the chapter's load-bearing decision made runnable. The composition
 * rule is to assign each quality concern to exactly ONE owner, so every finding comes from a single place
 * rather than the same nit being reported by three tools — composition, not accumulation. This type is the
 * local model of that decision: it accepts at most one owner per {@link Concern}, refuses a second
 * (the redundancy the rule removes), and reports the owners back in cheap-first order by {@link Moment}, the
 * order a coherent stack runs them in so a broken change fails as early as it can.
 *
 * <p>This is a model of the composition, not a runner: it decides who owns what and in what order, the way
 * the chapter's one-owner-per-concern map and the {@code -Pquality} profile order Checkstyle before
 * SpotBugs. It does not invoke the analyzers; the tools run in the IDE, the build, and CI.
 */
public final class LayeredStack {

    private final Map<Concern, Analyzer> owners = new EnumMap<>(Concern.class);

    /**
     * Assigns {@code analyzer} as the single owner of {@code concern}.
     *
     * @param concern  the concern to assign, never {@code null}
     * @param analyzer the tool that owns it, never {@code null}
     * @return this stack, for chaining
     * @throws IllegalStateException if the concern already has an owner — the redundancy the one-owner rule
     *                               removes; reassigning is a deliberate act, not a silent second checker
     */
    public LayeredStack assign(Concern concern, Analyzer analyzer) {
        Objects.requireNonNull(concern, "concern");
        Objects.requireNonNull(analyzer, "analyzer");
        // tag::one-owner[]
        Analyzer existing = owners.putIfAbsent(concern, analyzer);
        if (existing != null) {                                   // one owner per concern — refuse a second
            throw new IllegalStateException(
                concern + " is already owned by " + existing.name() + "; assign each concern exactly once");
        }
        return this;
        // end::one-owner[]
    }

    /**
     * The owner of a concern, if one is assigned. Returns empty rather than null so an unowned concern is a
     * value the caller must handle, not a surprise — a gap in coverage the stack makes visible.
     *
     * @param concern the concern to look up, never {@code null}
     * @return the assigned owner, or empty if the concern has no owner
     */
    public Optional<Analyzer> ownerOf(Concern concern) {
        return Optional.ofNullable(owners.get(Objects.requireNonNull(concern, "concern")));
    }

    /**
     * The required owner of a concern — the explicit failure path. A stack that is asked to run a concern it
     * does not cover must fail loudly rather than silently skip it, because an unowned concern is a blind
     * spot no tool watches.
     *
     * @param concern the concern that must have an owner, never {@code null}
     * @return the assigned owner
     * @throws IllegalStateException if the concern has no owner — a coverage gap, surfaced not swallowed
     */
    public Analyzer requireOwnerOf(Concern concern) {
        return ownerOf(concern).orElseThrow(() ->
            new IllegalStateException("no owner assigned for " + concern + " — a coverage gap"));
    }

    /**
     * The assigned owners in cheap-first order by their moment — the order the composed stack runs them in,
     * so the compile-bound checks fail before the source pass, the bytecode pass after compilation, and the
     * platform last. Ties keep insertion order, which is stable for an {@link EnumMap} over {@link Concern}.
     *
     * @return the owners ordered earliest-moment first, never {@code null}
     */
    public List<Analyzer> orderedCheapFirst() {
        return owners.values().stream()
            .sorted(Comparator.comparing(Analyzer::moment))
            .toList();
    }

    /**
     * How many concerns currently have an owner — the headline coverage metric a dashboard trends the way it
     * trends any health signal. Read against {@link Concern#values()}, it says how complete the stack is: a
     * count well below the number of concerns means blind spots; the full count means every concern is owned.
     *
     * @return the number of concerns with an assigned owner
     */
    public long ownerCount() {
        return owners.size();
    }

    /**
     * Whether the stack is ready to gate — a readiness probe over its coverage. A stack that owns none of
     * its concerns could only pass everything, the silent way a gate stops gating, so it reports not-ready
     * rather than waving changes through. Readiness here is "at least one concern is owned"; a team tightens
     * it to "every concern is owned" by checking {@link #ownerCount()} against {@link Concern#values()}.
     *
     * @return {@code true} once at least one concern is owned, the signal a health endpoint would expose
     */
    public boolean isReady() {
        return !owners.isEmpty();
    }
}
