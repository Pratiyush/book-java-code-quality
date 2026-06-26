<!--
  The counter-metric dashboard note the chapter's companion spec calls for. The chapter's metrics
  discipline (Chapter 1 / 38): never report an AI-productivity figure without its risk counter-metric. The
  executable half is org.acme.aigovernance.AiAdoptionCounterMetric, which refuses to return a healthy
  verdict on velocity alone. This note describes the dashboard a team wires it into; it carries NO baked-in
  statistic, because every AI-productivity and AI-review figure is a dated, attributed, often vendor-sourced
  snapshot, verified at the pin and treated as a snapshot — never a constant to reason from.
-->

# Counter-metric dashboard note

A dashboard that reports AI adoption and delivery speed *without* the risk counter-metric measures exactly
half the picture — the seductive half — and Goodhart does the rest. This note records the pairing.

## The pairing

| Velocity signal (the seductive half) | Its required counter-metric (Chapter 38, DORA) |
|---|---|
| AI-assisted share of changes (adoption %) | Change-failure rate, vs the pre-adoption baseline |
| Delivery speed / lead time | Change-failure rate and time-to-restore |
| AI-review comments posted | AI-review comments *dispositioned as real* vs false positives |

The rule the code enforces: adoption is only **healthy** when it has **not** come with a rise in the
change-failure rate over the pre-adoption baseline. `AiAdoptionCounterMetric.verdict()` returns
`RISK_RISING` whenever the failure rate climbs with adoption, and never returns `HEALTHY` for velocity
alone.

## What this note deliberately does not contain

No specific percentage. The chapter's cited figures (the productivity and risk shares, the AI-review catch
rates) are dated, attributed snapshots, often vendor-sourced; a team trends its **own** measured numbers
here, not a figure copied from a vendor's deck. Pulling a survey number into a live dashboard as a target
is the vanity-metric trap this whole chapter warns against.
