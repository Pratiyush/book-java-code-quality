# FLAG — key 26: undecidability claim needs a PRIMARY citation (⚠ UNVERIFIED)

**Chapter:** 26 — How static analysis works
**Status:** ⚠ UNVERIFIED — theoretical spine cited only to secondary/orientation sources so far

## The claim
The chapter's theoretical centre — "for any non-trivial *semantic* property of programs, deciding it is
**undecidable** (Rice's theorem; reduces to the halting problem), so no analyzer can be both **sound** and
**complete**" — is the unifying lens for the whole false-positive discussion and all of Part IV.

## Why flagged
Orientation came from **secondary** sources only (PVS-Studio blog, a Medium post, the SIGPLAN blog "What
Does It Mean for a Program Analysis to Be Sound?"). Per GUIDELINES §5 / Durable principle #1, a foundational
result stated as fact must carry a **primary** citation. "Common knowledge" is not an exemption.

## Action (at draft)
Fix a citeable **primary PL/compilers text** for Rice's theorem + the soundness/completeness trade-off
(e.g. a standard compilers or program-analysis textbook edition) and cite it exactly. The Checker Framework
manual ("values soundness over limiting false positives") is a valid *primary illustration of the design
choice*, but is NOT the citation for the theorem itself. Until the primary edition is fixed, keep the
undecidability statement marked `⚠ UNVERIFIED`.

## Proposed pipeline note
Add to GUIDELINES §5: "foundational CS theorems (Rice, halting problem, Big-O claims) still require a
primary-text citation" — sibling of the JLS-§ and standards-edition discipline rules.
