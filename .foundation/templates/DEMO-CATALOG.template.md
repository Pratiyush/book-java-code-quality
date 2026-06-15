# DEMO CATALOG — {{BOOK_SUBJECT}}

> **(technical/code profile — see BOOK-TYPE-PROFILES.md; book types without runnable examples drop this catalog along with the EXAMPLES-GUIDE and the COMPANION-REPO spec.)**

> The practical, runnable demos the book is built on. Each chapter ships **one small companion module** (`08-companion-code/NN_slug/`, gate-bearing at Step 4b) that realizes its demo, plus a fully-shown **TRY IT / exercise** the reader runs. Demos are deliberately **simple** — one idea each — and they all live in **one shared domain** so a reader who meets the domain in chapter 1 still recognizes the setup when it returns many chapters later.

> Companion to `templates/EXAMPLES-GUIDE.md` (module shape) and `PIPELINE.md` (Step 4b builds the module, Step 4c captures its UI). The shared domain is a **narrative motif, not a code reactor**: every module is standalone and independently buildable — **no module depends on another module's code** (the hybrid decision). Only the capstone wires several areas into one app.

---

## 1. The shared domain — pick one small, universally-understood world

A single small domain runs through every chapter. It must be **small enough to hold in your head, broad enough to exercise every {{BOOK_SUBJECT}} feature** the book teaches. Use a **real package/namespace root** per module (never a placeholder like `com.example.demo`). Domain constructs are introduced incrementally from the first chapter that needs them; the flagship demo (§2) lands at the chapter where the core stack first comes together.

> **WORKED EXEMPLAR (replace at instantiation).** The reference instance used a minimal e-commerce **storefront** (package root `org.acme.storefront`): a domain every reader already understands, where orders and payments naturally produce the events, transactions, and failure paths a technical book must teach, and "generate an expiring checkout link" is a real task with a real failure mode (the link expires) — exactly the honest-limitations beat every chapter owes. Pick your own equivalent: one familiar world whose natural operations generate the mechanisms your subject teaches.

**Entities (kept tiny — a few fields each; exemplar shown):**

| Entity | Fields (illustrative) | Used by |
|---|---|---|
| `Product` | `id`, `name`, `priceCents` | catalog, caching, persistence |
| `Order` | `id`, `items`, `totalCents`, `status` | the web/persistence/messaging chapters |
| `<FlagshipEntity>` | the fields the flagship demo (§2) needs | **the flagship demo** |
| `<FailureEntity>` | the fields a failure path needs | messaging, transactions |

**Why this works.** A familiar domain needs no explanation; its natural operations produce the events, transactions, and failure paths the book must teach; and at least one operation has a real, demonstrable failure mode — which is exactly the kind of honest-limitations beat every chapter owes.

---

## 2. Flagship demo — the one a reader recognizes everywhere

The first time the domain constructs come together as a full, end-to-end demo (at the chapter where the core stack first composes) — the one a reader recognizes everywhere after. It should exercise the whole core stack at once **and** carry an explicit failure path.

> **WORKED EXEMPLAR (replace).** The reference instance's flagship was **checkout-link generation**: one endpoint creates an order and returns a shareable, expiring URL; a second resolves it.
>
> ```
> POST /orders            {items:[…]}          → 201  {checkoutUrl, expiresAt}
> GET  /checkout/{token}                        → 200  order summary
>                                                  404  unknown token
>                                                  410  Gone  (link expired)
> POST /orders            {items:[]}            → 400  Bad Request (empty order)
> ```
>
> The URL and the link TTL were both **config**, runtime-overridable, varied by profile; an expired or unknown token was the explicit **failure path** (`410` / `404`).

**One demo, the whole core stack.** The flagship should exercise the chapter's core surfaces at once — the request/response layer, typed config + profiles, input validation, persistence, and a deliberate failure path — and ship a test that asserts every response, so prose, code, and test make the same claim. Later chapters extend **one facet** of the flagship without rebuilding it (persist it, secure it, emit an event from it, measure it) — never bundle.

---

## 3. The catalog — one simple demo + one exercise per chapter

Each row: the chapter's **demo** (what the module does), the **{{BOOK_SUBJECT}} surface** it shows, and the **TRY IT exercise** (fully shown, never open-ended).

> **Key ≠ chapter number.** The leftmost column is the frozen dossier **Key** (`NN_slug` — it names the draft folder and companion module), **not** the reading-order chapter. See `FINAL_INDEX.md` for chapter positions. Part headings group by topic for demo-planning; they do not all line up one-to-one with `FINAL_INDEX` Parts, and the catalog omits chapters whose module is not yet built.

> **INSTANTIATE: fill one block of rows per Part of your book.** Use the shape below (a worked row is shown so the column contract is unambiguous); replace every row with your subject's chapters and surfaces.

| Key | Demo (the module) | {{BOOK_SUBJECT}} surface | Exercise (TRY IT) |
|---|---|---|---|
| `NN_slug` | what the module does — one idea, in the shared domain | the single subject surface it shows | a constrained, fully-shown extension the reader runs against the module |
| *(exemplar)* `01 what_is_subject` | the gentlest intro demo, one endpoint/operation with a failure case | the subject's entry surface | add a field/case and assert it in a test |

---

## 4. The capstone (assembly)

The capstone has a reserved home: **`08-companion-code/99_capstone_<domain>/`** (frozen slug sorted last so it never collides with a dossier key). One capstone module wires several areas into a single runnable app — so a reader sees the pieces compose.

**This is the SINGLE bounded exception** carved into `GUIDELINES.md` §8 and `templates/EXAMPLES-GUIDE.md`: its **over-cap full-file listings** and **cross-module wiring** are allowed here and **nowhere else** — every other module stays standalone, and every other snippet stays within the snippet cap as a `// tag::` region.

**Still gated, just on a different track.** The capstone does **not** ride a chapter's per-chapter Step 4b. It is gated on its own — green `{{BUILD_CMD}}` (with the strictest lint clean at {{AUTHORITY_PIN}}) plus the CODE-REVIEW gate (FLOOR C) — and is built and assembled in **Phase 4 (ASSEMBLE)** once the chapters it composes are approved.

---

## 5. Rules for demos (so they stay simple and gate-clean)

- **One idea per demo.** A demo shows the chapter's single core concept and nothing more. If it needs a second feature to make sense, that feature has its own chapter — link forward, do not bundle.
- **Realistic identifiers, real package/namespace.** Name things for what they do; never `Foo`, `MyService`, `com.example.demo` (the `{{VOICE}}` code register).
- **Every demo carries its failure path.** The honest-limitations floor, demonstrated in code, not just prose (the EXAMPLES-GUIDE §1.1 failure path).
- **Standalone + gate-bearing.** Each module builds green on its own (`{{BUILD_CMD}}`) and is reviewed like a PR (Step 4b CODE-REVIEW). No module imports another module.
- **The exercise is fully shown.** A TRY IT is a constrained, complete extension the reader runs against the module — never "left as an exercise for the reader."
- **Pinned to {{AUTHORITY_PIN}}.** Every dependency coordinate inherits the version from the BOM/manifest; none of the `{{INVENT_UNITS}}` atoms is invented.

> **Cross-reference.** `templates/EXAMPLES-GUIDE.md` is the module-shape standard; this catalog is the demo source — every Step-4b companion module realizes its chapter's demo and TRY-IT row from this catalog.
