/**
 * The same orders app sliced <em>by domain feature</em>: {@code orders} and {@code billing}, each
 * self-contained.
 *
 * <p>A feature's data, service, and storage sit together, so a change to "orders" stays in one
 * package — its strongest case, high cohesion and local change. Its cost, which the chapter names, is
 * that sharing across features has to be deliberate: {@code billing} reaches {@code orders} only
 * through a narrow published type, not by touching its internals. Read beside
 * {@code org.acme.design.bylayer}; neither slicing is crowned.
 */
package org.acme.design.byfeature;
