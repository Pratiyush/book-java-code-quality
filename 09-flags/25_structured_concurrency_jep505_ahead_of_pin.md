# FLAG (AHEAD-OF-PIN) — key 25: JEP 505 Structured Concurrency is preview at JDK 25

- **Atom:** Structured Concurrency / `StructuredTaskScope`.
- **Status (verified by JEP `Release` field, curl @ openjdk.org/jeps/505):** **JEP 505 — Structured
  Concurrency (Fifth Preview), Release 25.** Still a **preview** API at the Java 25 forward-LTS; requires
  `--enable-preview`. (Preview chain: JEP 453 preview @21 → 480 third-preview @23 → 505 fifth-preview @25.)
- **Contrast (verified):** JEP 444 Virtual Threads = **final @21**; JEP 506 Scoped Values = **final @25**.
  Only structured concurrency remains preview.
- **Rule:** never present `StructuredTaskScope` / structured concurrency as a stable Java 25 feature anywhere
  in the key-25 chapter (or 08/12/20–24). Mark `⚠ AHEAD-OF-PIN`; horizon-note only; keep out of the compiled
  companion module (no `--enable-preview`). Reinforces SOURCE-PIN moving-target policy and the existing
  `09-flags/08_structured_concurrency_ahead_of_pin.md` / `09-flags/12_jep358_default_level_and_rule_ids.md`.
- **Re-check at:** any re-anchor past JDK 25.

---

**Web-verify update 2026-06-27 (status re-confirmed; STAYS AHEAD-OF-PIN / PREVIEW).** openjdk.org JEP 505
re-fetched via `curl` (browser UA). Header table confirms: **Title "Structured Concurrency (Fifth
Preview)", Type Feature, Scope SE, Status Closed/Delivered, Release 25** (Owner Alan Bateman; core-libs;
relates to JEP 499 fourth-preview and JEP 525 sixth-preview). The title's "(Fifth Preview)" qualifier
confirms `StructuredTaskScope` is still a **preview** API at the Java 25 forward-LTS (requires
`--enable-preview`) — consistent with this flag. JEP number + title + release verified correct; the
`⚠ AHEAD-OF-PIN` framing is **retained**. Not present in drafts 11/12/01 (Ch12 correctly cites the Java 21
preview JEP 453, not 505), so no draft edit in this pass.
