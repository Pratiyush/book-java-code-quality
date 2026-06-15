# FLAG — key 15: Valhalla value-class equality guidance AHEAD-OF-PIN

**Status:** ⚠ AHEAD-OF-PIN — not GA at the pinned runtime (Java 21 anchor / Java 25 forward LTS).
**Dossier:** `02-research/15_equals_hashcode_contracts/15_equals_hashcode_contracts_RESEARCH.md` §5, §7.

## What is ahead of pin
Project Valhalla **value classes** and evolving `@jdk.internal.ValueBased` / value-based identity-vs-equality
semantics are not GA at the pin. Any claim that value classes change how `==` vs `equals` behaves, or that a
new contract applies to value objects, must NOT be presented as settled fact.

## What IS sayable at the pin
The existing, GA guidance holds: do not synchronize on, lock, or `==`-compare **value-based classes**
(`Optional`, boxed primitives, etc.); use `equals`. Cite the value-based-classes note in the JDK API docs at
Java SE 21.

## Action
If the draft mentions Valhalla/value classes, mark `⚠ AHEAD-OF-PIN` with the JEP and keep it out of asserted
fact. Re-evaluate only if the runtime baseline is re-anchored past the pin (logged decision per SOURCE-PIN
re-pin runbook).
