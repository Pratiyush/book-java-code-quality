package org.acme.storefront.checkout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.acme.storefront.domain.Money;
import org.acme.storefront.domain.Product;

/**
 * An immutable, in-memory product catalog keyed by product id.
 *
 * <p>Lookups return an {@link Optional} rather than {@code null} (Chapter 9), so a missing product is
 * a value a caller must handle, not a {@code NullPointerException} waiting to happen.
 */
public final class Catalog {

    private final Map<String, Product> productsById;

    /**
     * Builds a catalog from the given products.
     *
     * @param products the products to index; must not be {@code null} and must have distinct ids
     */
    public Catalog(List<Product> products) {
        Objects.requireNonNull(products, "products");
        Map<String, Product> index = new LinkedHashMap<>();
        for (Product product : products) {
            if (index.putIfAbsent(product.id(), product) != null) {
                throw new IllegalArgumentException("duplicate product id: " + product.id());
            }
        }
        this.productsById = Map.copyOf(index);
    }

    /**
     * Returns a small sample catalog for demos and tests.
     *
     * @return a catalog of a few USD-priced products
     */
    public static Catalog withSampleData() {
        return new Catalog(List.of(
                new Product("BOOK-EJ", "Effective Java", new Money(4500L, "USD")),
                new Product("BOOK-CC", "Clean Code", new Money(3800L, "USD")),
                new Product("MUG-ACME", "Acme Mug", new Money(1200L, "USD"))));
    }

    /**
     * Looks up a product by id.
     *
     * @param productId the id to find
     * @return the product, or {@link Optional#empty()} if unknown
     */
    public Optional<Product> find(String productId) {
        return Optional.ofNullable(productsById.get(productId));
    }
}
