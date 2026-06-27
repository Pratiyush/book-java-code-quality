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

---

**Web-verify update 2026-06-27 (status re-confirmed; STAYS AHEAD-OF-PIN / PREVIEW).** openjdk.org JEP 507
re-fetched via `curl` (browser UA). Header table confirms: **Title "Primitive Types in Patterns,
instanceof, and switch (Third Preview)", Type Feature, Scope SE, Status Closed/Delivered, Release 25**
(Owner Angelos Bimpoudis; specification/language; relates to JEP 488 second-preview and JEP 530
fourth-preview). The title's "(Third Preview)" qualifier confirms it remains a **preview** language
feature at the Java 25 pin (requires `--enable-preview`) — consistent with this flag. JEP number + title
+ release verified correct; `⚠ AHEAD-OF-PIN` is **retained**. Not present in drafts 11/12/01 — the atom
lives in key 13 (`13_jep507`) — so no draft edit in this pass.
