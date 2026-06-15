# FLAG — key 21: Structured Concurrency is AHEAD-OF-PIN (preview)

**Raised by:** researcher (key 21 — immutability & safe publication)
**Date:** 2026-06-15
**Type:** ⚠ AHEAD-OF-PIN

## What
- **JEP 505 — Structured Concurrency** is **Fifth Preview** at **JDK 25** (verified: `openjdk.org/jeps/505`,
  Status Closed/Delivered, Release 25, title "Structured Concurrency (Fifth Preview)"). It requires
  `--enable-preview` and **must not** be presented as a stable Java 25 feature. Consolidates with the existing
  `09-flags/08_structured_concurrency_ahead_of_pin.md` and the key-12 `StructuredTaskScope` note.
- **JEP 506 — Scoped Values** is **GA at JDK 25** (verified: Release 25, Closed/Delivered). It is *not*
  ahead-of-pin in the preview sense, but it is **post-21**, so it is a forward-note only — the companion module
  (anchored on Java 21) must not depend on it.

## Action
In any draft: structured concurrency = preview / horizon-sidebar only; scoped values = "GA at 25, post-anchor
forward note." Never anchor the key-21 companion code on either. Re-confirm status at `/pin-source` and at any
re-anchor past JDK 25.
