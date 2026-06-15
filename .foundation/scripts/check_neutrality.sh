#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# =============================================================================
# check_neutrality.sh — Step 4a scripted pre-pass (the greppable first line of
# defence the holistic AUDIT read sits on top of). Scans ONE draft for:
#   1. NEUTRALITY banned comparative / superlative phrasings      -> FAIL
#   2. VOICE filler words (the book's reader-insulting/empty list) -> FLAG
#   3. em-dash density vs the configured per-1,000-word ceiling    -> FLAG
#   4. comparative / rival-subject names in headings              -> FLAG (review)
#
# The blocklist + filler list + rival-name list are book-specific data, so they
# live in the CONFIG block below — keep them in sync with NEUTRALITY.md and the
# VOICE guide. The scan logic is generic; only the word lists change per book.
#
# USED BY: ALL book types that carry a neutrality stance ({{NEUTRALITY_STANCE}}).
#          A book with no rival subjects leaves RIVAL_NAMES empty; the heading
#          and rival scans then degrade to the comparative-word check only.
#
# Usage: check_neutrality.sh <draft.md>
# Exit:  0 clean / 1 banned-phrase FAIL / 2 usage.
# =============================================================================
set -uo pipefail

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
# 1. BANNED — winner-crowning / cross-subject superlative phrasings. A match is a
#    FAIL (the neutrality floor). Extended-regex alternation, one '|'-joined list.
#    Generic comparatives belong here; "unlike <Rival>" / "beats <Rival>" style
#    constructs are caught generically via a leading-capital next word. Mirror
#    NEUTRALITY.md's banned list exactly.
BANNED='{{BANNED_PHRASES_REGEX}}'   # e.g. 'better than|unlike [A-Z]|the problem with|is superior|are superior|beats [A-Z]|cleaner than|more elegant than|faster than [A-Z]'
# 2. FILLER — VOICE empty/condescending words (advisory FLAG, never a FAIL).
#    Extended-regex alternation; use \< \> word boundaries to avoid substrings.
FILLER_REGEX='{{FILLER_REGEX}}'     # e.g. '(\<easy\>|\<easily\>|\<just\>|\<simply\>|\<obviously\>|of course|\<simple\>)'
FILLER_MAX={{FILLER_MAX}}           # e.g. 8   (over this many -> advisory FLAG)
# 3. EM-DASH ceiling per 1,000 words (the VOICE rhythm cap). Over it -> FLAG.
EMDASH_PER_1000={{EMDASH_PER_1000}} # e.g. 8
# 4. RIVAL_NAMES — other subjects/products/frameworks whose appearance in a
#    HEADING wants review (legal only inside an explicit comparison/migration
#    scope — your {{NEUTRALITY_STANCE}} buckets). Extended-regex alternation;
#    leave EMPTY for a book with no rival subjects (the heading scan then checks
#    only the generic comparative words below).
RIVAL_NAMES='{{RIVAL_NAMES_REGEX}}' # e.g. 'spring|micronaut|helidon|jakarta ee'
# Generic comparative words always flagged in a heading (book-independent):
HEADING_COMPARATIVES='vs\.?|versus|\<better\>|\<superior\>'
# =====================================================================

f="${1:-}"; [ -f "$f" ] || { echo "usage: check_neutrality.sh <draft.md>" >&2; exit 2; }
fail=0
echo "== check_neutrality: $f =="

# 1. Banned phrasings (NEUTRALITY floor) — a winner-crowning / cross-subject superlative is a FAIL.
hits="$(grep -niE "$BANNED" "$f" || true)"
if [ -n "$hits" ]; then
  echo "  FAIL — banned comparative/superlative phrasing (NEUTRALITY floor):"
  printf '%s\n' "$hits" | sed 's/^/     /'
  fail=1
else
  echo "  neutrality blocklist .......... clean"
fi

# 2. VOICE filler (advisory FLAG).
fn="$(grep -oiE "$FILLER_REGEX" "$f" 2>/dev/null | wc -l | tr -d ' ')"
note=""; [ "${fn:-0}" -gt "$FILLER_MAX" ] && note="  (FLAG: trim toward zero)"
echo "  filler words .................. ${fn}${note}"

# 3. em-dash density per 1,000 words (VOICE ceiling).
words="$(wc -w < "$f" | tr -d ' ')"; dash="$(grep -o '—' "$f" 2>/dev/null | wc -l | tr -d ' ')"
dens=0; [ "${words:-0}" -gt 0 ] && dens=$(( dash * 1000 / words ))
note=""; [ "$dens" -gt "$EMDASH_PER_1000" ] && note="  (FLAG: over the ~${EMDASH_PER_1000}/1000 ceiling)"
echo "  em-dash density .............. ${dens}/1000 (${dash} in ${words} words)${note}"

# 4. Comparative / rival names in headings (review — allowed only in an explicit comparison/migration scope).
hpat="$HEADING_COMPARATIVES"; [ -n "$RIVAL_NAMES" ] && hpat="$hpat|$RIVAL_NAMES"
hh="$(grep -nE '^#{1,6} ' "$f" 2>/dev/null | grep -iE "$hpat" || true)"
[ -n "$hh" ] && { echo "  headings to review (rival/comparative in a title — legal only in a comparison/migration chapter):"; printf '%s\n' "$hh" | sed 's/^/     /'; }

echo ""
if [ "$fail" = 0 ]; then echo "check_neutrality: PASS (blocklist clean; any FLAGs above are advisory for AUDIT)."
else echo "check_neutrality: FAIL — fix the banned phrasing before the AUDIT gate."; fi
exit "$fail"
