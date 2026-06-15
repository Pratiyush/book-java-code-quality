# FLAG — key 10: JEP 401 Value Classes are PREVIEW (⚠ AHEAD-OF-PIN)

- **Dossier:** `02-research/10_immutability_value_design/10_immutability_value_design_RESEARCH.md`
- **Severity:** material — must not be presented as settled/GA Java fact.

## Issue
**JEP 401 "Value Classes and Objects"** (Project Valhalla — developer-declared identity-free value classes,
heap flattening, scalarization) is a **PREVIEW** feature (preview at JDK 25 per JEP 401 / JDK-8251554).
It is **NOT GA** at either pin (Java 21 anchor, Java 25 forward LTS). A further draft, "Null-Restricted Value
Class Types (Preview)" (openjdk.org/jeps/8316779), builds on it and is also non-final.

The **stable, ship-today** material for key 10 is:
- records (JEP 395, GA Java 16),
- JDK immutable collections (`List/Set/Map.of`/`copyOf`, Java 9/10),
- defensive copying (Effective Java Item 50),
- the **value-based class contract** (JEP 390, GA Java 16) on existing JDK library types.

## Required handling in the draft
- Present value classes only as a clearly-labelled **horizon / preview sidebar**, marked preview.
- Do NOT show `value class Foo { ... }` as a current Java idiom or in the compiled companion module.
- Re-check JEP 401 status at draft time; if it remains preview at the pin, keep the `⚠ AHEAD-OF-PIN` label.

## Also queued (non-blocking, verify at pin)
- `List/Set/Map.copyOf` version (recorded Java 10 — confirm against pinned JDK API).
- `java:S2384` page returned HTTP 403 to automated fetch — confirm title/category/CWE at the pinned `sonar-java` resource.
