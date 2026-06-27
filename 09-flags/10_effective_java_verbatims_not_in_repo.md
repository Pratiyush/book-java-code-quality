# FLAG — key 10: Effective Java verbatims + page numbers not verifiable in-repo

- **Dossier / draft:** `02-research/10_immutability_value_design/`, `03-drafts/10_immutability_value_design/10_immutability_value_design_v1.md`
- **Severity:** procedural — named-canon authority is a *secondary* source (SOURCE-PIN §7) and its text is not redistributed into the repo.

## Issue
Chapter 10 leans on *Effective Java* 3rd ed. (Bloch, 2018) — Items 10/11/12/14/17/50 — for the
immutability discipline and the equals/hashCode/toString/Comparable canon. The book is pinned as an
**edition** (3rd ed., 2018) in SOURCE-PIN.md §7 (named book canon), but the book text itself is **not
present in this repo** (copyrighted, never redistributed; `_ref/`-class). Therefore any *exact
wording* attributed to the book, or any *page number*, cannot be verified against the pin from inside
the repo.

## Current handling in the draft (correct)
- The chapter **paraphrases** the Items in the locked voice; it does not reproduce verbatim quotations
  from the book and asserts **no page numbers**. Per SOURCE-PIN §7 canon rule, every Item claim is
  corroborated by a *primary* source where one exists (JEP 395 for records, the JDK 21 API contracts
  for equals/hashCode/Comparable), so the load-bearing facts trace to primaries, not the book alone.
- The one near-quote of Item 17 ("immutable unless there's a very good reason to be mutable",
  draft "When NOT to make it immutable" + Item-17 framing) is a short fair-use paraphrase, not a
  pinned verbatim.

## Required handling
- Do **not** introduce verbatim *Effective Java* quotations or page citations that cannot be checked
  against the pinned edition; keep claims as paraphrase corroborated by a primary (JEP/JLS/JDK API).
- If a verbatim quote is ever added, it must be confirmed against the pinned 3rd-ed. text out-of-band
  (the book is not in-repo) and attributed per LEGAL-IP §2/§5; until then it stays `UNVERIFIED`.

## Disposition
Left flagged by design: the primary-corroborated paraphrases are shippable; only book-verbatim/page
facts remain non-verifiable-in-repo, which is the expected state for a named-canon secondary source.
