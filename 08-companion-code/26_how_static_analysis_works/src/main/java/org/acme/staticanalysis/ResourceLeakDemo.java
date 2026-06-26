package org.acme.staticanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Move 3 of the ladder — the defect a control- and data-flow analysis finds.
 *
 * <p>Moves 1 and 2 cannot answer "is this stream always closed?" — that is a fact about how values flow
 * along every execution path, not about a token or a type. A data-flow analysis builds the method's
 * control-flow graph and tracks the open stream as a fact through it, discovering the path on which it is
 * never closed. SpotBugs runs exactly this analysis over the compiled <em>bytecode</em>, where it sees
 * what the compiler actually emitted, and reports the leak below ({@code OS_OPEN_STREAM} — "may fail to
 * close stream"). This is the one finding in the module the house gate genuinely raises at its Medium
 * threshold, so it carries a single reviewed suppression with a reason — the chapter's "suppress with a
 * reason, never disable a detector" control made concrete. The fix is {@link ResourceReader#readFirstLine},
 * which closes the stream with try-with-resources; this type is the counter-example, never the advice.
 */
public final class ResourceLeakDemo {

    private ResourceLeakDemo() {
    }

    /**
     * Reads the first line of a classpath resource, leaking the stream — the data-flow defect. The
     * reader is opened and read but never closed on any path, so a file handle escapes on every call.
     *
     * @param resourceName the classpath resource to read
     * @return the first line, or {@code null} when the resource is absent or empty
     * @throws IOException if the resource cannot be read
     */
    // tag::dataflow-leak[]
    public static String readFirstLine(String resourceName) throws IOException {
        InputStream in = ResourceLeakDemo.class.getResourceAsStream(resourceName);
        if (in == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        return reader.readLine();   // LEAK: 'reader' is never closed on any return path
    }
    // end::dataflow-leak[]
}
