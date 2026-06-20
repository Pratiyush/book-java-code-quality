<!--
Dossier key: 65 (owner, leads) + folds 66 — per 01-index/FINAL_INDEX.md Ch 28
Slug: 65_dependency_scanning_sbom_supply_chain (owner key 65)
Part / arc position: Part VII — Build, Dependencies & Supply Chain, Chapter 28 (Part VII = Ch 27-29)
Companion module: 08-companion-code/ (OWASP Dependency-Check failing on a seeded vulnerable dep + reviewed suppression; CycloneDX SBOM generated on verify) — ⚠ EXAMPLE-BUILD = PENDING (toolchain READY: JDK 21.0.11+25.0.3; DB download/network needed → REPRO caveat). Spec at foot.
Verified against SOURCE-PIN: 2026-06-20. Sources (concise main-loop dossiers; ⚠ SCA multi-tool + SPDX/CycloneDX crown neither, each cited to own docs):
- SCA / vuln scanning (65, ⚠): Software Composition Analysis = "do my dependencies contain KNOWN vulnerabilities?" — distinct from SAST (your code, Part VIII). Deps are most of an app (Ch 27); Log4Shell-class incidents live in TRANSITIVE deps → core security gate. Mechanism: inventory deps (direct+transitive) → match vs vuln DBs (NVD CVEs, OSV open-source-focused, GitHub Advisory) → findings (CVE id, CVSS severity, affected/fixed versions). Tools (each its case, crown none): OWASP Dependency-Check (OSS Maven/Gradle plugin; CPE matching vs NVD; fail build on CVSS threshold; known FPs from fuzzy CPE), OWASP Dependency-Track (server; consumes CycloneDX SBOMs; continuous monitoring NVD/OSV), Grype (Anchore; fast SBOM/image; pairs w/ Syft), Trivy (Aqua; broad — deps/images/IaC/secrets; gen SBOMs), Snyk (commercial; dev-focused fix advice; reachability some tiers). Where: build/CI gate (fail on severity) + continuous monitoring (CVEs disclosed AFTER ship — Track) + update bots (Ch 27 key 64) raise fix PRs. Suppression: reviewed suppression file w/ justification (key 39 discipline). LIMITS: false positives (CPE-based fuzzy matching; unmanaged → gate ignored; needs suppression process); "vulnerable" ≠ "exploitable" (CVE in a dep your code never calls; reachability some commercial tiers; don't treat every finding as a fire); DB lag/coverage (CVE not yet in DB invisible; OSV vs NVD differ → some run two); ONLY known vulns (zero-days + your own bugs out of scope → SAST key 70 + secure coding key 69); tool choice contextual.
- Supply chain / SBOM (66, ⚠ SPDX/CycloneDX): supply-chain attacks (compromised deps, build tampering) target what you SHIP not what you write. Defenses: SBOM (Software Bill of Materials = complete component inventory → "am I affected?" in minutes); provenance/attestation (prove how artifact built); SLSA (build-integrity maturity framework). SBOM formats (⚠ both legit): CycloneDX (OWASP; security-focused — components/services/vulns/licenses) / SPDX (Linux Foundation; ISO/IEC 5962:2021; broad licensing/provenance). Generate from build: Maven/Gradle CycloneDX plugins, or Syft (outputs both). Consume/monitor: OWASP Dependency-Track. Provenance: in-toto attestation (build inputs/steps); Sigstore cosign (signs artifacts/attestations); GitHub Actions OIDC (keyless signing identity); verify at deploy. SLSA (Supply-chain Levels for Software Artifacts): tiered (hardened build platform, provenance generation, non-falsifiable attestations) — roadmap not a tool. Where: build → SBOM → scan (key 65) → sign + attest → publish SBOM w/ release (key 83) → continuous re-scan. Compliance: US EO 14028, EU Cyber Resilience Act make SBOMs mandatory in sectors. LIMITS: SBOM = inventory NOT defense (enables response, fixes nothing; generated-and-ignored = theatre); accuracy gaps (misses dynamically-loaded/shaded/vendored; "adherence gap"); SLSA higher levels costly (hardened hermetic builds + attestation infra; most start low); format choice (CycloneDX security vs SPDX licensing/ISO; some need both, crown neither); signing key/identity mgmt own burden.
⚠ verify-at-pin: tool versions/plugin GAVs (Dependency-Check/Track/Grype/Trivy/Snyk/CycloneDX-plugin/Syft/cosign); CVSS-threshold + suppression-file format; NVD/OSV/GitHub-Advisory roles + CPE-matching wording; SPDX=ISO/IEC 5962:2021 + CycloneDX spec/ECMA status; SLSA current levels/version; in-toto/cosign specifics; EO 14028 / EU CRA SBOM-requirement specifics. REPRO: DB download/network needed for the scan → REPRO PENDING-RUNTIME where offline.
Routes: dependency hygiene/pinning/currency → Ch 27 (63/64); SAST (your code) → Part VIII (70); secure coding → Part VIII (69); reproducible builds → Ch 29 (67); license compliance (SBOM also inventories licenses) → Ch 29 (68); security gate → key 73; release/publish SBOM → key 83; suppression discipline → Ch 19 (39). SOURCE-PIN: tool §-rows TO-PIN; SPDX/SLSA/EO-14028/EU-CRA standards-edition discipline.
DRAFT v1 — gates manual; SCA-vs-SAST-distinction + three-questions-about-other-peoples-code (known-vulnerable?/what's-in-it?/can-I-prove-it?) + multi-tool-crown-none + two-SBOM-formats-crown-none + inventory-not-defense + vulnerable≠exploitable shapes; Log4Shell hook. EXAMPLE-BUILD pending.
-->

# Knowing What You Ship

*Scanning dependencies for known vulnerabilities, inventorying what you ship with an SBOM, and proving how it was built · 65 (folds 66) · Part VII*

> When the next Log4Shell drops, the question isn't "how do we fix it?" — that's a version bump. It's "where are we even using it?" Most teams can't answer for days.

## Hook

December 2021: a remote-code-execution flaw in Log4j — Log4Shell — became the most urgent security incident in a decade. The fix was trivial: bump the version. The hard part, the part that consumed weeks across the industry, was a different question entirely: *where are we using it?* Log4j was rarely a direct dependency; it sat four levels deep in the transitive tree of frameworks teams had pinned and forgotten. Organizations with hundreds of services grepped build files by hand, service by service, trying to assemble an inventory they should have had all along. The teams that answered in minutes instead of weeks had two things: a scanner that already flagged the vulnerable Log4j, and a **bill of materials** that listed every component in every service, transitive included.

That gap is the subject of this chapter, the security half of Part VII. The last chapter made the dependency tree *deterministic* — pinned, converged, current. This chapter makes it *securable*, and it does so by answering three distinct questions about the code you didn't write. **Is anything in it known to be vulnerable?** — that's **software composition analysis** (SCA), scanning your dependencies against vulnerability databases. **Do I even know what's in it?** — that's the **SBOM**, a complete component inventory that turns "am I affected?" into a query. **Can I trust how it, and my own build, was assembled?** — that's **provenance and SLSA**, attesting the integrity of the supply chain itself. The three build on each other: you can't scan what you haven't inventoried, and an inventory you can't trust is theatre. And all of it rests on the deterministic tree of the last chapter — you cannot meaningfully scan or inventory a graph that resolves differently on every build.

## Overview

**What this chapter covers**

- **Software composition analysis (SCA)**: matching dependencies against vulnerability databases (OWASP Dependency-Check, Dependency-Track, Grype, Trivy, Snyk), as a build gate and continuous monitor.
- The honest limits: false positives, *vulnerable* versus *exploitable*, and known-only coverage.
- **SBOMs** (CycloneDX, SPDX): inventorying what you ship, and why it's the Log4Shell answer.
- **Provenance and SLSA**: attesting how an artifact was built, and the compliance drivers making this mandatory.

**What this chapter does NOT cover.** Dependency pinning and currency (the previous chapter — the precondition). SAST — analyzing *your own* code for vulnerabilities — and secure coding (Part VIII). Reproducible builds and license compliance, the next chapter (the SBOM also inventories licenses; reproducibility is what makes provenance trustworthy). The security *gate* policy and release/publish mechanics (later). SCA tools and the two SBOM formats are presented **neutrally, crowning none**; each is cited to its own source.

**If you hold one idea**, hold this: *SCA tells you which of your dependencies are known-vulnerable, the SBOM tells you what you ship at all, and provenance tells you whether to trust how it was built — three answers to "the code I didn't write," none of which fixes anything by itself; they enable a fast, honest response.*

## How it works

### SCA: is anything known-vulnerable?

**Software composition analysis** answers one precise question: do my dependencies contain *known* vulnerabilities? It is distinct from SAST (Part VIII), which analyzes *your* code — SCA analyzes *other people's*, which is most of a modern Java application, and where Log4Shell-class incidents live. It is, dollar for dollar, the highest-ROI security control in Java, because the majority of breaches exploit known, unpatched CVEs in dependencies — not novel attacks.

The mechanism is matching. A scanner inventories your dependencies (direct *and* transitive) and matches each component against vulnerability databases — **NVD** (the National Vulnerability Database of CVEs), **OSV** (open-source-focused), and **GitHub Advisory** — emitting findings with a CVE identifier, a CVSS severity, and the affected and fixed version ranges. The tools take different approaches, and the book crowns none:

| Tool | Approach | Note |
|---|---|---|
| **OWASP Dependency-Check** | OSS Maven/Gradle plugin; CPE matching against NVD | fails the build on a CVSS threshold; CPE matching is fuzzy → false positives |
| **OWASP Dependency-Track** | server platform consuming CycloneDX SBOMs | continuous monitoring against NVD/OSV |
| **Grype** (Anchore) | fast SBOM/image scanner | pairs with Syft |
| **Trivy** (Aqua) | broad scanner — deps, images, IaC, secrets | generates SBOMs too |
| **Snyk** | commercial, developer-focused | fix advice; reachability in some tiers |

> **CONCEPT** *Point-in-time scanning isn't enough.* A dependency that's clean today gets a CVE disclosed tomorrow — for code you already shipped. So SCA runs in two places: as a **build/CI gate** (fail on a severity threshold) and as **continuous monitoring** (a platform like Dependency-Track re-scans your shipped SBOMs as new CVEs land). The update bots from the last chapter close the loop by raising the fix PR. A scan that runs only at build time and never again would have missed Log4Shell entirely for anything already in production.

SCA's honest limits are sharp and must be taught, because mishandled they get the whole gate ignored. **False positives** are common — CPE-based matching is fuzzy and flags CVEs that don't apply to your usage — so a reviewed suppression file (with a recorded justification, the discipline of Chapter 19) is mandatory, not optional. **"Vulnerable" is not "exploitable"**: most tools flag a CVE in a dependency even if your code never calls the affected path; *reachability* analysis (some commercial tiers) narrows this, but it isn't universal, so not every finding is a fire to fight today. **Database lag and coverage** vary — a CVE not yet in the database is invisible, and OSV and NVD don't cover identically, which is why some teams run two scanners. And SCA only catches *known* vulnerabilities — zero-days and your own bugs are out of scope, which is what SAST and secure coding (Part VIII) address.

### SBOM: do I know what's in it?

SCA can only scan what it can see, and the artifact that makes "what's in it" answerable — by a scanner, by an auditor, or by you at 2am during the next Log4Shell — is the **SBOM** (Software Bill of Materials): a complete, machine-readable inventory of every component you ship, transitive dependencies included. Its value is incident response speed: "are we affected by CVE-X?" becomes a *query* over the SBOM rather than an archaeology project across hundreds of build files. There are two standard formats, both legitimate, and the book crowns neither:

- **CycloneDX** (OWASP) — security-focused: components, services, vulnerabilities, and licenses, designed for the SCA/supply-chain use case.
- **SPDX** (Linux Foundation; standardized as **ISO/IEC 5962:2021**) — broader scope, with deep licensing and provenance emphasis.

You generate an SBOM from the build — Maven and Gradle CycloneDX plugins, or **Syft** (which outputs both formats) — and consume or monitor it with a platform like Dependency-Track. Some organizations need both formats (CycloneDX for security tooling, SPDX for license/compliance reporting), and the choice is by emphasis, not quality.

> **CONCEPT** *An SBOM is an inventory, not a defense.* It *enables* a fast response; it fixes nothing. A generated-and-ignored SBOM is theatre — the value is realized only when something queries it (a scanner, an incident responder). And SBOMs have accuracy gaps: tools miss dynamically-loaded, shaded, or vendored components, and there's a documented "adherence gap" between what the standards specify and what tools actually capture. Trust the inventory, but verify its completeness — an SBOM that silently omits a shaded Log4j manufactures false confidence, the most dangerous state of all.

### Provenance and SLSA: can I trust how it was built?

The deepest supply-chain question is not what you ship but whether the artifact you ship is the one your source actually produced — because supply-chain attacks increasingly target the *build system*, swapping a compromised artifact for the legitimate one. The defenses are **provenance** and **attestation**: an **in-toto attestation** records the build's inputs and steps; **Sigstore's cosign** signs the artifact and the attestation; and CI identity (e.g. GitHub Actions OIDC) supplies keyless signing, so the signature proves *which build* produced the artifact. You verify that provenance at deploy time, and a swapped artifact fails verification.

**SLSA** (Supply-chain Levels for Software Artifacts) frames this as a maturity ladder — tiered levels of build integrity, from "provenance exists" up through "hardened, non-falsifiable, hermetic builds." It's a roadmap, not a single tool, and the higher levels are a real investment (hardened build platforms and attestation infrastructure), so most teams start low and climb as the stakes warrant. The whole supply-chain pipeline composes: build → generate SBOM → scan it (SCA) → sign the artifact and attest its provenance → publish the SBOM with the release → continuously re-scan. And increasingly this isn't optional: the US Executive Order 14028 and the EU Cyber Resilience Act make SBOMs a *compliance requirement* in many sectors, turning supply-chain hygiene from best practice into a legal obligation.

## Deep dive: three questions, one chain, on a deterministic foundation

The chapter's three techniques aren't a menu — they're a chain, each enabling the next, and seeing the dependencies between them is what turns "run some security tools" into a coherent supply-chain practice.

Start from the Log4Shell question and work backward. To answer "are we affected by this CVE?" you need to *scan* (SCA) — but a scanner can only check components it can *see*, so it needs a complete *inventory* (SBOM); and an inventory is only trustworthy if you can *attest* it reflects what was actually built (provenance/SLSA). So the chain runs inventory → scan → attest, and each link fails without the one beneath it: SCA on an incomplete inventory reports false confidence (it didn't see the shaded Log4j), and an SBOM with no provenance can be quietly swapped along with the artifact it describes. The Log4Shell winners had the whole chain: an SBOM that listed transitive Log4j, a scanner that matched it to the CVE within hours of disclosure, and continuous monitoring that flagged already-shipped services.

And the whole chain rests on the last chapter's foundation. A scan or an SBOM is only as trustworthy as the build that produced it: if the dependency graph resolves differently on every build (unpinned versions, ranges), then the SBOM you generated and scanned describes a build that may never ship, and the CVE you cleared might reappear in a different resolution. *Determinism is the precondition for securability* — which is exactly why the previous chapter came first, and why the next one (reproducible builds) tightens the foundation further: you cannot generate a *trustworthy* attestation of how an artifact was built if you can't reproduce that build.

The honest center, shared across all three, is the same humility as every gate in the book: each is **necessary but not sufficient, and each enables rather than fixes**. SCA catches *known* vulnerabilities — it is blind to zero-days and to bugs in your own code (Part VIII's job). An SBOM is an inventory — it makes you *fast*, not *safe*. Provenance proves *integrity of assembly* — not that the assembled components are good. None of them, alone or together, makes the software secure; they make you *able to know and respond*. The discipline is to wire them as a chain that runs automatically — scan on every build and continuously, generate and publish the SBOM with every release, attest provenance, and route fixes through the update bots — so that when the next Log4Shell drops, "where are we using it?" is a query you run before lunch, not an archaeology project that costs a week. That readiness, not the absence of vulnerable dependencies (which is impossible), is what supply-chain security actually delivers.

## Limitations & when NOT to reach for it

- **SCA only catches *known* vulnerabilities.** Zero-days and bugs in your own code are out of scope (SAST and secure coding, Part VIII). A clean SCA scan is not a clean bill of health.
- **False positives erode the gate.** CPE-based matching flags CVEs that don't apply; without a reviewed suppression process (Chapter 19), the noise gets the whole gate ignored. Record a justification for every suppression.
- **"Vulnerable" ≠ "exploitable."** A CVE in a code path you never call is not an emergency; reachability analysis (some commercial tiers) helps, but absent it, triage by actual exposure rather than treating every finding as a fire.
- **Database coverage varies and lags.** A CVE not yet in NVD/OSV is invisible, and the two databases differ — high-assurance teams run more than one scanner, and accept that point-in-time scanning misses post-ship disclosures (hence continuous monitoring).
- **An SBOM is an inventory, not a defense.** It enables response; it fixes nothing. Generated and ignored, it's theatre — the value is realized only when something queries it.
- **SBOMs have accuracy gaps.** Tools miss dynamically-loaded, shaded, and vendored components; verify completeness rather than trusting the inventory blindly.
- **SLSA's higher levels are costly.** Hardened, hermetic builds and attestation infrastructure are real investment; a small internal app rarely justifies the top tiers — start low and climb with the stakes.
- **The format choice and signing infrastructure are real overhead.** CycloneDX versus SPDX is by emphasis, not quality (some orgs need both), and signing-key/identity management is its own operational burden.

## Alternatives & adjacent approaches

- **The SCA tools** (Dependency-Check, Grype, Trivy, Snyk, Dependency-Track) — OSS versus commercial, speed versus reachability, point-in-time versus continuous; choose by context, run more than one where coverage matters.
- **CycloneDX vs SPDX** — security-emphasis versus licensing/ISO-emphasis; Syft generates both, so the choice need not be exclusive.
- **The update bots** (Chapter 27) — the remediation arm: SCA *finds* the vulnerable version, the bot *raises the fix PR* through the gates.
- **SAST and secure coding** (Part VIII) — the complementary controls for *your* code and for zero-days that SCA structurally can't see.
- **Reproducible builds** (next chapter) — the foundation that makes an SBOM and a provenance attestation trustworthy.

These compose into one supply-chain program: a deterministic, reproducible build produces a complete SBOM, SCA scans it at build time and continuously, provenance attests it, the bots remediate, and SAST covers what's left.

## When to use what

- **"Is anything in my dependency tree known-vulnerable?":** SCA — a build gate on a severity threshold *plus* continuous monitoring of shipped SBOMs.
- **"Am I affected by this newly-disclosed CVE?":** query the SBOM — which is why you generate one on every build, transitive included.
- **A false-positive finding:** a reviewed suppression with a recorded justification — never silent, never blanket.
- **Triaging real findings:** by exposure (reachability) and severity, not first-come; not every CVE is an emergency.
- **"Can I prove this artifact is the one we built?":** provenance — in-toto attestation signed with cosign, verified at deploy.
- **Raising build integrity over time:** SLSA as a ladder — start at provenance generation, climb to hardened builds as the stakes warrant.
- **For your own code's vulnerabilities and zero-days:** SAST and secure coding (Part VIII) — SCA won't see them.

## Hand-off to the next chapter

This chapter assumed something it leaned on heavily: that the build which produced your scanned, inventoried, attested artifact is *trustworthy* — that the SBOM describes what actually ships and the provenance attests a build you could stand behind. That assumption only holds if the build is **reproducible**: if rebuilding the same source produces a bit-for-bit identical artifact, an attestation means something; if it doesn't, provenance is a signature over a moving target. The next chapter makes that foundation explicit — **reproducible builds** (the discipline and the Java tooling that deliver byte-identical outputs) — and pairs it with the other thing your SBOM already inventories: **license compliance**, the obligations and incompatibilities hiding in your dependency tree's licenses (SPDX license identifiers, automated license checks). Together they finish Part VII's account of the build and the tree it assembles: deterministic, secure, reproducible, and legally clear.

## Back matter — sources & traceability

- **SCA / vulnerability scanning** (key 65, ⚠ multi-tool) — SCA = "do my dependencies contain known vulnerabilities?" (vs SAST = your code). Inventory deps (direct+transitive) → match vs **NVD** (CVEs) / **OSV** (open-source) / **GitHub Advisory** → findings (CVE id, CVSS, affected/fixed). Tools (crown none, each own docs): **OWASP Dependency-Check** (OSS Maven/Gradle plugin, CPE matching vs NVD, fail-on-CVSS, fuzzy-CPE FPs), **OWASP Dependency-Track** (server, consumes CycloneDX SBOMs, continuous monitoring), **Grype** (Anchore, +Syft), **Trivy** (Aqua, broad), **Snyk** (commercial, reachability some tiers). Build/CI gate + continuous monitoring + update-bot fix PRs (Ch 27). Suppression = reviewed file + justification (Ch 19). *(mechanism verified; tool versions/GAVs/CVSS-config/suppression-format/CPE-wording ⚠ @pin.)*
- **Supply chain / SBOM** (key 66, ⚠ SPDX/CycloneDX) — supply-chain attacks target what you ship/build, not what you write. SBOM = complete component inventory → "am I affected?" in minutes. Formats: **CycloneDX** (OWASP, security-focused) / **SPDX** (Linux Foundation, **ISO/IEC 5962:2021**, licensing/provenance). Generate: Maven/Gradle CycloneDX plugins or **Syft** (both); consume: Dependency-Track. Provenance: **in-toto** attestation + **Sigstore cosign** signing + CI OIDC keyless identity, verified at deploy. **SLSA** = tiered build-integrity framework (roadmap not tool). Pipeline: build→SBOM→scan→sign+attest→publish→re-scan. Compliance: **US EO 14028**, **EU Cyber Resilience Act** mandate SBOMs. *(scope verified; SPDX=ISO/IEC 5962:2021 + CycloneDX spec status + SLSA levels + in-toto/cosign + EO-14028/EU-CRA specifics ⚠ @pin — standards-edition discipline.)*
- **Routing** — dependency pinning/currency → Ch 27 (63/64); SAST (your code) + secure coding → Part VIII (70/69); reproducible builds → Ch 29 (67); license compliance → Ch 29 (68); security gate → later (73); release/publish SBOM → later (83); suppression → Ch 19 (39). SOURCE-PIN: tool rows TO-PIN; standards (SPDX/SLSA/EO-14028/EU-CRA) verify-edition @pin. REPRO: scan needs DB download/network → PENDING-RUNTIME offline.

**Companion module (spec — ⚠ EXAMPLE-BUILD = PENDING; toolchain READY; scan needs network/DB → REPRO PENDING-RUNTIME):** `08-companion-code/65_dependency_scanning_sbom_supply_chain/` (or a profile on the flagship) — the **OWASP Dependency-Check** Maven plugin failing the build on a deliberately-old vulnerable dependency at a CVSS threshold, plus a **reviewed suppression** entry (with justification) for a verified false positive; and a **CycloneDX** SBOM generated on `verify` (cyclonedx-maven-plugin), optionally fed to a scan. **Failure path:** the seeded vulnerable dep fails the gate until upgraded (the fix the update bot would raise) or suppressed-with-reason. **Honest edge:** the suppressed false positive shows "vulnerable ≠ exploitable" / FP discipline; the SBOM shows the Log4Shell-answer inventory but is "an inventory, not a defense" (stated in a comment); the scan needs a DB download, so reproduction is network-gated.

## Next chapter teaser

Everything in this chapter assumed the build that produced your scanned, inventoried artifact is one you can stand behind — but a provenance attestation over a build that produces a different artifact every time is a signature on a moving target. The next chapter makes the foundation solid: reproducible builds that yield byte-for-byte identical outputs from the same source (the discipline, and the Java tooling that delivers it), paired with license compliance — the obligations and incompatibilities your SBOM's license data already exposes. It's the last piece of Part VII's account of the build and the dependency tree it assembles.
