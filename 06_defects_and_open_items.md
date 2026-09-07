# 06 — Defects Log & Open Items

## 6.1 Defects filed / driven by me (chronological; status as last known)

| Key | Date | Project / App | Summary | Status / outcome |
|---|---|---|---|---|
| MAVCVS-923 | early 2026 | CVS | paymentTermCodeRule missing (KeyNotFoundException, 169/525 tests) — template defect | reference |
| MAVCVS-949 | 2026-05-26 | CVS | PUT relationships 500 NRE (MAVCVS-913) | **wrong — payload shape**; cancelled |
| MAVCDAS-2921 | 2026-05-25 | CIL MS | ORA-06531 PKG_CREATE_CUST_ACCOUNT_V1 line 103 | first JSON-payload defect |
| MAVCVS-959 | 2026-05 | CVS | RESCOM fix (retest loop model for `/verify-defect`) | verified |
| MAVCVS-972 | 2026-06-18 | CVS | SA Address Line 4 free-form — validated | PASS |
| MAVCPDCUS-1075 | 2026-06 | CPD | DJT no validation; ORA-12899 leak | reassigned to Mani pending Rules ownership |
| MAVCPDCUS-1093 | 2026-07 | CPD | contact re-activation (D→A) | Working as designed (architects 07-14) |
| MAVCDAS-3040 | 2026-07-09 | CIL MS | CAS created on inactive D/EOL party site | Cancelled by Sondra (citing RPL epic 2999) → reopened 07-14 → later fixed by MAVCDAS-3065 (PASS 08-25) |
| MVKCDHCP-2653 | 2026-07-16 | Fusion | sync breakfix → Chandni Tomar | routed |
| MAVCVS-1017 | 2026-07-23 | CVS | statusReasonCode NOT_IN_USE rejected | Cancelled 07-24 (valid LOV: RPL, EOL, OOB, MAD, DUP, MAS, Party_Denied, Party_Site_Denied, Site_Denied) |
| MAVCVS-1018 | 2026-07-23 | CVS | account relationship rules | Cancelled 07-24 (only Funder valid; needs Funder Account target) |
| MAVCVS-1019 | 2026-07-23 | CVS | V4 payment-term Group not enforced | Cancelled 08-04 (new scope MAVCVS-1021, shipped flag OFF) |
| MAVCVS-1020 | 2026-07 | CVS | CO postal/state | closed (orchestration derivation) |
| MAVCDAS-3078 / 3081 | 2026-07-22 | CIL MS | POST legacyMap 500 / DOMS not persisted | **Complete 07-24** (contract solved) |
| MAV-750758 / MAV-750291 | 2026-07 (Nishanth) | Party / Fusion | trade status not in ORG_PARTY / Fusion events not published | Complete 07-30/31, fixes verified by me |
| MAVCPDCUS-1132 | 2026-07-31 | CPD | Party relationship: `I` → 200 but persists null (and `D` rejection half) | **OPEN**, Sudhakar, Sev 3/P3, 19 attachments |
| MAVCVS-1025 | 2026-08-03 | CVS | relationship route pins rules v3 | **Cancelled 08-04** — body `rulesVersion` drives selection |
| MAVCDAS-3028/29/30/32/33/69 | 2026-08-05 | CIL MS | RPL account gates (stories) | Waiting to Deploy with evidence; **3031 held In E2E** (N2) |
| CDAS ORA-16000 (P1 + P2 CAM NEXT) | 2026-08-13 | CAM | all POSTs fail, EMEA PDB read-only | drafted JSON (infra/DBA) |
| CDAS TestDBCalls NRE EMEA BUIDs | 2026-08-13 | test framework | GetConnectionString NRE for 551/909/5455 | drafted |
| MAVCDAS ORA-06531 add cust-acct site; party-contact inactive blank reason | 2026-08-14 | CIL MS | E2E failures | drafted JSON |
| MAVCPDCUS PATCH contactMethods blank areaCode 500 NRE | 2026-08-14 | CPD | US 108402 | drafted; hub staleness folded in |
| MAVCVS areaCodeRule mismatch | 2026-08-14 | CVS | published rule ≠ live enforcement | drafted |
| MAVCVS-1045 → 1046 | 2026-08-26 | CVS | UAE create without CRN accepted (942) | **both Cancelled 08-27** (requirement reversed by MAVCVS-1041) |
| MAVCDAS-3163 | 2026-08-26 | CIL MS | BR first-8 CNPJ not enforced (3012) | **Cancelled** by Suresh (not implemented yet) |
| MAVCVS rules-version-3 not deprecated | 2026-08-31 | CVS | rv3 still served past 24-Aug non-prod date | drafted with evidence bundle `defect_evidence/rv3_nonprod_deprecation_20260831` |
| MAVCVS-1053 | 2026-09-02 | CVS | CN O/COMM suburb 400 | **Cancelled** (Loqate license; tracked by story MAVCVS-1054) |
| MVKCDHCP-2794 / 2795 | 2026-09-03 | — | Fusion JBO-26075 / repush in-flight dupes | **misrouted → Cancelled** |
| **MAV-776962** | 2026-09-03 | MAV / PUBSUB::FUSION | GE4 (dev7) FoundationPartiesOrganizationService JBO-26104/26075 "Another user created the doc with same name" (Sev 2) | OPEN |
| **MAV-776983** | 2026-09-03 | MAV / PUBSUB | repush re-sends transactions only 10–160 s old (still in flight) → duplicate party_relationship pushes (Sev 3) | OPEN |

Exemplar defects for field profiles: MAV-650352 (Cardinal-Core), MAVCDAS-2378 (CIL MS), MAVCVS-923 (CVS), MAV-458154 / MAV-670088 / MAV-687670 / MAV-681280 (Fusion EP-CDM), MAV-753700 / MAV-775813 / MAV-774415 (PUBSUB in MAV).

## 6.2 Defect candidates NOT yet filed (decide before filing)

- `get-contacts`/`get-contact` return full PII for RPLS_FAILED parties (violates masking) — confirm scope with CPD (Selvi team) then file.
- N1 enumeration oracle: denied-account sub-resource GET resolves existence before RPL gate (real id → W005, fake → "No record found").
- N2 `GET /v1/customerAccounts/{dcn}/relationships` → 400 with all-null body for EVERY account (deliberately unfiled per Muneer — raise only if blocker; blocks MAVCDAS-3031).
- D4: `POST …/relationships` returns fabricated record alongside W005 block.
- `isSalesHold` accepts and persists `"X"` (stories specify Y/N) — from 09-04 batch.
- MAVCDAS-3141 replication requestor: end-dated site → 201 (guard missing); MAVCDAS-3139 lowercase party id not accepted; MAVCDAS-3136 paymentGroup inert (see MAVCVS-1035 not live).
- `update-account` silently ignores `paymentTermCode`; CUST_ACCOUNT_SITE.PAYMENT_TERM_CODE drift (copy-on-create).
- Trade-screening PATCH accepts `tradeScreeningStatus` but never persists (pending MAVCPDCUS-1071/1072 admin routes) — ask dev before filing.
- MAVCVS-995/996 BR name-match at v4 initially not enforcing (later 996 verified enforcing).
- FR SIREN/EAN format not validated for COMM (`eanNumber:"12"` accepted).
- UAE VAT regex EU-only yet VAT required → UAE create impossible without workaround.
- `Fusion_backfeed` reverts account payterm edits (~40 s); adds duplicate profile contact (benign).
- DSUI GE4 credit-party backend: `DnB_ValidationFailed` for every DUNS; without-DUNS 500 (raise with CMDM).
- Hub: 4 parties with NO OnPrem rows after 10×10 s (P15804390981 IN, P15804390791 MY, P15804388036 MY, P15804389461 US) — re-probe.
- PCG `update_party_trade_status` TX logs PARTY_SITE_ID literal `111` (cosmetic).
- Task audit createdDate UTC vs modifiedDate local in orchestration console (partner KT observation).

## 6.3 Open items / next steps (as of 2026-09-07)

1. **1002 In-E2E batch:** create Xray tests for the 8 MAVCDAS stories (isSalesHold ×4, 3136, 3139, 3141, 3184); get a VCID-bearing party for 3184; decide on isSalesHold "X" defect; chase 3012 feature-flag state (Ankit).
2. **MAVCPDCUS-1132** still open (CPDn nulls `I`); MAVCVS-1013 TC-02/03 Xray runs still read PASS — flip to FAIL manually in MAV-740929.
3. **MAVCDAS-3031** held In E2E pending N2 fix and a denied-party-with-Funder fixture; site-only RPL fixture needed for W006 branch of 3029/3069.
4. **Rules v3 deprecation defect** — file/track (rv3 still served on G4 after 2026-08-24; prod deadline 2026-10-02); harness default `RULE_VERSION=3` must migrate to 4/5 (MAV_0402 already moved "version 3 to 5" 2026-09-01).
5. **CN O/COMM creates** — re-validate after MAVCVS-1054 deploys (Loqate suburb preservation).
6. **Orchestration revamp ~2026-09-09** — QA focus on orchestration workflows (PartnerCreation, build-partner-hierarchy); practise creating onboarding requests from ARD.
7. **KOB cutover** — repoint automation configs from `cilmsnp` (PCF) to `cilms-np` (KOB) hostnames; confirm both `primary-customer-gateway` and `customer-gateway-api` included.
8. **Uncommitted work in main repo** — Program.cs changes (SK/AE countries, DEACT_* knobs, PrintResponseHeaders, SA probe harness, OLDCO_ADDR_LINE1, CN name-padding fix), 273 untracked `.devin/skills`+`agents`, deleted `.devin/workflows`; commit in logical batches (never `git add -A`).
9. **Memory hygiene** — re-run `python scripts/sync_claude_memory_to_devin.py --write` (mirror is from 2026-07-28, 94 entries; current memory dir has 37 newer files); keep this WORKSPACE_MEMORY pack updated.
10. **JIRA KB refresh** broken (PS parse errors, missing urllib3) — either fix `refresh_jira_knowledge.ps1`/install `requests urllib3`, or keep using live `search-jira`; also `release_manifest.py` reads `.devin/knowledge` while refreshers write `.windsurf/knowledge`.
11. **MCP whitelisting** — `cidm-agent` still not in Dell's Devin org allowlist; request text in `docs/MCP_WHITELISTING_REQUEST.md`.
12. **Defect-chase overrides** — `defect_team_overrides.json` (Ananda's CAM-migration-only / exclude pubsub-DB strays) is NOT wired into `send_defect_emails.py`; heads_cc must be re-merged before real sends.
13. **Regression pack** — port MAVCVS-1000/1001 once payment-term grouping is enforced; port 1002 batch stories as `Sprint1002/` + `Regression1002`.
14. **CDUI** — open items live in `C:\repos\CDUI\CDUI_PROJECT_MEMORY.md` (G1 Partner-class party for relationship tests, APID test data from ARD, bulk/rules-lookup MFEs uncovered).
15. **PROD** — SECRET2 client dead; update with a working client or keep pre-fetching tokens.
16. **UY (Uruguay) MAVCVS-1022** suburb non-mandatory — needs Countries dict entry before E2E.
17. **Weekly cadence** — Monday 00:00 defect-analysis auto-run + Tuesday sync share; aging report scheduled 11:00 daily (`logs/aging_report_*.log`), defect-chase 11:30 (admin).
