package org.acme.orders;

import java.util.Optional;

/**
 * A query port: looks up the unit price of a stock-keeping unit (SKU).
 *
 * <p>This is the chapter's example of a <em>query</em> collaborator — it answers a question and has
 * no side effect that matters. In a unit test it reads cleanest as a <em>stub</em>: the test programs
 * a canned answer with {@code when(catalog.priceOf(...)).thenReturn(...)} and then checks the
 * resulting state, rather than verifying that the call was made. The port is an interface the
 * application owns, which is why it is a sound thing to double at all.
 */
public interface PriceCatalog {

    /**
     * Returns the unit price for a SKU, or empty when the SKU is unknown.
     *
     * @param sku the stock-keeping unit to price, never {@code null}
     * @return the unit price, or {@link Optional#empty()} if the SKU is not stocked
     */
    Optional<Money> priceOf(String sku);
}
