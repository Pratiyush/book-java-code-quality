package org.acme.commerce.catalog;

import java.util.List;
import java.util.Optional;

/**
 * The persistence PORT for products (Part: architecture / hexagonal boundaries). The service depends
 * on this interface, not on any storage technology, so the in-memory adapter that ships can be
 * swapped for a database adapter without touching service logic. This is the seam where a real
 * datastore plugs in.
 */
public interface CatalogRepository {

    Optional<Product> findById(String id);

    List<Product> findAll();
}
