package org.acme.platform.http;

/**
 * A single HTTP endpoint: turn a {@link Request} into a {@link Response} (Part: API design). A
 * handler is pure application logic — it never touches sockets or streams, which the {@link HttpApp}
 * owns — so it is trivial to unit-test by constructing a request and asserting on the response.
 */
@FunctionalInterface
public interface Handler {

    Response handle(Request request);
}
