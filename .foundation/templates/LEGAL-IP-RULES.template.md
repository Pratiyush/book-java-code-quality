# LEGAL & IP RULES — {{BOOK_SUBJECT}} Book

> Copyright, licensing, and traceability law for the book. Every fact traces to a live, pinned source. Subordinate to `GUIDELINES.md`.

This file governs how the book quotes, cites, reuses, and attributes material, and how it stays defensible on copyright, license, and trademark grounds. It does **not** define image composition — those rules live in `templates/FIGURE-GUIDE.md`. This file covers only image **licensing**.

---

## 1. Version & source pin (HARD)

Every {{INVENT_UNITS}} in the book **must** trace to the single pinned authority source:

- **{{AUTHORITY_SOURCE}}** — pinned at **{{AUTHORITY_PIN}}** (the frozen tag+SHA / edition+year / corpus-snapshot date).
- **Source pin / commit:** `{{AUTHORITY_PIN}}`
- **Local (often ephemeral) clone/fetch root:** `{{AUTHORITY_CLONE_PATH}}`

Rules:
- **Never** trace a fact to an unpinned, faster-moving, or "latest" version of the source (e.g. a `main`/snapshot branch, an unfrozen edition, or a live web page). The pin is the only authority.
- Every verified detail records its **source path** (the exact file, page, section, or location) at verification time.
- A detail that cannot be traced to the pin is **cut** or marked `UNVERIFIED` and flagged to `09-flags/`.
- Re-pin only on an explicit version bump in `SOURCE-PIN.md`; the next pin is re-evaluated only on the schedule recorded there.

See `SOURCE-PIN.md` for the authoritative pin record.

---

## 2. Quote & snippet rules

- **Prose quotes:** under 15 words; one quote per source across a chapter.
- **Examples / code snippets** *(technical profile — see BOOK-TYPE-PROFILES.md; book types with {{GATES_OFF}} on the build adapt or drop the tag-include mechanics, keeping the fair-use ceiling)*: **9 lines maximum** per *displayed* snippet — the fair-use ceiling for technical documentation. The ceiling governs the listing region shown in the book: the tag-include window (`// tag::name[]` / `// end::name[]`) included into the page, not the file behind it. The full backing file under `08-companion-code/NN_slug/` is the runnable source and may be longer; the book reproduces it only through ≤9-line tag-included windows, so each displayed listing stays a minimal fair-use excerpt. Show the minimal example that makes the point.
- **Paraphrases** must be true rewrites, not close mirrors of the original wording or structure.
- Every snippet/example is verified against the pinned source/docs and records its source path (see §1).

---

## 3. Source-quality ranking (house style)

Cite the highest-ranked source that supports the claim. Lower tiers never override a higher one.

1. **Primary authority — {{AUTHORITY_SOURCE}} (pinned at {{AUTHORITY_PIN}})** — the primary authority.
2. **Underlying source / reference material** (at the pinned source) *(technical profile — the framework source code / JavaDoc at the pinned SHA)*.
3. **Specifications & standards** — the governing specs, standards, or canonical references for the subject *(technical profile — e.g. language/platform specs, JEPs, RFCs)*.
4. **Official release notes / vendor blog / authoritative announcements** from the subject's owner.
5. **Reputable expert sources** — named author or org, datable (engineering blogs, conference talks, peer reviews).
6. **Forums / community Q&A** — color only, **never** the sole support for a fact.

Hard prohibitions:
- **Never** invent a source or any of the {{INVENT_UNITS}}.
- **Never** cite content farms, AI-generated text, anonymous pages, AI wiki-clones, or advocacy sites as a factual source.
- Any cross-subject / comparative claim needs a cited source from the pin and must respect `NEUTRALITY.md` ({{NEUTRALITY_STANCE}}).

---

## 4. Citation format

- **In-text:** name the source inline ("The {{SUBJECT_SHORT}} guide states…", "Per the spec…").
- **End of chapter:** a mandatory **"Sources and further reading"** section, two-tier:
  - **Primary / official** — the pinned authority, underlying source/reference, specs, release notes, official channels.
  - **Accessible / further reading** — tutorials, talks, reputable community sources.
- Plain-text URLs only (canonical / official domains preferred). No hidden hyperlinks.
- **No academic author–year / DOI / peer-reviewed-paper bibliography model** *(science/scholarly profiles override this — see BOOK-TYPE-PROFILES.md, which uses author-year + DOI + access date)*.
- Every entry uses this schema:

  ```
  Source/guide · Version · Title · URL · Date-verified (YYYY-MM-DD)
  ```

  Example:
  ```
  {{AUTHORITY_SOURCE}} docs · {{AUTHORITY_PIN}} · "<Section title>" · <URL> · 2026-06-02
  ```

---

## 5. Code/content reuse & attribution ({{SUBJECT_LICENSE}})

{{BOOK_SUBJECT}} material is licensed under **{{SUBJECT_LICENSE}}**. Reusing the subject's code or docs in the book must follow that license.

- Snippets/excerpts reproduced from the source are short, illustrative excerpts under fair use (§2) and remain covered by the source's license ({{SUBJECT_LICENSE}}).
- When an excerpt is taken substantially verbatim from a specific source file, **attribute it** in-text or in the chapter sources (name the guide/file and the pinned source — see {{AUTHORITY_PIN}}).
- **Do not** copy whole files, large contiguous blocks, or any `NOTICE`/license header boilerplate into the manuscript; reproduce only the minimal lines that illustrate the point.
- The book's own examples are **original-for-this-book**; they must not be a lightly edited copy of upstream/source samples.
- Do not strip or alter copyright/license notices when reproducing any third-party material.
- **Companion-code licensing** *(technical profile — see BOOK-TYPE-PROFILES.md; book types without {{GATES_OFF}} on the build drop this)* — the runnable modules under `08-companion-code/NN_slug/` are licensed **{{SUBJECT_LICENSE}} (code)**, with a **separate notice for the book prose**; the licensing terms and any public-repo ({{REPO_REMOTE}}) publication are decided here and in `COMPANION-REPO.md`, gated on human/legal sign-off.
- **Attribution gate (companion code)** *(technical profile — see BOOK-TYPE-PROFILES.md; book types without {{GATES_OFF}} on the build drop this)* — at Step 4b the **code-reviewer / example-builder must confirm every companion-code file under `08-companion-code/NN_slug/` is ORIGINAL-FOR-THIS-BOOK** — written for this book, not a lightly-edited copy of an upstream sample, guide quickstart, or `_ref/` listing. Any file or block taken substantially verbatim from a specific source must **name its guide/file and the pinned source** (§1) in the chapter sources; an unattributed verbatim copy is a gate FAIL.

---

## 6. Trademark handling ({{BOOK_SUBJECT}} / owner marks)

"{{BOOK_SUBJECT}}" and its logo, plus the owning organization's name and related marks, are trademarks of their respective owners. The book references the **technology/subject**, not the brand, and must not imply endorsement.

- Use marks descriptively and accurately (nominative use): refer to "{{BOOK_SUBJECT}}" the subject, not as the book's own product.
- **Do not** alter, restyle, or recreate the {{BOOK_SUBJECT}} or owner logos. Logos appear only when directly relevant and only in unmodified, licensed form (see §7).
- **Do not** imply sponsorship, endorsement, partnership, or affiliation with the owning organization or the {{BOOK_SUBJECT}} project.
- The same restraint applies to comparable subjects' marks: descriptive reference only, no logo use beyond a necessary direct comparison, never crowning a winner (per {{NEUTRALITY_STANCE}}).

---

## 7. Image licensing

Composition, palette, and layout rules for figures live in `templates/FIGURE-GUIDE.md`. This section covers **licensing only** (per {{FIGURE_POLICY}}).

- Every designed figure is an original diagram authored for this book and rendered to a final asset (no third-party diagram is traced or reused). Captured screenshots show only {{BOOK_SUBJECT}}-native UIs from the book's own running example — never a third-party tool's UI.
- **No** third-party copyrighted images, stock art, screenshots of proprietary tools, or vendor logos — except an unmodified, properly licensed mark used solely for a necessary direct comparison.
- If any external asset is ever used, it must carry a clear license (public domain, CC, or explicit permission), recorded with the asset and the date verified.
- No real people's likenesses.

---

## 8. AI-originality clause (hooks the authenticity gate)

Every chapter is AI-produced. To stay defensible and to satisfy the authenticity floor:

- Generated prose must be **original expression**, not a regurgitation of a single source's wording — paraphrases follow §2.
- No fabricated facts, sources, or {{INVENT_UNITS}} may be introduced by the model; everything traces to the pin (§1) and the source ranking (§3).
- The **AUDIT gate** (auditor agent) is the enforcement point: it must judge that a sharp reader cannot tell a machine wrote the chapter, and that no passage is an unattributed close copy of an upstream source.
- **Two-corpus closeness check (AUDIT + source-verifier)** — both the **AUDIT gate** and the **source-verifier** must judge close-paraphrase and verbatim-overreach **not only against the licensed {{AUTHORITY_SOURCE}}** (where short attributed excerpts are licensed under {{SUBJECT_LICENSE}}; §5) **but also against the copyrighted `_ref/` corpus** (competing books / proprietary works on the same subject), which is **never** a permitted source to mirror. For any chapter whose topic overlaps a `_ref/` work, **open the corresponding `_ref/` chapter and judge structure/wording closeness**: matching section order, example progression, sentence framing, or distinctive phrasing against `_ref/` is a FAIL even when no source line is copied.
- **Required read** — the drafter and the auditor must read `09-flags/REF_do_not_copy.md` before drafting/auditing any chapter that overlaps the `_ref/` corpus.
- Any uncertain or untraceable AI-introduced claim is marked `UNVERIFIED` and flagged to `09-flags/` rather than shipped.

---

## Learnings & pipeline suggestions

- Record any recurring attribution or licensing edge case in `PIPELINE-LEARNINGS.md`; promote confirmed lessons into this file.
- If snippet-length or attribution disputes recur, consider a `lint_citations.sh` check that flags snippets over 9 lines and verbatim blocks lacking a source path.
