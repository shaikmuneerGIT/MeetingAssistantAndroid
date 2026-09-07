# CIDM MAV — COMPLETE WORKSPACE MEMORY (single-file pack)

_Generated 2026-09-07 by `WORKSPACE_MEMORY/build_single_file.py` from 14 source files. Edit the source files, not this file._

## Table of contents

- [WORKSPACE_MEMORY — CIDM MAV "second memory" pack](#workspace-memory-cidm-mav-second-memory-pack)  ·  `README.md`
- [01 — Identity, Role & Ground Rules](#01-identity-role-ground-rules)  ·  `01_identity_and_ground_rules.md`
- [02 — Project, Domain & Architecture](#02-project-domain-architecture)  ·  `02_project_and_architecture.md`
- [03 — Commands, Skills, Agents, Scripts & Tooling](#03-commands-skills-agents-scripts-tooling)  ·  `03_commands_skills_and_tooling.md`
- [04 — People, Teams, JIRA Structure & Releases](#04-people-teams-jira-structure-releases)  ·  `04_people_jira_and_releases.md`
- [05 — Timeline & Test Campaigns (what happened, when, with what result)](#05-timeline-test-campaigns-what-happened-when-with-what-result)  ·  `05_timeline_and_test_campaigns.md`
- [06 — Defects Log & Open Items](#06-defects-log-open-items)  ·  `06_defects_and_open_items.md`
- [07 — Technical Knowledge Base (hard-won facts, condensed; one bullet per memory)](#07-technical-knowledge-base-hard-won-facts-condensed-one-bullet-per-memory)  ·  `07_technical_knowledge_base.md`
- [08 — Meetings, KT Sessions & Decisions (minutes and takeaways)](#08-meetings-kt-sessions-decisions-minutes-and-takeaways)  ·  `08_meetings_and_kt_notes.md`
- [09 — How I work / standing preferences (for any assistant or teammate)](#09-how-i-work-standing-preferences-for-any-assistant-or-teammate)  ·  `09_working_preferences.md`
- [10 — Glossary (acronyms & jargon heard in meetings)](#10-glossary-acronyms-jargon-heard-in-meetings)  ·  `10_glossary.md`
- [11 — Sibling Projects & Repos I own or touch](#11-sibling-projects-repos-i-own-or-touch)  ·  `11_sibling_projects.md`
- [12 — Devin Skills & Agents Catalog (complete)](#12-devin-skills-agents-catalog-complete)  ·  `12_devin_skills_catalog.md`
- [WORKSPACE_MEMORY — Changelog](#workspace-memory-changelog)  ·  `CHANGELOG.md`


<!-- ===== SOURCE FILE: README.md ===== -->

# WORKSPACE_MEMORY — CIDM MAV "second memory" pack

**Purpose:** a complete, self-contained memory of Shaik Muneer's CIDM MAV workspace — project, architecture, tooling, people, JIRA structure, releases, campaigns, defects, hard-won technical facts, meeting notes, working preferences — written so a **meeting assistant or any LLM can answer questions and brief me from these files alone**. Snapshot date **2026-09-07**. No secrets are stored here (hostnames/usernames only; passwords, tokens, client secrets are deliberately omitted).

## How to use

| Need | Use |
|---|---|
| One file to load into a meeting assistant / LLM context | **`CIDM_MAV_COMPLETE_MEMORY.md`** (generated — all topic files concatenated, ~150 KB / ~1,300 lines) |
| Focused reading / editing | the numbered topic files below |
| Regenerate the single file after edits | `python WORKSPACE_MEMORY/build_single_file.py` (`--check` for stats) |
| Deep evidence trail behind a fact | `.devin/knowledge/claude_memory.md`, Claude memory dir `%USERPROFILE%\.claude\projects\c--repos-cidm-cilms-apiautomation\memory\`, root `EXECUTE_REPORT_*.md`, `TestLogs/` |

## Files

| File | Contents |
|---|---|
| `01_identity_and_ground_rules.md` | who I am, workspace, CRITICAL RULES, elevator pitch |
| `02_project_and_architecture.md` | domain (NewCo/OldCo/Fusion/CERN/RPL…), repo map, stack, environments & hosts, databases, countries/BUs, rules versions, CI pipelines, key data files |
| `03_commands_skills_and_tooling.md` | run.py mechanics, all 128 whitelisted commands, env knobs, skills (grouped), agents, scripts catalog, knowledge bases, MCP/webapp/Teams/MABL |
| `04_people_jira_and_releases.md` | people directory, JIRA projects & statuses, custom-field/option IDs, Xray endpoints, defect routing, releases & Test Plan/Exec keys, sprints, dashboards |
| `05_timeline_and_test_campaigns.md` | 2026 chronology; 0602 / 0803 / 1002 campaign results; other verified outcomes |
| `06_defects_and_open_items.md` | defects filed (with outcomes), unfiled candidates, open items / next steps |
| `07_technical_knowledge_base.md` | every gotcha and contract fact, condensed by area (env, auth, payloads, countries, rules, OldCo, Fusion, RPL, JIRA, DB, UI) |
| `08_meetings_and_kt_notes.md` | recurring meetings, MOMs (trade screening 07-16, partner onboarding KT 07-24 and 09-04), dated decisions log |
| `09_working_preferences.md` | how I want assistants/teammates to work with me |
| `10_glossary.md` | acronyms & jargon |
| `11_sibling_projects.md` | other repos/projects (MAV_0402, AMER/KOB, CDUI, MCP server, MeetingAssistantAndroid…) |
| `12_devin_skills_catalog.md` | every Devin skill with its description + the 9 sub-agents |
| `CHANGELOG.md` | update log |
| `build_single_file.py` | generator for the single file |

## How to keep it updated (routine)

1. After a session with new findings: add/adjust bullets in the relevant topic file (technical fact → `07`, decision/meeting → `08`, defect → `06`, timeline event → `05`, new person → `04`, new skill/command → `03`/`12`).
2. Append a row to `CHANGELOG.md`.
3. Run `python WORKSPACE_MEMORY/build_single_file.py` and copy `CIDM_MAV_COMPLETE_MEMORY.md` wherever the meeting assistant reads it (e.g. into `C:\repos\MeetingAssistantAndroid\docs\`).
4. Also keep the Claude memory dir + `python scripts/sync_claude_memory_to_devin.py --write` in sync (the long-form source).
5. Ask Claude Code: "update the workspace memory pack with today's work" — it knows this structure.

## 60-second meeting brief (current state, 2026-09-07)

- **Release 1002 (FY27FW36):** Test Plan MAV-770748 / Exec MAV-770749. Genuine passes: MAVCDAS-3065, 3086. UAE/SK e-invoicing scope moved to December; 3012 (BR first-8 CNPJ) not implemented. In-E2E batch 09-04: isSalesHold stories PASS (invalid value accepted — candidate defect), 3136/3139/3141 FAIL, 3184 blocked (need VCID party). Xray tests for this batch still to be created.
- **Open defects I'm driving:** MAV-776962 (Fusion JBO-26075, Sev 2), MAV-776983 (repush in-flight duplicates), MAVCPDCUS-1132 (relationship status `I` nulls), rv3 not deprecated on G4 (draft), MAVCDAS-3031 held (GET acct relationships 400-null).
- **Environment watch:** CN O/COMM creates broken by Loqate license (MAVCVS-1054 pending); Rules v3 prod deprecation 2026-10-02; KOB cutover pending config repoint; orchestration revamp ~09-09.
- **Automation health:** MAV_0402 hub run brought from 91 → 0 failures (Aug 19); Regression0803 pack 28/28; CDUI suite 219 cases (120 regression checks); weekly defect-analysis report + daily aging report running.
- **Rules:** run.py only, G4 only, DB read-only, no unsolicited JIRA comments, reports as tables, I push commits.


<!-- ===== SOURCE FILE: 01_identity_and_ground_rules.md ===== -->

# 01 — Identity, Role & Ground Rules

## Who I am

| Item | Value |
|---|---|
| Name | **Shaik Muneer** (display "Muneer, Shaik") |
| Work email | s.muneer@dell.com |
| JIRA login (Dell JIRA Server) | `Shaik_Muneer` (key JIRAUSER33126) — NOT `s_muneer` / `s.muneer` |
| Role | E2E / SIT test automation lead for **CIDM (Customer Identity Data Management) – MAV (Maverick) program**, Dell. JIRA team **MAV-CUST**; org **CIE** (Customer Information & Engagement) |
| Admin authority | Sole admin of `TeamBot/commands.json` (the whitelisted command registry). Only I can add commands or send defect emails |
| Manager | **Ananda** (gives defect-chase routing directives) |
| Closest collaborators | Mahesh T (Mahesh Timothy) — co-owner of defect-analysis, trade/partner coverage; Kiran Kumar Avsn; Nishanth K |
| Recurring meetings | Tuesday team sync-up (I share the weekly defect-analysis report); daily 6:30–7:00 triage call hosted by Tripti (partner/orchestration) |
| Workstation | Windows 11 Enterprise, Git Bash + PowerShell 5.1, Python 3.12 (user-local), .NET SDK 6.0.428 + 8.0.423 user-local at `%LOCALAPPDATA%\Microsoft\dotnet` (machine dotnet has runtimes only) |
| AI tooling | Claude Code (primary, this memory), Devin (`.devin/` skills/agents/knowledge), Windsurf (dropped by team 2026-06-05; `.windsurfrules` retained as always-on contract; Cascade workflows migrated to `.devin/skills/` on 2026-08-12) |

## Primary workspace

- **Main repo:** `C:\repos\cidm_cilms_apiautomation` — GitLab `https://gitlab.dell.com/des/cidm/cidm_cilms_apiautomation.git`
- **Current branch:** `CDM_MAV_TEST_g4` (ad-hoc / exploratory G4 testing harness; carries Program.cs tooling changes)
- **Regression repo (CI branch):** `C:\repos\cidm_cilms_apiautomation_MAV_0402` — branch `MAV_0402` (net8.0; real `TestDBCalls`; GitLab pipeline Run A/B/C/D). Every tested story is ported here as an xUnit test (`Sprint0803/`, Category `Regression0803`).
- **Worktree:** `C:\repos\cidm_mav0402_fix` (branch `MAV_0402_hubfix`) — scratch for hub fixes, now redundant.
- Other repos under `C:\repos`: `AMER` (CAM_NEXT_AMER_GOP), `CAM_NEXT_APJ_KOB`, `CAM_NEXT_EMEA_KOB`, `CAM_NEXT_LATAM_KOB`, `CDUI` (CDUI UI automation, has its own `CDUI_PROJECT_MEMORY.md`), `cidm-mcp-server`, `oldconewco`, `MeetingAssistantAndroid` (my Kotlin/Compose meeting assistant app — the consumer of this memory pack).

## CRITICAL RULES — never break (from CLAUDE.md)

1. **Every CIDM operation goes through `cidm-agent`**: (a) `cidm-agent` MCP tools when enabled, else (b) `python run.py <command> [args]`.
2. **NEVER** `dotnet run` directly, **NEVER** call `CIDMAgent.exe`/`CIDMAgent.dll` directly, **NEVER** bypass `run.py`.
3. **NEVER** create commands not in `TeamBot/commands.json`; **NEVER** modify `TeamBot/commands.json`, `TeamBot/command_guard.py`, `TeamBot/bot.py`. New command request → "Only the admin can add new commands. Contact s.muneer@dell.com".
4. **Defect emails are ADMIN-ONLY** (`/defect-chase-email`, `/defect-aging-report --send`, `/defect-rootcause-analysis --send`). Non-admin → "Only the admin can send defect emails. Contact s.muneer@dell.com."
5. **Environment:** `TEST_ENVIRONMENT=GE4` (only G4 supported for writes); PROD allowed for read-only reports only. Build configuration `Debug`.
6. **G4 DB write safety:** every existing table is read-only (SELECT only). Writes only to NEW `CPD_PUBSUB.MAV_*` staging tables, dropped only by explicit name.
7. **MAV_0402 regression pack = NewCo only.** Never add OldCo/CAM_NEXT or trade/RPL cases there.
8. **Reports are delivered as markdown tables in the terminal reply** — no Artifact pages.
9. **No unsolicited JIRA comments** on stories or test cases; only when explicitly asked. Setting Xray run statuses and attaching payloads during a requested execution is fine.
10. **Never `git push`** on my behalf — stage/commit locally, I push. No `Co-Authored-By: Claude` trailers, no "Generated by Claude/AI" attribution anywhere (reports, emails, JIRA, commits).
11. **Never `git add -A`** — `CIDM_APIAutomation/launchSettings.json` holds live tokens; stage paths explicitly.
12. Do **not** use the AskUserQuestion tool with me — state tradeoffs in prose, pick the conservative option, proceed.

## Elevator pitch (for a meeting)

> I own E2E/SIT test automation for Dell's CIDM MAV program (NewCo/CPDn customer party platform + OldCo/CAM_NEXT coexistence). The harness is a .NET CIDMAgent CLI (128 whitelisted commands via `run.py`), an xUnit regression suite (~1,750 tests, GitLab CI on branch MAV_0402), ~95 Devin skills, a local JIRA/Confluence knowledge base, Xray test-plan/execution automation, Fusion-outbound monitoring/reports, defect analytics (weekly report approved by management), and a CDUI Playwright UI suite. Environment is G4 (GE4). I file defects across MAVCVS / MAVCDAS / MAVCPDCUS / MAV and run release test campaigns (0602 → 0702 → 0803 → 1002).


<!-- ===== SOURCE FILE: 02_project_and_architecture.md ===== -->

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


<!-- ===== SOURCE FILE: 03_commands_skills_and_tooling.md ===== -->

# 03 — Commands, Skills, Agents, Scripts & Tooling

## 3.1 `run.py` — the only entry point

```
python run.py                      # lists all whitelisted commands
python run.py <command> [args...]  # validates against TeamBot/commands.json, runs dotnet <CIDMAgent.dll>
```

- DLL search order: `CIDMAgent/bin/Debug/net6.0/CIDMAgent.dll` → `CIDM_APIAutomation/bin/Debug/net6.0/CIDMAgent.dll` (the one that exists; CIDMAgent.csproj OutputPath points there) → Release. Falls back to `dotnet run` (App-Control-risky) only if no DLL — build first: `dotnet build CIDM_APIAutomation/CIDM_APIAutomation.csproj` then `dotnet build CIDMAgent/CIDMAgent.csproj`.
- Needs user-local dotnet on PATH: `export DOTNET_ROOT="$LOCALAPPDATA/Microsoft/dotnet" && export PATH="$LOCALAPPDATA/Microsoft/dotnet:$PATH"` (bash) / `$env:DOTNET_ROOT=...` (PS).
- Creds precedence: real env → `mcp_secrets.local.json` → `CIDM_APIAutomation/launchSettings.json`. BUT `LoadConfig()` in Program.cs then overwrites env from the **build-output** `bin/Debug/net6.0/launchSettings.json` — after ANY secret rotation sync 3 places (source → all `**/bin/**/launchSettings.json` → `mcp_secrets.local.json`).
- `find-newco-id` is Python-only (Playwright scrape of CPD workflow UI).
- `PYTHONIOENCODING=utf-8` when piping (JIRA titles have non-cp1252 chars). PowerShell drops `""` args and mangles nested quotes — use `--%` or single-quoted JQL in bash.

### Env knobs read by CIDMAgent
`RULE_VERSION` (default 3), `RULES_BUID` (probe other BU with get-rules), `RULES_CLIENT_APP_NAME`, `CLIENT_APP_NAME` (client-app-name header, MAVCDAS-3068), `OLDCO_RULE_VERSION`, `OLDCO_ADDR_LINE1`, `UA_POSTAL`, `SA_HUB/SA_PROVINCE/SA_POSTAL/SA_CURRENCY/SA_CATEGORY/SA_EUA/SA_CHANNEL/SA_SEGMENT`, `DEACT_REASON_CODES/DEACT_SKIP_ACCOUNT/DEACT_API_URL/DEACT_TOKEN_URI`, `PRE_OBTAINED_TOKEN`, `CIDM_PROD_TOKEN`, `TEST_ENVIRONMENT`, `TOKEN_URI`, `CUSTOMER_API_URL(2)`, `CUSTOMER_API_URL_CAM_NEXT`, `CUSTOMER_GATEWAY_API_URL`, `CIDM_INTERNAL_ENCODEDCLIENTSECRET(2|_CAM_NEXT)`, `CUSTOMER_ENCRYPTION_KEY`, `RULES_API_BASE_URL`, `RULES_API_ENCODEDCLIENTSECRET`, `JIRA_BASE_URL`, `JIRA_PAT_TOKEN`. Config values may be AES-encrypted `enc:<base64>` (`encrypt-config`).

## 3.2 The 128 whitelisted commands (`TeamBot/commands.json`)

**Diagnostics:** `health-check` · `party-summary <partyId>` · `get-rules <country>` · `encrypt-config <in> [out]`

**Customer (NewCo/CPDn):** `create-customer <cc> [type] [custAccountType]` · `create-billing-customer` · `create-shipping-customer` · `create-all [type]` · `create-customer-json <file>` · `get-party <id>` (DB) · `get-customer <id>` (API) · `update-customer <json>` (PATCH v1/customers) · `find-by-vcid <json>` (POST FindCustomerByVCID) · `create-person <json>` · `add-person-contact-method` · `admin-update-name` · `admin-update-usage` · `deactivate-parties <jsonFile>` · `replication-requestor <json>`

**Sites:** `get-sites` · `get-site` · `get-site-by-id <siteId>` · `add-site <party> <json>` · `update-site <party> <site> <json>` · `add-billing-site <party> <cc>` · `add-shipping-site <party> <cc>`

**Contacts:** `get-contacts` · `get-contact` · `add-contact <party> <json>` (profile contact; usageType Sales|PartnerContact) · `add-party-contact <party> <site> <cc>` · `extend-contact <party> <site> <contact> [cc]` · `add-contact-method` · `update-contact-method` · `add-site-contact` · `get-site-contact` · `update-site-contact`

**Notes / Relationships (party):** `get-customer-notes` · `add-customer-note` · `add-party-note` · `get-customer-relationships` · `add-customer-relationship <party> <json>` (PUT) · `update-customer-relationship <party> <relId> <json>` · `add-party-relationship <src> <tgt> [type]`

**Accounts (DCN):** `add-account <party> <site> <contact> <cc>` · `create-account <json>` · `get-account <dcn>` · `get-customer-accounts <party>` · `edit-account <dcn> <json>` (PATCH /edit — payment term changes) · `update-account <dcn> <json>` (PATCH /update — status, isSalesHold; ignores payterm) · `get-invoice-profile <dcn> <contactId?businessUnitId=…>` · `add-cust-acct-site` · `add-acct-site` · `get-acct-sites` · `get-acct-site` · `update-acct-site` · `edit-acct-site` · `add-cust-acct-contact` · `add-acct-site-contact` · `get-acct-contacts` · `get-acct-contact` · `update-acct-site-contact` · `get-acct-notes` · `add-acct-note` · `add-cust-acct-note` · `update-acct-note <dcn> <json>` · `get-acct-relationships` · `add-acct-relationship` · `update-acct-relationship <dcn> <json>` · `add-cust-acct-relationship <src> <tgt> [type]`

**Legacy map / coexistence:** `post-legacymap <json>` · `put-legacymap` (flat body; client-app-name header) · `get-legacymap <dcn> <bu> [--sites] [--contacts]` · `get-partymap <party> [--sites] [--contacts] [--ucid]` (site-keyed!) · `resolve-newco <oldcoDcn>` (CPD_PARTY bridge) · `newco-customer <cc>` / `newco-sites <cc>` (payload previews)

**OldCo (CAM_NEXT):** `create-oldco-customer <cc> [type]` · `add-oldco-billing-site <dcn>` · `add-oldco-shipping-site <dcn>` · `add-oldco-billing-contact <dcn>` · `add-oldco-shipping-contact <dcn>` · `get-oldco-customer <dcn>` · `get-oldco-sites <dcn>` · `find-newco-id <dcn>` (Playwright)

**Fusion / reports:** `check-fusion <party>` · `check-fusion-tx <txId>` · `get-fusion-ids` · `fusion-debug` · `fusion-report [days]` · `fusion-report-prod` · `fusion-report-prod-date <dd-MM-yyyy>` · `fusion-report-prod-weekly [days]` · `fusion-compare-prod` · `fusion-missing-records [from] [to]` · `fusion-missing-records-sit [days]` · `fusion-errors-detail` · `error-pattern-sit` (emails) · `sit-report` (emails) · `sit-defect-verify` · `repush-check` · `error-patterns` · `query-fusion <partyId>` (party IDs only, not SQL) · `adhoc-404-contactpoints` · `prod-pr-transactions` · `e2e-errors [date]` · `e2e-report [date]` · `e2e-columns` · `update-e2e-testdata`

**JIRA / Xray:** `get-jira <key>` · `search-jira <jql>` (25-row cap, no custom fields) · `comment-jira` · `assign-jira <key> <user>` · `transition-jira <key> <status>` · `link-jira <from> <to> <type>` (Tests/Relates/Duplicate…) · `add-test-steps <testKey> <steps.json>` · `get-test-steps` · `add-tests-to-plan <plan> <tests…>` · `add-tests-to-exec <exec> <tests…>` · `create-test-case "<summary>" <assignee> "<precon>" <steps.json> <storyKey> [--plan K] [--exec K] [--attach f…]` · `create-defect <json>` · `log-fusion-defect` · `search-users <q>` · `link-party <key> <party>` · `my-jira` · `jira-defect-report` (emails HTML) · `attach-jira <key> <files…>` · `create-test-plan <tag>` · `create-test-execution <tag> [--plan K]`

Aliases in Program.cs (not all whitelisted): create, get, fusion, fusion-tx, fusion-ids, report, report-prod, weekly-prod, compare-prod, e2e, email-report, jira, jql, comment, assign, transition, users, my-tickets, extend, add-billing, add-shipping, summary, party, rules, health, location, get-schema, sample-data…

**What run.py CANNOT do (use scripts/ or REST):** edit fields on an existing JIRA issue (REST PUT), set Xray run status (`scripts/pass_test_cases.py`), remove tests from plan/exec (Xray DELETE), move issues between projects (UI), Execution-Defect linkage (`scripts/link_defect_to_testcase.py`), OldCo site inactivation / same-address replay (`scripts/camnext_site_ops.py`), ad-hoc SQL, update payment term endpoint (`/updatepaymentterm`), admin compliance routes (`/v1/admin/customers/{id}/compliance`), UI-driven flows.

## 3.3 Devin skills (`.devin/skills/<name>/SKILL.md`, ~95) — grouped

| Group | Skills |
|---|---|
| Customer create | `create-customer`, `create-mailing-customer <JP|CN>`, `bulk-create`, `bulk-create-excel`, `oldco`, `cascade-route`, `e2e-customer`, `create-salesux-customer <USA1|MYS|AUS>`, `verify-salesux-customer` |
| Sites / contacts | `add-site`, `add-billing-site-primary`, `add-shipping-site-primary`, `extend-billing-primary-to-nonprimary`, `extend-billing-site-to-shipping`, `extend-billing-to-shipping`, `manage-sites`, `add-contact`, `manage-contacts` |
| OldCo by DCN | `add-oldco-site`, `add-oldco-billing-site`, `add-oldco-shipping-site`, `add-oldco-billing-contact`, `add-oldco-shipping-contact`, `get-oldco-customer`, `get-oldco-sites`, `find-newco`, `find-newco-id` |
| Notes / relationships / accounts / admin | `manage-notes`, `manage-relationships`, `account-ops`, `admin-ops` |
| Party inspection | `party-summary`, `verify-party`, `deepdive`, `dcn-trace`, `db-snapshot`, `db-diff`, `env-parity`, `super-diagnose`, `troubleshoot`, `payment-term-debug`, `customer-report-jw-compare` |
| Fusion | `fusion-check`, `fusion-reports`, `fusion-missing-records`, `fusion-repush`, `fusion-triage`, `error-reports` |
| Rules | `get-rules`, `download-rules`, `refresh-rules` |
| JIRA read | `jira-specs` (local snapshot), `jira-ops`, `my-open-defects`, `my-day [--email]`, `current-sprint-stories`, `release-scope <tag|current>` (READ-ONLY contract), `refresh-specs`, `confluence-sync` |
| JIRA write | `update-stories`, `init-release-jira <tag>`, `create-test-plan`, `create-test-execution`, `jira-test-cases <KEY>` (full chain: research → create MAV Tests → get-payloads → attach → verify → execute), `get-payloads <KEY>`, `attach-payloads <KEY>`, `execute-test-cases <KEY>`, `pass-test-cases <KEY|--plan|--exec>`, `link-defect-to-testcase` |
| Defects | `log-defect`, `defect-auto`, `defect-cvs`, `defect-mav`, `defect-mavcdas`, `defect-fusion`, `verify-defect <KEY>`, `defect-dedupe`, `defect-analysis [weeks]`, `defect-aging-report` (admin send), `defect-rootcause-analysis` (admin send), `defect-chase-email` (ADMIN-ONLY), `defect-chase-projects` (ADMIN-ONLY), `defects-Closed-Today`, `get-closed-defects-daily`, `get-closed-defects-weekly`, `get-defects-raised-today`, `get-defects-by-list`, `defects-weekly-metrics` |
| SDD / build | `build`, `build-story <KEY>`, `implement-story <KEY>`, `sync`, `onboard`, `review`, `cure`, `bvt-smoke`, `health-check`, `cidm-bot` (NL fallback) |

## 3.4 Devin sub-agents (`.devin/agents/`)
`knowledge-researcher` (JIRA/Confluence snapshot, read-only) · `codebase-navigator` (Program.cs/tests/scripts citations) · `defect-analyst` (read-only, scripts/) · `fusion-investigator` · `party-auditor` (PASS/FAIL per dimension) · `rules-analyst` (per country/version) · `db-analyst` (staging-only writes) · `test-engineer` (xUnit + Xray, writes) · `oldco-operator` (CAM_NEXT writes, confirms first).

## 3.5 `scripts/` helpers (committed Python)

| Script | Purpose |
|---|---|
| `e2e_pipeline.py` | CI orchestrator: discover / validate / transition / report |
| `execute_test_cases.py` | run payloads attached to a story's MAV Tests via run.py; PASS/FAIL table; `EXECUTE_REPORT_<STORY>_<date>.md`; per-TC comment (`--no-comment`) |
| `pass_test_cases.py` | flip Xray test runs TO DO→PASS/FAIL (story / `--plan` / `--exec --all-in-exec`); always `--dry-run` first |
| `release_manifest.py <tag|current>` | status-grouped release manifest (reads `.devin/knowledge` — stale path bug; refresh writes `.windsurf/knowledge`) |
| `my_day.py` | personal digest (stories/defects/execs), Outlook opt-in |
| `defect_analysis.py [--weeks 16]` | manager-approved weekly trend report (HTML+SVG, Severity field, Outlook COM to me) |
| `aging_report.py` | MAV-CUST ageing (>30d, 14–30d cumulative ">14"), live assignees, Cc-blank abort; scheduled 11:00 via `scheduled_aging_report.ps1` (logs in `logs/`) |
| `rootcause_analysis.py` | RCA themes from comments |
| `defect_chase_list.py`, `build_defect_recipients.py`, `send_defect_emails.py`, `defect_chase_projects.py`, `run_defect_chase.py` (+`defect_chase_jobs.yaml`, `run_defect_chase_1130.ps1`) | admin-only per-project chase emails; sender `svc_npe2etesting@amer.dell.com` on behalf of `noreply@dell.com`; `EXCLUDED_APPS`; heads_cc in `TempPayloads/defect_heads_cc.json` (PII, must re-merge before send) |
| `defect_query.py --jql|--keys` | paginated JIRA fetch with custom fields (no-MCP fallback) |
| `confluence.py {whoami|spaces|search|get-page|publish|sync}` | Confluence DC helper (PAT); sync builds local KB |
| `customer_report_g4_jw_compare.py` | Excel vs G4 Jaro-Winkler scoring into `CPD_PUBSUB.MAV_CUST_RPT_*` staging tables |
| `camnext_site_ops.py {inactivate|replay|getsite}` | CAM_NEXT gaps for dedupe testing |
| `check_payment_term_rule.py` | probe paymentTermCodeRule per BU (MAVCVS-923) |
| `onprem_hub_check.py <party>`, `onprem_hub_detail.py` | OnPrem EBR row counts |
| `excel_bulk_payloads.py --all|--country|--row [--record]` | bulk template → Maverick JSON + write-back |
| `auth_helper.py` | `get_bearer_token("gateway")`, `bootstrap_env_from_launch_settings()` (fill-only-missing) |
| `teams_notify.py` | Power Automate webhook card (URL in `TeamBot/.env`), `--send` |
| `sync_claude_memory_to_devin.py --write` | mirrors Claude memory → `.devin/knowledge/claude_memory.md` (EXCLUDE creds memory) |
| `mabl_to_postman.py`, `extract_bvt_payloads.py`, `bvt_send_report.py` | MABL/BVT tooling |
| `update_gtm_excel.py`, `compare_excel_to_db.py`, `find_fed_inactive.py`, `sprint_report_live.py`, `generate_workflows_*_html.py`, `link_defect_to_testcase.py` (referenced), `sp_upload.py` (referenced), `teams_proxy.py` (referenced) | misc |

Root scripts: `generate_specs_from_jira.py`, `build_knowledge_index.py`, `fetch_current_sprints.py`, `refresh_jira_knowledge.ps1` (broken: PS parse errors + missing urllib3 → **JIRA KB frozen at 2026-08-13 generated_at; use live `search-jira`**), `refresh_confluence_knowledge.ps1`, `find_newco_id.py`, `generate_defect_email.py`, `generate_sit_report.py`, `send_closed_defects_*.py`, `setup_mcp.ps1/.bat`, `run_webapp.ps1`, `post_avs_parties.ps1`, `.csx` ad-hoc (fusion_report_apr22, pr_transactions, repush_check, schema_query).

## 3.6 Knowledge bases (local-first)

- **JIRA snapshot:** `.windsurf/knowledge/jira_stories.json` (1,173 E2E-Required stories: MVKCDHCP 480, MAVCVS 420, MAVCDAS 245, MAVCPDCUS 28; statuses Complete 1026, Waiting to Deploy 52, Cancelled 48, In Development 19, Defining Details 14, In Functional Test 7, Proposed 7; `generated_at 2026-08-13`), per-story specs `.windsurf/specs/<bucket>/KEY.md` (buckets rules-validation 375, orchestration 350, misc 146, accounts 96, tax-attributes 50, database 39, _archive 39, relationships 29, sites 24, legacy-map 18, api 17, contacts 14, customer-create 10, notes 1, confluence 11), `_index.md`, `current_sprints.json` (fetched 2026-08-04; SP13 Jul22 was active).
- **Confluence mirror:** 43 pages (synced 2026-06-03) — Newco Customer Rules Engine update series (RW0203 v1 … RW1002 v5), CILMS API docs, DCN coexistence flows, Address Finder, AuthZ, Smart Search, Daily Tracker for Coexistence, ATC Maverick Defect Tracker (CSB, writable), Release-FY27-0602 page (1337524706). Core **Mavericks** space is access-restricted.
- **Memory mirror:** `.devin/knowledge/claude_memory.md` (94 memories as of 2026-07-28) + this pack.

## 3.7 MCP server & other consumers

- `CIDMMcpServer` (net9): tools Customer/Site/Contact/Account/Relationship/Fusion/E2e/OldcoNewco/PartyAdmin (JiraTools.cs exists but not registered — JIRA is banned by platform policy). Invoke `dotnet <path>\CIDMMcpServer.dll`; secrets via MCP `env` block from `mcp_secrets.local.json`; child stdin must be redirected+closed (fixed 43d1024f). Standalone repo `cidm-mcp-server` (orphan branch, 94 cmds, G4 only). Whitelisting: Dell allowlists by server NAME per org (`{ "command": "dotnet", "args": [".*CIDMMcpServer\\.dll$"] }`); still `[DISABLED]` in Devin as of Jun-2026 (request drafted in `docs/MCP_WHITELISTING_REQUEST.md`).
- `webapp/` CIDM Workflow Portal: FastAPI on 127.0.0.1:8090, runs skills headless via Claude CLI, GenAI chat via entitled service account (client_credentials, `gpt-oss-20b`), everyone non-admin.
- `TeamBot/bot.py` Teams outgoing webhook (HMAC) + `/pa-webhook` (unauth — keep local; proxy `scripts/teams_proxy.py`); `sp_poller.py` SharePoint queue (CA-blocked on tenant).
- `mabl/` BVT suite (bvt-001…026 incl. OldCo create + orchestration check).
- Playwright suites: `DellSalesUX_PlaywrightCS` (USA1/MYS/AUS create), `DSUI_Automation/DSUI_PlaywrightCS` (Credit Party Manager; GE4 backend defect DnB_ValidationFailed blocks), CDUI (separate repo).


<!-- ===== SOURCE FILE: 04_people_jira_and_releases.md ===== -->

# 04 — People, Teams, JIRA Structure & Releases

## 4.1 People directory (as encountered in this work; roles inferred from context)

| Person | JIRA / handle | Role / why they matter |
|---|---|---|
| **Shaik Muneer** | `Shaik_Muneer` | Me — E2E automation lead, admin of TeamBot, PAT owner (creator of all automated JIRA writes) |
| **Mahesh T (Mahesh Timothy)** | — | Senior QA/lead peer; co-owner of defect-analysis; attends trade/partner KTs; CAM PROD read-only DB user; recipient of PCF→KOB migration mail |
| **Ananda** | — | Manager; defect-chase routing rules (CAM only for migration defects; exclude Cardinal-Core pubsub/DB strays) 2026-06-16; approved weekly defect-analysis report 2026-05-22 (wants Monday 00:00 auto-run, Tuesday sync share, CIE-wide later) |
| **Kiran Kumar Avsn** | — | QA teammate (partner onboarding debugging owner) |
| **Nishanth K** | `K, Nishanth` | QA/dev — trade-screening TC sheet owner; filed MAV-750758 / MAV-750291 fixes |
| **Arun Mahendrakar** | — | Architect/lead (orchestration; gray-area defect ownership; gate reviews) |
| **Saroj Patra** | — | Trade Architect (GTM/RPL ground rules, 2026-07-16 call) |
| **Ariba Syed** | — | Customer Orchestration — partner onboarding/hierarchy KT presenter; grants G4 orchestration UI access |
| **Sowmya** | — | Orchestration workflow deep-dives |
| **Tripti** | — | Hosts daily 6:30–7:00 triage call |
| **Harshitha B / Grishma A S** | — | ARD team — partner onboarding KT 2026-09-04 (reach out for help) |
| **Sondra Saenz** | `Sondra_Saenz` | MAVCDAS project lead (DAS / "MVK: Customer Data and Services"); product sign-off; cancelled MAVCDAS-3040; PCF→KOB migration owner |
| **Rafael Parise** | `Rafael_Parise` | Product/Developer sign-off approver for E2E & release comms (cc on descope comments) |
| **Suresh Kumar20** | — | CILMS/MAVCDAS dev lead; owns trade epic MAVCDAS-2999; cancelled MAVCDAS-3163 ("changes not implemented yet") |
| **Ankit Lnu** | `Ankit_Lnu` | MAVCDAS-3012 (BR first-8 CNPJ) dev — feature flag question |
| **Vishnu Nambiar** | `Vishnu_Nambiar` | MAVCDAS-2992/2994 (legacyMap DOMS) dev |
| **Praveen Manicka** | — | DOMS / legacyMap POST |
| **Karthik Raja** | — | CAM dev (CDAS-12328 add-site dedupe); CMDM outbound-mapping side |
| **Mani CBS** | `Mani_CBS` | MAVCVS project lead / Rules PO (Loqate license root cause; v5 descope; reporter of MAVCVS-1035) |
| **Selvi Tamil** | `T_Selvi` | CVS Rules developer — default assignee for MAVCVS defects (MAVCVS-877/879/881/904 owner; 1045/1046 cancel request; SA rules owner) |
| **Ramyasri Yalla** | — | CVS dev — MAVCVS-1021/1035 payment-term grouping, MAVCVS-1041 (make CRN optional SK/UAE) |
| **Snehlata Kumari** | — | CVS dev — MAVCVS-1011/1015/1022/1054 |
| **Nilesh Mishra** | — | dev — MAVCVS-1012 (KR address) |
| **Manoj Mamillapalli** | — | dev — MAVCVS-942; v5 descope Teams discussion |
| **Yeshwanth Kumar** | — | dev (2nd most-assigned in snapshot) |
| **James J. Montgomery** | — | MAVCPDCUS project lead (CPD Customer team); triaged MAVCPDCUS-1132; DJT clarification |
| **Sudhakar Guntaka** | `Sudhakar_Guntaka` | CPDn DB dev — MAVCPDCUS-1102/1132 (relationship status), 1082 siteName backfill |
| **Rajesh Bolisetti** | — | CPD dev (DJT validation question) |
| **Shomari Thomas** | — | MAVCPDCUS-1090 PO-Box block |
| **Ashish Kothale** | — | PCG dev — FindCustomerByVCID (MAVCDAS-2948) |
| **Prakash Naidu** | — | MVKCDHCP project lead |
| **Chandni Tomar / Smita Rao / Abirami Harinathan** | — | Fusion-side (Oracle) team assignees (sync breakfix / ODW-JBO / CustomerAccountService) |
| **Jagruthi** | — | RPL reference data |
| **Saranya Sekar / Surya Apparsamy** | — | CPD fix comments (trade screening) |
| **Sivan Thirumoorthly / Julio** | — | CERN team owners (app 1005528) |
| **Panneer** | — | CERN masking question owner |
| **Litty K G / Mary** | — | Release management (used to create Test Plans/Executions manually — now we bootstrap them) |
| **Hartmann** | — | Manager-level sponsor of Windsurf/AI tooling; suggested CPD Orchestration API integration |
| **Meenal** | — | DSA payload (PascalCase) author |
| **Srikanth Gaddam** | `Srikanth_Gaddam` | Teammate whose PAT once sat in the bin launchSettings (wrong-author JIRA writes) |
| **Lavanya** | — | CDUI regression tracker owner (priorities P1/P2/P3) |
| **Renaud Melissa** | — | QA program mgmt — reference for mandatory defect fields |
| **Jennifer Mazzarella** | — | former hard-coded default assignee (removed 2026-05-20) |
| **Paola, Sreekanth** | — | teammates (branches CILMSPaola, CILMS_SREEKANTH) |

Team labels: **MAV-CUST** (JIRA Team / Sub-Program id 60357), **CIE** org, **CDM team** (E2E), **DAS**, **CVS/Rules (CMVS)**, **CPD Customer**, **CAM/CILMS**, **Fusion/CDM (EP-CDM)**, **ARD**, **Orchestration**, **CERN**, **Trade/GTM**, **CDUI pods**.

## 4.2 JIRA projects

| Key | Name | Lead | Content |
|---|---|---|---|
| **MAV** | Maverick program | — | Test Plans, Test Executions, Xray Tests (MAV-6xxxxx…7xxxxx), program-level defects (Cardinal-Core, Fusion, **PUBSUB**), epics (MAV-605430 = E2E defects epic) |
| **MAVCVS** | MVK Customer Validation Services (Rules) | Mani CBS | rules stories/defects; requires Sub-Program |
| **MAVCDAS** | MVK: Customer Data and Services (DAS / CILMS) | Sondra Saenz | cust-account API/DB stories, trade-compliance CILMS stories |
| **MAVCPDCUS** | MAV CPD Customer | James J. Montgomery | createParty / party API + DB |
| **MVKCDHCP** | MVK_CDHCP (Fusion/PubSub dev stories) | Prakash Naidu | dev/story project — **never file Fusion/PUBSUB defects here** |
| **CDAS** | CSI (CAM) incidents/stories | — | CDAS-12328, 12380, 12388, 12365, 12370, 12384 |
| **CVS** | Dell Digital Jira CVS | — | CVS-1587 (UA postal) |

Story statuses: Defining Details → Waiting for Dev → In Development → In Functional Test → Waiting for E2E → **In E2E** → **Waiting to Deploy** → Complete (QA never marks Complete; we stop at Waiting to Deploy) / Cancelled / Obsolete (Tests). Defect flow includes Proposed, Waiting for Dev, In UAT…; Cancelled only from Waiting to Deploy in MAVCVS; MAVCPDCUS reopen = Cancelled → Waiting for Dev (id 31).

## 4.3 Custom fields & option IDs (Dell JIRA Server)

| Field | ID | Notes |
|---|---|---|
| E2E Required | `customfield_10204` | Yes=10135, No=10136, Empty=10137 |
| Release Target | `customfield_10220` | option object e.g. "FY27FW19-0602" id 66929; unreliable on Test Plan/Exec |
| Sprint Fix Version / Found During Release | `customfield_10213` (harness maps both → Found During Release is LOST; set in UI) | "Not Related to a Release"=10177 |
| Found During | `customfield_10209` | E2E Functional=10146, E2E Regression=10148, SIT Functional=20504 |
| Defect Category ("Type") | `customfield_10210` | Data Defect=10153 |
| Found-in Environment | `customfield_10211` | SIT=10165 |
| Severity | `customfield_10212` | Sev 1..4 = 10169–10172 (Sev 3 = 10171) |
| Repro Steps | `customfield_10217` | raw text |
| Found-in Sub-Environment | `customfield_10406` | GE4=13203, GE1=13200 |
| Application Name | `customfield_10500` | Cardinal - Core=21045; CIL Microservices=21279; Contact Method Validation Services=21297; MVK CDM=60801; EP-Customer Data Module (CDM)=72535; Customer Account Management (CAM)=455999 |
| Sub-Program / Team | `customfield_10700` | MAV-CUST = `{"id":"60357"}` (raw; friendly wrapper fails) |
| Component (App-level) | `customfield_10703` | Internal Application Code / Content Issue / Compile Error / Implementation Error |
| Epic Link | `customfield_10101` | MAV-605430 (E2E defects), MAVCVS-941, MAVCDAS-3062… |
| Sprint | `customfield_10100` | bare int, active/future sprint of the project's own board |
| Xray steps | `customfield_12004` (NOT used — write via `/rest/raven/1.0/api/test/{key}/step` PUT) | |
| Test Plan association on Execution | `customfield_12026` | |
| Priority | numeric id "1".."4" stable (P4 - Medium default) | names vary per project |

Xray Server REST: `GET/PUT /rest/raven/1.0/api/test/{key}/step`, `POST /rest/raven/1.0/api/testplan/{key}/test {"add":[…]}`, `POST …/testexec/{key}/test`, `GET …/testexec/{key}/test[?detailed=true]` (no `?limit=`), `GET …/testrun?testExecIssueKey=&testIssueKey=` → `PUT …/testrun/{id}/status?status=PASS|FAIL`, `POST …/testrun/{id}/defect ["KEY"]`, `DELETE …/{testexec|testplan}/{key}/test/{testKey}`. JQL: `issue in testPlanTests('MAV-770748')`, `testExecutionTests('…')`, `testTestExecutions(testKey)`; `linkedIssues()` is index-lagged (use `summary ~ '<STORY>'`); `testsForTestPlan()` disabled.

## 4.4 Defect routing (by Application, not project)

| Failure area | Project | Application (cf_10500) | Skill | Default assignee |
|---|---|---|---|---|
| Rules validation (CVS) | MAVCVS | Contact Method Validation Services (1000104) | `/defect-cvs` | T_Selvi (or PAT owner) + Sub-Program MAV-CUST |
| Party / Cardinal Gateway | MAV or MAVCDAS | Cardinal - Core (1004020) | `/defect-mav` | PAT owner |
| Cust account / ORA / CILMS | MAVCDAS | CIL Microservices (1001959) | `/defect-mavcdas` | PAT owner (or story dev) |
| Fusion outbound **and** PUBSUB/repush | **MAV** | EP-Customer Data Module (CDM) (1008000) / CAM (455999) per profile MAV-775813 | `/defect-fusion` | PAT owner; summary prefix `PUBSUB::` (+`FUSION::`), label `CMDM_Error` |
| CPDn party API/DB | MAVCPDCUS | — | `/log-defect` | Sudhakar/James team |
| CAM legacy | CDAS | CAM | — | Karthik Raja |

Defaults: Priority P4-Medium (numeric "4"), assignee+reporter = PAT owner, Severity from impact (Sev 3 default), Found During E2E Functional, SIT/GE4, Epic MAV-605430, Release Target = current tag, E2E Required Yes, Team MAV-CUST, attach raw `TestLogs/API_*.json` evidence via `attach-jira`. Build the FULL field set on first create — run.py cannot edit afterwards (REST PUT can). Never recreate a defect just to fix fields (creates visible duplicates — lesson MAVCVS-1045/1046).

## 4.5 Releases (Dell fiscal tags `FY<yy>FW<ww>-<MMDD>`)

| Tag | Short | Test Plan / Execution | Notes |
|---|---|---|---|
| FY27FW10-0402 | 0402 | — | Sprint0402 tests (10 stories, 25/38 pass) |
| FY27FW12-0404 | 0404 | — | Sprint0404 MAVCVS stories (15/65 pass; CVS not deployed) |
| FY27FW16-0503 | 0503 | — | Sprint0503 dir in MAV_0402 |
| **FY27FW19-0602** | 0602 | MAV-657348 / MAV-657351 (created by Mary; Release Target wrong) | 21 TCs, 17 C# tests; STATUS_REPORT 2026-05-21 |
| FY27FW23-0702 | 0702 | MAV-698574 / MAV-698575 | MAVCDAS-2948 VCID tests MAV-699994…700000; MAVCPDCUS-1063 dedupe |
| **FY27FW29-0803** | 0803 | **MAV-740928 / MAV-740929** | Rules v4-17-August; 21-story campaign → 80/80 PASS (2 stale); 18 stories descoped 08-05; code lock early Aug |
| **FY27FW36-1002** | 1002 | **MAV-770748 / MAV-770749** | Rules v5; 11 tests → 6 in scope → 2 genuine PASS (3065, 3086); In-E2E batch 09-04; 1,646 issues program-wide, 53 CIDM, 31 test-ready |
| December release | — | — | UAE (10784) / SK (10703) e-invoicing mandatory scope moved here (MAVCVS-1041) |
| FY26FW36-1002 etc. | — | — | historical (snapshot) |

Sprint naming `MAV-FY27\SP<nn>\<MonDD>`, 2-week cadence (SP10 Jun10, SP11 Jun24, SP12 Jul08, SP13 Jul22 …). Board sprint ids differ per project board (56xxx vs 97xxx).

Key dashboards/pages: JIRA **MAV-CUST Defects Metrics** dashboard pageId 59623 (Severity field, Rich Filter rf=8395); Confluence "Attention FY27 RW0803 v4 / RW1002 v5: Newco Customer Rules Engine update" (DCP 1320885099 / 1320885136); ATC Maverick Defect Tracker (CSB 1102710676); Release-FY27-0602 page 1337524706; Daily Tracker For Coexistence (1277702839).


<!-- ===== SOURCE FILE: 05_timeline_and_test_campaigns.md ===== -->

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


<!-- ===== SOURCE FILE: 06_defects_and_open_items.md ===== -->

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


<!-- ===== SOURCE FILE: 07_technical_knowledge_base.md ===== -->

# 07 — Technical Knowledge Base (hard-won facts, condensed; one bullet per memory)

Long-form originals: `.devin/knowledge/claude_memory.md` (94 entries, 2026-07-28) and `%USERPROFILE%\.claude\projects\c--repos-cidm-cilms-apiautomation\memory\*.md` (37 entries, Jul 30 → Sep 4). Grep those when you need the full evidence trail.

## 7.1 Environment, build & tooling quirks

- **App Control** blocks locally-built `.exe`; run.py invokes `dotnet <CIDMAgent.dll>`; MCP configs must use `command: dotnet, args: [dll]`. Never regress to `dotnet run --project`.
- **dotnet SDK is user-local** (`%LOCALAPPDATA%\Microsoft\dotnet`, 6.0.428 + 8.0.423). Build order: CIDM_APIAutomation first, then CIDMAgent → DLL lands in `CIDM_APIAutomation/bin/Debug/net6.0/`. run.py never builds.
- **Credentials source of truth** = `CIDM_APIAutomation/launchSettings.json` (never edit bin/ as source). But the DLL reads the **bin copy**; and `mcp_secrets.local.json` beats source in run.py → sync all three after rotation. A stale bin token can be **another user's valid PAT** → JIRA writes attributed to the wrong person (Creator immutable; stray comments can't be deleted via API).
- `LoadConfig()` overwrites shell env vars from build-output launchSettings → env overrides (e.g. to hit G1) silently ignored; check the printed `[OK] API URL:` line. Only working override = edit `bin/Debug/net6.0/launchSettings.json` then restore.
- `launchSettings.json` is gitignored but was once tracked — never `git add -A`; `net7.0/net8.0` bin copies are malformed JSON.
- `refresh_jira_knowledge.ps1` has em-dash parse errors and exits 0; `generate_specs_from_jira.py` needs `urllib3`/`requests` (absent). JIRA snapshot frozen (2026-08-13) → live `search-jira` (25-row cap; `[ERROR] Cannot access child value` → narrow JQL with `issuetype = Story`).
- `scripts/release_manifest.py` reads `.devin/knowledge` but refreshers write `.windsurf/knowledge` → under-reports.
- PowerShell 5.1: drops `""` args (shifts positionals — never pass "" for create-test-case assignee), mangles nested quotes in JQL (use `--%` or bash single quotes), `Get-Content` without `-Encoding UTF8` mojibakes em-dashes (Outlook COM), heredoc/`python -c` multi-line fails. PS scripts must be ASCII.
- Windsurf workflow "command not recognized" = YAML frontmatter colon-space in `description:` → quote it.
- Empty/missing workflow file → STOP, never substitute another command (`/defect-analysis` ≠ `jira-defect-report`).
- Workflow + `.windsurfrules` + SKILL.md must stay in sync (SKILL.md `create-test-case` signature drift broke Windsurf runs).
- Python 3.13+ breaks `aia_auth` (`cgi` removed) → use 3.12 / `legacy-cgi`. SSL-inspected segments need Windows roots appended to certifi `cacert.pem` (webapp hardcodes `verify=certifi.where()`).
- MCP stdio server spawning a child must redirect+close child stdin (else 120 s hang). MCP tool output must be clean JSON (`CidmRunner.CleanOutput`, structuredContent).
- Devin org allowlist is per MCP-server NAME (server-side); correct local config + working binary isn't enough.
- Dell email from desk: SMTP 587 → 530 auth required, 25 → 554 IP-locked; **Outlook COM via PowerShell** is the path (Outlook must be open). defect-chase uses `smtp-dev.dell.com:587` with `svc_npe2etesting` (From noreply, Sender svc → "on behalf of"; no Reply-To).
- Teams: no file attach via webhooks; SharePoint upload/device-code auth **CA-blocked** on tenant (also Stream transcripts → download manually). `TeamBot/.env` holds webhook secret & HMAC.
- `TeamBot/cidm.py run_cidm()` still uses `dotnet run` (do-not-modify list) — only `sp_poller` was repointed to run.py.
- `test_case_payloads/` is gitignored (local only). `CustomerInfo.json` gets cleared per Consolidate run.
- xUnit 8-way parallelism + unsynchronized `CustomerInfo.json` writes lost data (fixed with `CustomerInfoFileLock`, MAV_0402 ab189e28).
- `GetCustomerInfo` returns the exception **message string** on a missing key → cascades into ~15 misleading "dynamic mapping failed" errors after one 400.

## 7.2 Auth & tokens

- DAIS `/di/api/v3/oauth/token` needs **JSON body** to return a JWS the gateway accepts; form-urlencoded token fails IDX12709.
- G4 client secret authenticates on G1 too. PROD `CIDM_INTERNAL_ENCODEDCLIENTSECRET2` is dead (401) → `PRE_OBTAINED_TOKEN`.
- `USE_SSO=true` → `scripts/auth_helper.py` (aia_auth) injects `PRE_OBTAINED_TOKEN`. Never set true in CI.
- Rules API client rotated 2026-07-15 (`4858552c-…` 164-char); old `e04a690e-…` dead since ~Jun-10.
- GenAI headless: entitled service account `567b2d20…` (client_credentials, DEV gateway, `gpt-oss-20b`) in gitignored `webapp/genai_auth.local.ps1`; the TTS repo `.env` still has old unentitled creds + `USE_SSO=true` (landmine if launched without `run_webapp.ps1`). `GET /models` 200 vs chat 401 = read-but-not-inference entitlement.
- CERN orchestrator gateway (`cpd-workflow-orchestrator-ge4…/gateway/...`) is queryable read-only **without auth** from corp network; direct CERN ER API needs auth (403).
- OldCo rules contract: `GET /v1/addresscontactruleset/{cc}` on `rules-api-g4.cilmsnp.us.dell.com`, headers `rules_version: 16` + `client-app-name`, Bearer from **G1** token endpoint; `GET /v1/rulesetversion` lists per-country doc versions.
- CAM_NEXT swagger: `GET {CUSTOMER_API_URL_CAM_NEXT}/swagger/v1/swagger.json`. PCG swagger `/swagger/v1/swagger.json` (80 paths, identical PCF/KOB).

## 7.3 Payload contracts (NewCo / Maverick)

- **Create payload shape** (top-level, in order): `customerPartySubType, segment, class, usages, customerName{name,legalName,legalNameSource,additionalNames}, firmographics, contactMethods, site, custAccount, additionalAttributes{items{rulesVersion,performorgdedupe,datajurisdictiontag}}, auditControl{createdBySalesRepId,createdBySalesRepNt,createdByEmail,createdBy}`. NO `party` wrapper, NO `customerPartyNumber`. ~4 KB. ALWAYS clone a recent `Request_<CC>_*.json` / `CustomerInfo.json` entry.
- `customerName.name` and `custAccount.name` must match when both provided ("Customer name and CustAccount name must match").
- **Individual (I/CNSR):** generator emits party-level `contactMethods[]` → 400 "Organization Contact Method is not Valid for Individual Customer"; strip top-level `contactMethods`, keep `site.contact.contactMethods`, repost via `create-customer-json`. CJK names: padding fixed 2026-09-03 (`GetFixedLengthName` 'XXXX' removed in PersonInfo.cs + PostCustomerMaverick.cs; `ToEmailLocalPart` Latin fallback). Read/write CN payloads as UTF-8.
- **Update customer** (`PATCH v1/customers`) canonical = `PostCustomerMaverick.GetPatchParty()`: customerPartyNumber + countryCode + status + statusReasonCode + usages + customerAccount{dellCustomerNumber,status,statusReasonCode}; no top-level DCN/BU. Party `D` needs statusReasonCode from LOV (EOL works). Account block returns 200 but is NOT persisted → separate `PATCH /v1/customerAccounts/{dcn}/update` (needs businessUnitId, numeric `rules_version` underscore header; rejected once party is Inactive → reactivate→deactivate account→re-deactivate party). Primary sites cannot be inactivated.
- **PATCH site** needs customerPartyNumber + siteId + countryCode + rulesVersion in body; direct party-layer purposes PATCH 400s; SA update-site re-validates via CVS ("Suburb is not Valid") → re-POST via create-customer-json to isolate a field.
- **Relationships (party)**: `PUT/PATCH v1/customers/{p}/relationships` body `{"relationship":{sourceCustPartyNumber,targetCustPartyNumber,type,status,additionalAttributes:{items:{rulesVersion}}},"auditControl":{…}}`; type `"Partner To Sales"` WITH spaces (others no spaces); status words Active/Inactive/… or letters A/D/C/P/S/R; flat/array bodies → misleading 500 NRE. **Validator reads rulesVersion from BODY** (header ignored on this route): body v4 + `D` → 200; `I` → 200 but persists **null** (MAVCPDCUS-1132). Deactivated relationships disappear from GET ("No record found"). Default `partnertosales` needs source COMM + class Partner; use `billto` for generic tests.
- **Partner To Sales recipe**: both parties same `site.address` + shared explicit `site.siteName{name,legalName,legalNameSource:"Manually/Custom Provided"}`, DIFFERENT org names (dedupe keys on customerName), source `class:"Partner"`; pair P15803175475 → P15803175513 (2026-07-22). Existing E2E test `PartyRelation.VerifyCreateRelationBwTwoPartieswithTypeAsPartnerToSales` is broken by these rules.
- **Account relationships**: only `Funder`(/Floorer) between accounts; target must be `custAccountType:"Funder Account"`; PATCH needs `additionalAttributes.items.rulesVersion` in body + `currency`; no status validation (garbage 200); GET 400-null (N2). `update-acct-note`/`update-acct-relationship` take `<dcn> <json>` (no id arg despite docs).
- **Cust acct site PATCH** (`update-acct-site`) requires `custAccountSiteId`(L) + `siteId`(party S) + `segment` + `businessUnitId` + `status` (template `TeamBot/CTL_patchCustAcctSite_v2.json`) else "No rules found for this request" (= request couldn't be keyed, not missing ruleset). Primary switch = one atomic PATCH with `purposes[{type,isPrimary:true}]` — old primary auto-demoted. Same party-site → CAS dedupe 201 WARNING "MATCHED SITE ALREADY EXISTS". `.../sites/{sid}/edit`: siteId=S, custAccountSiteId=L. Contact PATCH needs `custAccountContactId` + status + segment.
- `get-acct-site`, `get-invoice-profile` need `businessUnitId` → pass `"<id>?businessUnitId=108401"` inline. `get-invoice-profile <dcn> <C-contact?businessUnitId=…>`.
- Helper commands `add-account`, `add-cust-acct-site`, `add-cust-acct-contact` die on local validation — use JSON variants `create-account`, `add-acct-site`, `add-acct-site-contact`.
- `create-customer` hardcodes body `rulesVersion: 3` even with `RULE_VERSION=5` header → version-sensitive tests must hand-author body.
- **Firmographics/DUNS**: `site.firmographics{duns,hqDuns,businessLocIndicator ∈ 0/1/2/4}` (top-level → "Found invalid data while decoding"); request DUNS is silently dropped — DUNS come only from CERN match (MAVCPDCUS-731).
- **Dedupe (createParty Org)**: match = full address AND class AND usage AND segment AND (DUNS | name case-insensitive); {COMM,LPUB} cross-match, NGOV isolated; usage extends; `performorgdedupe:"Y"` flag; response WARNING "Matched an existing party" + `createdCustomer` = existing ids; DJT (`datajurisdictiontag`, ≤10 chars e.g. 5553000000) differentiates at create, ignored in search. Match = PASS for positive TCs; negative TCs need fresh unique names (dedupe fires BEFORE validation and masks 400s).
- **Mailing/Delivery purpose**: string `type` in `site.purposes` + `custAccount.site.purposes` + matching `responsibilities` entries in both contacts (`create_JP_Mailing.json`, `TeamBot/create_{US,GB,CA}_Delivery_*.json`).
- **Profile contact** (`POST /contacts`, run.py `add-contact`): body customerPartyNumber, profileId, personPartyId, fusionContactId, usageType (Sales|PartnerContact), divisionDepartment, auditControl. Re-POST same profileId = silent no-op (201, same id) → usageType must be on FIRST create; GET never exposes usageType (prove via TX type `sales_contact`). `partner_contact` outbound always fails for API-created parties (no Partner Account — only ARD orchestration mints partner track ids).
- **extend-contact** keeps Billing and adds Shipping; `isPrimary` is per responsibility TYPE (two `isPrimary:true` on one contact is correct).
- **Replication requestor**: `{dpId, orderEntity:[{type, customerPartyNumber, siteId, partySitePurpose}]}`; dpId single-use ("THIS DPID IS ALREADY EXISTS"); siteId optional (MAVCDAS-2975).
- **Deactivate parties** (`deactivate-parties <jsonFile>`): valid reason codes `RPL, EOL, OOB, MAD, DUP, MAS, Party_Denied, Party_Site_Denied, Site_Denied` (MAVCVS-732); default now `EOL,OOB,DUP,MAS`; env `DEACT_*`; accounts GET lists ACTIVE only.
- **FindCustomerByVCID** (`POST /v1/customers/FindCustomerByVCID`): body `{"Criteria":{"VirtualCustomer":{"Ids":[{"Type":"vcid","Value":"…"}],"UserProfile":{"UserProfileId":"<guid>"}},"Customer":{"SitePurpose":[{"Type":"Billing"}]}}}`; seeded VCID 3556931758416414 (UserProfileId 0d66bfe0-8479-4ea1-ad21-f84defa0fb42); response items have `dellIdentifiers`; find VCIDs in `VCID_MAIN.VIRTUAL_CUST_SITE_CONTACT WHERE CREATED_BY_EMAIL='cpd@dell.com'`.
- **LegacyMap POST** working body: `{customerPartyNumber, siteId, contactId, custAcct:{dellCustomerNumber,siteId,businessUnitId,contactId}, legacy:{dellCustomerNumber,siteId,businessUnitId,camLocationId,domsSequenceNumber(≤3 digits),ucid,countryCode,purposes:[{isPrimary,type}]}}` + header `client-app-name`; needs ALL NewCo identifiers incl. top-level siteId (else ORA-01400). **PUT legacyMap is FLAT** (DCN/BUID/SITEID/STATUS mandatory when UCID null). PartyMap GET is site-keyed (must pass S-id). LATAM BUs: `domsSequenceNumber = 0` is correct (not in DOMS path); null = no migration row. Legacy DOMS DCNs 9-digit. `resolve-newco` chain: 631800057474→P15803216192, 631800009696→P15803185469, 631793426565→P15798963289.
- IDs are **case-sensitive** (lowercase P/S/D/R/L → "No record found" with HTTP 200 WARNING). 2026-09-04: lowercase DCN now works on get-account, lowercase party id still doesn't (MAVCDAS-3139 partial).
- **Read-after-write lag**: list endpoints (`get-sites`, `get-contacts`) lag minutes; verify with single-resource GET; never re-run the add. On G1 `get-sites` stays wrong even later.
- Fusion location outbound rejects fake Bogus addresses (HZ-120485) → use real addresses (`1 Dell Way, Round Rock TX 78682`) for all-Y runs.

## 7.4 Country-specific rules

| Country | Facts |
|---|---|
| US 108401 | payment terms 30 Days Inv.(Y/TERMS), AR Assignment(Y/WCS), Floorplanning(Y/WCS), Prepaid(N/TERMS), IMMEDIATE(N/TERMS); no Leasing; `isTermsPaymentCode` must match (IMMEDIATE/Prepaid=N, credit terms=Y); US_FED 108402; US effectively never trade-screened |
| MX 10484 | `create-customer MX` ~50% "FTI is invalid" (random XAXX RFC) → retry; generic RFC XAXX/XEXX accepted (1004); AddressLineOne ≤~30, Colonia in `suburb`; RPL fixture `TeamBot/create_MX_RPL_ONZEBLACK.json` (Guadalajara JAL 44660) |
| BR 10076 | alphanumeric CNPJ; taxAttributes `issuingCountry=null`; add-billing-site generator "Title is invalid" → title "Sr."; name-match (996) enforcing; first-8 CNPJ root check (3012) NOT deployed; `DCNDtls/Rules_10076_BR.json` self-seed pattern |
| MY 10458 | address lines ≤30 chars; city/state/zip coherence enforced (misleading "ProvinceStateCode cannot be null"); state codes ISO numeric (Selangor 10, KL 14, Sarawak 13…); BRN regex (902); MY TIN rejected in SalesUX UI but accepted by PCG; mobile must start with 1; best trade-screening country |
| CN 10156 | O/COMM creates 400 "Suburb cannot be null" since Sep-2026 (Loqate license) — I/CNSR passes; known-good Beijing set suburb 东城区 / city 北京 / cityCode BJ000 / province BJ / zip 100000; Mailing purpose allowed |
| JP 10392 | Mailing purpose allowed (safer than CN); languageCode EN/JP-KATA (half-width, omit languageCode for backfill) |
| GB 10826 | provinceStateCode optional; `ENG` INVALID (use LND/EDH; 219 ISO-3166-2 codes); postcode `E1 6AN` style; VAT `GB950774538` valid; OldCo GB province "ENG" now 400s (Edinburgh/EDH/UF2 8DF works) |
| CA 10124 | province required (ON/BC…), postal `A1A 1A1`, phone exactly 10 digits; OldCo hub 707 |
| SA 10682 | addressLineThree `^\d{4}$` (e.g. 4800) at create; AL4 free-form (972); Arabic only in primary block (EN alternate block must be Latin); OldCo province 2-digit numeric 01–12/14; VAT not EU regex |
| FR 10250 | province = region code (`IDF`); US titles rejected; VAT + `validationStatus` mandatory; SIREN in `eanNumber`; SIREN format not validated for COMM |
| TR 10792 | state mandatory at v4 (v3 accepts none); province "34"; VAT `^(TR)(\d{10}$)|^UNREGISTERED$`; `Resources/tur_zipcodes.csv` |
| KR 10410 | province numeric (11=Seoul); title Mr.; invoiceDeliveryMethod `N`; VAT 10-digit; address 30 bytes/30 chars (1012) |
| CO 10170 | province `DC`/`SAP`; title Sr.; FTI subType `CO NIT`… with EMPTY subType on some paths; NIT regex `^(?:\d{6,10}-\d|\d{10})$`; `create-customer CO` broken (Bogus es_CL) |
| UA → 10826 | zip `^\d{5,6}$` at v4/v5 (v2/v3 still 5-digit); payloads `TeamBot/create_UA_*_KYIV.json`; OldCo UA via GB hub 202 builder, `UA_POSTAL` |
| SK 10703 | zip "811 09" (space); DIC `^(\d{10}|UNREGISTERED)$` template live but isRequired=False; December scope |
| AE 10784 | TIN `^\d{10}$` required (CNSR exempt); CRN maxLen 20; EAN `^\d{4}:\d{10}$`; VAT regex EU-only yet required → workaround; `create-customer AE` emits empty VAT/SVAT; December scope |
| DE 10276 | VAT subType `""`; EAN/TIN v5 structures present, templates empty; "Found invalid TaxTypes at SiteLevel: TIN" |
| IN 10356 | GST+PAN mandatory; payment terms I1/I2/I3 segment codes unresolved (904) |
| IT 10380 | VAT + FiscalCode + ReceiverCode |
| NZ 10554, ES 10724, BE 10056 | ES local rules file was a stale BE copy |
| UY | MAVCVS-1022 suburb non-mandatory — not in Countries dict yet |

Mandatory tax summary: MX=RFC(FTI)+regimenFiscal, IN=GST+PAN, BR=CNPJ/CPF(FTI), MY=TIN+BRN+SST+eInvoicing, DE/GB/BE/ES/SA=VAT+issuingCountry, IT=VAT+FiscalCode+ReceiverCode, US/CN/JP/NZ=none. Tax `number:"UNREGISTERED"` needs `validationStatus:null` + `issuingCountry`.

## 7.5 Rules API facts

- `get-rules` = `POST /v1/createcustomerruleset` (creates-or-returns; idempotent; only versions 2–5; hub BU 202 invalid). Per-operation rulesets: `POST /v1/updaterelationruleset` (statusRule rv3 A,I,C,P,S,R; rv4/5 A,D,C,P,S,R), `GET /v1/updatecustomerruleset/{cc}` (statusRule A,D + reason codes), `GET /v1/updatecustacctrelationshipruleset`. Swagger at `GET /swagger/v1/swagger.json`.
- Payment terms list at `custAccountRule.paymentTermCodeRule.existsInValues.paymentTermCodeDescriptionList` (`{code,description,isTermsPaymentCode,group,languageCode,default,endDate}`); `paymentGroupRule` codes Leasing/Wcs/Terms, isRequired=False at v5 (2026-09-01) — field inert on G4 (never persisted).
- RESCOM valid = `Res`/`Com` only. BusinessLocIndicator 0/1/2/4. Segment/class/purpose enumerations identical rv4↔rv5.
- Rules cache `Rules_<BU>_<CC>.json` must be a flattened array; raw object = "Cannot deserialize … List<RequiredFieldInfo>" → restore from sibling bin. Stale cache causes wrong 400s (refresh with `get-rules` before payload generation — /jira-test-cases Step 7.4).
- CVS-ElasticSearch validator accumulates all field errors (doesn't short-circuit). "No rules found for this request" = request not keyable.
- Confluence rules-release page JIRA-ID column is wrong for 956/957 (956 = First/Last name mandatory, 957 = Turkey state); trust live JIRA.

## 7.6 OldCo / CAM_NEXT

- Header `rules_version: 16` (underscore; only 16 supported; harness default 3 → "The rules-version is not supported"; G4 rejects `rules_version: 8`). `SetupHttpClient_OldCo` sends both headers. Run `RULE_VERSION=16 python run.py create-oldco-customer US`.
- Contact inactivation: OldCo never allows deactivating primary contacts; NewCo allows for the last active contact; re-activation D→A is not a valid use case (MAVCPDCUS-1093).
- Add-site dedupe (CDAS-12328): returning the existing INACTIVE site id is expected in AMER (address_id = site_id, `UNQ_BUID_DCN_SITE`); ORA-01403/ORA-00001 crash modes fixed; returned inactive id absent from GET list and 404s by id. `add-oldco-*-site` uses random faker addresses but saves the request to `TestLogs/OldCoAddSiteRequest_<cc>_<dcn>_<ts>.json` for replay (`scripts/camnext_site_ops.py`).
- Site inactivation PATCH `/v1/customers/{dcn}/sites/{siteId}`: FLAT body, `salesChannel` (US `US_19`), status `D` (`I` retired), `auditControl` mandatory, createdBy/modifiedBy length-capped (use `E2E`); GET site needs `?businessUnitId=11`. GET sites hides inactive.
- PATTEN→P replacement rule removed (CDAS-12380 PASS); `OLDCO_ADDR_LINE1` override.
- EMEA PDB was read-only (ORA-16000 at PKG_CREATE_CUSTOMER_CILMS_V1 line 383) 2026-08-13 — reaching ORA-16000 means validation passed. Hubs 5455/909/551: SA province numeric; currency hub-scoped (909 USD/EUR); EndUserAuthorizationCode per-BUID reference data (unresolved); `SA_*` env overrides.
- Migration: `create-oldco-customer MX` (3232) → async NewCo party; bridge `CPD_PARTY.T_CAM_DCN_CAMLOC_CPD_DATA_MIGRATION` (resolve-newco); `find-newco-id` scrapes CPD workflow UI (Playwright, SSO persisted in `.playwright-auth/`, cache cleared each run, snapshot truncates long strings at ~140 chars).
- OldCo AMER changes → `C:\repos\AMER` (CAM_NEXT_AMER_GOP; US=BU 11, CA=707; CI on G2; tests `*_11.cs`). DSUI seeding on G4 needs `PostCustomerMock_NewCo.GetCustomerFor*_OldCo()` builders + `rule-version` header.

## 7.7 Fusion / PubSub / Hub

- `check-fusion` queries TRANSACTION_LOG_EBR + FUSION_OUTBOUND (all party transactions); `query-fusion` takes party IDs only.
- Fusion replication async: fusion ids null for minutes (5–15 min SIT); EBR lag <1 min to ~35 min; Fusion backfeed (OCDM_OCI_IN) updates site ~80 s; `Fusion_backfeed` reverts payterm edits and adds duplicate profile contacts.
- Trade TXes `update_party_trade_status` / `update_site_trade_status` publish 0 FUSION_OUTBOUND rows (inbound-only). Party-relationship updates on some parties produce no outbound event.
- Repush: `replication-requestor`, `PUBSUB_REPUSH_ADHOC` insert, or CDM request; repush re-sends transactions still in flight (10–160 s) → duplicates (MAV-776983).
- Hub validation lessons: pair phones by usage+number; delta-aware asserts; `GetTransactionCount_Hub` pass-1 kept a random parent TX → use `_AllEvents`; never loosen the Contact status `'A'` assert (A-vs-D is settle race, proven 2026-06-05); publisher freezes post-deactivation state into add-event payload (allow `D` only for the deactivation-scenario party); AVS/Loqate REPLACES cities (Monterrey→San Pedro Garza Garcia) — `AssertCityAvsAware`; `KnownUnwiredByDesign` seed matrix; PCG_InActivation entries were wrong and removed.
- On-prem staging query filters only on Party_id/TRANSACTION_TYPE (BusinessUnitId is context only). Bucket C contact-method staleness (hub read-back returns pre-PATCH value) is real.
- Sub-transaction `location` ≈ 85% of PROD errors; SIT statuses S/E/P, PROD Y/E/N.
- Defect routing: Fusion AND PUBSUB/repush defects → **MAV project** (never MVKCDHCP), summary prefix `PUBSUB::`/`FUSION::`, label `CMDM_Error`, Application "Customer Account Management (CAM) (455999)" per MAV-775813 profile (or EP-CDM 72535 per older template), no components. Fusion-side (Oracle) assignees: Chandni Tomar (sync), Smita Rao (ODW/JBO), Abirami Harinathan (acct-site events); CMDM outbound mapping: Karthik Raja.

## 7.8 Trade screening / RPL

- Live on G4; coverage by country (MY > JP/CN/IN/GB/DE/MX; US never). Force a match with a real RPL entry (ONZE BLACK / RPLS_91261) → RPLS_REQUIRES REVIEW (not FAILED; FAILED needs GTM manual review). Tables: `CPD_PUBSUB.PARTY_GTM_SCREENING`, `PARTY_SITE_GTM_SCREENING`, `T_TRADE_API_REQUEST_LOG`; columns `ORG_PARTY/PARTY_SITE.TRADE_SCREENING_STATUS`.
- Masking: W005 "Requested customer … restricted party list and cannot be disclosed. Contact <mdg.compliance.rpl.team.ml@dell.com>." (HTTP 200 on GETs, 400 on writes), W006 for sites when party still active; REQUIRES-REVIEW parties are NOT masked; contact GETs leak PII (unfiled).
- Rules: party+site level flags; consumers never screened; nothing inactivated at party/site (purposes end-dated; linked accounts inactivated Party_Denied); primary replacement logic; createCustomer allows 1 billing + 1 shipping; blocked CRUD list. Fixture denied party P15803561498 / D11320917257 (GB); control D11320918275 (CA). Score inverted (block = PASS). Restricted MY party P15803565754.
- PATCH `compliance{tradeScreeningStatus,tradeScreeningProcessDate}` validates (400 for invalid) but never persists via public API — admin routes `/v1/admin/customers/{id}[/sites/{id}]/compliance` (MAVCPDCUS-1071/1072) are the writers (no run.py command).
- MAVCDAS-3040 (CAS on D/EOL site) was outside RPL epic MAVCDAS-2999 — later fixed via MAVCDAS-3065.

## 7.9 JIRA / Xray operational facts

- Xray Server: steps via PUT `/rest/raven/1.0/api/test/{key}/step` one TestStepBean per call (POST=405, arrays=400, cf_12004 ignored). Plan/Exec membership via Xray REST (`link-jira "Test Plan"` type doesn't exist). Run status via `testrun/{id}/status`. Execution Defect via `testrun/{id}/defect`. Reads `testexec/{key}/test` (no `?limit=`, paginate `page`). Remove via DELETE.
- Test issue `Status` (To Do) ≠ Xray run result; Plan stores no per-test result (roll-up from executions).
- `linkedIssues()` JQL is index-lagged → check existing TCs with `summary ~ '<STORY>'` to avoid duplicates; dupes → Duplicate-link + transition **Obsolete** (Tests can't be deleted via CLI).
- `create-test-case` real signature: `"<summary>" <assignee> "<precon>" <steps.json> <storyKey> [--plan K] [--exec K] [--attach f…]` (assignee+reporter = PAT owner; never inherit story assignee; never `""` in PowerShell). Never attach step files (arrays of `{action,data,expectedResult}`) — only payload JSON + `.meta.json`.
- Test Plan/Execution creation: summary = release tag / "Functional Execution for <tag>"; don't set cf_10220 on them; discover via `text ~ "<tag>"`; from FY27FW20 onward we bootstrap them ourselves.
- `transition-jira` prints transitions AND executes — always re-check status after (esp. interrupted turns). QA never marks stories Complete (stop at Waiting to Deploy). Cancel a MAVCVS defect only from Waiting to Deploy; no "Withdrawn"; MAVCPDCUS reopen = Cancelled→Waiting for Dev.
- `create-defect <json>`: friendly fields under `customFields`; priority numeric; Sprint Fix Version needs option id (omit); Severity friendly label may fail (use raw id); MAVCVS requires Sub-Program raw `{"id":"60357"}`; "Found During Release" and "Type"/"Found in Production" unmapped → set in UI; `Custom fields landed: N` is not proof.
- Editing existing issue fields: REST `PUT /rest/api/2/issue/{key}` with `{"fields":{…}}` (check `editmeta`); Sprint field needs the project's own active board sprint id.
- JIRA project move is UI-only; assigning cross-project is enough for triage.
- Dashboard config readable via `GET /rest/dashboards/1.0/{id}`; Rich Filter endpoints are browser-session gated.
- Local specs miss Release Target (cf_10220) — query live `cf[10220] = 'FY27FW36-1002'` (single quotes in PowerShell).
- JIRA attachments (dev screenshots, docx = zip with media) are legitimate test-data sources.

## 7.10 Database facts

- CNSR parties live in `ORG_PARTY` with NAME "Last, First" (PROD sometimes "FIRST LAST"); `PERSON_PARTY` linked via `ORG_PARTY_CONTACT.PERSON_PARTY_ID`. Excel ADDRESS_ID S… = `PARTY_SITE.PARTY_SITE_ID`; DCN D… = `CUST_ACCOUNT.CUST_ACCT_ID`; `ADDRESS.STATE_CODE` is NUMBER. `JW(NULL,x)=0`.
- `CUST_ACCOUNT_SITE.PAYMENT_TERM_CODE` = create-time copy from account (`PRC_INS_CUST_ACCOUNT_SITE`); never updated (`PRC_UPDATE_CUST_ACCOUNT_SITE` assignment commented out); dedicated `/updatepaymentterm` endpoint updates CUST_ACCOUNT only.
- `T_CAM_DCN_*` migration tables have no DOMS column (root cause of 2992/2994 null before dev fix); only `DOMS_ADDRESS_SEQ` in `TMP_OMEGA_COEXISTENCE_TO_CPDNXT`.
- UCID canonical table `CPD_PARTY.T_UCID_CPD_DATA_MIGRATION`.
- CPD_PUBSUB has SELECT ANY TABLE / CREATE TABLE / DROP ANY TABLE → staging-only discipline. Staging tables from Aug: `MAV_CUST_RPT_STG_0804`, `MAV_CUST_RPT_SCORED_0804`, `MAV_CUST_RPT_PSNAP_*`, `MAV_CUST_RPT_SCORED_PROD`.
- Trade/GTM feed `PROCESSING_STATUS` N→Y; `A_*` audit tables with N_/O_ columns.

## 7.11 UI automation facts

- SalesUX: region selector top-right → "New" tab → pick BU (`<Country> - <CODE>`; APJ redirects to `sales-sit-g4-apjc`); `#customers` locator; address MUST be a picked Loqate suggestion (bind combobox by label, type `1 Dell Way`, wait ~3 s, click numbered option; City/State/Postal read-only); Override Validation checkbox once; Save `#createCustomer`; success URL `…/customer/details/CustomerNumber/P…`; create POST `salesapp/api/v2/customercrud/CmxCustomer/{buid}` (browser SSO cookie, not OAuth).
- DSUI G4 URL `creditparty-app-sit-g4.r1.pcf.dell.com/dsui`; menu items `:has-text()`; "Create Party without Duns"; headed Edge; auth state cached `bin/.../.auth/dsui-state.json`.
- CDUI: DDS side nav `data-testid="side-nav-test-id"`, `role="menuitem"`; post-login `/review-list`; Party 360 tab ids `tab-details|tab-party-sites|tab-customer-account|tab-customer-contacts|tab-customer-relationships|tab-hierarchy-visualization|tab-org-profiles`; every edit raises a real MDG ticket → read-only regression.
- Playwright MCP `browser_snapshot` truncates long values (~140 chars).


<!-- ===== SOURCE FILE: 08_meetings_and_kt_notes.md ===== -->

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


<!-- ===== SOURCE FILE: 09_working_preferences.md ===== -->

# 09 — How I work / standing preferences (for any assistant or teammate)

## Communication & deliverables
- Reports and summaries as **markdown tables in the reply** — no Artifact pages, no HTML unless it is the email body.
- Terse, direct replies; lay out tradeoffs in prose and proceed with the conservative option — **never** the AskUserQuestion multi-choice tool.
- **No AI attribution anywhere** (reports, emails, JIRA comments, docs, commits). Footers like "Defect Analysis Report · MAV-CUST · 2026-05-21".
- Report emails: **only to me** (s.muneer@dell.com) unless I name recipients for that send; attach the `.html` only, never the `.md`; UTF-8 read for Outlook COM; High Importance for aging/chase.
- Faithful outcome reporting: if tests fail say so; verify JIRA transitions after every attempt.

## JIRA hygiene
- No unsolicited comments on stories or test cases; Xray run statuses and payload attachments during a requested execution are fine.
- QA never marks stories Complete; we stop at Waiting to Deploy. Transition only when I name the story and the transition.
- `/jira-test-cases` acts only on the exact key given; assignee + reporter = PAT owner; never comment on the parent story; check existing TCs via `summary ~` before creating (linkedIssues lags).
- Defects: P4-Medium default, Sev from impact, PAT owner assignee/reporter unless I name the dev, full mandatory field set on first create, raw `TestLogs/API_*.json` evidence attached, Fusion/PUBSUB → MAV project, CVS → MAVCVS (+Sub-Program), cust-acct → MAVCDAS, party → MAV/MAVCPDCUS. Ask me for Severity if not stated. Don't recreate a defect to fix fields. Don't cancel defects unilaterally.
- Live assignees at send time for any defect email; excluded apps (ADP/EP-PRM/EP-CRM/MKT UNI) stay excluded without manager OK; EP-CDM is OURS.

## Code, git & environment
- Never `git push`, never force-push shared branches, never `git add -A`, never add Co-Authored-By trailers. Stage explicit paths; I review and push.
- Every tested story becomes a C# xUnit regression test in `C:\repos\cidm_cilms_apiautomation_MAV_0402` (`TestScripts/Sprint<release>/`, `Regression<release>` category, `[Theory]+[InlineData(BUID, CC…)]`, inherit `Regression<release>Base`) — NewCo only, no trade/RPL, no OldCo/CAM.
- OldCo AMER/US changes → `C:\repos\AMER` (CAM_NEXT_AMER_GOP).
- Match the surrounding code style; keep existing tests and public signatures working; never loosen the hub Contact `'A'` assert.
- Rebuild CIDMAgent after Program.cs edits (`dotnet build CIDMAgent/CIDMAgent.csproj`); run.py runs the prebuilt DLL.
- Keep workflow/skill + `.windsurfrules` + SKILL.md in sync; quote YAML descriptions with colons.
- After adding memories, re-run `python scripts/sync_claude_memory_to_devin.py --write` and update this WORKSPACE_MEMORY pack.

## Testing discipline
- Prefer `create-customer-json` with a cloned known-good payload to isolate a single field under test; control-with-valid-value first; fresh unique names for negative cases (dedupe masks 400s).
- Read back after every write (single-resource GET, DB when API hides it); a 201 alone proves nothing (CDAS-12328 lesson); fire multiple probes on churny endpoints.
- Distinguish env defect vs harness/data issue before filing; ask dev about feature flags / pending-deploy before logging (MAVCVS-1019 precedent).
- Don't re-run creates that are failing for env reasons (each retry adds load); don't run `deactivate-parties` against PROD data unintentionally (check ID series).
- G4 DB: SELECT only on existing tables; new `CPD_PUBSUB.MAV_*` staging only.
- PROD DB connections must be run by me from my own shell (classifier blocks them in auto-mode).

## Scope conventions
- "0602" = FY27FW19-0602 (release target cf_10220, not just sprint); "current" resolves via fiscal-year rule.
- "In E2E" = our scope for a release batch; Trade Compliance / CDUI stories are out of scope for API E2E.
- "PLAN" in MABL context = MABL suite.
- Only G4 (GE4) is a write environment; PROD read-only reports; G1 only via build-output override.


<!-- ===== SOURCE FILE: 10_glossary.md ===== -->

# 10 — Glossary (acronyms & jargon heard in meetings)

| Term | Meaning |
|---|---|
| **CIDM** | Customer Identity Data Management (Dell) |
| **MAV / Maverick / MVK** | Program building the NewCo customer platform; JIRA prefix MAV* |
| **NewCo / CPDn / CPD / Cardinal / PCG** | New customer platform: CPD = Customer Party Data; CPDn = CPD Next; Cardinal Core = party services; PCG = Primary Customer Gateway (`primary-customer-gateway`) |
| **OldCo / CAM / CAM_NEXT / GOP / CILMS / CIL MS** | Legacy customer master (Customer Account Management), its next-gen gateway (`customer-gateway-api`), Global Order Platform, CIL Microservices |
| **Coexistence** | OldCo ↔ NewCo dual-running with DCN migration and bridge tables (legacyMap, partyMap, UCID) |
| **DCN** | Dell Customer Number = customer account (`D…` in NewCo; 12-digit numeric in OldCo) |
| **BU / BUID** | Business Unit id (country selling unit, e.g. US 108401); hub BUID in OldCo (11/3232/202/4046) |
| **Party / Site / Contact / CAS** | Customer, address location, person; CAS = customer-account site (`L…`) |
| **UCID / VCID / APID / DUNS / GU DUNS / HQ DUNS** | Unified Customer ID; Virtual Customer ID (VirtualCustomer/Anchor model); Affinity Partner ID; D&B numbers (Global Ultimate / Headquarters) |
| **CERN / CERN-ER** | Dell entity-resolution service (D&B backed) — dedupe + DUNS assignment |
| **CVS / CMVS / Rules API** | Customer (Method) Validation Services — per-country rules engine (Contact Method Validation Services app) |
| **Loqate / AVS v2** | Address verification vendor / service |
| **RESCOM** | Residential/Commercial flag (`Res`/`Com`) |
| **FTI / RFC / CNPJ / CPF / GST / PAN / BRN / SST / TIN / VAT / SVAT / DIC / EAN / CRN / SIREN / SIRET / NIT** | Tax identifiers: Foreign Tax ID (generic), Mexico RFC, Brazil CNPJ/CPF, India GST/PAN, Malaysia BRN/SST/TIN, VAT, Sales VAT, Slovakia DIC, e-invoicing EAN, UAE Company Registration Number, France SIREN/SIRET, Colombia NIT |
| **Payment term / isTermsPaymentCode / paymentGroup** | Account payment term code; Y for credit terms; grouping Leasing/Wcs/Terms (v4+, not yet enforced) |
| **Fusion / OCDM / OIC** | Oracle Fusion Cloud ERP customer master; Oracle Customer Data Management; Oracle Integration Cloud (inbound backfeed `OCDM_OCI_IN`) |
| **FUSION_OUTBOUND / EBR / PubSub / Hub / OnPrem** | Outbound replication table; Enterprise Business Record transaction logs; Kafka pub-sub; on-prem hub staging consumers |
| **Repush / REPUSH_ADHOC** | Re-sending a failed outbound transaction |
| **JBO-xxxxx / HZ-xxxxx / ORA-xxxxx / IDX12709** | Oracle ADF BC errors / Fusion TCA (HZ) address errors / Oracle DB errors / JWT validation error |
| **GTM / RPL / RPLS_* / W005 / W006** | Global Trade Management; Restricted Party List screening; screening statuses; masking warning codes |
| **Party_Denied / Site_Denied / EOL / OOB / MAD / DUP / MAS** | status reason codes |
| **SEGMENT** COMM / CNSR / LPUB / NGOV | Commercial / Consumer / Large Public / Non-Government |
| **Class** Direct / Partner / ServiceProvider; **Usage** Customer / Sales Account / Partner Account | party classification |
| **P2S / Partner To Sales** | relationship partner→direct party |
| **DOMS / DOMS sequence number** | legacy order-management sequence per site/contact (LATAM: 0) |
| **CDMEE / ARD / CDUI / MDG / Stewardship ticket** | Customer Data Mgmt Enterprise Edition tickets; Account Relationship Data; Customer Data UI; Master Data Governance queue |
| **Store / SPIL / DAM / Participant / Track** | Partner onboarding tasks (store creation; Sales Pipeline Integration; Digital Asset Management); sales-rep association; partner track (T…) |
| **Affinity / SFDC / GAMA / ODW** | OldCo partner systems; Salesforce; GAMA app (UDA tab); ODW ID translation (bridge) |
| **Orchestration / Workflow builder / correlation id** | CPD workflow engine (DCNMigrationCreateCustomer, PartnerCreation, build-partner-hierarchy) |
| **DSUI / SalesUX / Credit Party** | Dell Sales UI apps |
| **KOB / PCF / Layer7** | Kubernetes-on-bare-metal target platform (`cilms-np`) replacing Pivotal Cloud Foundry (`cilmsnp`); API gateway |
| **G1/G2/G4 (GE1/GE2/GE4) / SIT / DIT / PROD / dev7** | environments; dev7 = Fusion dev pod |
| **DAIS / SSO / PAT** | Dell identity token service; single sign-on; JIRA/Confluence Personal Access Token |
| **Xray / Test Plan / Test Execution / Test run / Execution Defect** | Xpand-IT test management in JIRA Server |
| **E2E Required / Release Target / Found During / Sub-Program** | JIRA custom fields (cf_10204 / cf_10220 / cf_10209 / cf_10700) |
| **MAV-CUST / CIE / CDM team / DAS / CPD Customer** | team names |
| **FY27FW29-0803** | Dell fiscal-year/week release tag + calendar month-day; "0803" shorthand |
| **Consolidate / BVT / Run A/B/C/D** | xUnit categories and pipeline lanes (Run A create+validate, B hub/OnPrem, C Fusion check, D trade compliance) |
| **Cascade / Devin / Windsurf / Claude Code / MCP** | AI tooling; MCP = Model Context Protocol server (`cidm-agent`) |
| **SDD** | Skill-Driven Development flow: spec → plan → JIRA tests → implement → CI |
| **JW** | Jaro-Winkler similarity (`UTL_MATCH`) |
| **MOM / KT** | Minutes of meeting / knowledge transfer |


<!-- ===== SOURCE FILE: 11_sibling_projects.md ===== -->

# 11 — Sibling Projects & Repos I own or touch

| Project | Location / branch | What it is | State (2026-09-07) |
|---|---|---|---|
| **CIDM MAV API automation (working repo)** | `C:\repos\cidm_cilms_apiautomation` @ `CDM_MAV_TEST_g4` | CIDMAgent CLI + run.py + skills + KBs + reports (this pack lives here) | active; many uncommitted changes |
| **MAV_0402 regression repo** | `C:\repos\cidm_cilms_apiautomation_MAV_0402` @ `MAV_0402` | GitLab CI pipeline (Run A/B/C/D), net8.0, real TestDBCalls, `Sprint0403_0404/0503/0602/0803`, `Regression0803` (17 traits, 28 cases) | last commits: 09-04 CN changes, 09-02 transaction failures, 09-01 rules version 3→5, 08-19 hub fixes (91→0) |
| **OldCo AMER** | `C:\repos\AMER` @ `CAM_NEXT_AMER_GOP` | CAM_NEXT US (BU 11) / CA (707) suites, CI on G2 (`Priority_11_Next`, `Priority_707_Next`), older SpecFlow DSUI | 08-18: token auth per env + OldCo US customer test |
| **OldCo APJ / EMEA / LATAM KOB** | `C:\repos\CAM_NEXT_APJ_KOB`, `CAM_NEXT_EMEA_KOB`, `CAM_NEXT_LATAM_KOB` | regional CAM_NEXT suites retargeted to KOB/PROD; TrxerConsole net8 | 08-18 token caching / TOKEN_URI fixes; EMEA fix 08-13 |
| **CDUI UI Automation** | `C:\repos\CDUI\CDUI_UIAutomation` (+ `CDUI_PROJECT_MEMORY.md`) | Playwright/.NET 8/xUnit UI suite for CDUI (React MFE shell) on G4 (+G1); 219 automated cases (120 regression checks, 96 P1, 84 tooling, 15 write flows); read-only golden rule; references CIDM_APIAutomation in-process for provisioning; Python + Outlook COM reporting; Devin skills per MFE | started 2026-08-19; snapshot 2026-09-06; owner Lavanya's tracker priorities |
| **cidm-mcp-server** | `C:\repos\cidm-mcp-server` @ `cidm-mcp-server` (orphan branch on same GitLab project) | secrets-free MCP server (net9) + CIDMAgent + tests; JIRA-free, G4-only, 94 cmds, `enc:` secrets, structuredContent | 07-01 last commit; awaiting Dell allowlist |
| **oldconewco** | `C:\repos\oldconewco` | copies of `CDM_Maverick_olCO_newCO` + repo checkout | reference |
| **MeetingAssistantAndroid** | `C:\repos\MeetingAssistantAndroid` @ `main` | Kotlin + Jetpack Compose Android meeting assistant: real-time STT (Android SpeechRecognizer + OpenAI Whisper), OpenAI Q&A/summaries/action items, TTS, meeting persistence; docs for Oracle OIC/VBCS/ERP + Devin skills | last commit 2026-05-02 (Whisper pause race fix). **Target consumer of this memory pack** |
| **GENAI TTS/STT repo** | `C:\Users\Shaik_Muneer\source\repos\GENAI\dev-genai-text-to-speech-speech-to-text` | Dell GenAI helper (aia_auth); its `.env` has old creds + USE_SSO=true (landmine) | reference |
| **MAV_POC (legacy path)** | `C:\Users\Shaik_Muneer\source\repos\MAV_POC` (branch MAV_POC) | earlier checkout of the same GitLab project where workflows/KB/MCP were built Apr–Jul 2026 | superseded by `C:\repos\cidm_cilms_apiautomation` after laptop migration 2026-07-28 |
| **CDM_Maverick_Consolidated_BVT** | GitLab branch | BVT suite + original SpecFlow/Selenium DSUI_Automation (locator reference) | reference |
| **Confluence spaces** | DCP, CSB (writable), BOS, DBODO; Mavericks (restricted) | rules-engine update series, CILMS docs, coexistence flows, defect tracker | mirrored locally (43 pages) |
| **Teams / SharePoint** | channel webhook (Power Automate), `CIDMQueue` list (CA-blocked) | notifications | outbound works via `teams_notify.py --send` |

Windows helper scripts at `C:\repos`: `check_alternate_parties.ps1`, `parse_contact_status.ps1`, `parse_failing.ps1`, `parse_it_jp.ps1`, `parse_parties.ps1` (ad-hoc log parsers from the Aug hub-failure triage).

Desktop artefacts referenced: `Desktop\Old Data\Customer Reporting-G4-scored-2026-08-04.xlsx`, `Desktop\New folder\Documents\Test123\CustomerInfo.json` (91→23 validation run data), `C:\Prod Data` (P145x PROD party list cleaned 08-14).


<!-- ===== SOURCE FILE: 12_devin_skills_catalog.md ===== -->

# 12 — Devin Skills & Agents Catalog (complete)

Source: `.devin/skills/<name>/SKILL.md` (frontmatter `description`), `.devin/agents/*.md`. Invoke as `/<name>` in Devin (or by intent-match). Skills orchestrate `python run.py …` and `scripts/*.py` behind preview gates — never `dotnet run`. Migrated from Cascade workflows (`.windsurf/workflows/*.md`) on 2026-08-12; old `.devin/workflows/*.md` are deleted in the working tree.

## 12.1 Customer creation

| Skill | Description |
|---|---|
| `create-customer` | Create a new CIDM customer for a given country (US, MX, IN, GB, etc.) and report the partyId. NewCo/CPDn. |
| `create-mailing-customer <JP\|CN>` | Create a customer that includes a Mailing address purpose via `create-customer-json` (only path that produces Mailing). |
| `bulk-create` | Bulk customer creation — billing-only, shipping-only, from JSON, or all 14 countries at once. |
| `bulk-create-excel` | Convert every filled `Bulk_Create` row of `CIDM_Bulk_Customer_Create_Template.xlsx` to Maverick JSON (`scripts/excel_bulk_payloads.py --all`) and create each via `create-customer-json` after ONE batch confirm gate; results written back to the sheet OUTPUT columns. |
| `oldco <cc> [type]` | Create a CAM_NEXT OldCo customer (US, MX, GB, MY) via the legacy gateway and report partyId + DCN. |
| `cascade-route` | Preview NewCo payloads (`newco-customer`/`newco-sites`), create OldCo customers, look up NewCo IDs. |
| `e2e-customer <cc>` | Full E2E — create a customer, verify party + sites, then confirm Fusion outbound success. |
| `create-salesux-customer <USA1\|MYS\|AUS\|…>` | Drive the Dell SalesUX UI (sales-sit-g4) end-to-end via Playwright MCP — BU select → Create Customer form → Save; encodes Loqate-suggestion gotchas. |
| `verify-salesux-customer <PARTY-ID>` | Verify a UI-created customer landed: party, sites, contact, DCN, Fusion. Read-only. |

## 12.2 Sites & contacts (NewCo)

| Skill | Description |
|---|---|
| `add-site` | Add a new site (billing or shipping) to an existing party. |
| `add-billing-site-primary` | Add a billing site and PROMOTE it to primary (demotes current primary billing site). |
| `add-shipping-site-primary` | Add a shipping site and PROMOTE it to primary. |
| `extend-billing-primary-to-nonprimary` | Demote a primary billing site to non-primary. |
| `extend-billing-site-to-shipping <party> <site> [cc]` | Extend an existing BILLING site so the SAME site also serves SHIPPING (adds purpose via `update-site`). |
| `extend-billing-to-shipping <party> <site> <contact> [cc]` | Extend an existing BILLING contact so it also handles SHIPPING (wrapper around `extend-contact`; Shipping ends up primary-of-type). |
| `manage-sites` | Get, look up (by siteId alone), or update sites. |
| `add-contact` | Add a new contact to an existing party (attached to a site). |
| `manage-contacts` | Get, extend, or update contacts and contact methods. |

## 12.3 OldCo (CAM_NEXT) by DCN — country/siteId auto-detected from the create log

| Skill | Description |
|---|---|
| `add-oldco-site <DCN>` | Add a billing or shipping site to an OldCo customer (US, MX, GB, MY). |
| `add-oldco-billing-site <DCN>` / `add-oldco-shipping-site <DCN>` | Add billing / shipping site. |
| `add-oldco-billing-contact <DCN>` / `add-oldco-shipping-contact <DCN>` | Add a contact to the existing billing / shipping site. |
| `get-oldco-customer <DCN>` | Fetch OldCo customer record by DCN. |
| `get-oldco-sites <DCN>` | List all sites (with contacts) for an OldCo customer. |
| `find-newco <DCN>` | Find the NewCo Party ID for a migrated OldCo DCN — drives the CPD workflow UI via Playwright MCP, returns migration metadata. |
| `find-newco-id <DCN>` | Same lookup, older skill wording (triggers: "find newco id <dcn>", "what is the newco id for <dcn>"). |

## 12.4 Notes, relationships, accounts, admin

| Skill | Description |
|---|---|
| `manage-notes` | Get or add notes on a party. |
| `manage-relationships` | Get or add party-level relationships (parent/child, Partner To Sales, BillTo…). |
| `account-ops` | Account (DCN) operations — create, lookup, edit, plus account sites/contacts/notes/relationships (23 commands). |
| `admin-ops` | Deactivate parties, update party name/usage, replication requestor, person parties, VCID lookup, update-customer. |

## 12.5 Party inspection & diagnostics

| Skill | Description |
|---|---|
| `party-summary <party>` | One-page summary — details, sites, contacts, accounts (DCN), Fusion status. |
| `verify-party <party>` | Full integrity check — sites, contacts, accounts, account-sites/contacts, notes, relationships, Fusion IDs consistent → PASS/FAIL with gaps. |
| `deepdive <party>` | SUPER MODE — exhaustive party investigation in one shot (12+ commands in parallel → single Markdown report incl. JIRA cross-ref). |
| `dcn-trace <DCN>` | Walk a DCN through every layer — DCN → account → party → sites → contacts → Fusion IDs. |
| `db-snapshot <party>` | Capture full party state as one JSON snapshot (before/after a test). |
| `db-diff` | Field-level diff of two snapshots; markdown report. |
| `env-parity <party\|DCN>` | Compare the same party across SIT (GE4) and PROD. |
| `super-diagnose` | Defect → root cause → fix proposal (correlates JIRA + SIT errors + Fusion + Program.cs). |
| `troubleshoot` | Error-catalog router — paste any MAV_POC error, get the known cause + fix. |
| `payment-term-debug <DCN>` | Why a `paymentTermCode` rule fails — pulls live CVS rules, invoice profile, Fusion state (MAVCVS-923/849 pattern). |
| `customer-report-jw-compare` | Excel "Customer Reporting" vs live G4 via `UTL_MATCH.JARO_WINKLER_SIMILARITY` (≥85), staging table upload, scored Excel. |

## 12.6 Fusion & error reports

| Skill | Description |
|---|---|
| `fusion-check <party>` | Check Fusion outbound status for a single party (auto-debug on failure). |
| `fusion-reports` | SIT or PROD reports — daily / weekly / comparison / missing records / errors detail. |
| `fusion-missing-records` | Audit rows published to Fusion with no FUSION_OUTBOUND entry (SIT GE4 / PROD), per-row error message. |
| `fusion-repush` | Recover a failed/missing Fusion outbound — detect → diagnose → repush (replication-requestor, `PUBSUB_REPUSH_ADHOC` insert, or CDM-team request) → verify → escalate to `/defect-fusion`. |
| `fusion-triage` | Daily Fusion-end health sweep — SIT + PROD (reports, REPUSH-vs-REAL split, missing records, defect cross-ref) with routed action list. |
| `error-reports` | SIT or E2E error reports — patterns, repush analysis, defect cross-reference. |

## 12.7 Rules

| Skill | Description |
|---|---|
| `get-rules <cc>` | Required Rules-API fields for a country (tax IDs, FTI for MX…). |
| `download-rules` | Download rulesets for ALL 14 countries with per-country PASS/FAIL. |
| `refresh-rules` | Regenerate cached flattened rule JSONs under `DCNDtls/` by running the RulesTestCases xUnit tests (when create-customer fails for one country only). |

## 12.8 JIRA — read-only / planning

| Skill | Description |
|---|---|
| `jira-specs` | Look up stories from the LOCAL snapshot — by key, bucket, sprint, or keyword. |
| `jira-ops` | Live JIRA — get, search, comment, assign, transition (mutating actions confirmed). |
| `my-open-defects` | My open defects (assignee = me, not Done). |
| `my-day [--email]` | Personal morning digest — my open sprint stories + defects + pending test executions; Outlook opt-in; scoped `currentUser()`. |
| `current-sprint-stories` | Current-sprint manifest, readiness, my-work, plan/exec discovery (reads local snapshot). |
| `release-scope <tag\|current>` | **READ-ONLY** release-scoped view via `scripts/release_manifest.py` — status-grouped manifest (Waiting to Deploy → In E2E → Waiting for E2E → In Development → Defining Details → Complete), my work, coverage gaps, plan/exec discovery, readiness. MUST NOT propose/create anything. |
| `refresh-specs` | Refresh the local JIRA snapshot (pull stories, rebuild index, sprint marker) — currently broken on this machine. |
| `confluence-sync` | Refresh the local Confluence KB (`scripts/confluence.py sync`). |

## 12.9 JIRA — test cases, release bootstrap, mutations

| Skill | Description |
|---|---|
| `update-stories` | Update a JIRA story — comment / assign / transition (mutating). |
| `init-release-jira <tag>` | One-shot release bootstrap — creates BOTH Test Plan (summary = tag) and Test Execution ("Functional Execution for <tag>"), Cloners-linked to prior release; idempotent. Run at the start of every release. |
| `create-test-plan <tag>` / `create-test-execution <tag> [--plan K]` | Solo creators when only one is missing. |
| `jira-test-cases <STORY-KEY>` | Full authoring chain: preflight → discover Plan/Exec (`text ~ "<tag>"`) → research (spec+epic+siblings+defects) → step files → confirm → `create-test-case` loop (assignee+reporter = PAT owner, `--plan/--exec`) → Step 7.4 `get-rules` per country → Step 7.5 `/get-payloads --from-step-files` (BACKFILL wipes stale payloads) → 7.6 `attach-jira` payload+meta → 7.7 DONE checklist (Story link, Plan, Exec, 2 attachments) → 7.8 `scripts/execute_test_cases.py`. Variants `--no-execute`, `--no-payloads`, `--keep-old-payloads`. Only the EXACT key passed; never comments on the parent story. |
| `get-payloads <STORY-KEY>` | Deep-analysis payload generator — one HTTP-request-body JSON per scenario in `test_case_payloads/<KEY>/`; HARD RULE: base on a recent successful `Request_<CC>_*.json` / `CustomerInfo.json`, never invent the shape. |
| `attach-payloads <STORY-KEY>` | Backfill payload attachments onto existing MAV Test issues. |
| `execute-test-cases <STORY-KEY>` | Thin wrapper on `scripts/execute_test_cases.py` — verify wiring, run every payload via run.py, PASS/FAIL/SKIP table, `EXECUTE_REPORT_<STORY>_<date>.md`, per-TC comment (`--no-comment`, `--dry-run`, `--tc`). |
| `pass-test-cases <STORY-KEY> \| --plan P --exec E \| --exec E --all-in-exec` | Flip Xray test runs TO DO → PASS/FAIL inside a Test Execution via Xray Server REST (`scripts/pass_test_cases.py`); plan/exec scopes are FORCE-PASS — always `--dry-run` first. |
| `link-defect-to-testcase <DEFECT> <TEST…> [--exec] [--reopen]` | Xray Execution-Defect association on the test RUN (`testrun/{id}/defect`), optional reopen (Cancelled → Waiting for Dev). |
| `build-story <KEY>` | SDD: read spec, enrich plan, scaffold C# xUnit class (stub shells). |
| `implement-story <KEY>` | SDD: locate affected C# files, IMPLEMENT the change + real xUnit tests, build, run, report (three confirmation gates). |

## 12.10 Defects

| Skill | Description |
|---|---|
| `log-defect` | Generic defect logger (proper formatting, evidence, assignment; mandatory field table §2b). |
| `defect-auto` | Paste an error trace → auto-detect owning team (CVS / MAV-Party / DAS / Fusion) → route to the right `/defect-*`. |
| `defect-cvs` | MAVCVS project, Application "Contact Method Validation Services (1000104)", CVS-rules template (MAVCVS-923). |
| `defect-mav` | Cardinal - Core (party ops), Team MAV-CUST, Phase SIT, Test Type SIT Regression (MAV-650352). |
| `defect-mavcdas` | DAS team / MAVCDAS, "CIL Microservices (1001959)" (MAVCDAS-2378) — cust account API/DB, ORA errors. |
| `defect-fusion` | EP-Customer Data Module (CDM) (1008000) — Fusion outbound / ERP integration (MAV-458154). **Note 2026-09-03: Fusion AND PUBSUB/repush defects go to project MAV, never MVKCDHCP.** |
| `verify-defect <KEY>` | Verify a deployed fix on G4 (control-with-valid-value first), post evidence, transition to Waiting to Deploy (`--complete`, `--no-transition`, `--to`). |
| `defect-dedupe` | READ-ONLY pre-filing dedupe — `sit-defect-verify -3` + one open-defect cross-check; groups patterns into root families; verdict table LINK-AS-EVIDENCE vs NEW-DEFECT CANDIDATE; STOPs. |
| `defect-analysis [weeks]` | READ-ONLY trend report (`scripts/defect_analysis.py`, default 16w, Severity field, HTML with SVG chart, emails HTML only to me). Manager-approved; weekly Tuesday sync artefact. |
| `defect-aging-report` | MAV-CUST ageing (>30d / 14–30d, all open statuses incl. in-test), "why waiting" from comments, High-Importance email; dry-run default; **send = admin only**. |
| `defect-rootcause-analysis` | RCA of every open in-scope defect from comment trail — theme %, evidence quotes, triage state; `--send` admin only. |
| `defect-chase-email [projectFilter]` | **ADMIN-ONLY.** Per-project accountability emails (To lead+assignees, Cc managers+leadership); dry-run default; `--test-to` self-test; sender svc account on behalf of noreply. Non-admin: STOP before any step. |
| `defect-chase-projects --projects "…" [--test-to \| --send]` | **ADMIN-ONLY** one-shot chase for a project list; aborts if ≤6 open defects. |
| `defects-Closed-Today` / `get-closed-defects-daily` | Defects closed today, categorized HYPERCARE / PROD / Other × SEV1-3, HTML. |
| `get-closed-defects-weekly` | Closed since start of week + basic analysis. |
| `get-defects-raised-today` | Defects created today still open. |
| `get-defects-by-list` | Paste defect IDs / title lines → Outlook-safe HTML summary by severity & environment. |
| `defects-weekly-metrics` | Raised/closed/topics metrics since beginning of week. |

## 12.11 Build, sync, onboarding, misc

| Skill | Description |
|---|---|
| `build` | `dotnet build` CIDMAgent and verify the DLL is fresh; unattended. |
| `sync` | Daily startup — git pull + build CIDMAgent + health-check (confirms before pull if dirty). |
| `onboard` | First-day onboarding — verify env, fill config, smoke checks, workflow menu. |
| `health-check` | API + DB (non-prod + prod) + JIRA connectivity. |
| `bvt-smoke` | FULLY UNATTENDED BVT — create customer + sites + contacts + methods + notes + relationships + account stack, poll Fusion until counts match, HTML report, email me. |
| `review` | Code review for bugs, security, conventions. |
| `cure` | (no description in frontmatter) |
| `cidm-bot` | Natural-language fallback: map request → whitelisted run.py command; refuses unknown commands with the admin message. |

## 12.12 Sub-agents (`.devin/agents/`)

| Agent | Owns | Writes? |
|---|---|---|
| `knowledge-researcher` | JIRA snapshot, Confluence mirror, skills, knowledge base — cited synthesis | no |
| `codebase-navigator` | `Program.cs` handlers, `commands.json` wiring, xUnit tests, `scripts/`, `RequestFiles/`, `TestLogs/` | no |
| `defect-analyst` | MAV-CUST trends, aging, dedupe, RCA, chase lists via `scripts/` | no (email admin-only) |
| `fusion-investigator` | single party / SIT & PROD sweeps / REPUSH-vs-REAL; warns before email-side-effect commands (`sit-report`, `error-pattern-sit`, `fusion-errors-detail`) | no |
| `party-auditor` | PASS/FAIL per dimension (party, sites, contacts, accounts, CAS, contacts, notes, relationships, Fusion IDs) | no |
| `rules-analyst` | per country/BU, rules-version deltas, NewCo vs CAM header traps | no |
| `db-analyst` | G4 read-only queries, snapshots, diffs, JW reconciliation; writes only to `CPD_PUBSUB.MAV_*` staging | staging only |
| `test-engineer` | xUnit regression authoring (`Sprint<release>/`, `Regression<release>`), Xray plans/executions, payload attachment | yes |
| `oldco-operator` | CAM_NEXT creates / adds by DCN (US 11, MX 3232, GB 202, MY 4046), DCN→NewCo correlation | yes (confirms first) |

All agents inherit CLAUDE.md rules (run.py only, no new commands, GE4 only, DB read-only, terminal-table reports, no unsolicited JIRA comments).


<!-- ===== SOURCE FILE: CHANGELOG.md ===== -->

# WORKSPACE_MEMORY — Changelog

Append one line per update (newest first). Regenerate the single file after each edit: `python WORKSPACE_MEMORY/build_single_file.py`.

| Date | Files touched | What changed |
|---|---|---|
| 2026-09-07 | all | Initial complete pack built from: CLAUDE.md, AGENTS.md, .windsurfrules, docs/, TeamBot/commands.json (128), .devin skills (95) + agents (9), .devin/knowledge (claude_memory.md 94 entries, 2 MOMs, JIRA/Confluence snapshots), Claude memory dir (37 entries), execution reports, defect JSONs, git history of this repo + sibling repos, CDUI project memory. |
