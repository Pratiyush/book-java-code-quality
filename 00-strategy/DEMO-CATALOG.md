# DEMO CATALOG — Java code quality

> **(technical/code profile — see BOOK-TYPE-PROFILES.md; book types without runnable examples drop this catalog along with the EXAMPLES-GUIDE and the COMPANION-REPO spec.)**

> The practical, runnable demos the book is built on. Each chapter ships **one small companion module** (`08-companion-code/NN_slug/`, gate-bearing at Step 4b) that realizes its demo, plus a fully-shown **TRY IT / exercise** the reader runs. Demos are deliberately **simple** — one idea each — and they all live in **one shared domain** so a reader who meets the domain in chapter 1 still recognizes the setup when it returns many chapters later.

> Companion to `templates/EXAMPLES-GUIDE.md` (module shape) and `PIPELINE.md` (Step 4b builds the module, Step 4c captures its UI). The shared domain is a **narrative motif, not a code reactor**: every module is standalone and independently buildable — **no module depends on another module's code** (the hybrid decision). Only the capstone wires several areas into one app.

---

## 1. The shared domain — pick one small, universally-understood world

A single small domain runs through every chapter. It must be **small enough to hold in your head, broad enough to exercise every Java code quality feature** the book teaches. Use a **real package/namespace root** per module (never a placeholder like `com.example.demo`). Domain constructs are introduced incrementally from the first chapter that needs them; the flagship demo (§2) lands at the chapter where the core stack first comes together.

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

Each row: the chapter's **demo** (what the module does), the **Java code quality surface** it shows, and the **TRY IT exercise** (fully shown, never open-ended).

> **Key ≠ chapter number.** The leftmost column is the frozen dossier **Key** (`NN_slug` — it names the draft folder and companion module), **not** the reading-order chapter. See `FINAL_INDEX.md` for chapter positions. Part headings group by topic for demo-planning; they do not all line up one-to-one with `FINAL_INDEX` Parts, and the catalog omits chapters whose module is not yet built.

> **INSTANTIATE: fill one block of rows per Part of your book.** Use the shape below (a worked row is shown so the column contract is unambiguous); replace every row with your subject's chapters and surfaces.

| Key | Demo (the module) | Java code quality surface | Exercise (TRY IT) |
|---|---|---|---|
| `NN_slug` | what the module does — one idea, in the shared domain | the single subject surface it shows | a constrained, fully-shown extension the reader runs against the module |
| *(exemplar)* `01 what_is_subject` | the gentlest intro demo, one endpoint/operation with a failure case | the subject's entry surface | add a field/case and assert it in a test |

---

## 4. The capstones (assembly) — FOUR microservice applications

The book ships **four** capstones, not one. Each is a small but real, **microservice-based**
application over a **distinct** real-world domain, and all four are built on one shared platform
library. They live under a single reserved home, `08-companion-code/capstones/` (sorted after every
dossier-key module so they never collide with a key):

| Path | Domain | Services | The real problem it models |
|---|---|---|---|
| `capstones/shared-platform/` | — (the platform lib) | — | the zero-dependency JDK-only base every service is built on: `HttpApp` (httpserver + virtual threads + template routing, `/health`, `/metrics`), `Json`, `Result`, `Money`, `EventBus`, `ServiceClient`, RFC-7807 `ProblemDetails`/`ApiException`, `Config`, `Metrics`, `Ids` |
| `capstones/01-commerce-checkout/` | e-commerce checkout | catalog · payment · order | authoritative pricing + at-most-once payment (idempotency) |
| `capstones/02-fintech-ledger/` | money movement | account · ledger · transfer | balanced double-entry + no overdraft + idempotent transfer |
| `capstones/03-logistics-fulfil/` | warehouse fulfilment | inventory · shipment · orchestrator | no oversell under concurrency + saga compensation + no double-ship |
| `capstones/04-quality-operations/` | CI quality gating | ingest · metrics · gate | idempotent quality-event ingest + per-project metric aggregation + a deterministic PASS/FAIL gate decision against a policy (the book's own subject, dogfooded) |

Each capstone is a Maven aggregator whose children are **independently-runnable microservices** that
talk over HTTP (`ServiceClient` / JDK `HttpClient`) and an in-process `EventBus`. Persistence is
**hexagonal**: every service depends on a repository *port* and ships an in-memory adapter, with a
documented seam where a real datastore plugs in. Outbound cross-service calls also go through ports,
so an orchestrator's logic is unit-tested with fakes; each capstone additionally has an **end-to-end
test** that drives the real HTTP adapters against lightweight stubs. See `capstones/README.md` and
each capstone's `README.md` for the service map, endpoints, and honest limitations.

**This is the SINGLE bounded exception** carved into `GUIDELINES.md` §8 and `templates/EXAMPLES-GUIDE.md`: the capstones' **over-cap full-file listings** and **cross-service / cross-module wiring** are allowed here and **nowhere else** — every other (per-chapter) module stays standalone, and every other snippet stays within the snippet cap as a `// tag::` region.

**Still gated, just on a different track.** The capstones do **not** ride a chapter's per-chapter Step 4b. Each is gated on its own — green `mvn -B -Pquality verify` (tests + the Chapter-16 Checkstyle + SpotBugs gate clean at the pins in SOURCE-PIN.md) plus the CODE-REVIEW gate (FLOOR C) — and built and assembled in **Phase 4 (ASSEMBLE)** once the chapters they compose are approved. The reactor pins the toolchain once (`08-companion-code/pom.xml` → `capstones/pom.xml`); a re-pin is a one-line edit.

> **Legacy note.** Earlier drafts of this catalog reserved a single capstone at
> `08-companion-code/99_capstone_<domain>/`. That is superseded by the four-capstone layout above;
> there is no `99_capstone_*` module.

---

## 5. Rules for demos (so they stay simple and gate-clean)

- **One idea per demo.** A demo shows the chapter's single core concept and nothing more. If it needs a second feature to make sense, that feature has its own chapter — link forward, do not bundle.
- **Realistic identifiers, real package/namespace.** Name things for what they do; never `Foo`, `MyService`, `com.example.demo` (the `the locked voice in 00-strategy/VOICE-GUIDE-JAVA-QUALITY.md` code register).
- **Every demo carries its failure path.** The honest-limitations floor, demonstrated in code, not just prose (the EXAMPLES-GUIDE §1.1 failure path).
- **Standalone + gate-bearing.** Each module builds green on its own (`./mvnw -B verify`) and is reviewed like a PR (Step 4b CODE-REVIEW). No module imports another module.
- **The exercise is fully shown.** A TRY IT is a constrained, complete extension the reader runs against the module — never "left as an exercise for the reader."
- **Pinned to the pins in SOURCE-PIN.md.** Every dependency coordinate inherits the version from the BOM/manifest; none of the `rule IDs, config/ruleset keys, tool flags, API signatures, GAV coordinates, version numbers, benchmark figures, and quoted claims` atoms is invented.

> **Cross-reference.** `templates/EXAMPLES-GUIDE.md` is the module-shape standard; this catalog is the demo source — every Step-4b companion module realizes its chapter's demo and TRY-IT row from this catalog.
