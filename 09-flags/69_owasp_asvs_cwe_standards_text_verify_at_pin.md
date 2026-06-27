# FLAG — key 69: OWASP/ASVS/CWE/JCA standards-text atoms not verifiable in-repo (verify at pin)

- **Dossier / draft:** `02-research/69_secure_coding_owasp/`, `03-drafts/69_secure_coding_owasp/69_secure_coding_owasp_v1.md` (folds keys 72, 74)
- **Severity:** procedural — these are standards/spec atoms whose exact text is cited, never redistributed (SOURCE-PIN.md §1; standards-edition discipline). The chapter's load-bearing Java facts trace to the BUILT companion module; only the standards-text identifiers below remain non-machine-checkable in-repo.
- **Companion sibling flag:** `69_findsecbugs_not_in_local_engine.md` (FindSecBugs / crypto-deser rule IDs — OPEN, separate scope).

## Issue
Chapter 30 (key 69) anchors on **OWASP Top 10:2025**, which SOURCE-PIN.md §1 pins (✅ 2026-06-20;
"Top 10:2025 (final; A01 Broken Access Control, supply-chain elevated)", owasp.org/Top10/2025). The
**edition** is therefore confirmed and stated as fact in the draft. The following finer-grained
standards atoms, however, are **not enumerated in the pin** and cannot be verified from inside the repo:

1. **OWASP Top 10:2025 exact full category list + numbering.** The pin names only **A01 Broken Access
   Control** and the supply-chain elevation. The draft must NOT assert the legacy 2021-style numbering
   (A02 Cryptographic Failures / A03 Injection / A07 Auth failures / A08 Software-and-Data-Integrity)
   as 2025-current — those numbers carry `⚠ verify-at-pin` against owasp.org/Top10/2025.
2. **ASVS current version number.** Pinned only as a grouped row ("OWASP Top 10 / ASVS / Cheat Sheets");
   no ASVS version string in the pin.
3. **CWE mappings / titles** (CWE-89, CWE-78, CWE-502, CWE-327, CWE-330). MITRE CWE is cited, not
   pinned as a row; the numbers are well-established but the exact titles are not machine-checkable here.
4. **JEP 290 number + Java 9 attribution** for `ObjectInputFilter`. SOURCE-PIN's JEP index is
   "per-feature (confirm each JEP # at use)"; JEP 290 is not enumerated. The `ObjectInputFilter` API
   itself IS confirmed by the green companion build; only the JEP#/version label is unverified-in-repo.
5. **Effective Java serialization item number** ("prefer alternatives to Java serialization"). EJ 3rd
   ed. is pinned as a named-canon edition (§7) but the book text is not in-repo (see
   `08_effective_java_verbatims_not_in_repo.md`); the phrasing is cited as a principle, not a page-cited
   verbatim.
6. **Specific RCE CVEs** (OGNL / Log4Shell). No CVE pin; the draft keeps these general in the body,
   which is correct.
7. **Algorithm recommendations** (key sizes, modes, password-hash work factors). No NIST row in the
   pin. The companion module's parameters (AES/GCM 128-bit tag, 12-byte nonce, PBKDF2 256-bit /
   210,000 iterations) are a **dated baseline choice shown in code**, not a pin-traced recommendation;
   the draft dates them against "current OWASP/NIST guidance" and must keep DATING them, never assert
   them as timeless.

## Confirmed (resolved, NOT in scope of this flag)
- The vuln→fix Java APIs and JCA transformation strings the module uses — `PreparedStatement` bind
  parameter; `ObjectInputFilter.Config.createFilter` allow-list + fixed-shape record parse;
  `Cipher.getInstance("AES")` (ECB) → `"AES/GCM/NoPadding"` + `SecureRandom` + `"PBKDF2WithHmacSHA256"`;
  `MessageDigest.getInstance("MD5")` — all **compile and round-trip green** at JDK 21.0.11
  (`08-companion-code/69_secure_coding_owasp/`, report `69_secure_coding_owasp_EXAMPLE.md`:
  19 tests / 0 Checkstyle / 0 unsuppressed SpotBugs). The two core-SpotBugs findings
  (`SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE`, `DMI_RANDOM_USED_ONLY_ONCE`) fire and are narrowly,
  reasonably suppressed. These are facts, not flagged.
- OWASP Top 10 **edition** (2025 current, supersedes 2021; A01 leads; supply-chain elevated) —
  confirmed vs the SOURCE-PIN OWASP row; stated as fact in the draft.

## Required handling (draft is correct as-is — shippable)
- Keep the seven atoms above under their `⚠ verify-at-pin` / `⚠ @pin` annotations in the draft header
  comment and back-matter; do not assert any of them as confirmed until checked against the primary at
  the pin (owasp.org/Top10/2025, OWASP ASVS, MITRE CWE, openjdk.org/jeps/290, the pinned EJ edition,
  NIST/OWASP crypto guidance dated at use).
- Resolve at the next `/pin-source` pass: enumerate the exact 2025 category list, pin an ASVS version,
  and confirm the JEP 290 / EJ item identifiers, then re-trace these atoms.

## Disposition
Left flagged by design. The chapter's load-bearing content (the three vulnerability classes and their
design-out fixes) is proven by the GREEN companion module and the confirmed OWASP edition; only
spec-exact identifiers remain non-verifiable-in-repo, the expected steady state for cited (never
redistributed) standards text. Raised by source-verifier during deferred-marker resolution
(2026-06-27).
