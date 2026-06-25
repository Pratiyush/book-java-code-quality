#!/usr/bin/env bash
# validate.sh — the pre-push validation suite (the spec's "Validation commands before commit/push"),
# adapted to this GitHub repo. Runs each gate, prints a summary, and exits non-zero if a HARD check
# fails. Python/bash checks are unconditional; the Maven quality build is opt-in (slow) and CI also
# runs it. Usage:  bash .claude/scripts/validate.sh [--with-maven]
set -uo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT"
WITH_MAVEN=0; [[ "${1:-}" == "--with-maven" ]] && WITH_MAVEN=1
fail=0
have() { command -v "$1" >/dev/null 2>&1; }
run() {  # run <name> <hard:0|1> <command...>
  local name="$1" hard="$2"; shift 2
  if "$@" >/tmp/validate.$$ 2>&1; then
    echo "  ✅ $name"
  elif [[ "$hard" == 1 ]]; then
    echo "  ❌ $name (HARD) — see output:"; sed 's/^/       /' /tmp/validate.$$ | tail -8; fail=1
  else
    echo "  ⚠️  $name (soft)"
  fi
  rm -f /tmp/validate.$$
}

echo "validate: running the pre-push suite (root: $ROOT)"
run "source-pin check"   0 bash .claude/scripts/ensure_source_pin.sh check
run "status drift"       1 python3 .claude/scripts/status.py --check-only
run "snippets (04-approved)" 1 bash .claude/scripts/check_snippets.sh 04-approved
if [[ -d 06-assembly && -n "$(ls -A 06-assembly 2>/dev/null)" ]]; then
  run "crossrefs (06-assembly)" 1 bash .claude/scripts/check_crossrefs.sh 06-assembly
else
  echo "  ⏭  crossrefs (skip: 06-assembly empty)"
fi
run "figures (20-param)" 1 python3 .claude/scripts/review_figures.py --check-only
run "git whitespace"     1 git diff --check
if have ruby; then run "gitlab-ci.yml yaml" 1 ruby -e "require 'yaml'; YAML.load_file('.gitlab-ci.yml')"; else echo "  ⏭  yaml lint (skip: no ruby)"; fi

if [[ "$WITH_MAVEN" == 1 ]]; then
  JH="$(/usr/libexec/java_home -v 21 2>/dev/null || true)"
  if [[ -n "$JH" ]]; then
    run "mvn companion-code" 1 env JAVA_HOME="$JH" mvn -B -q -Pquality -f 08-companion-code/pom.xml verify
    run "mvn capstones"      1 env JAVA_HOME="$JH" mvn -B -q -Pquality -f 08-companion-code/capstones/pom.xml verify
  else
    echo "  ⏭  maven (skip: no JDK 21 via /usr/libexec/java_home)"
  fi
else
  echo "  ⏭  maven (skip: pass --with-maven to run the quality build)"
fi

echo "────"
if [[ "$fail" == 0 ]]; then echo "validate: ✅ all HARD checks pass"; exit 0
else echo "validate: ❌ a HARD check failed (fix before PR)"; exit 1; fi
