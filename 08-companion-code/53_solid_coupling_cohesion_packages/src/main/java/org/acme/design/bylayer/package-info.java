/**
 * The same small orders app sliced <em>by technical layer</em>: {@code controller}, {@code service},
 * {@code repository}.
 *
 * <p>The slicing maps cleanly onto a layered architecture and is familiar to most readers — its
 * strongest case. Its cost, which the chapter names, is that one feature's code is scattered across
 * three packages, so a change to "orders" touches all three and each layer package is a low-cohesion
 * bucket of unrelated features. Read beside {@code org.acme.design.byfeature}, which keeps a feature
 * together. Neither slicing is crowned; the trade-off is the point.
 */
package org.acme.design.bylayer;
