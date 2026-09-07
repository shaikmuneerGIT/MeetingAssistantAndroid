# 02 — Project, Domain & Architecture

## 2.1 What CIDM MAV is

**CIDM** = Customer Identity Data Management. **MAV / Maverick** = Dell's program replacing the legacy customer master (**OldCo** = CAM / CAM_NEXT / GOP / CILMS) with the new platform (**NewCo** = **CPDn** / Cardinal / Primary Customer Gateway **PCG**). The two coexist ("coexistence"); OldCo DCNs migrate to NewCo parties via the CPD orchestration workflow (`DCNMigrationCreateCustomer`), bridged by legacyMap / partyMap / UCID tables.

Domain objects (NewCo IDs):

| Entity | Prefix | Example | Notes |
|---|---|---|---|
| Party (customer) | `P` | P15804901359 | Org or Person; `customerPartySubType` O/R/I |
| Party Site | `S` | S16006203553 | purposes Billing / Shipping / Mailing / Delivery, isPrimary per purpose |
| Party Contact | `R` | R13751301852 | responsibilities mirror site purposes |
| Customer Account (**DCN**) | `D` | D11320931069 | `dellCustomerNumber`; per BU; payment terms, tax profile |
| Cust Account Site | `L` | L11056719314 | account-level site (CAS) |
| Cust Account Contact | `C` | C10397026186 | |
| Person party | `P` | P15804845689 | |
| Transaction ID (Fusion) | numeric | 38798877884508488 | |
| Fusion party id | numeric | 300004774792342 | |

ID series tells you the environment: **G4 = P158x / D113x / S160x**, **G1 = P100x / D100x / S107x**, **PROD = P145x**. OldCo DCNs are 12-digit (e.g. 640402258686 AMER, 631800057474 LATAM, 307405565133 APJ, GB6408001144 EMEA); legacy DOMS DCNs are 9-digit.

Key downstream/adjacent systems:

| System | Role |
|---|---|
| **CPDn / PCG** (`primary-customer-gateway`) | NewCo REST API (`/v1/customers`, `/v1/customerAccounts`, `/v1/legacyMap`, `/v1/partymap`, `/v1/admin/...`). Response `source: CPDn` |
| **CVS / CMVS Rules API** (`customer-rules-api-g4.cmvs-r3-np.kob.dell.com`) | Customer Validation Services — per-country rulesets (versions 3/4/5). Validation errors show `source: CVS-ElasticSearch` |
| **CAM_NEXT gateway** (`customer-gateway-api-g4`) | OldCo REST; response `source: CAM_NEXT_AMER/_LATAM/_EMEA/_APJ`; header `rules_version: 16` (underscore) |
| **Fusion (Oracle ERP)** | Outbound replication: `CPD_PUBSUB.FUSION_OUTBOUND`; sub-transaction types party_org, party_person, location, party_site_siteuses, party_relationship, org_contactpoints, cust_acct, acct_site_siteuses, acct_contactpoints, acct_site_contact_role_resp, cust_acct_relationship, org_org_relationship; backfeed via `OCDM_OCI_IN` / `Fusion_backfeed` |
| **Hub / OnPrem PubSub** | `CPD_PUBSUB.TRANSACTION_LOG_EBR`, `TRANSACTION_LOG_ON_PREM_EBR`, hub staging; Kafka topics |
| **CERN** (entity resolution, D&B) | Only writer of DUNS/HQ_DUNS; dedupe on name+address(+segment/class/usage); read-only via orchestrator gateway |
| **CPD Orchestration** (`cpd-workflow-builder-ge4.cpd-orchestration-dev-a1-np.kob.dell.com`, UI `cpd-workflow-ui-ge4`) | Workflows: DCNMigrationCreateCustomer, PartnerCreation, build-partner-hierarchy |
| **GTM / RPL** (trade screening) | Restricted Party List screening; statuses RPLS_SENTFOR_REVIEW → RPLS_PASSED / RPLS_REQUIRES REVIEW → RPLS_FAILED; masking codes W005/W006 |
| **ARD / CDMEE / CDUI** | Account Relationship Data, stewardship tickets, Customer Data UI (`g4.cdui-np.kob.dell.com`) |
| **SalesUX** (`sales-sit-g4.dell.com/salesux`) | Sales UI that creates customers via `salesapp` BFF → PCG |
| **DSUI Credit Party Manager** | Credit-party UI (`creditparty-app-sit-g4.r1.pcf.dell.com/dsui`) |
| **Loqate / AVS v2** | Address validation vendor (license downgrade Sep-2026 broke CN suburb) |

## 2.2 Repository map (main repo `C:\repos\cidm_cilms_apiautomation`)

| Path | Purpose |
|---|---|
| `CIDMAgent/Program.cs` (~17,900 lines) | ALL CLI commands (181 case labels incl. aliases). `Countries` dict, `RelationshipTypes`, `RuleVersion`, `LoadConfig()` (~L1227, overwrites shell env from build-output launchSettings) |
| `run.py` | ONLY sanctioned entry point. Validates against `TeamBot/commands.json`, runs `dotnet <CIDMAgent.dll>` (App-Control-safe), loads creds from `mcp_secrets.local.json` → `CIDM_APIAutomation/launchSettings.json` (fill-only-missing), optional `USE_SSO` token injection |
| `TeamBot/` | `commands.json` (128 whitelisted commands, `admins`), `command_guard.py`, `bot.py` (Teams outgoing-webhook FastAPI bot; `/pa-webhook` unauthenticated — never expose), `cidm.py`, `llm.py`, `speech.py`, `sp_poller.py`, `mcp_server.py`, `cidm_skill.md`, payload templates (`create_*.json`, `RPL_GB_*.json`, `CTL_*.json`, `PRIMSW*.json`, `rules_v5/`) |
| `CIDM_APIAutomation/` | xUnit test project (net6.0 here; net8.0 in MAV_0402). `TestScripts/` (175 test classes, ~1,755 test methods), `TestUtils/` (ApiMethods.cs ~1,900 lines, TestBase, AuthService, THelper/), `RequestFiles/` (payload builders: `Customer/PostCustomerMaverick.cs`, `PostCustomerMock_NewCo.cs`), `DBUtils/` (DBConnection, TestDBCalls, oracle-wallet), `DCNDtls/` (rules cache `Rules_<BU>_<CC>.json`, `CustomerInfo.json`), `TestLogs/` (every API request/response `API_*.json`, `Request_<CC>_*.json`), `launchSettings.json` (gitignored creds; template `launchSettings.template.json`) |
| `CIDMMcpServer/` (net9) + `CIDMMcpClient/` | MCP server exposing ~30 tools (no JIRA); `Tools/*.cs`, `CidmRunner.cs` |
| `XRetry/` | xUnit retry attributes (`RetryFact`/`RetryTheory`) |
| `scripts/` | ~55 committed Python helpers (defect analytics, e2e pipeline, Xray, Confluence, JW compare, CAM_NEXT ops, bulk excel…) |
| `.devin/` | `skills/<name>/SKILL.md` (~95), `agents/` (9 sub-agents), `knowledge/` (claude_memory.md mirror, jira_stories.json, confluence_pages.json, current_sprints.json, 2 MOMs), `specs/` + `plans/` (per-story markdown by bucket) |
| `.windsurf/` | `knowledge/` (the copy the refresh scripts actually write), `specs/`, `plans/` (mirrors); `.windsurfrules` (always-on AI contract) |
| `docs/` | MAV_TOOL_OVERVIEW, TEAM_ONBOARDING, WORKFLOWS(+GUIDE/QUICKSTART/CATALOG), CIDMAgent_System_Prompt, MCP setup + whitelisting request, API_Calling/, DB_Calling/, JIRA_Calling/ templates, Consolidate_OnPrem_Failure_Report_2026-08-19.html |
| `mabl/` | MABL BVT suite export (26 flows × 8 countries), SalesUX + orchestration browser suites, Postman collection |
| `webapp/` | CIDM Workflow Portal (FastAPI, port 8090, headless Claude agent engine, GenAI chat) |
| `DellSalesUX_PlaywrightCS/`, `DSUI_Automation/` | Playwright C# UI suites (SalesUX create; DSUI Credit Party Manager + older SpecFlow suite) |
| `Email/`, `TrxToHtml/`, `TrxToHtmlReport/` | report tooling |
| Root `EXECUTE_REPORT_*.md`, `DEFECT_ANALYSIS_REPORT_*`, `STATUS_REPORT_FY27FW19-0602.md`, `defect_*.json`, `defect_evidence/`, `logs/` (scheduled aging/chase logs), `TempPayloads/`, `test_case_payloads/` | execution evidence and artefacts |
| `CIDM_Bulk_Customer_Create_Template.xlsx` | bulk create template (5 sheets) |
| `verification/v4-rollout/` | v4 rules verification runner |
| `refresh_*.ps1`, `generate_specs_from_jira.py`, `build_knowledge_index.py`, `fetch_current_sprints.py`, `find_newco_id.py`, `setup_mcp.ps1` | maintenance scripts |

Git: remote `origin` GitLab; notable branches `main`, `MAV_POC` (workflow/tooling history Apr–Jul 2026), `MAV_0402` (CI regression), `CDM_MAV_TEST_g4` (current), `CAM_NEXT_*` regional OldCo families, `CDM_Maverick_*`, `DSUI`, `cidm-mcp-server` (orphan, secrets-free). Working tree currently has ~40 modified + 273 untracked (skills/agents) + 413 deleted (old `.devin/workflows/*.md` replaced by skills) — uncommitted.

## 2.3 Stack

| Layer | Technology |
|---|---|
| CLI / API harness | .NET 6/8, C# (`CIDMAgent`), Newtonsoft, Bogus faker |
| Tests | xUnit (+XRetry), Shouldly; Playwright (UI); SpecFlow (legacy DSUI) |
| DB | Oracle 19c PDBs, ODP.NET, wallet at `DBUtils/oracle-wallet/`, TCPS |
| Messaging | Kafka / Pub-Sub |
| Scripting | Python 3.12 (requests often missing → urllib), PowerShell 5.1 |
| CI/CD | GitLab (Windows PowerShell runner) |
| Browser automation | Playwright (.NET) + Playwright MCP (`npx @playwright/mcp@latest`), Edge channel for SSO |
| AI | Claude Code, Devin, (Windsurf retired), Dell GenAI gateway (`aia.gateway.dell.com`), OpenAI (meeting app) |
| Email | Outlook COM via PowerShell (only path from desk); `smtp-dev.dell.com:587` with `svc_npe2etesting` for defect-chase |

## 2.4 Environments & endpoints

| Env | Purpose | Key hosts |
|---|---|---|
| **G4 / GE4** (SIT) | primary write environment | PCG PCF `https://primary-customer-gateway-g4.cilmsnp.us.dell.com` · PCG KOB `...-g4.cilms-np.us.dell.com` (KOB at full parity, cutover in progress) · CAM_NEXT `https://customer-gateway-api-g4.cilmsnp.us.dell.com/` · Rules `https://customer-rules-api-g4.cmvs-r3-np.kob.dell.com` · OldCo rules `rules-api-g4.cilmsnp.us.dell.com` · Token `https://www-sit-g4.dell.com/di/api/v3/oauth/token` · Orchestration builder `cpd-workflow-builder-ge4...` / UI `cpd-workflow-ui-ge4...` / orchestrator gateway `cpd-workflow-orchestrator-ge4...` · CDUI `https://g4.cdui-np.kob.dell.com/` · SalesUX `https://sales-sit-g4.dell.com/salesux/` · DSUI `creditparty-app-sit-g4.r1.pcf.dell.com/dsui` |
| **G1 / GE1** | secondary SIT (CDUI G1, seed party P10016062323) | `primary-customer-gateway-g1.cilmsnp.us.dell.com`, token `www-sit-g1.dell.com/...`; G4 client secret works on G1; `TEST_ENVIRONMENT` must stay GE4 (ConfigSettings throws on GE1) — override by editing build-output launchSettings |
| **G2 / GE2** | OldCo AMER CI (`C:\repos\AMER`) | `customer-gateway-api-g2.cilmsnp.us.dell.com` |
| **PROD** | read-only reports only | DB `cpdppr4dbscn.amer.dell.com:1523/cpdp_ro.prd` user `CPD_READ_ONLY`; PROD gateway `primary-customer-gateway-api.cil-ms.us.dell.com`; PROD SECRET2 client is dead (401) → needs pre-fetched token (`PRE_OBTAINED_TOKEN`/`CIDM_PROD_TOKEN`) |
| DAIS token quirk | JSON body `{"grant_type":"client_credentials"}` with `Content-Type: application/json` → valid JWS; form-urlencoded → token gateway rejects (IDX12709) |

## 2.5 Oracle databases (no passwords recorded here)

| Schema / conn | Host | Service | User | Notes |
|---|---|---|---|---|
| GE4 default (CPD_PUBSUB) | `cpd4sepe1dbscn.amer.dell.com:1523` | `cpd4ss.sit.amer.dell.com` | `CPD_PUBSUB` | cross-schema SELECT on CPD_PARTY/CPD_CUSTACCT; CREATE TABLE (staging allowed) |
| GE4 CPD_CUSTACCT | same | same | `CPD_CUSTACCT` | use for migration-table reads (owner CPD_PARTY blocked by classifier) |
| PROD | `cpdppr4dbscn.amer.dell.com:1523` | `cpdp_ro.prd.amer.dell.com` | `CPD_READ_ONLY` | SELECT only |
| CAM AMER/EMEA/APJ PROD (read-only) | `camdbpr2db-cname` / `ceapr2db-cname` / `cappr2db-cname.us.dell.com:1523` | `camnap_ro` / `ceap_ro` / `capp_ro` | `Mahesh_Timothy` | |
| CAM EMEA G4 PDB | — | — | — | was open READ ONLY (ORA-16000) 2026-08-13 |

Schemas: `CPD_PARTY` (PARTY, ORG_PARTY, PERSON_PARTY, PARTY_SITE, ADDRESS, ORG_PARTY_CONTACT, CONTACT_RESPONSIBILITY, PERSON_PARTY_CONTACTMETHOD, PARTY_SITE_PURPOSE, PARTY_SITE_IDENTIFIER, T_CAM_DCN_* migration/bridge tables, T_UCID_CPD_DATA_MIGRATION, A_* audit), `CPD_CUSTACCT` (CUST_ACCOUNT, CUST_ACCOUNT_SITE, CUST_ACCOUNT_CONTACT, CUST_ACCT_SITE_PURPOSE, CUST_ACCT_TAX_IDENTIFIER, …), `CPD_PUBSUB` (FUSION_OUTBOUND, INPUT_TRANSACTION_DETAILS, TRANSACTION_LOG_EBR, TRANSACTION_LOG_ON_PREM_EBR, PUBSUB_REPUSH_ADHOC, PARTY_GTM_SCREENING, PARTY_SITE_GTM_SCREENING, T_TRADE_API_REQUEST_LOG, MAV_* staging), `VCID_MAIN` (VIRTUAL_CUST_SITE_CONTACT), CAM: `CAM_MAIN`, `CAM_CODE` packages (PKG_CREATE_CUSTOMER_CILMS_V1, PKG_ADD_SITE_CONTACT_CILMS_V1).

Fusion status codes: SIT `S`=success `E`=error `P`=pending; PROD `Y`=success `E`=error `N`=new. PROD ~38k Fusion records/day; `location` ≈ 85% of errors; spikes 08:00–09:00 UTC (Apr-2026 observation).

## 2.6 Countries / BU IDs (CIDMAgent `Countries` dict, as of 2026-09-07)

| CC | BU | CC | BU | CC | BU |
|---|---|---|---|---|---|
| US | 108401 (US_FED 108402) | GB | 10826 | DE | 10276 |
| MX | 10484 | BR | 10076 | IN | 10356 |
| CN | 10156 | JP | 10392 | MY | 10458 |
| BE | 10056 | IT | 10380 | SA | 10682 |
| NZ | 10554 | ES | 10724 | **CA** | 10124 (added 2026-07-30) |
| **FR** | 10250 | **TR** | 10792 | **KR** | 10410 |
| **CO** | 10170 | **SK** | 10703 | **AE** | 10784 |
| **UA** | → 10826 (no own BU; validated under selling BU GB) | | | | |

Pattern: BUID = `"10"` + ISO-3166-1 numeric (except US 108401/108402). Documented "14 supported countries" in CLAUDE.md = US MX IN CN DE JP GB BR MY BE IT SA NZ ES. `create-customer CO` and `SK` crash (missing Bogus locale) → use `create-customer-json`; KR/AE generators produce bad defaults → hand-author.

OldCo / CAM_NEXT hubs: **US 11 (CAM_NEXT_AMER)**, **CA 707**, **MX 3232 (CAM_NEXT_LATAM)**, **GB 202 (CAM_NEXT_EMEA)**, **MY 4046 (CAM_NEXT_APJ)**; EMEA also 5455 / 909 / 551 (valid, SA province numeric 01–12/14, currency hub-scoped); LATAM DOMS BUs AR 4747, BR 3696, CL 8585, CO 7878, PE 1435 (read-only, not creatable). UA rides EMEA hub 202. `create-oldco-customer` supports US, MX, GB, MY (+UA, SA probe harness).

Customer types: `O` organization (default, segment COMM), `R` reseller, `I` individual (segment CNSR). Segments: CNSR / COMM / LPUB / NGOV. Classes: Direct / Partner / ServiceProvider. Usages: CUSTOMER, PARTNER, Sales Account… (class Partner is forced by usages CUSTOMER+PARTNER). Cust account types: `Cust Acct` | `Funder Account`. Purposes: Billing, Shipping, Delivery (global), Mailing (JP/CN). Relationship types: Partner To Sales (with spaces!), SalesToPartner, PartnerToFinance, FinanceToPartner, PartnerToSupplier, SupplierToPartner, SupplierToFinance, FinanceToSupplier, BillTo, ShipTo, Funder (account-level: only Funder/Floorer).

## 2.7 Rules API versions

| Version | State (as of 2026-09) | Delta |
|---|---|---|
| v2 | dead in GE1/GE4 since 2026-07-13; dies in prod at RW0803 | |
| **v3** | harness default (`RULE_VERSION=3`); **should have deprecated in non-prod 2026-08-24 but still served** (defect MAVCVS "rules version 3 NOT deprecated" 2026-08-31); prod deprecation 2026-10-02 | 244 fields (US) |
| **v4** | live GE1/GE4 since 2026-07-13; release 0803 ships `v4-17-August` | + `nameMatchRule` (BR name alignment) |
| **v5** | early-deployed GE4 2026-07-17 (RW1002 scope) | + 4 Slovakia DIC tax-attribute rules; UAE TIN/CRN, SK DIC, DE EAN/TIN e-invoicing (partially live; UAE/SK scope moved to **December** release) |
| v6 | 400 "Unsupported version(6)" | |

Header `rule-version` (NewCo gateway), `rules-version` (Rules API), body `additionalAttributes.items.rulesVersion`; some routes (relationship PATCH, account PATCH) read the BODY value only. CAM_NEXT uses `rules_version: 16` (underscore).

## 2.8 CI pipelines

**This repo (`.gitlab-ci.yml`, branches main/MAV_0402/MAV_POC, PowerShell runner):** stages preflight (vars + JIRA/OAuth smoke) → discover (`scripts/e2e_pipeline.py discover --release`) → validate (execute payloads of linked TCs) → transition (manual gate, "Waiting to Deploy") → report → chase (`RUN_DEFECT_CHASE=true` → `scripts/run_defect_chase.py`, modes dry/send). Required CI vars: JIRA_PAT_TOKEN, JIRA_BASE_URL, TOKEN_URI, CIDM_INTERNAL_ENCODEDCLIENTSECRET, RULES_API_ENCODEDCLIENTSECRET, RULES_API_BASE_URL, CUSTOMER_API_URL, CUSTOMER_API_URL2 (+SMTP_* for chase).

**MAV_0402 repo pipeline:** build → `api-smoke-test-a` (Run A per BU_ID, ~3h; filters `Category=Consolidate…`, excludes `Scenarious` and `Regression0803`) → `api-regression-test` (`Category=Regression0803`) → merge-customer-info → wait-2m → `api-smoke-test-b` (Run B hub/OnPrem, ~2h) → send-both-reports → wait-45m-fusion → `api-fusion-test` (Run C `Consolidate_FusionCheck`) → send-fusion-report → `api-trade-compliance-test` (Run D) → send-trade-compliance-report. Test categories: `Consolidate`, `Consolidate_OnPrem`, `Consolidate_FusionCheck`, `Consolidate_PubsubCA`, `BVT`, `Priority_<BU>`, `MAV_Party`, `MAV_CustAccount`, `Regression0803`, `Sprint_0404`, `APJ/EMEA`, `CAM_NEXT_APJ`…

xUnit runs 8 parallel collections → `CustomerInfo.json` write race fixed 2026-08-19 (`CustomerInfoFileLock`, commit ab189e28 in MAV_0402).

## 2.9 Data files that matter

- `CIDM_APIAutomation/bin/Debug/net6.0/DCNDtls/CustomerInfo.json` — live customer data from last run (keys `CreateParty_<BU>`); cleared per Consolidate run.
- `DCNDtls/Rules_<BU>_<CC>.json` — flattened rules cache (must be a JSON **array**; raw object = corrupted). `get-rules` overwrites it each run.
- `TestLogs/Request_<CC>_<ts>.json` — canonical create payload templates (~4 KB; a 1.5 KB payload is wrong-shaped).
- `TestLogs/API_*.json` — raw captured request/response = defect evidence.
- `TestLogs/Jira_<KEY>_*.json` — raw JIRA issue JSON (source of custom-field option values).
