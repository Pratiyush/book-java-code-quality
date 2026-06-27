# FLAG — JEP 358 verbatim text not read from primary (key 11)

**Status:** ⚠ UNVERIFIED (primary source unreachable via fetch tool)

**What.** Facts about JEP 358 (Helpful NullPointerExceptions) used in
`02-research/11_null_safety_optional/`:
- Flag name `-XX:+ShowCodeDetailsInExceptionMessages`.
- Off by default in JDK 14; **on by default since JDK 15** (so on at the Java 21/25 anchor).
- Example message form: `Cannot invoke "String.toLowerCase()" because the return value of
  "getEmailAddress()" is null`.

**Why flagged.** `https://openjdk.org/jeps/358` returned **HTTP 403** to WebFetch. The above are
corroborated only via secondary (Baeldung "Java 14 NPE") + the JEP bug record (JDK-8220715). The
flag name and default-on-15 line are widely consistent but not read from the JEP text itself.

**Resolution before draft.** Read JEP 358 from a non-403 source (gh / mirror) OR confirm the
default-on-15 statement against the **JDK 15 release notes** at the pin. Confirm the example message
wording verbatim. Do not block-quote the example until confirmed.

---

**UPDATE 2026-06-27 (Ch 11 v1 draft cleaned).** The behavioural facts are now confirmed enough to
state in prose without an inline `@pin` marker: helpful NPEs being **on by default at the Java 21
anchor** is empirically confirmed by `08-companion-code/11_null_safety_optional/` (the `BrokenCheckout`
unguarded-deref demo relies on it; module builds/runs green on JDK 21, bytecode 65). The flag-name
`-XX:+ShowCodeDetailsInExceptionMessages` and off-14/on-15 are JDK-release facts within the pin.
STILL OPEN (kept flagged): the **verbatim JEP-358 page text** is still unread (openjdk.org/jeps/358 =
HTTP 403); the draft block-quotes **no** JEP-page wording — the hook message is a constructed example,
not a claimed-verbatim JEP quote. Do not introduce a verbatim JEP-page block-quote until the page text
is read at a non-403 source.
