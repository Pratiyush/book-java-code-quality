# FLAG — key 32: null-safety annotation adoption is AHEAD-OF-PIN

**Severity:** material (never present as anchor baseline)
**Anchor:** Java 21 LTS (forward Java 25 LTS) per `SOURCE-PIN.md`.

The *current* null-safety annotation story is past the Java 21 anchor and must be cited as **current
direction**, never as anchor-baseline fact. Each item is `⚠ AHEAD-OF-PIN`:

- **Spring Framework 7.0 / Spring Boot 4.0 (blog dated 2025-11-12)** adopt **JSpecify** annotations
  (`org.jspecify.annotations`) and **deprecate** Spring's own JSR-305-style annotations
  (`org.springframework.lang.@Nullable`/`@NonNull`); Spring recommends **NullAway** as the build-time checker
  (Java 25, or 21.0.8+ with specific javac flags). Source: spring.io/blog/2025/11/12/null-safe-applications-with-spring-boot-4/.
- **IntelliJ IDEA 2025.3** aligns JSpecify support with other analyzers (NullAway). Source: JetBrains blog 2025-11.
- **Kotlin** consumes JSpecify from **1.8.20+** (interop) — cite JSpecify's own conformance note.
- **Project Valhalla null-restricted (`!`) types** — a *language-level* non-null at the JVM — is
  **exploratory, not in JDK 21 or 25**. Never assert as imminent (folklore guard; cf. keys 11/14 Valhalla).

**Action:** in the draft, anchor all enforcement facts on Java 21; present Spring 7 / Boot 4 / IntelliJ
2025.3 / Valhalla strictly as "where the ecosystem is heading," dated. Re-confirm at `/pin-source`.
Cross-links `09-flags/11_nullsafety_ahead_of_pin.md`.
