# FLAG — JEP 467 (Markdown documentation comments) is AHEAD-OF-PIN

- **Raised by:** key 09 research (Designing clear APIs & method contracts).
- **Status:** `⚠ AHEAD-OF-PIN`.
- **Fact:** JEP 467 introduces Markdown documentation comments (the `///` form) as an alternative to
  HTML-and-`@tag` Javadoc.
- **Why flagged:** JEP 467 targets **JDK 23**. The book's runtime anchor is **Java 21 LTS** (SOURCE-PIN
  runtime baseline). Markdown doc comments are therefore NOT available at the pin and must not be presented as
  current/available when discussing Javadoc-as-contract at Java 21.
- **Allowed use:** mention only as a Java 23+ delta / direction-of-travel, explicitly labelled, never as a
  feature the reader can use at the anchor.
- **Inherited by:** keys 17 (comments/Javadoc), 89 (docs-as-contract), 13/95 (modern-Java/migration). Same
  caution applies wherever doc-comment authoring is shown.
- **Source:** https://openjdk.org/jeps/467
- **Resolution:** confirm exact JDK level at `/pin-source`; if the book ever re-anchors past 23, lift the flag.

---

**Web-verify update 2026-06-27 (fact confirmed; STAYS AHEAD-OF-PIN).** openjdk.org JEP 467 fetched via
`curl` (browser UA — the WebFetch 403 was a default-UA artifact). Confirmed from the page header table:
**Title "Markdown Documentation Comments", Type Feature, Scope SE, Status Closed/Delivered, Release 23**,
Owner Jonathan Gibbons, Component tools/javadoc. JDK 23 reached GA 2024-09-17 (openjdk jdk-dev GA notice).
JEP number/title/version are all **correct** as stated in this flag — no correction needed. Because Release
23 is **past the Java 21 anchor**, the `⚠ AHEAD-OF-PIN` framing is retained. Note: JEP 467 does **not**
appear in drafts 11/12/01 (this web-verify pass's edited set); it lives in keys 09/17/89/13/95, so no draft
edit was made here — only this verification record.
