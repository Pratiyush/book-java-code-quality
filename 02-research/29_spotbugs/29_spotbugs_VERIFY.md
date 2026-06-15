# GATE REPORT — SOURCE-VERIFY (key 29, SpotBugs + FindSecBugs + fb-contrib)

- **Gate:** VERIFY (step-2 SOURCE-VERIFY, pre-pin)
- **Artifact:** `02-research/29_spotbugs/29_spotbugs_RESEARCH.md`
- **Date:** 2026-06-15
- **Verdict:** **PASS_WITH_FLAGS** · **Blockers: 0**

## Scripts run vs manual
- `check_source_pin.sh` — **RAN → FAIL by construction.** Pin is multi-authority; clone ABSENT at the
  ephemeral job dir and **unhealable** ({URL} placeholder, all §2 rows `TO-PIN`, `/pin-source` never run).
- `verify_sources.sh` — RAN → FAIL ("pinned clone absent"). Cannot trace atoms against a pin that does not exist.
- `check_neutrality.sh` — RAN → **PASS** (blocklist clean; advisory: 2 filler words, em-dash 21/1000 → CLARITY/AUDIT, not VERIFY).
- `lint_citations.sh` / `check_snippets.sh` — n/a at research step (no draft, no `<!-- include -->` markers).
- **Posture:** identical to keys 20/22/23/25/30 — at pre-pin this is a **flag-discipline audit**, NOT atom
  byte-verification. A PASS here means "the right things are flagged," not "atoms verified." Re-trace every
  version/GAV/count/default atom after `/pin-source`.

## Checked claims / issues
| # | Claim / check | Location | Result |
|---|---|---|---|
| 1 | FindBugs treated as dead / lineage only, never current | §1, §5, §8, §9, Learnings | PASS — successor framing, `findbugs-maven-plugin`→`spotbugs-maven-plugin`, `edu.umd.cs.findbugs.*` = retained lineage |
| 2 | Neutrality blocklist (better than / unlike X / superior / beats …) | whole file | PASS — none present (script + grep) |
| 3 | Cross-tool verdict routed, not asserted | §1/§4 + 9× "key 37", §1 "key 70" | PASS — Checkstyle/PMD/Error Prone/Sonar named as approaches, each cited to own source; layering verdict → 37, SAST → 70 |
| 4 | Plugins framed as capabilities (Bucket i), not rivals | §1, §4, Learnings | PASS — FindSecBugs/fb-contrib = detectors into one engine |
| 5 | HONEST-LIMITATIONS floor (hardest objection + when-NOT-to-use) | §4 | PASS — SpotBugs (heuristic/bytecode-distance FPs), FindSecBugs (pattern not taint), fb-contrib (noise), shared static-analysis limits, all with when-NOT |
| 6 | Java 11+ "experimental" label carried verbatim, not upgraded | §1, §4, §5, §7 | PASS — not asserted as "fully supports 21/25"; Java 21/25 bytecode flagged `⚠ verify at pin` |
| 7 | All versions/GAVs marked `⚠ verify at pin` (27 markers) | §0 header, §2.5 table, §5, §6, §7 | PASS — live-line versions never asserted as pinned fact |
| 8 | Folklore guard — no banned figures stated as fact | whole file | PASS — no 1:10:100, MI-as-score, coverage-as-quality; "necessary not sufficient" stated correctly (§4) |
| 9 | Flag file present & accurate | `09-flags/29_spotbugs_versions_and_defaults_unverified.md` | PASS — covers versions, drift, pattern count, defaults, JDK-support |
| F1 | **`☑ verbatim` glyphs + `✅` in §2.5/§8 read as pin-verified** | §2.5 table, §8 Primary table | FLAG (recurring keys 07/10/11/13/15/19/25/30) — §8 header honestly says "@ the live line"; demote `☑`/`✅` to "live-line, verify at pin" |
| F2 | **`144 patterns` (quoted "verbatim") vs `149 distinct pattern codes` (own scan log)** | §2.3/§3/§7 (144) vs §9 line 506 (149) | FLAG — quote-vs-own-count drift; reconcile at draft which is the page's stated headline (sibling of key-19/25 quote-drift) |
| F3 | **Doc URLs use `en/latest/` (moving target)** | §0, §1, §8 (14×) | FLAG — readthedocs `/latest/` drifts; pin the versioned URL at `/pin-source` (recurring readthedocs class) |
| F4 | Effort `min` "Conserve Space"/"Skip Huge Methods >6000" + `≥512 MB` | §2.1, §4 | FLAG (minor) — version-sensitive numeric atoms; re-confirm verbatim at pin |
| F5 | FindSecBugs "826 unique API signatures" | §1, §3, §7 | FLAG (minor) — count moves per release; re-confirm at pinned plugin version (already in §7/flag) |

## Blockers
- None.

## VERIFY gate-specific checks
- [x] Pin confirmed first (ABSENT/unhealable → flag-discipline audit, atom verification deferred to `/pin-source`)
- [x] No invented or unflagged atom (every version/GAV/count/default carries `⚠ verify at pin`)
- [x] No folklore-as-fact; FindBugs-as-current guard clean
- [x] No off-pin/SNAPSHOT/main/master citation; no version newer than a (nonexistent) pin asserted
- [x] NEUTRALITY: no crowning, blocklist clean, cross-tool claims cited + routed to 37/70
- [x] HONEST-LIMITATIONS floor met per tool + shared centre
- [x] Synthesized/comparative claims supported (bytecode-sees-more; necessary-not-sufficient) and routed, not crowned
- [ ] Atom byte-verification — DEFERRED to post-`/pin-source` (cannot run pre-pin)

## Required fixes (carry to draft; none blocking)
1. Reconcile **144 vs 149** FindSecBugs pattern figures — quote only the page's stated headline; mark the other as observed-code-count (F2).
2. Demote `☑`/`✅`/"verbatim" glyphs to "live-line, verify at pin" until `/pin-source` (F1).
3. Pin the versioned doc URL (not `en/latest/`) at `/pin-source` (F3).
4. Re-confirm effort/memory numeric atoms + 826-signatures count verbatim at the pinned version (F4/F5).
5. Re-trace ALL version/GAV/default atoms after `/pin-source` (per flag file).

## Learnings & pipeline suggestions
- **New trap — quote-vs-own-observed-count drift:** the dossier quotes "144 patterns" as verbatim while its
  own scan log counted "149 distinct codes" on the same page. Extends the key-19/25 same-quote-drift lint to
  *quote-vs-self-observation*: a figure quoted "verbatim" must equal what the dossier's own scan recorded, or
  be demoted to an observation, not a quote.
- **readthedocs `/latest/` is a moving-target URL class** (SpotBugs, and likely PMD key 28). Propose a
  `/pin-source` rule: replace `en/latest/` with the pinned `en/<version>/` URL during pinning.
- Reconfirms the pre-pin caveat (keys 12/20/22/23/25/30): a SOURCE-VERIFY PASS_WITH_FLAGS = "flagged the right
  things," not "atoms verified." Reserve `☑`/"@the pin" for post-pin.
