package org.acme.vthreads;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * The externalized fan-out configuration: which targets to fetch, the back-pressure cap on in-flight
 * work, and the per-call timeout. Values are read from {@code virtual-threads.properties} under a
 * {@code dev} or {@code prod} profile, never hard-coded, so a deployment retunes the fan-out without a
 * code change.
 *
 * <p>The target list is defended on the way in and on the way out (Item 50): the constructor copies the
 * caller's list and {@link #targets()} returns a copy, so neither the caller's original list nor the
 * returned one can mutate this config's state. Back-pressure is a configuration fact here, not an
 * afterthought: virtual threads make fan-out cheap, but a downstream pool is finite, so
 * {@link #maxConcurrent()} caps in-flight work.
 */
public final class FanOutConfig {

    private static final String RESOURCE = "/virtual-threads.properties";

    private final List<String> targets;
    private final int maxConcurrent;
    private final Duration perCallTimeout;

    /**
     * Creates a configuration, copying the caller's target list so later edits to it cannot change this
     * config.
     *
     * @param targets        the target ids to fan out over, never {@code null} or empty, no null element
     * @param maxConcurrent  the cap on in-flight fetches, strictly positive
     * @param perCallTimeout the timeout applied to each fetch, never {@code null} or negative
     * @throws NullPointerException     if any argument or list element is {@code null}
     * @throws IllegalArgumentException if the list is empty, the cap is not positive, or the timeout is
     *                                  negative
     */
    public FanOutConfig(List<String> targets, int maxConcurrent, Duration perCallTimeout) {
        List<String> copy = new ArrayList<>(Objects.requireNonNull(targets, "targets"));
        copy.forEach(t -> Objects.requireNonNull(t, "target"));
        if (copy.isEmpty()) {
            throw new IllegalArgumentException("targets must not be empty");
        }
        if (maxConcurrent <= 0) {
            throw new IllegalArgumentException("maxConcurrent must be positive: " + maxConcurrent);
        }
        Objects.requireNonNull(perCallTimeout, "perCallTimeout");
        if (perCallTimeout.isNegative()) {
            throw new IllegalArgumentException("perCallTimeout must not be negative: " + perCallTimeout);
        }
        this.targets = List.copyOf(copy);
        this.maxConcurrent = maxConcurrent;
        this.perCallTimeout = perCallTimeout;
    }

    /**
     * Loads the configuration for a profile from {@code virtual-threads.properties} on the classpath.
     *
     * @param profile the profile to load, {@code "dev"} or {@code "prod"}, never {@code null}
     * @return the configuration for that profile, never {@code null}
     * @throws NullPointerException  if {@code profile} is {@code null}
     * @throws IllegalArgumentException if the profile has no keys in the properties file
     * @throws UncheckedIOException   if the properties resource cannot be read
     */
    public static FanOutConfig forProfile(String profile) {
        Objects.requireNonNull(profile, "profile");
        Properties props = load();
        String targetsKey = "vthreads." + profile + ".targets";
        String capKey = "vthreads." + profile + ".max-concurrent";
        String timeoutKey = "vthreads." + profile + ".per-call-timeout-millis";
        String raw = props.getProperty(targetsKey);
        if (raw == null) {
            throw new IllegalArgumentException("unknown profile: " + profile);
        }
        List<String> targets = Arrays.stream(raw.split(",")).map(String::trim).toList();
        int cap = Integer.parseInt(props.getProperty(capKey));
        long timeoutMillis = Long.parseLong(props.getProperty(timeoutKey));
        return new FanOutConfig(targets, cap, Duration.ofMillis(timeoutMillis));
    }

    /**
     * Loads the configuration for the profile named by {@code vthreads.active-profile}.
     *
     * @return the active configuration, never {@code null}
     */
    public static FanOutConfig active() {
        return forProfile(load().getProperty("vthreads.active-profile", "dev"));
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = FanOutConfig.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("missing configuration resource: " + RESOURCE);
            }
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new UncheckedIOException("could not read " + RESOURCE, e);
        }
    }

    /**
     * Returns the configured target ids.
     *
     * @return a copy of the target list, never {@code null}, never empty
     */
    public List<String> targets() {
        return new ArrayList<>(targets);
    }

    /**
     * Returns the back-pressure cap on in-flight fetches.
     *
     * @return the maximum number of concurrent fetches, strictly positive
     */
    public int maxConcurrent() {
        return maxConcurrent;
    }

    /**
     * Returns the per-call timeout applied to each fetch.
     *
     * @return the per-call timeout, never {@code null}, never negative
     */
    public Duration perCallTimeout() {
        return perCallTimeout;
    }
}
