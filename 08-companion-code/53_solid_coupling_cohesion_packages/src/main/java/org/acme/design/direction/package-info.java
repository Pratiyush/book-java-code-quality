/**
 * The dependency-direction surface: the chapter's instability measure, plus the module's
 * observability, configuration, and failure-path plumbing.
 *
 * <p>{@link org.acme.design.direction.DependencyDirection} computes {@code I = Ce / (Ca + Ce)} and
 * checks that a dependency runs toward stability. {@link org.acme.design.direction.DirectionConfig}
 * loads the active profile's policy from the externalized {@code design.properties}, and
 * {@link org.acme.design.direction.UnstableDependencyException} is the typed error the strict profile
 * raises. Enforcing such rules across a real codebase is the next chapter's subject; here the surface
 * only makes direction measurable and gives the module a real failure path.
 */
package org.acme.design.direction;
