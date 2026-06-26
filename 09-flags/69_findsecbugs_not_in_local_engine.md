# FLAG — key 69 — FindSecBugs not present in the local build engine (crypto/deser detection prose-only)

- **Severity:** material (key 69's crypto + deserialization detection claims attribute the catch to
  FindSecBugs).
- **Issue:** `SOURCE-PIN.md` §2 pins **SpotBugs 4.10.2 (+ FindSecBugs, fb-contrib)** as one row, but the
  companion code builds against the locally cached **core SpotBugs 4.9.3** engine (the established working
  set used by modules 09/26), and the **FindSecBugs plugin is not present in the local `~/.m2`**. No
  network fetch is available during the offline FLOOR-C build. Core SpotBugs has **no** crypto-specific
  detector (ECB, MD5, weak algorithm) and **no** deserialization-sink detector — those are FindSecBugs
  patterns.
- **Atoms affected:** the chapter's claim that "FindSecBugs … flag[s] MD5, ECB, and `Random`-for-keys at
  build time" (draft §C) and the deserialization-detection hand-off. The FindSecBugs GAV
  (`com.h3xstream.findbugs:findsecbugs-plugin`) and its rule IDs (e.g. `ECB_MODE`,
  `WEAK_MESSAGE_DIGEST_MD5`, `PREDICTABLE_RANDOM`, `OBJECT_DESERIALIZATION`, `STATIC_IV`) are **not**
  verified at a pin and are **not** asserted in the module.
- **Required action:** pin a FindSecBugs plugin version in a SOURCE-PIN §2 sub-row (or confirm it ships
  with the SpotBugs 4.10.2 line) and add it to the companion `quality` profile's SpotBugs plugin
  dependencies, then re-trace key 69's crypto/deser detection rule IDs against it. Until pinned, those
  rule IDs carry `⚠ verify at pin` and stay out of the prose as specific FindSecBugs identifiers (the
  prose names FindSecBugs as the tool, not specific rule IDs).
- **Status:** OPEN — resolve at `/pin-source`.

## Key-69 EXAMPLE-BUILD handling (2026-06-26)

The Chapter 30 (key 69) companion module `08-companion-code/69_secure_coding_owasp/` demonstrates three
vulnerability classes. Because FindSecBugs is **not** in the local engine (this flag), the crypto (ECB,
MD5, predictable-IV) and deserialization (`readObject`) **detection** is shown by **tests and prose**,
not by the gate — `SecureCodingTest` proves each defect's behavior (ECB leaks block equality; MD5 is
unsalted/colliding; a same-seed `Random` reproduces its stream; `readObject` reconstitutes whatever the
bytes encode; the `ObjectInputFilter` allow-list rejects a disallowed class). **No FindSecBugs output is
faked.**

What the **core engine genuinely raises** at the module's threshold IS gated and shown, with narrow
reasoned suppressions (verified load-bearing, deterministic across runs):
- `SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE` on `VulnerableCustomerLookup.findIdsByEmail` (SQL injection);
- `DMI_RANDOM_USED_ONLY_ONCE` on `VulnerableTokenCrypto.predictableIv` (bad randomness).

So the chapter's "the analyzer flags the bad, the fix passes" thesis is delivered in code by the two
findings core SpotBugs actually produces, while the FindSecBugs-specific catches remain prose+test until
the plugin is pinned. When FindSecBugs is pinned (resolve above), the module's `quality` profile can add
the plugin and the ECB/MD5/deser counter-examples will additionally fire at the gate (each then carrying
its own reasoned suppression), with no change to the source shapes. The module README records this scope
explicitly.
