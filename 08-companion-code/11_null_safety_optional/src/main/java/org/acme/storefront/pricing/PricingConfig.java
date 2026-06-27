package org.acme.storefront.pricing;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.jspecify.annotations.Nullable;

/**
 * Reads the externalized pricing config and exposes the profile-selected values.
 *
 * <p>The configuration lives in {@code pricing.properties}, not in the code, and the active profile is
 * chosen at runtime by the {@code storefront.profile} system property (for example {@code dev} or
 * {@code prod}). The default promo code is deliberately optional: the {@code %prod} profile leaves it
 * empty. The value is held internally as a {@code @Nullable String} field — not an {@code Optional}
 * field, which Item 55 advises against — and {@link #defaultPromoCode()} lifts it into an
 * {@link Optional} only at the return, the one use Item 55 sanctions, so a caller is handed the absence
 * in the type. A missing or blank value is normalised to {@code null} here, at the config boundary.
 */
public final class PricingConfig {

    private static final String DEFAULT_PROFILE = "prod";
    private static final String PROFILE_PROPERTY = "storefront.profile";
    private static final String DEFAULT_CODE_SUFFIX = ".storefront.promo.default-code";

    private final @Nullable String defaultPromoCode;

    private PricingConfig(@Nullable String defaultPromoCode) {
        this.defaultPromoCode = defaultPromoCode;
    }

    /**
     * Loads the config for the profile named by the {@code storefront.profile} system property,
     * defaulting to {@code prod}.
     *
     * @return the loaded config, never {@code null}
     * @throws UncheckedIOException if the bundled config resource cannot be read
     */
    public static PricingConfig load() {
        return load(System.getProperty(PROFILE_PROPERTY, DEFAULT_PROFILE));
    }

    /**
     * Loads the config for an explicit profile — the seam the test uses to exercise both profiles.
     *
     * @param profile the profile to read (for example {@code dev} or {@code prod}), never {@code null}
     * @return the loaded config, never {@code null}
     * @throws UncheckedIOException if the bundled config resource cannot be read
     */
    public static PricingConfig load(String profile) {
        Objects.requireNonNull(profile, "profile");
        Properties props = new Properties();
        try (InputStream in =
                PricingConfig.class.getResourceAsStream("/pricing.properties")) {
            if (in == null) {
                throw new IllegalStateException("pricing.properties not found on the classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("failed to read pricing.properties", e);
        }
        String raw = props.getProperty(profile + DEFAULT_CODE_SUFFIX, "");
        String code = raw.isBlank() ? null : raw.strip();
        return new PricingConfig(code);
    }

    /**
     * Returns the configured default promo code, or empty when the active profile sets none. The
     * internal {@code @Nullable String} is lifted into an {@link Optional} here — a method return is
     * Item 55's sanctioned use — so the absence reaches the caller in the type, never as a raw null.
     *
     * @return the default promo code, or an empty {@code Optional} — never {@code null}
     */
    public Optional<String> defaultPromoCode() {
        return Optional.ofNullable(defaultPromoCode);
    }
}
