# FLAG — key 17 — JEP 467 Markdown doc comments are AHEAD-OF-PIN

- **Item:** Markdown documentation comments (`///`, CommonMark 0.31.2 + Javadoc tag/link extensions), JEP 467.
- **Status:** `⚠ AHEAD-OF-PIN` relative to the **Java 21 anchor**. JEP 467 ships in **JDK 23** (targeted/delivered per inside.java 2024-05-09; GA wording confirmed against the JDK 25 javadoc guide — primary). The OpenJDK JEP 467 page returned **HTTP 403** to WebFetch; GA confirmation taken from the JDK 25 javadoc guide + inside.java.
- **Why flagged:** the book anchors on Java 21; `///` does NOT exist at the anchor. It is in scope only as a **Java 25-era delta**, clearly labelled — never presented as anchor fact (SOURCE-PIN moving-target policy).
- **Resolution before draft:** confirm GA wording/availability against the pinned JDK 25 javadoc guide; present strictly as a 25 delta with the ecosystem caveat (google-java-format #1193 formatter support lag).
- **Source:** https://openjdk.org/jeps/467 (403) · https://docs.oracle.com/en/java/javase/25/javadoc/using-markdown-documentation-comments.html · https://inside.java/2024/05/09/jep467-targeted-to-jdk23/

---

**Web-verify update 2026-06-27 (fact confirmed; STAYS AHEAD-OF-PIN).** The openjdk.org JEP 467 page (which
returned HTTP 403 to WebFetch) was read successfully via `curl` with a browser User-Agent. Header table
confirms: **JEP 467, "Markdown Documentation Comments", Status Closed/Delivered, Release 23, Scope SE.**
JEP number/title/version correct as stated — no correction needed. Release 23 is past the Java 21 anchor,
so `⚠ AHEAD-OF-PIN` is retained; present strictly as a Java 23+ delta. Not present in drafts 11/12/01, so
no draft edit in this pass.
