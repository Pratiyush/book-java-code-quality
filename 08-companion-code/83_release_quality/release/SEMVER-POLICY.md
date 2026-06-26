# Semantic-versioning policy

This project versions its releases with [semantic versioning](https://semver.org) (`MAJOR.MINOR.PATCH`),
the contract that tells consumers the nature of a change before they upgrade (key 60). This file is the
policy; the `org.acme.release.SemanticVersion` type enforces the one distinction the release gate needs
from it — a release version versus a pre-release.

## The contract

Given a version `MAJOR.MINOR.PATCH`, a change increments:

- **MAJOR** — when it makes a backward-incompatible (breaking) change to the public API.
- **MINOR** — when it adds functionality in a backward-compatible way.
- **PATCH** — when it makes a backward-compatible bug fix.

A breaking change demands a MAJOR bump; shipping one under a MINOR or PATCH is the broken promise the
contract exists to prevent. For a library or shared module, the bump is not a judgement call: an API-diff
tool (revapi / japicmp, key 60) computes the required increment from the actual API change and can fail
the build when the declared bump does not match — turning "did we break someone?" from hope into a gate.

## Pre-releases and snapshots

A version with a pre-release suffix — `2.5.0-rc.1`, or Maven's development `2.5.0-SNAPSHOT` — is **not** a
release. A `-SNAPSHOT` is a moving target that resolves to a different build over time, which is exactly
the irreproducibility a release must not have (Chapter 29). The release-readiness gate's `RELEASE_VERSION`
check rejects any pre-release, so a snapshot can never be tagged and shipped as a release.

## The honest edge

Semantic versioning is a discipline, not magic. The tools detect *signature* breaks, not *behavioural*
ones: a method that keeps its signature but changes what it computes passes an API-diff check and still
breaks consumers (key 60). That class of change is caught by tests, the changelog, and human review — the
same prevention layer the rest of the book is about — not by the version string. The version communicates
the change honestly; it does not make the change safe.
