# 1. Keep the companion modules JDK-only at runtime

Date: 2026-06-20

## Status

Accepted

## Context

Each companion module in this book demonstrates one chapter's idea and must build green on the anchor LTS
(Java 21) with the minimum that distracts from that idea. A web framework, an embedded server, or a DI
container would add runtime dependencies, version-management surface, and CVE exposure that the chapters
are not about — and would have to be pinned, scanned, and explained for their own sake.

This is an Architecture Decision Record in the form Michael Nygard described: a short, immutable,
point-in-time record of one decision, its context, and its consequences. It lives in the repository under
`docs/adr/` and is reviewed like code. Its purpose is to preserve the *why* so a future contributor does
not re-open a settled question — and, if they do re-open it deliberately, does so knowing what was traded.

## Decision

The companion modules keep their **runtime** dependencies to the JDK only. Test and analysis tooling
(JUnit, AssertJ, the SpotBugs annotations on the compile path) are allowed because they are the subject or
the scaffolding, never shipped. A module that genuinely needs a runtime library to make its chapter's
point states why in its own README.

## Consequences

- **Positive:** small, fast, reproducible builds; a near-zero runtime attack surface; the reader's
  attention stays on the chapter's idea, not on framework wiring.
- **Negative:** some realistic scenarios (a live HTTP health endpoint, a real message broker) are modelled
  in plain Java rather than shown end-to-end on a framework. The trade is deliberate: fidelity to the
  chapter's single idea over breadth.
- This decision is revisited only by superseding ADR, not by silent drift. If a later chapter needs a
  runtime dependency, that is a new, recorded decision.
