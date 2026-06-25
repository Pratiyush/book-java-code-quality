package org.acme.qualityops.gate;

/**
 * The gate orchestrator (Part: composition). It is the only place that knows the gate flow: read the
 * project's aggregated quality through quality-metrics-service, evaluate it against the supplied
 * policy, and record the outcome idempotently by {@code (project, commit)}. Every collaborator is an
 * injected port, so this logic is unit-tested with fakes and never reaches across a network in a test.
 *
 * <p>Recording is idempotent: the first decision for a {@code (project, commit)} is authoritative, so
 * a re-evaluation of the same commit returns the stored decision unchanged rather than overwriting
 * it. That keeps a commit's gate result stable even when CI retries the gate step.
 */
public final class GateService {

    private final MetricsPort metrics;
    private final DecisionRepository repository;

    public GateService(MetricsPort metrics, DecisionRepository repository) {
        this.metrics = metrics;
        this.repository = repository;
    }

    /**
     * Decides whether {@code commit} of {@code project} meets {@code policy}, recording the outcome
     * once. A second call for the same commit returns the recorded decision without re-evaluating.
     */
    public GateDecision decide(String project, String commit, GatePolicy policy) {
        return repository.find(project, commit).orElseGet(() -> {
            ProjectQuality quality = metrics.qualityOf(project);
            GateDecision decision = GateDecision.evaluate(project, commit, quality, policy);
            return repository.saveIfAbsent(decision);
        });
    }
}
