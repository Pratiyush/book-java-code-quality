package org.acme.security;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Counter-example: an order intake that reconstitutes request bytes with native Java
 * deserialization. Calling {@link ObjectInputStream#readObject()} on attacker-controlled bytes is
 * the remote-code-execution-prone operation behind OWASP Top 10:2025 A08 (CWE-502): the stream
 * decides which classes to instantiate, and a "gadget chain" assembled from whatever classes are on
 * the classpath can reach code execution during reconstruction.
 *
 * <p>This type is a deliberate teaching counter-example. It is exercised only for behavior by
 * {@code SecureCodingTest} (reading bytes it wrote itself) and is never wired to a request path. The
 * construction that designs the class out is {@link OrderIntake#parse(String)}; the allow-list
 * mitigation for when native serialization is unavoidable is {@link OrderIntake#readFiltered}.
 */
final class VulnerableOrderIntake {

    /**
     * Reconstitutes an object from request bytes, letting the stream choose the class to instantiate.
     *
     * @param requestBytes the untrusted request payload
     * @return the reconstituted object
     * @throws IOException            if the stream cannot be read
     * @throws ClassNotFoundException if a named class is not on the classpath
     */
    // tag::deser-native[]
    Object readOrder(byte[] requestBytes) throws IOException, ClassNotFoundException {
        // readObject lets the bytes choose which classes to instantiate — the RCE-prone operation.
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(requestBytes))) {
            return in.readObject();
        }
    }
    // end::deser-native[]
}
