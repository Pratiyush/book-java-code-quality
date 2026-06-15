---
description: "Pipeline step 0 — re-fetch the Java code quality authority source at the pinned reference and refresh SOURCE-PIN.md. Hard gate; must pass before any verification reads."
---

# /pin-source — Step 0: pin the authority source

You are the conductor for **pipeline step 0**. Nothing downstream (research, verify, draft) may read a Java code quality fact until this step passes. The pin in `SOURCE-PIN.md` — not the copy on disk — is the source of truth.

> **Self-heal entry:** `scripts/ensure_source_pin.sh` is the one-shot self-heal — it re-fetches if the local copy is absent or off-pin and then verifies the pin in the same run. Prefer it. `check_source_pin.sh` is the verify-only check it calls. If a script is not present or not executable, run this step as the **manual procedure** below and say so in your report. Do not claim a script ran if it did not.

## The pin (read, never assume)
Read `SOURCE-PIN.md` and use the values there. The pin is `the pins in SOURCE-PIN.md` for `the pinned authority set (00-strategy/SOURCE-PIN.md)`, fetched to `the per-tool fetch dirs in SOURCE-PIN.md`.

## What to do

1. **Check the local-copy state.**
   - If `scripts/check_source_pin.sh` exists and is executable, run it and report its verdict.
   - Otherwise, manually: confirm `the per-tool fetch dirs in SOURCE-PIN.md` exists and that it matches the pin `the pins in SOURCE-PIN.md` exactly (e.g. for a code source, `git -C the per-tool fetch dirs in SOURCE-PIN.md rev-parse HEAD` prints the pinned SHA; for a literature/corpus source, the fetched snapshot matches the recorded edition/access date).

2. **If absent or off-pin, re-fetch AND re-verify as ONE non-skippable step.** Prefer the self-heal entry, which does both atomically:
   ```sh
   scripts/ensure_source_pin.sh
   ```
   If running manually, the re-fetch is not done until the pin is re-confirmed — the two are bound, never separate. A re-fetch whose verification does not equal `the pins in SOURCE-PIN.md` is a hard fail (step 4), not a pass. *(technical profile — see BOOK-TYPE-PROFILES.md; the local clone may live in ephemeral storage and vanish on reboot — re-fetching is routine, not an error.)*

3. **Refresh `SOURCE-PIN.md`** only if the pin itself changed (a deliberate, logged re-pin event — see the re-evaluation trigger in that file). A routine re-fetch of the same pin does **not** change the file; just confirm its values still match disk.

4. **Hard-fail and block** if the local copy cannot be brought on-pin. A wrong-pin copy is worse than none — it silently feeds drift into every chapter. Stop the pipeline and flag to `09-flags/`.

## Gate
- **PASS** only when `the per-tool fetch dirs in SOURCE-PIN.md` is present and its verification equals `the pins in SOURCE-PIN.md`.
- A pin change (tag/SHA/edition/date) requires a human to re-confirm `FINAL_INDEX.md` and triggers re-verification of every previously banked fact.

## Report
State: copy present? on-pin? did you re-fetch? script-run or manual? Close with **"Learnings & pipeline suggestions"** and have the maintainer log the run.
