# FLAG — key 23: Structured Concurrency + Scoped Values + VT no-pinning are AHEAD-OF-PIN at the Java 21 anchor

- **Key:** 23 — Concurrency utilities over hand-rolled locking (java.util.concurrent done right)
- **Type:** `⚠ AHEAD-OF-PIN`
- **Atoms / status at pin (verified by JEP `Release` field, curl):**
  - **Structured Concurrency** (`java.util.concurrent.StructuredTaskScope`) — **JEP 453 = Preview @ JDK 21**,
    still **JEP 505 = Fifth Preview @ JDK 25**. NOT GA on 21 or 25; the `StructuredTaskScope` factory shape has
    changed across previews. (Cross-refs `09-flags/08_structured_concurrency_ahead_of_pin.md`.)
  - **Scoped Values** — **JEP 506 = Closed/Delivered @ JDK 25** (incubator JEP 429 @ 20 earlier). Final only at
    25 → **AHEAD-OF-PIN at the Java 21 anchor**; horizon note only on 21.
  - **JEP 491 "Synchronize Virtual Threads without Pinning" = Release 24.** Removes most `synchronized`-pinning
    of virtual threads. A Java-24 delta — AHEAD-OF-PIN at 21.
- **Why flagged:** the chapter (a) uses virtual threads (JEP 444, **GA @21 — may be stated as fact**) as the
  modern executor backing, and (b) gives the "prefer `ReentrantLock` over `synchronized` in virtual-thread code"
  guidance, which is a **Java-21-era reality that JEP 491 narrows at 24**. Structured concurrency / scoped values
  are tempting as "the modern way" but are preview/post-anchor.
- **Required handling in the draft:** mark every mention with the JEP number + `⚠ preview`/`Java-N delta`; keep
  `StructuredTaskScope` and `ScopedValue` OUT of the compiled companion module (no `--enable-preview`); present the
  VT-pinning advice with its explicit version boundary (21→narrowed at 24), never as a permanent rule.
- **Source:** `openjdk.org/jeps/453`, `/505` (preview), `/506` (final @25), `/491` (R24), `/444` (R21 GA).
- **Resolution:** re-evaluate if structured concurrency / scoped values reach GA on the pinned baseline (logged
  re-pin of the runtime baseline per SOURCE-PIN moving-target policy).
