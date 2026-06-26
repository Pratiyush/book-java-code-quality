package org.acme.staticanalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * The data-flow fix for {@link ResourceLeakDemo} — the same read, closed on every path.
 *
 * <p>try-with-resources gives the stream a close on normal return, on early return, and on exception.
 * Run the same SpotBugs data-flow analysis over this method and the open-stream fact reaches a close on
 * every path, so no leak is reported — the difference Move 3 makes visible. This is the shape the module
 * ships as advice.
 */
public final class ResourceReader {

    private ResourceReader() {
    }

    /**
     * Reads the first line of a classpath resource, closing the stream on every path.
     *
     * @param resourceName the classpath resource to read
     * @return the first line, or {@code null} when the resource is absent or empty
     * @throws IOException if the resource cannot be read
     */
    public static String readFirstLine(String resourceName) throws IOException {
        InputStream in = ResourceReader.class.getResourceAsStream(resourceName);
        if (in == null) {
            return null;
        }
        try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            return reader.readLine();
        }
    }
}
