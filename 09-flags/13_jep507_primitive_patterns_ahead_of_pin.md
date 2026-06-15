# FLAG — key 13: JEP 507 primitive type patterns are AHEAD-OF-PIN

**Severity:** material (prevents presenting a preview feature as stable).

**What:** JEP 507 — *Primitive Types in Patterns, instanceof, and switch* — is **Release 25, Third Preview**
(verified by direct fetch of `openjdk.org/jeps/507`: Status "Closed / Delivered", but the JEP title itself
says "(Third Preview)" and the summary states "This is a preview language feature"). Earlier rounds:
JEP 455 (Preview, Release 23).

**Why flagged:** at the Java 25 pin this feature is **preview only** — it requires `--enable-preview` and may
change or be removed. It must NOT be presented as a settled Java 25 language feature.

**Rule:** in any draft of key 13 (or key 95), mark primitive type patterns `⚠ AHEAD-OF-PIN`; describe them as
"previewing at 25," never as stable. The chapter's stable surface stops at the Java 21 feature set
(records, sealed, pattern `switch`, record patterns, text blocks) plus JEP 512 (final at 25).

**Owner:** draft of key 13.
