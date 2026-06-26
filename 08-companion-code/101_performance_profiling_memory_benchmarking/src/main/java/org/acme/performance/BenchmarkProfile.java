package org.acme.performance;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

/**
 * The benchmark's externalized execution profile: warmup iterations, measurement iterations, and
 * forks, read from {@code benchmark-<profile>.properties} on the classpath rather than hard-coded.
 *
 * <p>A quick local run ({@code dev}) and a thorough run an honest result is reported from ({@code
 * prod}) differ by profile, not by editing the benchmark source — the externalized-config discipline
 * the rest of the book applies, here applied to a benchmark. The profile is selected with the {@code
 * benchmark.profile} system property and defaults to {@code dev}.
 *
 * @param warmupIterations      iterations whose results are discarded so the JIT reaches steady state
 * @param measurementIterations iterations that are actually recorded
 * @param forks                 fresh JVMs to fork, exposing run-to-run variance
 */
public record BenchmarkProfile(int warmupIterations, int measurementIterations, int forks) {

    /** The system property that selects the profile; absent means {@link #DEFAULT_PROFILE}. */
    public static final String PROFILE_PROPERTY = "benchmark.profile";

    /** The profile used when none is selected — the fast local run. */
    public static final String DEFAULT_PROFILE = "dev";

    /** Validates the profile's values; every count must be positive to mean anything. */
    public BenchmarkProfile {
        if (warmupIterations < 1 || measurementIterations < 1 || forks < 1) {
            throw new IllegalArgumentException(
                "benchmark counts must all be positive: warmup=" + warmupIterations
                    + " measurement=" + measurementIterations + " forks=" + forks);
        }
    }

    /**
     * Loads the profile named by the {@link #PROFILE_PROPERTY} system property (default {@link
     * #DEFAULT_PROFILE}).
     *
     * @return the loaded profile, never {@code null}
     * @throws IllegalArgumentException if the named profile resource is absent or malformed
     */
    public static BenchmarkProfile load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads a named profile from {@code benchmark-<profile>.properties} on the classpath.
     *
     * @param profile the profile name, for example {@code dev} or {@code prod}; never {@code null}
     * @return the loaded profile, never {@code null}
     * @throws IllegalArgumentException if the resource is absent or a required key is missing/invalid
     */
    public static BenchmarkProfile load(String profile) {
        Objects.requireNonNull(profile, "profile");
        String resource = "/benchmark-" + profile + ".properties";
        try (InputStream in = BenchmarkProfile.class.getResourceAsStream(resource)) {
            if (in == null) {
                // Explicit failure path: a misnamed profile fails fast and names the resource, rather
                // than silently falling back to defaults and reporting a number from the wrong run.
                throw new IllegalArgumentException("no benchmark profile resource: " + resource);
            }
            Properties props = new Properties();
            props.load(in);
            return new BenchmarkProfile(
                intValue(props, "benchmark.warmupIterations", resource),
                intValue(props, "benchmark.measurementIterations", resource),
                intValue(props, "benchmark.forks", resource));
        } catch (IOException e) {
            throw new UncheckedIOException("failed reading benchmark profile " + resource, e);
        }
    }

    private static int intValue(Properties props, String key, String resource) {
        String raw = props.getProperty(key);
        if (raw == null) {
            throw new IllegalArgumentException("missing key '" + key + "' in " + resource);
        }
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "key '" + key + "' is not an integer in " + resource + ": " + raw, e);
        }
    }
}
