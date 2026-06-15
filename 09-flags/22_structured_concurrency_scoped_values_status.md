# FLAG — key 22: structured-concurrency preview + scoped-values/pinning version deltas

- **Key:** 22 — Virtual threads & structured concurrency (21 preview → 25)
- **Type:** `⚠ AHEAD-OF-PIN` (structured concurrency) + version-delta discipline (pinning, scoped values)
- **Cross-link:** extends `09-flags/08_structured_concurrency_ahead_of_pin.md` (which records the SC-preview
  status from the Bloch-canon angle); this flag adds the JEP-491 and JEP-506 deltas.

## Atoms

1. **Structured Concurrency is STILL preview through Java 25 — `⚠ AHEAD-OF-PIN`.**
   - Java 21: **JEP 453** (Preview). 22/23/24: **JEP 462/480/499** (2nd/3rd/4th Preview). Java 25: **JEP 505**
     (Fifth Preview). Java 26: **JEP 525** (Sixth Preview, ahead of forward LTS).
   - The public API **changed shape**: Java 21 used `StructuredTaskScope` constructors with
     `ShutdownOnFailure`/`ShutdownOnSuccess`; Java 25 uses `StructuredTaskScope.open(Joiner…)` static
     factories (JEP 505 verbatim: "opened via static factory methods rather than public constructors").
   - **Required handling:** never present as a stable idiom; always `--enable-preview`; no companion code
     depends on it as stable API. Teach as a *direction*. Source: openjdk.org/jeps/453, /505, /525 (curl).

2. **Scoped Values are GA at Java 25, NOT at Java 21 — Java-25 delta.**
   - **JEP 506** Closed/Delivered, Release **25** (GA). Preview at 21 (JEP 446). On the anchor (21) use
     `ThreadLocal` (with the at-scale caveat); scoped values are a forward note. Source: openjdk.org/jeps/506.

3. **`synchronized` pinning behaviour changed across the LTS window — date all pinning advice.**
   - Java 21 (**JEP 444**): a virtual thread is pinned to its carrier inside a `synchronized` block/method or
     a native/foreign-function call; pinning + a blocking call blocks the carrier OS thread.
   - Java 24 (**JEP 491**, verbatim): "the `synchronized` keyword no longer pins virtual threads, but we will
     retain it for other pinning situations." So at 24+/25, only native/FFM pins.
   - **Required handling:** any "replace `synchronized` to avoid pinning" advice is **anchor (21) specific**;
     state the JDK version. Source: openjdk.org/jeps/444, /491 (curl).

## Resolution
Re-evaluate when structured concurrency reaches GA past the pin (logged re-pin of the runtime baseline per
SOURCE-PIN moving-target policy). Until then: SC = preview, scoped-values GA only at 25, pinning advice dated.
