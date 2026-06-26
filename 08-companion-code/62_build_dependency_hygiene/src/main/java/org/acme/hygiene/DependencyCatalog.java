package org.acme.hygiene;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A single source of version truth: each {@code groupId:artifactId} key maps to exactly one pinned
 * version, the way a Maven {@code <dependencyManagement>} section or an imported BOM holds one version
 * per coordinate and child modules omit versions.
 *
 * <p>This class is the in-code analogue of the chapter's hygiene mechanisms. Adding a coordinate whose
 * key is already known with a <em>different</em> version is the convergence conflict the Maven Enforcer
 * fails the build on; here it raises {@link ConvergenceException}. Re-declaring the same key with the
 * same version is idempotent (a child module legitimately referencing a managed coordinate). The whole
 * point — both here and in the {@code pom.xml} — is to make a divergent version a deterministic, early
 * failure rather than a silent nearest-wins downgrade discovered at runtime.
 */
public final class DependencyCatalog {

    private static final Logger LOG = System.getLogger(DependencyCatalog.class.getName());

    /** Insertion-ordered so a rendered catalog reads in declaration order, like a BOM. */
    private final Map<String, PinnedDependency> managed = new LinkedHashMap<>();

    /** Observability: how many add calls were rejected because they would break convergence. */
    private final AtomicLong convergenceRejections = new AtomicLong();

    /**
     * Records one pinned coordinate as the single managed version for its key.
     *
     * @param dependency the coordinate to manage, never {@code null}
     * @throws NullPointerException if {@code dependency} is {@code null}
     * @throws ConvergenceException if the key is already managed at a different version
     */
    public void manage(PinnedDependency dependency) {
        Objects.requireNonNull(dependency, "dependency");
        String key = dependency.managementKey();
        PinnedDependency existing = managed.get(key);
        if (existing != null && !existing.version().equals(dependency.version())) {
            convergenceRejections.incrementAndGet();
            LOG.log(Level.WARNING, "convergence conflict on {0}", key);
            throw new ConvergenceException(key, existing.version(), dependency.version());
        }
        managed.put(key, dependency);
    }

    /**
     * The version a key resolves to, if it is managed.
     *
     * @param managementKey a {@code groupId:artifactId} key, never {@code null}
     * @return the single managed version, or {@code null} if the key is not in the catalog
     */
    public @org.jspecify.annotations.Nullable String versionOf(String managementKey) {
        Objects.requireNonNull(managementKey, "managementKey");
        PinnedDependency dependency = managed.get(managementKey);
        return dependency == null ? null : dependency.version();
    }

    /** The managed coordinates, in declaration order — an immutable snapshot. */
    public List<PinnedDependency> managedDependencies() {
        return List.copyOf(managed.values());
    }

    /**
     * Observability surface: the running count of add calls rejected to keep the graph converged.
     *
     * @return the number of convergence rejections since construction, never negative
     */
    public long convergenceRejectionCount() {
        return convergenceRejections.get();
    }

    /**
     * A readiness probe: the catalog is a usable single source of truth once it manages at least one
     * coordinate. An empty catalog manages nothing, so nothing inherits a version from it.
     *
     * @return {@code true} when at least one coordinate is managed
     */
    public boolean isReady() {
        return !managed.isEmpty();
    }
}
