# FLAG — key 08: Structured Concurrency is AHEAD-OF-PIN (preview, not GA)

- **Key:** 08 — *Effective Java* in practice (the Bloch canon dated against 21/25)
- **Type:** `⚠ AHEAD-OF-PIN`
- **Atom:** Structured Concurrency (`java.util.concurrent.StructuredTaskScope`).
- **Status at pin:** **Preview** at JDK 25 — **JEP 505** ("Structured Concurrency (Fifth Preview)"),
  with a further preview **JEP 525** ("Sixth Preview") existing ahead of the pin. It is **NOT** a GA /
  final language feature on either the Java 21 anchor or the Java 25 forward LTS.
- **Why flagged:** the chapter dates Bloch's concurrency items (78–84, esp. Item 80 "prefer executors,
  tasks, and streams to threads") against modern Java. Virtual threads (final JDK 21) are GA and may be
  stated as fact. Structured concurrency must be presented only as a **direction** the canon points
  toward — never as a settled/stable idiom or "the modern Item 80."
- **Required handling in the draft:** any mention carries the `⚠ preview` label + the JEP number; no
  companion-code example depends on it as stable API (the JDK 25 API opens `StructuredTaskScope` via
  static factory methods, a shape that has changed across previews and may change again).
- **Source:** openjdk.org/jeps/505 (preview status verified); openjdk.org/jeps/525 (further preview ahead of pin).
- **Resolution:** re-evaluate if/when structured concurrency reaches GA past the pin (logged re-pin of the
  runtime baseline per SOURCE-PIN moving-target policy).
