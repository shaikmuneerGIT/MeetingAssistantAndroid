# 08 — Meetings, KT Sessions & Decisions (minutes and takeaways)

## 8.1 Recurring meetings & cadences

| Meeting | When | Notes |
|---|---|---|
| Team Tuesday sync-up | weekly | I share the prior week's `/defect-analysis` report (manager-approved format: 16-week line chart with shaded gap, donut, aging bars, KPI cards); auto-run target Monday 00:00 |
| Daily triage call | 6:30–7:00 (host Tripti) | partner onboarding / orchestration issues |
| Release gates | per release | review coverage flow with Arun |
| Aging report | daily 11:00 scheduled | `scripts/scheduled_aging_report.ps1` → `logs/aging_report_*.log`; real send Cc leadership (14) admin-only |
| Defect chase | 11:30 scheduled (admin) | `scripts/run_defect_chase_1130.ps1`; last real multi-project send 2026-06-26 (all 7 projects unfiltered by informed override) |

## 8.2 MOM — Trade Screening Scenarios Alignment & Coverage Review (GTM–RPL) — 2026-07-16 (~34 min)

Attendees: Saroj Patra (Trade Architect), Mahesh T, Nishanth K, Arun Mahendrakar, Shaik Muneer, Kiran Kumar Avsn. Source: Teams recording on Mahesh's OneDrive.

Scope: CIDM team owns Rules, Cardinal Core, Cust Account, Customer Orchestration (interfaces only), PubSub, Fusion; create customer in scope. CDMEE not in scope; Smart Search N/A.

Decisions / answers:
1. RPL is at BOTH party and site level (GTM matches on company name OR address) → add party-level TCs incl. party/site mismatch combos.
2. Class = Direct or Partner only; Usage = Customer / Customer,Sales Account / Customer,Partner Account (Funder-on-sales-account permutations valid).
3. Consumers are NOT screened (Fusion never sends them) → consumer TCs are negative scenarios.
4. Status flow: RPLS_SENTFOR_REVIEW → PASSED (no match) / REQUIRES_REVIEW (match) → manual → PASSED/FAILED. SENTFOR_REVIEW not yet in CPD.
5. Nothing inactivated at party/site on failure; site purposes selectively end-dated; linked cust accounts inactivated (Party_Denied); Fusion sent "ended".
6. Primary replacement: primary billing fails → latest billing becomes primary; secondary fails → no swap; shipping same; different B/S sites and shipping fails → primary billing extended as primary shipping; identifying site = Prim Bill else Prim Ship.
7. createCustomer allows only 1 billing + 1 shipping; more via add-site (rename "create with multiple billing" TCs).
8. Failed entities masked in GETs ("cannot be disclosed"); getSites returns only active sites.
9. Blocked CRUD when RPL-failed: add site/contact, add/update cust account, update cust account site, update site/contact, update customer.

Actions: Nishanth/Mahesh — party-level TCs, class/usage fixes, consumer TCs negative, add-site scenarios, update-customer coverage, reshare sheet; Mahesh — ask Panneer re CERN masking (runtime vs stored), trial patch-inactivate then re-create, sync with Suresh (Sparkler) & Sondra's team on cust-account RPL warnings; Nishanth — review flow with Arun. Closure: Saroj fine with coverage. Still open then: SLA/timing in G4 and how to force a match (both answered by 2026-07-31 E2E: ~80–110 s; copy a real RPL entry), Funder Account screening treatment.

## 8.3 MOM — Partner Onboarding & Partner Hierarchy KT (Customer Orchestration) — 2026-07-24 (~37 min)

Presenter: Ariba Syed. Attendees: Mahesh T, Kiran Kumar Avsn. (Transcript file mis-named "trade_screening_transcript_full (3).txt".)

Partner onboarding flow: CDMEE approves ticket → ARD builds payload → Kafka notification topic → orchestration. Request = DUNS + address + usages + classes; DUNS present → CERN DUNS search, absent → entity search. Outcome: partner party (class Partner) + GU party, each with a partner account (returns partner track ID); contacts added after (person-party-ID contact or profile contact). CDMEE tasks Store / Spill / DAM (0–3, none mandatory) cascaded as-is with field substitutions (DAM: partner party ID; Spill: person party ID + partner track ID); Store & Spill return request IDs (orchestration's responsibility ends; Spill ~3–4 h). Final CDMEE update: partner party ID, GU partner party ID, partner track ID, store/spill request IDs. Affinity/coexistence → ticket Complete; non-affinity stays In Progress until Store/Spill finish. Onboarding is almost always for the HQ DUNS (HK branch exception); existing GU handled by hierarchy flow; onboarding creates only partner + GU parties (not full hierarchy).

Debugging from G4 orchestration UI: search ticket number → failed workflow → correlation IDs → error detail (source + message, e.g. CBS "ProvinceStateCode cannot be null or empty") → "scroll to failed task" → request/response payloads. Ariba grants UI access.

Partner hierarchy (build-partner-hierarchy): triggered by party-creation Pub/Sub (class Partner only). Direct-party resolution 3-layer dedupe: Hierarchy API get-hierarchy (DUNS + Direct + Customer) → CERN → create in Cardinal (createParty dedupe as last net). Establish Partner-to-Sales (P2S) partner(green)→direct(orange). GU detection: CERN site status indicator = 0 → post-hierarchy source=GU DUNS, target=null (root). CERN immediate-child DUNS endpoint → per child same resolution → P2S → post-hierarchy child→parent → recursive top-down via each party's own Pub/Sub notification. Non-GU notification does not build the whole tree (never traverses up).

Actions: Ariba — UI access; Kiran — practise creating onboarding requests from ARD; Mahesh/Ariba — gray-area defect ownership discussion with Arun.

## 8.4 KT — Partner onboarding E2E (ARD) — 2026-09-04

Presenter Harshitha B (ARD), demo Grishma A S, env GE4.
- Partner marker = usage Partner + class Partner (sales accounts = class Direct). Partner create mints partner-class party + direct-class customer party linked "Partner-to-Sales". Participant = sales-rep association at track level (ARD + Fusion SPM; not QA scope). ~15–18 partner track types; global account parent ↔ country account child. Known data issue: CPD shows sales-account usage but ARD has no account.
- Flow: My Account form → ARD ticket in CDUI → Data Stewardship vetting → approve → orchestration `PartnerCreation`. CERN match at approval keys on name + address + DUNS; match ⇒ "Create Partner Track" on existing party (user may reject and create new).
- Tasks: DAM (logo copy), Store creation (only Tier-1 + Hybrid; Tier-2 none), SPIL = Sales Pipeline Integration (on-prem; associates user email to partner + global account only AFTER Fusion acknowledges account+track). Fusion: party → CDM module, account → PRM module. All complete → Kafka → ARD auto-closes ticket → email via CC application. **SPIL SLA 4 h**; breach ⇒ task "Attention Required" (manual). Main failure zone: CPD→Fusion out-of-sync (account before party); ARD publishes account only after Fusion ID on party. OldCo coexistence: ARD extract → SFDC → Affinity creates APID → coexistence job links APID↔party (CDUI > ODW Transactions; GAMA > UDA tab).
- Demo: ticket TKT1026065, party P15805031680, store STUSA11011132, track T10000016128; PartnerCreation 37.5 s (searchable by TKT in cpd-workflow-ui-ge4). Bug candidate: task audit createdDate UTC vs modifiedDate local.
- People: Harshitha/Grishma (ARD help), Sowmya/Arun (orchestration deep-dives), Tripti (daily triage). **Orchestration revamp expected ~2026-09-09** → QA focus.

## 8.5 Other recorded decisions & conversations

| Date | With | Decision / takeaway |
|---|---|---|
| 2026-05-17 | Muneer (direction) | Defect defaults: P4-Medium, assignee/reporter = PAT owner, sprint from current_sprints.json; `/jira-test-cases` only for the exact key passed |
| 2026-05-21 | Muneer | Test cases: assignee+reporter = PAT owner; no parent-story comment; no AI attribution; `/release-scope` status-grouped only |
| 2026-05-22 | Manager (via Muneer) | `/defect-analysis` report approved ("closest to an accurate and well displayed testing report"); formalize weekly (Mon 00:00 run, Tue sync), evals in `scripts/evals/`, broaden to CIE later; co-owners Mahesh + Muneer |
| 2026-05-22 | Muneer | User pushes commits; attach only HTML (never .md) to report emails |
| 2026-05-30 | Muneer | "PLAN" in MABL context = MABL suite, not JIRA Test Plan |
| 2026-06-04 | dashboard reconciliation | Severity = Dell Severity field (not priority); dashboard 59623 is source of truth |
| 2026-06-15/16 | Windsurf/security team | MCP whitelisting only if zero JIRA dependency, universal path, `dotnet <dll>` |
| 2026-06-16 | Ananda | CAM chased only for migration defects; exclude Cardinal-Core pubsub/DB strays (admin-curated key list — human judgment) |
| 2026-06-20 | Muneer | Report emails → me only; scripts must run anywhere (fill-only-missing creds) |
| 2026-06-26 | Muneer (admin) | One-off: send all 7 chase projects unfiltered (informed override of Ananda's rule) |
| 2026-06-29 | Platform gatekeeper | "We have official JIRA MCPs; no other MCP may interface with JIRA" — final |
| 2026-07-09 | James J. Montgomery / Saroj | NewCo: primary contact can be deactivated when it is the last active contact; sites may exist without contacts |
| 2026-07-14 | Architects (MAVCPDCUS-1093) | Contact re-activation D→A is not a valid use case |
| 2026-07-15 | Muneer | OldCo/GOP never allows deactivating primary contacts |
| 2026-07-23 | Muneer | No comments on stories during QA sign-off; QA stops at Waiting to Deploy |
| 2026-07-24 | Muneer | Match/dedupe 200 counts as PASS for positive TCs; LATAM domsSequenceNumber 0 is correct |
| 2026-07-24 | Selvi / Mani | Valid statusReasonCode LOV; only Funder account-relationship type |
| 2026-07-30 | Shaik / dev | OldCo official rule-version is 16; CAM ignores hyphen header |
| 2026-07-31 | Team (via Muneer) | ALL mandatory defect fields must be filled; attach raw captured logs |
| 2026-08-03 | James Montgomery | MAVCPDCUS-1132 triage: BUG 1 Rules, BUG 2 Sudhakar (later: BUG 1 = body rulesVersion behaviour, 1025 cancelled) |
| 2026-08-04 | Muneer | G4 DB write safety rule; MAVCVS-1025 cancel |
| 2026-08-05 | dev (JW compare) | Artifact taxonomy: corporate designators, CNSR name order, state name-vs-code |
| 2026-08-06 | Karthik Raja / Suresh | CDAS-12328: returning inactive site id is expected in AMER |
| 2026-08-06 | Muneer | Every tested story → xUnit regression in MAV_0402; NewCo only; OldCo AMER changes in C:\repos\AMER |
| 2026-08-06 | Sondra / Mahesh (email) | PCF can be removed for the 43 primaryCustGatewayExternal routes — KOB parity confirmed |
| 2026-08-11 | Muneer | Remove 8 obsolete dupes from MAV-740928/929; CDAS-12328 → Waiting to Deploy without comment |
| 2026-08-11 | CBS / Mani | MAVCVS-1021 shipped with mandatory-Group flag OFF (clients not ready) |
| 2026-08-17 | Muneer | Don't use AskUserQuestion |
| 2026-08-25 | Muneer | Reports in terminal tables; no unsolicited JIRA comments ("dont update in story") |
| 2026-08-26 | Mani / Manoj (Teams) | v5 validations removed for MAVCVS-1002/948/940/936/937; Tamil: UAE+SK scope → December, fields non-mandatory (MAVCVS-1041) → cancel 1045; Suresh cancelled MAVCDAS-3163 |
| 2026-08-27 | Mani CBS | Asked why 1046 duplicated 1045 → answered honestly; lesson: don't recreate defects to fix fields |
| 2026-09-03 | Muneer | Fusion + PUBSUB defects → MAV project (corrected twice) |
| 2026-09-04 | Mani (PO) / Mahesh | CN suburb 400 = Loqate premium license exhausted → MAVCVS-1053 cancelled, fix via MAVCVS-1054 |
| 2026-09-04 | Muneer | "In E2E" status = our scope; Trade Compliance / CDUI stories ignored for the 1002 batch |
