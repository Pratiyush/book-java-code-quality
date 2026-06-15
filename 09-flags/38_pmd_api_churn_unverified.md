# FLAG — key 38: PMD custom-rule API churns across major versions (verify node/selector names at pin)

**Key:** 38 — Writing custom rules (PMD slice)
**Severity:** version-sensitive (`⚠ verify at pin`) — not a blocker.
**Date:** 2026-06-15

## Issue
PMD's custom-rule authoring API (the Java AST node model, the visitor/rulechain mechanism, and the XPath-rule
wrapper class) has **changed across major versions** (the 7.x line reworked node names and the rulechain
selector model — `AbstractJavaRulechainRule` + `buildTargetSelector()`). A custom PMD rule written against one
PMD line may not compile against another. PMD is the **most upgrade-sensitive** of the five custom-rule
surfaces in this chapter.

## Atoms to re-trace at the pinned PMD version
- AST node names cited from the current doc: `ASTMethodDeclaration`, `ASTFieldDeclaration`, `ASTVariableId`,
  `ASTIfStatement` — confirm these spellings exist at the pinned PMD version (some were renamed across 7.x).
- The rulechain selector API (`buildTargetSelector()`) and the "visit methods must not recurse" contract on
  `AbstractJavaRulechainRule`.
- The **XPath-rule wrapper `class` attribute** value used in the ruleset `<rule>` for an XPath-backed rule
  (this class name has moved across PMD versions) — not asserted in the dossier; resolve at pin.
- The `definePropertyDescriptor(PropertyFactory....)` / `getProperty(descriptor)` property API surface.

## Action
When `SOURCE-PIN.md` §2 PMD row is pinned, re-trace the PMD rows of the §2.7 matrix against
`docs.pmd-code.org` at that exact version before any draft asserts a node name or the XPath wrapper class.
Pairs with `09-flags/38_tool_versions_and_apis_unverified.md`.
