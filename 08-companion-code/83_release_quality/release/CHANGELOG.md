# Changelog

All notable changes to this project are recorded here, in the [Keep a Changelog](https://keepachangelog.com)
convention (grouped by type — Added / Changed / Fixed / Removed / Security) and ordered newest first.
Versions follow semantic versioning (`MAJOR.MINOR.PATCH`, semver.org): a breaking change bumps MAJOR, an
additive one MINOR, a fix PATCH. The `[Unreleased]` section accrues entries as work merges; cutting a
release renames it to the new version with the release date.

This file is one of the release preconditions the release-readiness gate checks (`org.acme.release`): the
gate's `CHANGELOG_ENTRY` check is satisfied only when the version being released has an entry here, so a
release can never ship without its changes written down for the people who consume it.

<!-- An illustrative changelog entry in the Keep a Changelog convention. The version heading carries the
     release date; changes are grouped by type so a reader scans for what concerns them. -->
## [Unreleased]

### Added
- Nothing yet.

<!-- tag::changelog-entry[] -->
## [2.4.0] - 2026-06-27
### Added
- Canary rollout for the checkout service, gated behind the `checkout-v2` feature flag.
### Fixed
- Order totals no longer round half-down on multi-currency carts.
### Security
- Upgraded the JSON parser past a disclosed CVE (advisory id in the release notes).
<!-- end::changelog-entry[] -->

## [2.3.1] - 2026-05-30

### Fixed
- Health endpoint reported ready before the connection pool had warmed.
