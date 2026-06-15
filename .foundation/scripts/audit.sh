#!/usr/bin/env bash
# .foundation kernel script — configure the CONFIG block below per book.
# =============================================================================
# audit.sh — the pipeline audit. Run it any time to answer three questions:
#   1. DRIFT     — is every step followed, with PIPELINE <-> tracker <-> agents
#                  consistent? (delegates to check_process.sh)
#   2. NEXT STEP — for each chapter, what is the next pipeline step? (advisor)
#   3. EVIDENCE  — does every gate the tracker calls "passed" have its report
#                  artifact on disk, and is every action logged?
#
# It reconstructs state from the AUTHORITATIVE artifacts (the chapter tracker +
# the on-disk gate reports), so it cannot be fooled by a missing log line; the
# JSONL activity log is the richer provenance layer on top.
#
# USED BY: ALL book types. Process/state integrity is genre-independent. A
#          non-code book turns the toolchain-gated steps OFF (see the EXAMPLE_*
#          config below) and trims the gate maps to the columns its board has.
#
# Owner: production-manager (PROCESS-CHECK step). Exit: 0 clean /
# 1 drift or missing evidence / 2 missing inputs.
# =============================================================================
set -euo pipefail

# ===== CONFIGURE PER BOOK (see .foundation/BOOK-TYPE-PROFILES.md) =====
TRACKER_REL="{{TRACKER_REL}}"        # e.g. 01-index/CHAPTER-TRACKER.md
DRAFTS_REL="{{DRAFTS_DIR_REL}}"      # e.g. 03-drafts
LOG_REL="{{LOG_REL}}"                # e.g. 10-logs/activity.jsonl
PROC_REL="{{PROC_REL}}"              # e.g. .claude/scripts/check_process.sh (the drift guard)
# Per-chapter gate columns in PIPELINE order, used by the NEXT-STEP advisor. The
# advisor walks these left-to-right and names the first gate not yet passed.
# Keep ONLY the columns this book's board has (a non-code book drops example/
# figure; see {{GATES_OFF}}). Space-delimited gate names, board order:
NEXTSTEP_GATES="{{NEXTSTEP_GATES}}"  # e.g. "research verify draft example clarity audit score figure reconcile approve"
# Field index (1-based, splitting the row on '|') of each gate column above, in
# the SAME order. A leading '|' makes f[1] empty, so the first real cell is f[2].
# e.g. board "| Ch | NN | Topic | research | verify | draft | example | clarity |
# audit | score | figure | reconcile | approve |" -> Ch=2 NN=3 Topic=4, gates 5..14:
NEXTSTEP_FIELDS="{{NEXTSTEP_FIELDS}}"  # e.g. "5 6 7 8 9 10 11 12 13 14"
# Field index of the Ch (number) and NN (key) columns, for the advisor's label:
CH_FIELD="{{CH_FIELD}}"              # e.g. 2
NN_FIELD="{{NN_FIELD}}"              # e.g. 3
TOPIC_FIELD="{{TOPIC_FIELD}}"        # e.g. 4
# EVIDENCE map — for each gate whose tracker cell says "passed", the on-disk
# report it MUST have produced. Space-delimited "gate:field:suffix" tokens:
# gate name (for the message), the row field index of that gate's column, and
# the report-file suffix under <DRAFTS>/<slug>/<slug><suffix>.md. Trim tokens
# for any gate this book's board doesn't carry.
EVIDENCE_MAP="{{EVIDENCE_MAP}}"      # e.g. "verify:5:_VERIFY example:8:_EXAMPLE clarity:9:_CLARITY audit:10:_AUDIT score:11:_SCORE reconcile:13:_RECONCILE"
# Slug to skip under <DRAFTS> (the bulk/raw staging dir, not a real chapter):
RAW_SLUG="{{RAW_SLUG}}"              # e.g. _draft-raw
# Toolchain-gated steps (TECHNICAL profile only). When this book has a runnable
# example gate, set EXAMPLE_GATE to that gate's name and TOOLCHAIN_PROBE to a
# command whose success means the build toolchain is present; the advisor then
# flags that gate as BLOCKED when the probe fails. For a non-code book leave
# EXAMPLE_GATE empty — the toolchain check is skipped entirely.
EXAMPLE_GATE="{{EXAMPLE_GATE}}"      # e.g. example   (empty for a non-code book)
TOOLCHAIN_NAME="{{TOOLCHAIN_NAME}}"  # e.g. "JDK 17+/21"  (only used in the message)
# A shell snippet, eval'd, that exits 0 iff the toolchain is present. Code book
# example: a JDK probe. Leave as 'true' (always present) for a non-code book.
TOOLCHAIN_PROBE='{{TOOLCHAIN_PROBE}}'  # e.g. command -v java >/dev/null 2>&1 && java -version >/dev/null 2>&1
# Optional parenthetical the advisor appends to a few well-known gates so the
# next action is unambiguous. Leave any blank to print just the gate name.
APPROVE_GATE="{{APPROVE_GATE}}"      # e.g. approve   (the human bottleneck gate)
APPROVE_NOTE="{{APPROVE_NOTE}}"      # e.g. "human approval (the bottleneck)"
RESEARCH_GATE="{{RESEARCH_GATE}}"    # e.g. research
RESEARCH_NOTE="{{RESEARCH_NOTE}}"    # e.g. "dossier not yet banked/traced"
EXAMPLE_NOTE="{{EXAMPLE_NOTE}}"      # e.g. "build the runnable companion + repro"
# =====================================================================

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$REPO_ROOT"
TRACKER="$TRACKER_REL"
DRAFTS="$DRAFTS_REL"
LOG="$LOG_REL"
PROC="$PROC_REL"
[ -f "$TRACKER" ] || { echo "audit: missing $TRACKER" >&2; exit 2; }
rc=0

# ---------- 1/3 DRIFT (delegate to check_process.sh) -------------------------
echo "== AUDIT 1/3 — process drift =="
if [ -x "$PROC" ] || [ -f "$PROC" ]; then
  if bash "$PROC"; then echo "  drift: none"; else echo "  drift: FOUND (see check_process output above)"; rc=1; fi
else
  echo "  $PROC not found — cannot verify drift" >&2; rc=1
fi
echo ""

# ---------- 2/3 NEXT-STEP advisor (per chapter, from the tracker) ------------
echo "== AUDIT 2/3 — next step per chapter =="
HAS_TOOLCHAIN=yes
if [ -n "$EXAMPLE_GATE" ]; then
  if eval "$TOOLCHAIN_PROBE" >/dev/null 2>&1; then HAS_TOOLCHAIN=yes; else HAS_TOOLCHAIN=no; fi
  [ "$HAS_TOOLCHAIN" = no ] && echo "  (no local $TOOLCHAIN_NAME — the '$EXAMPLE_GATE' gate and any toolchain-gated steps are blocked)"
fi

awk -v has_tc="$HAS_TOOLCHAIN" \
    -v gates_s="$NEXTSTEP_GATES" -v fields_s="$NEXTSTEP_FIELDS" \
    -v ch_f="$CH_FIELD" -v nn_f="$NN_FIELD" -v topic_f="$TOPIC_FIELD" \
    -v ex_gate="$EXAMPLE_GATE" -v ex_note="$EXAMPLE_NOTE" -v tc_name="$TOOLCHAIN_NAME" \
    -v appr_gate="$APPROVE_GATE" -v appr_note="$APPROVE_NOTE" \
    -v res_gate="$RESEARCH_GATE" -v res_note="$RESEARCH_NOTE" '
  function passed(c){ return (c ~ /done|SHIP|green|CR|PWF|PASS|approved/ || c ~ /[0-9]+\/[0-9]+/) }
  function notapp(c){ return (c ~ /n-a/) }
  BEGIN{ n=split(gates_s,g," "); split(fields_s,fc," "); maxf=ch_f
         for(i=1;i<=n;i++) if(fc[i]+0>maxf) maxf=fc[i] }
  /^\| *[0-9]+ *\|/ {
     m=split($0,f,"|"); if(m<maxf) next;
     ch=f[ch_f]; nn=f[nn_f]; topic=f[topic_f];
     gsub(/^ +| +$/,"",ch); gsub(/^ +| +$/,"",nn); gsub(/^ +| +$/,"",topic);
     nxt="(all gates passed)"; blk="";
     for(i=1;i<=n;i++){ v=f[fc[i]]; gsub(/^ +| +$/,"",v);
        if(notapp(v)) continue;
        if(!passed(v)){ nxt=g[i]; break } }
     if(ex_gate!="" && nxt==ex_gate && has_tc=="no") blk=" [BLOCKED: install " tc_name ", then run the " ex_gate " gate]";
     else if(ex_gate!="" && nxt==ex_gate && ex_note!="") blk=" (" ex_note ")";
     else if(appr_gate!="" && nxt==appr_gate) { if(appr_note!=""){ nxt=appr_note } blk=" (the bottleneck)" ; if(appr_note=="") blk=" (human approval — the bottleneck)" }
     else if(res_gate!="" && nxt==res_gate && res_note!="") blk=" (" res_note ")";
     printf "  Ch %-3s (%-3s) %-40s -> NEXT: %s%s\n", ch, nn, substr(topic,1,40), nxt, blk;
  }
' "$TRACKER"
echo ""

# ---------- 3/3 EVIDENCE — tracker "passed" must have its report on disk -----
echo "== AUDIT 3/3 — evidence (active chapters) + activity log =="
miss=0
for d in "$DRAFTS"/*/; do
  [ -d "$d" ] || continue
  slug="$(basename "$d")"; [ -n "$RAW_SLUG" ] && [ "$slug" = "$RAW_SLUG" ] && continue
  nn="${slug%%_*}"
  # the tracker row for this NN (first data row whose key field == nn)
  row="$(awk -v k="$nn" -v kf="$NN_FIELD" -F'|' '/^\| *[0-9]+ *\|/{g=$kf; gsub(/^ +| +$/,"",g); if(g==k){print; exit}}' "$TRACKER")"
  [ -z "$row" ] && { echo "  ! $slug: no chapter-tracker row for key $nn"; miss=1; continue; }
  for tok in $EVIDENCE_MAP; do
    gate="${tok%%:*}"; rest="${tok#*:}"; ci="${rest%%:*}"; suf="${rest##*:}"
    val="$(printf '%s' "$row" | awk -F'|' -v c="$ci" '{v=$c; gsub(/^ +| +$/,"",v); print v}')"
    case "$val" in *done*|*SHIP*|*PWF*|*PASS*|*green*|*/50*)
        if ! ls "$d"${slug}${suf}.md >/dev/null 2>&1; then
           echo "  ! $slug: tracker says $gate passed ('$val') but ${slug}${suf}.md is MISSING"; miss=1; fi;;
    esac
  done
done
[ "$miss" = 0 ] && echo "  evidence: every 'passed' gate on an active chapter has its report on disk."
[ "$miss" != 0 ] && rc=1

if [ -f "$LOG" ]; then
  echo "  activity log: $(wc -l < "$LOG" | tr -d ' ') entries in $LOG (richer provenance layer)."
else
  echo "  activity log: $LOG not present yet — actions are reconstructed from artifacts; create it via log_action.sh for finer provenance."
fi
echo ""

# ---------- verdict ----------
if [ "$rc" = 0 ]; then echo "AUDIT PASS — no drift; evidence complete; next steps above."
else echo "AUDIT FAIL — drift or missing evidence above; resolve before the next batch."; fi
exit $rc
