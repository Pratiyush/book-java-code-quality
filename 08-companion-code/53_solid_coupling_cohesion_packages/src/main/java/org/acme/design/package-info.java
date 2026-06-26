/**
 * Chapter 25 — one small order domain shown in contrasting shapes so the cost of each design choice
 * is concrete (Principles, Measures, and Where the Lines Fall).
 *
 * <p>The sub-packages are the contrasts the chapter draws, each meant to be read against its pair:
 * <ul>
 *   <li>{@code overengineered} vs {@code balanced} — SOLID at its over-application trap beside the
 *       same outcome with abstraction kept to where a real variation exists.</li>
 *   <li>{@code cycle} vs {@code inverted} — a two-package dependency cycle beside the dependency
 *       inversion that breaks it.</li>
 *   <li>{@code bylayer} vs {@code byfeature} — the same app sliced by technical layer and by domain
 *       feature.</li>
 * </ul>
 *
 * <p>{@link org.acme.design.direction.DependencyDirection} computes the chapter's instability measure
 * and exposes the module's observability and failure surfaces. No shape is crowned: each pairing is a
 * trade-off, and the metrics are proxies for the real test — whether a change stays local.
 */
package org.acme.design;
