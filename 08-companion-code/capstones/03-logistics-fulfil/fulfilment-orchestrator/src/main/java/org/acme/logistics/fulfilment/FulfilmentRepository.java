package org.acme.logistics.fulfilment;

import java.util.Optional;

/** The persistence PORT for completed fulfilments, keyed by order id. */
public interface FulfilmentRepository {

    Optional<Fulfilment> findByOrderId(String orderId);

    void save(Fulfilment fulfilment);
}
