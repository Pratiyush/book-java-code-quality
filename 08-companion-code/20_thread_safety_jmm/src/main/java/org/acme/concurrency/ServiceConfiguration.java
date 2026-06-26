package org.acme.concurrency;

import java.util.Map;
import java.util.Objects;

/**
 * An immutable configuration safely published through {@code final} fields — the cheapest
 * safe-publication idiom.
 *
 * <p>Every field is {@code final} and set in the constructor, so the JLS SE 21 &sect;17.5 freeze
 * guarantees that any thread which sees a fully-constructed reference also sees the correct field
 * values, with no lock and no {@code volatile}. The map is wrapped with {@link Map#copyOf} so the
 * {@code final} reference points at a genuinely immutable target — a {@code final} reference to a
 * <em>mutable</em> object would cover the reference, not later mutations of the target. The same
 * guarantee is why a {@code record} with all-{@code final} components is thread-safe to share for
 * free. The one precondition the spec states: the reference must not escape during construction (no
 * {@code this}-escape), which is why nothing here registers a listener or starts a thread from the
 * constructor.
 */
public final class ServiceConfiguration {

    // tag::final-publication[]
    private final int maxConcurrency;
    private final Map<String, String> settings;

    public ServiceConfiguration(int maxConcurrency, Map<String, String> settings) {
        this.maxConcurrency = maxConcurrency;
        this.settings = Map.copyOf(settings); // final reference to a genuinely immutable target
    }
    // end::final-publication[]

    /**
     * Returns the configured maximum concurrency.
     *
     * @return the maximum number of worker threads, always positive
     */
    public int maxConcurrency() {
        return maxConcurrency;
    }

    /**
     * Returns the value of a setting, if present.
     *
     * @param key the setting name, never {@code null}
     * @return the setting value, or {@code null} if the key is absent
     * @throws NullPointerException if {@code key} is {@code null}
     */
    public String setting(String key) {
        return settings.get(Objects.requireNonNull(key, "key"));
    }
}
