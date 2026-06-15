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
