package org.acme.commerce.catalog;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.acme.platform.money.Money;

/**
 * The in-memory adapter for {@link CatalogRepository} — the runnable default (Part: architecture).
 * It is seeded with a small fixed catalog so the capstone runs end-to-end with no external store.
 * Backed by a {@link ConcurrentHashMap} because the HTTP layer serves requests on many virtual
 * threads at once. A production deployment supplies a database-backed adapter instead.
 */
public final class InMemoryCatalogRepository implements CatalogRepository {

    private final ConcurrentHashMap<String, Product> products = new ConcurrentHashMap<>();

    public InMemoryCatalogRepository() {
        seed(new Product("sku-keyboard", "Mechanical keyboard", Money.of(7999, "USD")));
        seed(new Product("sku-mouse", "Wireless mouse", Money.of(2999, "USD")));
        seed(new Product("sku-monitor", "27-inch monitor", Money.of(24999, "USD")));
    }

    private void seed(Product product) {
        products.put(product.id(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(products.values());
    }
}
