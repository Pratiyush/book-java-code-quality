#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# =============================================================================
# extract_snippet.sh — resolve one tag-include region from a companion-code file
# into a fenced Markdown code block.
# -----------------------------------------------------------------------------
# USED BY: TECHNICAL profile (and REFERENCE/cookbook when recipes are runnable).
#          Book types with {{GATES_OFF}}=example-build (science / business /
#          narrative) do not display compiled snippets and DROP this script —
#          see .foundation/BOOK-TYPE-PROFILES.md.
#
# The book manuscript is Markdown, which has no native AsciiDoc include directive.
# Companion-code files carry AsciiDoc-style line-comment markers:
#     // tag::name[]   ... region the book shows ...   // end::name[]
# (kept in AsciiDoc syntax for upstream consistency and forward-compat with
#  Asciidoctor). This script slices the region, strips marker lines, asserts the
# displayed region is <= MAX_LINES (the fair-use ceiling), and emits a fenced
# block whose language is inferred from the file extension.
#
# Usage:   extract_snippet.sh <NN_slug/relative/path/File.ext#tagname>
#   e.g.   extract_snippet.sh 01_hello/src/main/java/org/acme/Greeting.java#hello
# Resolves the path relative to CODE_ROOT. Prints the fenced block on stdout.
# Exits non-zero (with a diagnostic on stderr) on any failure.
set -euo pipefail

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
CODE_ROOT_REL="{{CODE_ROOT_REL}}"      # e.g. 08-companion-code (dir under repo root holding modules)
MAX_LINES="9"                          # fair-use snippet ceiling (GUIDELINES Rule 4 / LEGAL-IP)
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
CODE_ROOT="$REPO_ROOT/$CODE_ROOT_REL"

spec="${1:-}"
if [[ -z "$spec" || "$spec" != *"#"* ]]; then
  echo "extract_snippet: usage: extract_snippet.sh <NN_slug/path/File.ext#tag>" >&2
  exit 2
fi

rel="${spec%#*}"          # NN_slug/path/File.ext
tag="${spec##*#}"         # tag
file="$CODE_ROOT/$rel"

if [[ ! -f "$file" ]]; then
  echo "extract_snippet: backing file not found: $file" >&2
  exit 1
fi
if [[ ! "$tag" =~ ^[A-Za-z0-9_-]+$ ]]; then
  echo "extract_snippet: bad tag name '$tag' (allowed: A-Za-z0-9_-)" >&2
  exit 2
fi

# Slice the region between tag::TAG[] and end::TAG[], dropping any nested
# tag/end marker lines that fall inside it.
region="$(awk -v tag="$tag" '
  $0 ~ ("tag::" tag "\\[\\]")  { grab=1; next }
  $0 ~ ("end::" tag "\\[\\]")  { if (grab) { grab=0 }; next }
  grab && $0 !~ /(tag|end)::[A-Za-z0-9_-]+\[\]/ { print }
' "$file")"

if [[ -z "${region//[$'\n\t ']/}" ]]; then
  echo "extract_snippet: tag '$tag' not found (or empty) in $rel" >&2
  exit 1
fi

n=$(printf '%s\n' "$region" | wc -l | tr -d ' ')
if (( n > MAX_LINES )); then
  echo "extract_snippet: region '$tag' is $n lines (> $MAX_LINES-line ceiling) in $rel" >&2
  exit 1
fi

case "$rel" in
  *.java) lang=java ;;
  *.kt)   lang=kotlin ;;
  *.xml)  lang=xml ;;
  *.properties) lang=properties ;;
  *.yaml|*.yml) lang=yaml ;;
  *.json) lang=json ;;
  *.sh)   lang=bash ;;
  *.sql)  lang=sql ;;
  *)      lang="" ;;
esac

printf '```%s\n%s\n```\n' "$lang" "$region"
