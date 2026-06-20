package org.acme.storefront.checkout;

import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

/**
 * Externalized configuration for the checkout service (Chapter 10: configuration belongs outside the
 * code). Carries the link time-to-live, the public base URL used to build checkout links, the
 * default currency, and the payment approval ceiling.
 *
 * @param linkTtl how long a checkout link stays valid; must be positive
 * @param baseUrl the public base URL for checkout links; must not be blank
 * @param currency the default three-letter currency code; must not be blank
 * @param paymentCeilingMinor the inclusive payment approval ceiling in minor units; must be {@code >= 0}
 */
public record CheckoutConfig(
        Duration linkTtl, String baseUrl, String currency, long paymentCeilingMinor) {

    /** Validates the components (fail-fast, Chapter 10). */
    public CheckoutConfig {
        Objects.requireNonNull(linkTtl, "linkTtl");
        Objects.requireNonNull(baseUrl, "baseUrl");
        Objects.requireNonNull(currency, "currency");
        if (linkTtl.isNegative() || linkTtl.isZero()) {
            throw new IllegalArgumentException("linkTtl must be positive: " + linkTtl);
        }
        if (baseUrl.isBlank()) {
            throw new IllegalArgumentException("baseUrl must not be blank");
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("currency must not be blank");
        }
        if (paymentCeilingMinor < 0) {
            throw new IllegalArgumentException(
                    "paymentCeilingMinor must be >= 0: " + paymentCeilingMinor);
        }
    }

    /**
     * Returns the built-in defaults (15-minute links, USD, a 10,000.00 approval ceiling).
     *
     * @return the default configuration
     */
    public static CheckoutConfig defaults() {
        return new CheckoutConfig(Duration.ofMinutes(15), "https://shop.acme.org", "USD", 1_000_000L);
    }

    /**
     * Builds a configuration from properties, falling back to {@link #defaults()} for any key that
     * is absent. Keys: {@code checkout.link.ttlSeconds}, {@code checkout.baseUrl},
     * {@code checkout.currency}, {@code payment.ceilingMinor}.
     *
     * @param properties the source properties (for example, loaded from a file or the environment)
     * @return a configuration merging the properties over the defaults
     */
    public static CheckoutConfig fromProperties(Properties properties) {
        Objects.requireNonNull(properties, "properties");
        CheckoutConfig d = defaults();
        Duration ttl = d.linkTtl();
        String ttlSeconds = properties.getProperty("checkout.link.ttlSeconds");
        if (ttlSeconds != null) {
            ttl = Duration.ofSeconds(Long.parseLong(ttlSeconds.trim()));
        }
        String ceiling = properties.getProperty("payment.ceilingMinor");
        return new CheckoutConfig(
                ttl,
                properties.getProperty("checkout.baseUrl", d.baseUrl()),
                properties.getProperty("checkout.currency", d.currency()),
                ceiling == null ? d.paymentCeilingMinor() : Long.parseLong(ceiling.trim()));
    }
}
