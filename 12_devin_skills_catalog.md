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
