# Capstone 04 — quality-operations

A CI quality-gate pipeline, split into three independently-runnable microservices that talk over
HTTP. This is one of the book's four capstones (the others are `01-commerce-checkout`,
`02-fintech-ledger`, and `03-logistics-fulfil`); all four are built on the zero-dependency
`shared-platform` library. Fittingly for a book on code quality, this capstone's domain *is* code
quality: it ingests the signals a CI run emits, aggregates them per project, and gates a commit.

> **The real problem it models.** The same CI webhook can fire twice, so both ingestion and the gate
> decision must be **idempotent** — a re-delivered signal must not double-count, and a re-run gate
> step must not flip a commit's recorded verdict. And a release gate must judge a commit against
> *authoritative, aggregated* numbers, not a single tool's one-off report — which is why aggregation
> lives behind metrics-service and the gate only reads from it.

## Services

| Service | Port (default) | Responsibility |
|---|---|---|
| `quality-ingest-service` | 8091 | Ingests a quality event `{project, commit, tool, timestamp, metrics}` from a CI run; idempotent by event id; emits a `QualityEventIngested` domain event on a genuinely new one. |
| `quality-metrics-service` | 8092 | Computes per-project aggregates — latest coverage, total violations and bugs, a composite quality score — by reading the events from ingest-service over HTTP. |
| `quality-gate-service` | 8093 | Given a commit and a policy `{minCoverage, maxViolations, maxBugs}`, reads the metrics and decides `PASS` / `FAIL` with the failing reasons; records the decision idempotently by `(project, commit)`. |

```
CI ──POST /events──▶ quality-ingest-service
                          ▲
                          │ GET /events/{project}
                          │
CI ──POST /gate──▶ quality-gate-service ──GET /metrics/{project}──▶ quality-metrics-service
```

## Endpoints

```
quality-ingest-service    POST /events                → 201 a new event · 200 an already-ingested one
                          GET  /events/{project}      → 200 the project's events (an envelope)

quality-metrics-service   GET  /metrics/{project}     → 200 the aggregate · 404 no events yet

quality-gate-service      POST /gate                  → 200 the decision (PASS / FAIL) · 422 no metrics
```

Every service also exposes `GET /health` and `GET /metrics` from the shared platform.

Re-POSTing an event with an id already seen is a no-op that returns `200` instead of `201`, and
re-gating a `(project, commit)` already decided returns the recorded decision unchanged — so both the
idempotent and the first-time paths are reproducible without any external state.

## The composite quality score

`quality-metrics-service` folds a project's events into one 0–100 number a gate can threshold on:

```
qualityScore = clamp(latestCoverage − 2·totalViolations − 5·totalBugs, 0, 100)
```

The weights (2 per violation, 5 per bug) are this team's cited choice, not a universal truth — a
different shop would tune them, which is why they live in one named method (`ProjectMetrics.compositeScore`).

## Architecture notes

- **Hexagonal persistence.** ingest-service depends on an `EventRepository` *port* and gate-service on
  a `DecisionRepository` *port*; each ships an in-memory adapter. A real datastore adapter slots in at
  that seam without touching service logic.
- **Ports for outbound calls too.** metrics-service depends on `EventSourcePort` and gate-service on
  `MetricsPort`, wired in production to the HTTP clients (`IngestEventsClient`, `MetricsServiceClient`)
  and faked in unit tests — so the aggregation and decision logic are tested without a network.
- **Anti-corruption views.** metrics-service and gate-service each read a smaller, local view of the
  upstream event/aggregate (`IngestedEvent`, `ProjectQuality`), so a producer field they do not use
  can change without touching them.
- **Honest limitations.** The in-memory adapters and the in-process `EventBus` are single-process:
  idempotency and events do not survive a restart or span instances. In production the event id and
  the `(project, commit)` decision key each need a unique constraint in a shared datastore, and the
  domain events need a broker.

## Build & run

From the repo's `08-companion-code/` directory (the parent reactor pins the toolchain once):

```bash
# build + test + Checkstyle + SpotBugs for this capstone
mvn -B -Pquality -f capstones/04-quality-operations/pom.xml verify
```

Run the three services (each in its own terminal), then drive the flow:

```bash
mvn -q -f capstones/04-quality-operations/quality-ingest-service/pom.xml  exec:java -Dexec.mainClass=org.acme.qualityops.ingest.Main  -Dport=8091
mvn -q -f capstones/04-quality-operations/quality-metrics-service/pom.xml exec:java -Dexec.mainClass=org.acme.qualityops.metrics.Main -Dport=8092
mvn -q -f capstones/04-quality-operations/quality-gate-service/pom.xml    exec:java -Dexec.mainClass=org.acme.qualityops.gate.Main    -Dport=8093

# ingest a CI signal, then gate the commit against a policy
curl -s localhost:8091/events -d '{"id":"e1","project":"checkout","commit":"abc123","tool":"jacoco","timestamp":1,"metrics":{"coveragePercent":92,"violations":1,"bugs":0}}'
curl -s localhost:8093/gate   -d '{"project":"checkout","commit":"abc123","policy":{"minCoverage":80,"maxViolations":5,"maxBugs":0}}'
```

> The `JAVA_HOME` for the build is the pinned JDK from `SOURCE-PIN.md` (21 LTS anchor; forward-checked
> on 25). `exec:java` is shown for convenience; it is not wired into the build.
