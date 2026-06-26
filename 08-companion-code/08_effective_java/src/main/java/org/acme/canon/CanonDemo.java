package org.acme.canon;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

/**
 * A tiny runnable surface that prints the idioms' canonical {@code toString} forms (Item 12) through
 * the platform logger and then exercises the explicit failure path. It is illustrative, not a
 * service: the observability seam here is that an immutable value's {@code toString} is self-
 * describing, so a structured log line carries every component without extra formatting code.
 *
 * <p>Run it with {@code mvn -q exec:java} after wiring an exec plugin, or read it as the worked
 * example the tests assert. The failure path is the caught {@link IllegalArgumentException} from a
 * sub-zero {@link Temperature}, demonstrated rather than merely described.
 */
public final class CanonDemo {

    private static final Logger LOG = System.getLogger(CanonDemo.class.getName());

    private CanonDemo() {
    }

    /**
     * Prints each idiom's canonical string and the failure-path message.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Point point = new Point(3, 4, "origin-ish");
        LOG.log(Level.INFO, "record value: {0}", point);
        LOG.log(Level.INFO, "rounded price (minor units): {0}",
            PricingPolicy.INSTANCE.roundUpToMajorUnit(1_99L, 100));
        LOG.log(Level.INFO, "square area: {0}", Areas.of(new Shape.Square(2.0)));

        try {
            new Temperature(-1.0);
        } catch (IllegalArgumentException rejected) {
            LOG.log(Level.INFO, "failure path rejected an invalid temperature: {0}",
                rejected.getMessage());
        }
    }
}
