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
