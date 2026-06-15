# FLAG — key 20: Structured Concurrency preview + JCStress experimental (AHEAD-OF-PIN / status)

**Raised by:** key 20 research (thread-safety / JMM).
**Type:** `⚠ AHEAD-OF-PIN` + status caveat.

## Items
1. **JEP 505 Structured Concurrency — Fifth Preview at Java 25** (Release 25, Closed/Delivered, but
   *preview*; JEP 453 was preview at 21). `StructuredTaskScope` requires `--enable-preview`. **Never present
   structured concurrency as a stable Java 25 feature.** Consistent with existing flags
   `08_structured_concurrency_ahead_of_pin.md` and the key-12 `StructuredTaskScope` note. Owned in depth by key 22.
2. **JCStress is self-labelled "experimental harness"** (`github.com/openjdk/jcstress` project description,
   verified verbatim). Cite as the canonical JMM stress tool, but do not imply it is a stable/supported
   product API; it proves bug *presence* better than *absence*. Owned in depth by key 24.

## Status correction (NOT a flag, recorded to prevent mis-statement)
- **JEP 506 Scoped Values is FINAL/GA at Java 25** (Release 25, Closed/Delivered — *not* preview). Do not
  group it with the still-preview structured concurrency.

## Resolution
Keep all structured-concurrency references marked `⚠ AHEAD-OF-PIN` until/unless it finalizes past the pin.
Re-confirm JEP statuses at `/pin-source`.
