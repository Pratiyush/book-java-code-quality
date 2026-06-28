# FLAG — key 09: Effective Java Item-title verbatims paraphrased (RESOLVED — no in-repo verbatim remains)

- **Dossier / draft:** `02-research/09_api_method_contracts/`, `03-drafts/09_api_method_contracts/09_api_method_contracts_v1.md`
- **Severity:** procedural — the named-canon authority (*Effective Java* 3rd ed., Bloch 2018) is a *secondary* source (SOURCE-PIN §7) whose text is not redistributed into the repo (`_ref/`-class; `_ref/` currently empty).

## Issue (original)
Chapter 7 ("A Method Is a Promise", key 09) leans on *Effective Java* 3rd ed. — Items 15–17, 49–56 — for the
method-design canon. The book is pinned as an **edition** (3rd ed., 2018) in SOURCE-PIN §7, but the book text
is **not present in this repo** (copyrighted, never redistributed). The earlier draft presented three Item
**titles as verbatim quotations in quotation marks**, attributed to Bloch:
- Item 15 — "Minimize the accessibility of classes and members"
- Item 17 — "Minimize mutability"
- Item 56 — "Write doc comments for all exposed API elements" (presented as "Item 56 states the rule: …")

Because the 3e text is not fetchable in-repo, those quoted-as-verbatim spans could not be machine-checked
against the pin, which capped printed-Ch7 ACCURACY (40/50): material presented as a verbatim quote but not
verifiable against the source.

## Action taken (2026-06-28)
- All three quoted Item titles were **rewritten as faithful attributed paraphrases in the book's own words**
  (genuine rewording of each Item's documented guidance, no quotation marks, not close-paraphrase). Attribution
  + Item number are retained in every case.
- **No verbatim *Effective Java* wording remains in the draft.** A repo scan confirms the only remaining
  quote-marked spans in the chapter are the book's **own voice** (e.g. "no elements", "a result may be absent",
  "at least one required", "is this machine-checkable?") — none attributed to Bloch.
- **No unsupported specific was introduced** by the rewrite; no page numbers are asserted (repo scan: zero
  `p.`/`page NN` citations); no figure depended on any removed quote (Fig 7.1's caption is the book's own line,
  unchanged).
- The back-matter sources note was updated to state the chapter presents this guidance as paraphrase with no
  verbatim Item-title quotation and no page reference.

## Item# → title mapping (web-confirmed against the public 3e table of contents)
Confirmed via WebSearch against multiple public 3e TOC listings (O'Reilly, InformIT/Pearson, jkmcl gist):
- Item 15 — Minimize the accessibility of classes and members
- Item 16 — In public classes, use accessor methods, not public fields
- Item 17 — Minimize mutability
- Item 49 — Check parameters for validity
- Item 50 — Make defensive copies when needed
- Item 51 — Design method signatures carefully
- Item 52 — Use overloading judiciously
- Item 53 — Use varargs judiciously
- Item 54 — Return empty collections or arrays, not nulls
- Item 55 — Return optionals judiciously
- Item 56 — Write doc comments for all exposed API elements

All Item attributions in the draft match this mapping.

## Status of in-repo verbatim atoms
**N/A — paraphrased.** There are no in-repo *Effective Java* verbatim atoms left to verify against the pin for
this chapter; the named-canon dependency is now paraphrase + Item-number, corroborated by primaries
(JDK 21 `java.util.Objects`/`Optional`, JLS SE 21, JEPs 395/409/441) exactly as the sibling chapters do.

## Disposition
**RESOLVED for the verbatim concern.** Steady state now matches the key-08 and key-10 EJ flags: primary-
corroborated paraphrases are shippable; only book-verbatim/page facts would be non-verifiable-in-repo, and the
chapter asserts none. Sibling flags: `08_effective_java_verbatims_not_in_repo.md`,
`10_effective_java_verbatims_not_in_repo.md`. (Item titles can be re-confirmed against the physical 3e text
out-of-band at `/pin-source` if a verbatim quote is ever reintroduced — which it should not be.)
