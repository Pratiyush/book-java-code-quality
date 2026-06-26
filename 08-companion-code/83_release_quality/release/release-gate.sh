#!/usr/bin/env bash
# Release-readiness gate, in shell — the last check before a tag is cut (Chapter 36).
#
# This file is illustrative CONFIGURATION; it is not run by this module's Maven build. It is the same
# decision org.acme.release.ReleaseReadiness makes, expressed as a pipeline step so a release workflow
# can run it directly (ci/release.yml invokes the equivalent). It asserts the chapter's release
# preconditions and exits non-zero — a hard stop — if any required one fails, naming exactly which:
# a release version not a -SNAPSHOT (semver, key 60), a changelog entry for the version, every CI gate
# green on the release commit, the artifact signed with an SBOM (Part VII, key 66), and a smoke test
# passed against the staged build. A green run means the artifact is the traceable thing the pipeline
# produced — NOT that the code is correct (that is the gates and human review upstream).
#
# Inputs arrive as environment variables a real pipeline sets from its own systems (the CI run status,
# the signing step's result, the smoke job's exit). The Maven version literals a release uses
# (maven-release-plugin / versions-maven-plugin) are NOT separately pinned by SOURCE-PIN — see
# 09-flags/83_release_versioning_plugin_versions_unpinned.md.
set -euo pipefail

VERSION="${RELEASE_VERSION:?set RELEASE_VERSION, e.g. 2.4.0}"
CHANGELOG="${CHANGELOG_FILE:-release/CHANGELOG.md}"
CI_GREEN="${CI_GREEN:-false}"
SIGNED_WITH_SBOM="${SIGNED_WITH_SBOM:-false}"
SMOKE_TESTED="${SMOKE_TESTED:-false}"

failures=()

# tag::release-gate-sh[]
case "$VERSION" in
  *-SNAPSHOT|*-*) failures+=("RELEASE_VERSION: '$VERSION' is a pre-release, not a release") ;;
esac
grep -q "\[$VERSION\]" "$CHANGELOG" || failures+=("CHANGELOG_ENTRY: no entry for $VERSION in $CHANGELOG")
[ "$CI_GREEN" = "true" ]         || failures+=("CI_GREEN: pipeline is not green on the release commit")
[ "$SIGNED_WITH_SBOM" = "true" ] || failures+=("SIGNED_WITH_SBOM: artifact is not signed/attested")
[ "$SMOKE_TESTED" = "true" ]     || failures+=("SMOKE_TESTED: staged build has not passed a smoke test")
# end::release-gate-sh[]

if [ "${#failures[@]}" -gt 0 ]; then
  echo "release blocked — $VERSION is not ready:" >&2
  printf '  - %s\n' "${failures[@]}" >&2
  exit 1
fi
echo "release ready: $VERSION cleared every release gate."
