package org.acme.concurrency;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.util.Properties;

/**
 * The coordinator's externalized configuration, loaded from {@code thread-safety.properties} and
 * selected by profile.
 *
 * <p>The pool size and shutdown grace period are deployment knobs, so they live in a properties file
 * with a {@code dev} and a {@code prod} profile rather than as constants in the service. The loader
 * selects the profile at construction (default {@code dev}) and reads the matching keys. Once built,
 * the instance is immutable: the two fields are {@code final}, so a constructed reference is safe to
 * publish to other threads under JLS SE 21 &sect;17.5 without further synchronization.
 */
public final class WorkCoordinatorConfig {

    /** The profile names the externalized config file defines. */
    public enum Profile {
        DEV, PROD
    }

    private static final String RESOURCE = "thread-safety.properties";

    private final int maxConcurrency;
    private final Duration shutdownGrace;

    private WorkCoordinatorConfig(int maxConcurrency, Duration shutdownGrace) {
        this.maxConcurrency = maxConcurrency;
        this.shutdownGrace = shutdownGrace;
    }

    /**
     * Loads the configuration for the given profile from the externalized properties file.
     *
     * @param profile the profile to read, never {@code null}
     * @return an immutable configuration for that profile
     * @throws UncheckedIOException if the properties resource cannot be read
     */
    public static WorkCoordinatorConfig load(Profile profile) {
        String prefix = "coordinator." + profile.name().toLowerCase(java.util.Locale.ROOT) + ".";
        Properties props = new Properties();
        try (InputStream in = WorkCoordinatorConfig.class.getResourceAsStream("/" + RESOURCE)) {
            if (in == null) {
                throw new IOException("missing classpath resource: " + RESOURCE);
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("could not load " + RESOURCE, e);
        }
        int concurrency = Integer.parseInt(props.getProperty(prefix + "max-concurrency"));
        long graceMillis = Long.parseLong(props.getProperty(prefix + "shutdown-grace-millis"));
        return new WorkCoordinatorConfig(concurrency, Duration.ofMillis(graceMillis));
    }

    /**
     * Returns the configured worker-pool size.
     *
     * @return the number of concurrent worker threads, always positive
     */
    public int maxConcurrency() {
        return maxConcurrency;
    }

    /**
     * Returns the configured graceful-shutdown grace period.
     *
     * @return the time to wait for in-flight work to drain before forcing shutdown
     */
    public Duration shutdownGrace() {
        return shutdownGrace;
    }
}
