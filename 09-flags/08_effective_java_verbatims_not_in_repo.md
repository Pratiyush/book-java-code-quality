# FLAG — key 08: Effective Java Item titles (verbatim) + page numbers not verifiable in-repo

- **Dossier / draft:** `02-research/08_effective_java/`, `03-drafts/08_effective_java/08_effective_java_v1.md`
- **Severity:** procedural — the named-canon authority is a *secondary* source (SOURCE-PIN §7) whose text is not redistributed into the repo.

## Issue
Chapter 08 ("The Canon, Dated") leans on *Effective Java* 3rd ed. (Bloch, 2018) — Items 3, 10, 11,
12, 15, 16, 17, 49, 72 (and the serialization item) — for the immutability/contracts/composition
canon it dates against Java 21/25. The book is pinned as an **edition** (3rd ed., 2018) in
SOURCE-PIN.md §7 (named-book canon), but the book text itself is **not present in this repo**
(copyrighted, never redistributed; `_ref/`-class). Therefore any *exact wording* of an Item title,
or any *page number*, cannot be verified against the pin from inside the repo. (Companion-module
source is unaffected — the idioms are original-for-this-book Java, confirmed green; see
`08_effective_java_EXAMPLE.md`.)

## Current handling in the draft (correct — shippable)
- The chapter **paraphrases** the Items in the locked voice and cites them by **Item number only**;
  it asserts **no page numbers** (repo scan: zero `p.`/`page NN` citations).
- The one quoted phrase close to an Item title — "prefer alternatives to Java serialization" (the
  gist of Item 85's title) — is presented as a principle name, not as a page-cited verbatim. It is
  accurate to the 3rd-edition title but, like every Bloch verbatim, cannot be machine-checked against
  the pin from inside the repo.
- Every load-bearing "the language changed underneath it" claim is corroborated by a **primary**
  source — JEP 395 (records), JEP 409 (sealed), JEP 441 (pattern switch), JEP 440 (record patterns),
  JEP 378 (text blocks), JEP 286 (`var`), JEP 444 (virtual threads) — confirmed in dossier 13's
  VERIFY (checked against the JEP head tables) and by the companion module's green build on JDK
  21.0.11. The load-bearing facts therefore trace to primaries, not to the book alone.

## Required handling
- Do **not** introduce verbatim *Effective Java* Item-title quotations or page citations that cannot
  be checked against the pinned edition; keep Item claims as paraphrase + Item number, corroborated
  by a primary (JEP/JLS/JDK API).
- If a verbatim Item title or page citation is ever added, it must be confirmed against the pinned
  3rd-edition text out-of-band (the book is not in-repo) and attributed per LEGAL-IP §2/§5; until
  then it stays `UNVERIFIED`.

## Disposition
Left flagged by design: the primary-corroborated paraphrases are shippable; only book-verbatim /
page facts remain non-verifiable-in-repo, which is the expected steady state for a named-canon
secondary source. (Companion `08-companion-code/08_effective_java/` is GREEN and out of scope.)
Sibling flag for the same category on a different chapter: `10_effective_java_verbatims_not_in_repo.md`.
