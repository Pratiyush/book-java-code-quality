package org.acme.commerce.catalog;

import java.util.List;
import org.acme.platform.error.ApiException;

/**
 * Catalog application logic (Part: layering). It sits between the HTTP layer and the repository port
 * and turns an absent product into a typed {@link ApiException} the HTTP layer maps to 404 — so the
 * not-found rule lives in one place rather than being re-checked at every call site.
 */
public final class CatalogService {

    private final CatalogRepository repository;

    public CatalogService(CatalogRepository repository) {
        this.repository = repository;
    }

    /** The product, or a 404 problem when no such id exists. */
    public Product require(String id) {
        return repository.findById(id)
            .orElseThrow(() -> ApiException.notFound("product-unknown", "no product with id " + id));
    }

    public List<Product> list() {
        return repository.findAll();
    }
}
