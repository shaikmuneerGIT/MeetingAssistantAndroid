# 05 — Timeline & Test Campaigns (what happened, when, with what result)

## 5.1 Chronology (2026)

| Date | Event |
|---|---|
| 2026-02-17 | CAM_NEXT regional branches "G4 TO PROD" (AMER/APJ/EMEA/LATAM OldCo suites moved to PROD targets) |
| 2026-04-07 | First "Created Customers — All Regions" run (11 BUs) on MAV_POC |
| 2026-04-09/13 | PROD Fusion report insights (38k/day, `location` 85% of errors); ErrorPattern PROD reports |
| 2026-04-25 | `get-rules` command added; `.windsurfrules` NL mappings |
| 2026-05-04 | `fusion-report-prod` fixes (transaction-scoped dedup) |
| 2026-05-09 | Secrets scrubbed, G4 locked; specs/plans generated for 1,294 stories, 13 buckets |
| 2026-05-11 | JIRA story knowledge base built ("AI second memory"); team onboarding guide; daily refresh scripts; active-sprint tracking |
| 2026-05-12 | OldCo `create-oldco-customer` end-to-end (US/MX/GB/MY, CAM_NEXT); `find-newco-id` Playwright lookup; 21 Cascade workflows; MAV_TOOL_OVERVIEW doc; leaked secrets redacted from launchSettings (URGENT commit) |
| 2026-05-13/14 | `/download-rules`, `/find-newco`, `/onboard`, `/troubleshoot`, `/bvt-smoke`; Onboarding report (all green) |
| 2026-05-16/17 | Discovered Dell uses **Xpand-IT Xray** (steps via PUT `/rest/raven/1.0/api/test/{key}/step`); `add-tests-to-plan/exec` commands; defect defaults (P4, PAT owner) |
| 2026-05-18 | OldCo MY fixed; PAT token removed from repo |
| 2026-05-21 | `/jira-test-cases` chain hardened, `/release-scope` read-only contract, `/init-release-jira`; **STATUS_REPORT FY27FW19-0602** (4 stories, 21 TCs, 17 C# tests, 5/17 passing); App Control block → run.py switched to `dotnet <dll>` |
| 2026-05-22 | `/get-payloads` schema-source-of-truth rule (MAVCVS-881 incident); `/execute-test-cases`; **manager approved `/defect-analysis` report** → weekly Tuesday artefact; "user pushes commits" rule |
| 2026-05-25 | First JSON-payload defect MAVCDAS-2921 (ORA-06531); Rules stories validation report (0602 scope: 877/881/889/902/2854/853/2830 validated; 913 blocked → MAVCVS-949 later found to be payload-shape mistake) |
| 2026-05-26/27 | Relationship payload schema solved (MAVCVS-913 6/6); GitLab E2E validation pipeline (PowerShell runner) |
| 2026-05-28 | DAIS OAuth JSON-body quirk found (MABL IDX12709) |
| 2026-05-30 | RESCOM valid values Res/Com (MAVCVS-959 retest); `/verify-defect`; MABL OldCo + orchestration flows |
| 2026-05-31 → 06-03 | SalesUX Playwright create (USA1/MYS/AUS); Loqate recipe; Confluence KB (43 pages); `/my-day`, `/pass-test-cases`, `/confluence-sync` |
| 2026-06-04 | defect-analysis switched to Severity field (dashboard 59623 reconciliation); 7 new workflows; MABL/BVT suite; catalog |
| 2026-06-05 | **Team moved from Windsurf to Devin** (`.windsurf → .devin`, commit 5a5878c9) |
| 2026-06-08 → 06-15 | defect-chase-email (admin-only gate 06-10), `/defect-dedupe` (06-10: 696 errors → 29 patterns → ~3 real defects), `/get-defects-by-list` HTML, first real defect-chase send to assignees + leadership (06-15) |
| 2026-06-16 | Ananda's chase routing rules; MCP whitelisting conditions (no JIRA, universal path, dotnet dll); live-assignee fix for aging emails |
| 2026-06-17/18 | GenAI headless auth online (entitled service account); SSL-inspection/certifi + Python 3.13 `cgi` fixes for teammates; PowerShell empty-arg drop found (MAVCVS-972) |
| 2026-06-19 → 06-21 | Teams integration (outbound `teams_notify.py`, inbound `sp_poller`, SharePoint CA-blocked); `/pa-webhook` exposure rule; DJT dedupe behaviour verified (MAVCPDCUS-1063/1075) |
| 2026-06-23 | VCID FindCustomerByVCID contract solved (`Criteria.VirtualCustomer.Ids`); firmographics under `site.firmographics` |
| 2026-06-24 | Aging report scope aligned with dashboard (>14 cumulative, in-test statuses) |
| 2026-06-29 → 07-01 | `cidm-mcp-server` standalone orphan repo (JIRA-free, G4-only, enc: secrets, structuredContent, stdin fix); `defect_query.py` fallback; `check-fusion` scans all transactions |
| 2026-07-03 | MY address line ≤30 chars; aging Cc-blank safeguard |
| 2026-07-07/09 | REST PUT field edits proven (MAVCPDCUS-1093); wrong-user PAT in bin discovered (writes attributed to Srikanth); Claude→Devin memory sync set up; MAVCDAS-3040 reopened |
| 2026-07-10 → 07-13 | Bulk-create Excel template + converter (14 BUs, tax matrix, firmographics); MY zip/state coherence; isTermsPaymentCode mapping; CERN orchestrator read-only access |
| 2026-07-13 | **Rules v4 deployed GE1/GE4** |
| 2026-07-14/15 | v4 rollout verification (FR live, TR verified via v3/v4 differential, BR 995 not enforcing); Rules API credential replaced; KR/CO onboarding prep |
| 2026-07-16 | **Trade Screening alignment call (Saroj)** — MOM; Fusion-side team routing (MVKCDHCP-2653) |
| 2026-07-17 | **Rules v5 early-deployed to GE4** |
| 2026-07-22 | Partner-To-Sales recipe (class Partner + shared siteName); KR (10410) / CO (10170) first creates; legacyMap POST persistence defect proven (no DOMS column) |
| 2026-07-23 | In-E2E 0803 batch (2962/2980/2992/2994/2996): 10 new TCs, 14/22 executed 0 FAIL; admin whitelisted get-legacymap/get-partymap/resolve-newco; MAVCVS-1013 executed → defects 1017/1018 (both cancelled 07-24, works as designed); MAVCVS-1000 accidentally transitioned Complete (verify-transitions rule); linkedIssues lag → 8 duplicate TCs → Obsolete |
| 2026-07-24 | **Partner onboarding & hierarchy KT (Ariba)** — MOM; DOMS=0 is correct for LATAM; legacyMap POST contract solved (3078/3081 complete); v5 scope announcement |
| 2026-07-27 | MAVCPDCUS-1102 deployed to G4 (I→D half); mandatory defect classification fields (QA program mgmt back-filled MAVCVS-1019) |
| 2026-07-28 | **Laptop migration backup** commit; memory synced to Devin (94); trade-screening E2E report (PATCH validates but drops tradeScreeningStatus) |
| 2026-07-29 | MAV-740928 campaign: 21/21 tested, 19/21 complete; `put-legacymap` + CLIENT_APP_NAME (MAVCDAS-3068); DSUI Playwright suite |
| 2026-07-30 | CA (10124) added; Delivery purpose live globally (US/GB/CA parties); UA postal CVS-1587 PASS both paths (CAM `rules_version: 16`); rules v3/v4/v5 probes; JIRA KB refresh found broken; user-local .NET 6 SDK installed; MAV-750291 fixed |
| 2026-07-31 | **Trade screening LIVE E2E** (MY pass 79s, MX forced match ONZE BLACK → REQUIRES REVIEW, US never screened, contact GET PII leak); MAVCVS-1013 sign-off corrected to 2/4; MAVCPDCUS-1132 filed (19 attachments); RPL write sweep v1; defect mandatory-fields feedback |
| 2026-08-01 | RPL write sweep v2: 19 blocked / 1 defect (`update` bypass) / retractions |
| 2026-08-03/04 | Rules per-operation ruleset endpoints; MAVCVS-1025 filed then **cancelled** (body `rulesVersion` drives validation — D works with body v4); CDAS-12380 PATTEN PASS; CDAS-12328 dedupe (3 failure modes in one day); JW compare pipeline built + PROD run; G4 DB write-safety rule |
| 2026-08-05 | RPL enforcement: all 19 writes block, D1/D2/D3 fixed; MAVCDAS-3028/29/30/32/33/69 → Waiting to Deploy, 3031 held (N2); **18 stories descoped from 0803 E2E**; KOB gateway parity sweep (1,229 requests, 0 diffs) |
| 2026-08-06 | Regression pack batches 1+2 in MAV_0402 `Sprint0803` (28/28 green); OldCo AMER repo rule; payment-term copy-on-create finding; `.NET 8.0.423` installed; KOB migration reply drafted |
| 2026-08-11 | MAV-740928: 8 obsolete dupes removed → 80/80 PASS; CDAS-12328 → Waiting to Deploy (dev verdict: inactive site id expected); MAVCVS-1021 complete with flag OFF |
| 2026-08-12 | Cascade workflows → `.devin/skills` |
| 2026-08-13 | CAM EMEA PDB read-only (ORA-16000) + GB "ENG" province regression; EMEA hub 5455/909/551 probe; CDAS defects drafted (ORA-16000 P1/P2, TestDBCalls NRE) |
| 2026-08-14 | Hub/OnPrem pubsub failure analysis (49 failures; Bucket A misdiagnosis retracted; contact-method staleness real); MAVCPDCUS blank-areaCode NRE + MAVCVS areaCodeRule mismatch defects drafted; ORA-06531 add-site + inactive party-contact defects drafted |
| 2026-08-17/18 | 145-party E2E batch created; AskUserQuestion rule; PROD probe flipped clone launchSettings |
| 2026-08-19 | **MAV_0402 hub fixes rounds 1–6** (91→23→14→6→2→0 failures; CustomerInfo.json write race root cause; `GetTransactionCount_Hub_AllEvents`); commits 089fd37d…f848d49d; Consolidate_OnPrem_Failure_Report |
| 2026-08-20 | CAS primary switch verified; real-address fix for Fusion HZ-120485; `deactivate-parties` reworked (DEACT_* knobs, valid LOV) → 145/145 G4 parties D/EOL |
| 2026-08-24 | Rules v3 non-prod deprecation date (NOT applied) |
| 2026-08-25 → 08-27 | **1002 campaign** (Plan MAV-770748/Exec MAV-770749, 11 tests) → v5 probes (UAE/SK/DE) → descope (5 tests Obsolete) → MAVCVS-1045/1046 + MAVCDAS-3163 filed then all **cancelled** (UAE/SK moved to December; 3012 not implemented); reports-in-terminal + no-unsolicited-comments rules; PROD 11 parties deactivated |
| 2026-08-28 | GE1 access proven (G4 secret works on G1; env override only via build-output launchSettings); CDUI G1 seed party |
| 2026-08-31 | rv3 still served past deprecation → MAVCVS defect drafted with evidence bundle; partner profile-contact recipe verified |
| 2026-09-01 | MAVCVS-1035 paymentGroup verified NOT live (inert field); version 3→5 commit in MAV_0402 |
| 2026-09-02/03 | CN O/COMM suburb 400 (Loqate license) → MAVCVS-1053 cancelled, story MAVCVS-1054; CNSR create payload fix (XXXX padding removed); Fusion JBO-26075 + repush in-flight defects misrouted to MVKCDHCP-2794/2795 → cancelled → refiled **MAV-776962 / MAV-776983**; ErrorPattern PROD report |
| 2026-09-04 | **In-E2E 1002 batch** (8 MAVCDAS stories): isSalesHold 4/4 PASS, 3136/3139/3141 FAIL, 3184 blocked; **Partner onboarding KT (Harshitha, ARD)**; MAV_0402 "CN changes" commit c268b79a |
| 2026-09-06 | CDUI project memory snapshot (219 automated cases) |
| 2026-09-07 | This workspace memory pack created |

Upcoming: orchestration revamp ~2026-09-09; Rules v3 prod deprecation 2026-10-02; December release for UAE/SK e-invoicing; KOB cutover (repoint automation configs from `cilmsnp` to `cilms-np`).

## 5.2 Campaign: FY27FW19-0602 (status 2026-05-21)

| Story | Title | JIRA Tests | C# | Result |
|---|---|---|---|---|
| MAVCVS-877 | BR CNPJ alphanumeric | 4 (MAV-666848..851) | 4 | 3/4 (special chars accepted — open question) |
| MAVCVS-879 | RESCOM validate on updateSite | 6 (MAV-666523..528, 542) | 4 | 2/4 → later: valid values are `Res`/`Com` |
| MAVCVS-881 | SA Address Line 4 non-mandatory | 6 (MAV-666537..542) | 5 | 0/5 (base payload AL3 `^\d{4}$`) → 5/12 after regen |
| MAVCVS-904 | India payment terms I1/I2/I3 | 5 (MAV-666951..958) | 4 | 0/4 (segment codes / isTermsPayment pairing) |
| Also validated 05-25 | MAVCVS-889, 902 (MY BRN 5/5), 853, MAVCDAS-2854, 2830; MAVCVS-913 blocked (payload shape) | | | |

## 5.3 Campaign: FY27FW29-0803 — Test Plan MAV-740928 / Exec MAV-740929

Live state 2026-08-11: **80 test runs, all PASS** (8 Obsolete dupes removed); true = 78 PASS / 2 FAIL (MAV-743225/743227 = MAVCVS-1013 TC-02/03 never flipped).

| Story | Owner | TCs | Verdict |
|---|---|---|---|
| MAVCVS-1004 MX generic RFC XAXX/XEXX | Muneer | 4/4 | PASS |
| MAVCVS-1006 CO postal+state mandatory | Muneer | 4/4 | PASS (orchestration derivation; 1020 closed) |
| MAVCVS-1009 Segment uppercase | Muneer | 4/4 | PASS |
| MAVCVS-1011 CO SAP state rename | Snehlata | 3/3 | PASS incl. Fusion UI |
| MAVCVS-1012 KR address 30 bytes/chars | Nilesh | 5/5 | PASS |
| MAVCVS-1013 Status I→D update APIs | Muneer | 2/4 | TC-02/03 FAIL (relationship path) — see MAVCPDCUS-1132 |
| MAVCVS-1015 FR "Côte"→"Cote" | Snehlata | 3/3 | PASS |
| MAVCDAS-2962 MY e-invoicing override coexistence | Muneer | 4/4 | PASS (OldCo DCN 307405565133) |
| MAVCDAS-2980 France VAT/SIREN/SIRET | Muneer | 6/6 | PASS |
| MAVCDAS-2992 Post LegacyMap DOMS | Muneer | 3/3 | PASS (write→read 555/888/777) |
| MAVCDAS-2994 Get LegacyMap DOMS | Muneer | 3/3 | PASS |
| MAVCDAS-2996 Get PartyMap DOMS (6 LATAM BUs) | Muneer | 2/6 | MX+BR verified; AR/CL/CO/PE no data → accepted |
| MAVCDAS-3068 PUT LegacyMap CreatedBy/ModifiedBy | Muneer | 4/4 | PASS |
| MAVCDAS-3026 KOB URL in Layer7 | Suresh | 2/2 | PASS |
| MAVCVS-931 FR EAN as SIREN | Muneer | 4/4 | PASS |
| MAVCVS-957 TR state mandatory | Muneer | 3/3 | PASS |
| MAVCVS-996 BR name alignment | Muneer | 3/3 | PASS |
| MAVCPDCUS-1090 PO-Box shipping block | Shomari | 3/3 | PASS |
| MAVCPDCUS-1082 siteName backfill | Sudhakar | 3/3 | PASS |
| MAVCVS-1000/1001 Payment Terms V4 create/update | Muneer | 5/5, 4/4 | PASS after 1019 cancelled / 1021 shipped flag OFF |
| Added late: MAVCPDCUS-1102 (I→D DB), MAVCVS-1021, MAVCVS-1022 (UY suburb), MAVCDAS-3005 (infra) | | | |

Descoped from 0803 E2E (2026-08-05/06, E2E Required → No, cc Sondra_Saenz + Rafael_Parise): MAVCDAS-3064, 3010, 3063, 3011, 3009, 3077, 3096, 3094, 3095, 3012, 3102, 3060, 3082; CDAS-12388, 12365, 12370, 12384, 12328.

Regression port (MAV_0402 `Sprint0803`, 28 cases green 2026-08-06): MAVCVS-1009/1004/996/1013(TC-01)/957/1015/1006/1011/1012, CVS-1587, MAV-743860 (Delivery), MAVCDAS-2980+MAVCVS-931, MAVCDAS-2996/2994+2992/3068, MAVCPDCUS-1082/1090. Not ported: 1000/1001; excluded permanently: RPL/trade, OldCo/CAM.

## 5.4 Campaign: FY27FW36-1002 — Test Plan MAV-770748 / Exec MAV-770749

11 tests MAV-770753/754/755/756/758/761/762/763/764/765/767 for MAVCVS-1002/948/939/940/942/943/936/937 + MAVCDAS-3065/3086/3012.

| Test | Story | Result |
|---|---|---|
| MAV-770764 | MAVCDAS-3065 CAS on D/EOL site blocked | **PASS** (400 "Party Site is Inactive… EOL") |
| MAV-770765 | MAVCDAS-3086 MODIFIED_BY/DATE by ORCH LAYER, no dup keys | **PASS** |
| MAV-770767 | MAVCDAS-3012 BR first-8 CNPJ mismatch | FAIL ×3 → **deferred** (not implemented; MAVCDAS-3163 cancelled by Suresh) |
| MAV-770758 | MAVCVS-942 UAE CRN mandatory | FAIL → **not a defect** (MAVCVS-1041 makes it optional; UAE/SK → December); MAVCVS-1045/1046 cancelled |
| MAV-770755/761 | MAVCVS-939 TIN / 943 CRN update (UAE) | PASS but December scope |
| MAV-770753/754/756/762/763 | 1002 DE TIN / 948 DE EAN / 940 UAE EAN / 936 SK DIC / 937 SK EAN | **Obsolete** — validations removed from dev (Mani/Manoj 08-26) |
| Dupes | MAV-770771, MAV-771465 (942) | Obsolete, Duplicate-linked |

In-E2E batch 2026-09-04 (8 MAVCDAS stories, party P15804901359 / D11320931069): 3118/3120/3122/3138 isSalesHold **PASS** (but invalid "X" persists → defect candidate); 3136 payment-term dedup UDT **FAIL** (paymentGroup inert); 3139 case-sensitive IDs **PARTIAL FAIL** (lowercase DCN ok, lowercase party id not); 3141 replication-requestor end-dated guard **FAIL** (201 on D/EOL site); 3184 VCID relationship role **BLOCKED** (no VCID party). No Xray tests created yet for this batch.

## 5.5 Other verified outcomes (quick list)

- Trade screening (GTM/RPL) LIVE on G4: ~80–110 s create→verdict; MY/MX screened, US never; W005/W006 masking; all 19 account writes blocked for denied party as of 08-05; open: N1 enumeration oracle, N2 GET acct relationships 400-null for all accounts, contact GET PII leak (unfiled).
- Delivery purpose live for all countries (2026-07-30); Mailing for JP/CN.
- KOB == PCF parity (2026-08-06); PCF removal confirmed to Sondra/Mahesh.
- CDAS-12380 PATTEN removal PASS; CDAS-12328 dedupe → dev-accepted behaviour.
- Customer Reporting JW compare: G4 88,600 rows 0 mismatches; PROD 1,105 rows (name-order & designator artifacts explain sub-85).
- MAV_0402 hub run: 91 → 0 failures after six fix rounds (all automation-side).
- Partner record recipe verified (profile contact usageType Sales|PartnerContact); `partner_contact` outbound needs ARD-minted partner account.
- CDUI UI automation: 219 automated cases, 120 regression checks, P1 96, read-only golden rule (separate memory in `C:\repos\CDUI\CDUI_PROJECT_MEMORY.md`).
