/**
 * Chapter 27 — dependency-hygiene domain (The Foundation Under Every Gate).
 *
 * <p>The chapter's subject is the <em>build</em> and the dependency graph it assembles, so the
 * load-bearing artifact of this module is its {@code pom.xml}, not this code: the Enforcer rules
 * ({@code dependencyConvergence}, {@code requireUpperBoundDeps}, {@code bannedDependencies}), the
 * single source of version truth ({@code <dependencyManagement>} with an imported BOM), and the
 * versions plugin that surfaces available updates. This small package exists so the build has a real
 * compile/test graph for the Enforcer to resolve and rule on — the configuration is exercised, not
 * merely declared.
 *
 * <p>The package is {@link org.jspecify.annotations.NullMarked}: every parameter and return in this
 * scope is non-null unless a member is explicitly annotated {@link org.jspecify.annotations.Nullable}.
 */
@NullMarked
package org.acme.hygiene;

import org.jspecify.annotations.NullMarked;
