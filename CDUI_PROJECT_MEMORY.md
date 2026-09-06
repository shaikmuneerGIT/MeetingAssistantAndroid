# CDUI UI Automation - Complete Project Memory

**Project:** UI / E2E test automation for **CDUI** (Customer Data User Interface), Dell MDG
**Author / owner:** Shaik Muneer (E2E Testing)
**Snapshot date:** 2026-09-06 (project started 2026-08-19)
**Local workspace:** `C:\repos\CDUI` (framework in `C:\repos\CDUI\CDUI_UIAutomation`)
**Sister project:** `C:\repos\cidm_cilms_apiautomation\CIDM_APIAutomation` (API automation, referenced in-process)

This file is the single "memory" for the project: what the application is, which micro-frontends
(MFEs) we test, the technology, how many test cases exist and how they are counted, why the
framework is worth having, how to run it, the test data, every hard-won gotcha, defect status,
run history, and open items. It is written so that a person or an AI assistant can resume the
work from this file alone.

---

## 1. Project at a glance

| Item | Value |
|---|---|
| Application under test | CDUI - React **module-federation shell** hosting ~22 micro-frontends, built on the Dell Design System (DDS) |
| Primary environment | **G4** (pre-prod): `https://g4.cdui-np.kob.dell.com/` |
| Secondary environment | **G1**: `https://g1.cdui-np.kob.dell.com/` (`CDUI_ENV=G1`, own `launchSettings.G1.json`) |
| Stack | C# / .NET 8, xUnit 2.9, Shouldly 4.2, Microsoft.Playwright 1.49, Microsoft Edge (channel), Oracle (CPD_PARTY), Python + Outlook COM for reporting, GitLab CI, Devin skills |
| Automated test cases | **219** (175 `[Fact]` + 44 `[InlineData]` rows across 12 `[Theory]` methods) in **34 test classes** (~6,470 lines of test code) |
| Regression checks | **120** (111 in the six in-scope MFEs + 9 out-of-scope search MFEs) |
| P1 suite (`Priority=P1`) | **96** cases across the 6 tracker features |
| Tooling / investigation cases (not coverage) | **84** (34 discovery, 33 DB probes, 17 defect-investigation probes) |
| Write / creation flows (excluded from routine runs) | **15** |
| Full read-only regression runtime | ~50 minutes (45-70), headless, unattended |
| Golden rule | **Read-only.** Every edit in CDUI raises a real MDG stewardship ticket in G4. Creation, Provisioning and the E2E ticket test are excluded from every routine run. |

---

## 2. The application: CDUI and its MFEs

CDUI is a React module-federation shell. The side navigation is DDS (`data-testid="side-nav-test-id"`,
groups and links are both `role="menuitem"`). Each menu entry loads a remote module (an MFE) served by
its own Kubernetes pod ("POD" in team talk = the pod serving an MFE, or a Dell agile team).
Post-login redirect is `/review-list` (Ticket List).

### 2.1 MFE inventory (routes, owners, tracker priority, automation status)

| # | MFE / screen | Route(s) | Owner (tracker) | Priority | Automation |
|---|---|---|---|---|---|
| 1 | Homepage / app shell (dashboard, side nav, Access Request) | `/`, `/review-list`, `/access-request` | CDUI | P1 | 11 checks + 1 guard |
| 2 | Ticket List | `/tickets`, `/tickets/TKT…` | CDUI | P1 | 10 checks |
| 3 | Create Stewardship Ticket | `/tickets/create` | CDUI | P1 | 35 checks + 3 guards |
| 4 | Party Search (+ Party 360 details tabs, Relationships, Sites, Add Sites) | `/customer-search`, `/customer-search/party?customerPartyId=P…&activeTab=tab-…`, `/add-sites` | CDUI / CPD Core (tabs come from several teams; relationship tabs from PCG) | P1 | 22 checks + 7 guards |
| 5 | ODW ID Translation | `/odw-id-translation` | CDUI | P1 | 17 checks + 2 guards |
| 6 | Account Search | `/account-search` | ARD | P1 | 3 checks (thin: MFE renders blank) |
| 7 | Partner User Management | `/my-account` | MyAct | P1 | load check (blank) |
| 8 | UCID Search | `/ucid-search` | Castle | P2 | 1 search check |
| 9 | Castle Hierarchy Search | `/hierarchy/search` | Castle + CSS | P2 | 1 search check |
| 10 | VCID Search | `/vcid-search-customer` | CPH | P2 | 1 search check |
| 11 | User Search | `/user-search` | CPD Profile | P2 | 1 search check |
| 12 | Org Profile Search | `/org-profile-search` | CPD Profile | P2 | 1 search check |
| 13 | DNB Search | `/dnb-search` | - | P2 | 1 search check |
| 14 | Sponsoring Distributor Contacts | `/sponsoring-distributor` | - | - | load check (blank) |
| - | Bulk Upload (OldCo), Bulk Management (Create/Edit Template, Upload Data, Bulk Ticket List), Rules Lookup (P3), Access Review / Access Details, Create Org Profile, Dashboard widgets | - | - | - | **not covered yet** |

Party 360 tab ids: `tab-details`, `tab-party-sites`, `tab-customer-account`, `tab-customer-contacts`,
`tab-customer-relationships`, `tab-hierarchy-visualization`, `tab-org-profiles`.
Visible tab labels: Details, Sites, Customer Billing Account, Contacts, Relationships, Core Party Hierarchy, More.

### 2.2 Regression scope (agreed with Muneer, 2026-08-27)

Only six MFEs are reported on: **Homepage, Ticket (Ticket List), Create Stewardship Ticket,
Party Search & Related actions, ODW ID Translation, Account Search.** The other search MFEs still
execute but are excluded from the report and counted separately. Lavanya's regression tracker
priorities (08/01): P1 = Homepage, Ticket list, Create stewardship ticket, Party Search & Related
actions, Account Search (ARD), Partner User Management (MyAct), ODW ID translation; P2 = Bulk upload
OldCo, UCID search, Org Profile search, Castle Hierarchy, VCID search, User search; P3 = Rules Lookup.

---

## 3. Test cases: how many, and how they are counted

### 3.1 Headline numbers (the honest split)

| Group | Cases | Purpose |
|---|---:|---|
| **Regression checks** | **120** | Assert correct behaviour; a failure means the application is wrong. **This is the number we report.** |
| - of which in the six in-scope MFEs | 111 | Homepage 11, Ticket 10, Create Stewardship Ticket 35, Party Search & Related 22, ODW ID Translation 17, Account Search 3, plus 13 defect guards spanning the MFEs |
| - of which out-of-scope search MFEs | 9 | Load-and-respond checks for VCID, UCID, DNB, Org Profile, User Search, Castle Hierarchy, Partner UM, Sponsoring Distributor |
| **Tooling & investigation** | **84** | Discovery walkthroughs (34), Oracle relationship DB probes (33), defect-investigation probes (17). Zero-assertion or record-only; never counted as coverage, never in regression runs. |
| **Write / creation flows** | **15** | Create parties, sites, relationships via CIDM API and UI. Excluded because every edit raises a real MDG ticket. |
| **Total automated** | **219** | 175 `[Fact]` + 44 `[InlineData]` rows |

Per-MFE Devin skills count the guards inside each MFE: Homepage 12, Ticket 10, Create Ticket 38,
Party Search 30, ODW 19, Account Search 3.

### 3.2 Per test class

| Test class (Tests/…) | Category trait | Cases | Reported MFE / group |
|---|---|---:|---|
| Smoke/SmokeTests | Smoke, P1 | 3 | Homepage |
| Smoke/ServiceAccountAuthTests | AuthPreflight | 2 | Homepage (pipeline preflight) |
| Navigation/NavigationTests | Navigation, P1 | 6 (3 + theory×3) | Homepage |
| Tickets/TicketListTests | TicketList, P1 | 6 | Ticket |
| Search/TicketSearchTests | Search, P1 | 4 | Ticket |
| Tickets/CreateStewardshipTicketTests | Stewardship, P1 | 10 (7 + theory×3) | Create Stewardship Ticket (incl. the E2E creator, excluded from routine runs) |
| Tickets/CreateTicketValidationTests | Stewardship, P1 | 25 (7 + 3 theories×18) | Create Stewardship Ticket |
| PartySearch/PartySearchTests | PartySearch, P1 | 6 | Party Search & Related |
| PartySearch/PartyDetailsTests | PartyDetails, P1 | 3 | Party Search & Related |
| Search/PartyIdentifierSearchTests | Search, P1 | 4 | Party Search & Related |
| Search/SitesAndContactsSearchTests | Search, P1 | 3 | Party Search & Related |
| Creation/RelationshipValidationMatrixTests | Creation | 3 | Party Search & Related (read-only validation matrix) |
| Creation/ArmToCustomerTests | ArmToCustomer | 3 (theory) | Party Search & Related |
| OdwTranslation/OdwIdTranslationTests | OdwTranslation, P1 | 4 | ODW ID Translation |
| OdwTranslation/OdwBridgeTranslationTests | OdwTranslation, P1 | 10 (5 + theory×5) | ODW ID Translation |
| Creation/OdwDcnTranslationTests | Creation | 3 | ODW ID Translation |
| AccountSearch/AccountSearchTests | AccountSearch, P1 | 3 | Account Search |
| Regression/DefectRegressionTests | DefectRegression (+ KnownDefect traits) | 13 (11 + theory×2) | Guards, mapped per test to its MFE |
| Search/GlobalSearchSweepTests | Search | 9 (6 + theory×3) | Out-of-scope search MFEs |
| Creation/RelationshipCreationTests | Creation | 3 | Write flow |
| Creation/RelationshipHappyPathTests | Creation | 2 (theory) | Write flow |
| Creation/RelationshipTypeMatrixE2ETests | RelationshipMatrix | 3 (theory) | Write flow |
| Creation/SiteTests | Creation | 3 | Write flow |
| Provisioning/ProvisioningTests | Provisioning | 4 | Write flow (API/DB provisioning) |
| Provisioning/RelationshipDbProbe | Provisioning | 33 | Tooling: Oracle probes |
| Discovery/AppExplorerTests | (none) | 9 | Tooling: page dumps |
| Discovery/WalkthroughTests | (none) | 22 | Tooling: dev-question walkthroughs Q01-Q26 |
| Discovery/G1DataProbe, PartyClassProbe, TicketStatusProbe | (none) | 3 | Tooling |
| DefectValidation/JiraDefectValidationTests | DefectValidation | 11 | Tooling: MAVCDUI-510..519 replays |
| DefectValidation/ReporterExactProbe, ConfirmationProbe, AccessRequestProbe | DefectProbe | 6 | Tooling |

### 3.3 What each in-scope MFE covers

- **Homepage (11):** SSO reaches the app; shell renders (not blank); no failed module requests;
  personal welcome; side nav lists all 22 MFE entries; 3× priority MFE opens via menu; unknown route
  does not crash the shell; service-account sign-in + Ticket List access (AuthPreflight).
- **Ticket List (10):** loads with tickets; MDG-requested columns; Ticket ID filter (known MDG
  callout); Status filter; pagination Next/Previous; items-per-page; every key filter finds a known
  ticket (ID / Type / Issue / Status / Source / FED); composed filters; clear filter restores; deep link
  `/tickets/TKT…` renders detail.
- **Create Stewardship Ticket (35):** form renders; Issue Types follow Ticket Type; conditional fields
  appear; empty submit blocked; 300-char description enforced; country auto-populates from identifier;
  valid identifier enables Submit (Party ID / UCID / DCN triple); 8× ticket/issue × identifier combos
  incl. Location ID; 7× malformed identifier rejected; non-existent party rejected; "Input record not
  found" for unknown ids; all five identifier types offered; per-type hint text; Castle Hierarchy
  reduced field set; changing Ticket Type resets Issue Type; Location ID / Partner Track ID lookups;
  country-mismatch observation; **E2E ticket creation verified in queue** (excluded from routine runs).
- **Party Search & Related actions (22):** all 12 criteria fields; Reset Form; Party ID search; fuzzy
  Company + Country; non-existent handled; identifier deactivates fuzzy; identifiers mutually exclusive;
  every Site ID of a multi-site party resolves; DUNS round-trip; new party by name; party link opens
  details; summary fields; all 6 detail tabs open clean; Sites filter by Site ID / Purpose; Contacts tab;
  relationship validation matrix (Partner→Direct, Partner→Partner, wrong-class source rejected);
  ARM To Customer.
- **ODW ID Translation (17):** page loads with required Search Type; type list offers the MDG
  translations; each type renders its form; DCN Legacy + Party sections; DCN grid responds;
  **DCN ↔ Party strict round-trip**; APID form + search; 5× APID → Party; **APID round-trip**;
  **party with 3 APIDs returns all**; UCID form + search; **UCID ↔ Party strict round-trip**.
- **Account Search (3):** page loads with a Search control; criteria fields render; search executes.
  Deliberately thin because the MFE has been too unstable to build on.
- **Defect guards (13):** one per confirmed MAVCDUI-510..519 defect plus a positive control
  (`Sites_Tab_SiteId_Filter_Honours_NonMatching_Value`). Red while the defect is open, green when fixed.

---

## 4. Technology stack and why each piece was chosen

| Layer | Choice | Why |
|---|---|---|
| Language / runtime | C# on .NET 8 | Same stack as CIDM_APIAutomation, so one team maintains both and the API suite is called in-process from UI tests |
| Test framework | xUnit 2.9.2 + Shouldly 4.2.1 | Data-driven theories cover large matrices compactly; Shouldly messages read as plain English in the report |
| Browser automation | Microsoft.Playwright 1.49.0, **Edge channel** | Auto-waiting removes flaky sleeps; Edge carries Dell IT auth policies so Desktop SSO works (bundled Chromium fails DSSO) |
| Authentication | Persistent browser profile (`LaunchPersistentContextAsync`) | OIDC → Okta → Entra completes once; session reused across runs; no credentials in code |
| Design pattern | Page objects + DDS component wrappers (`DdsField`, `DdsDropdown`, `SideNav`) | DDS renders random element ids per re-render; controls are resolved by label/role, so the suite survives UI rebuilds |
| Test data | CIDM API (`CidmApiClient`) + Oracle (`CidmDbClient`, `Oracle.ManagedDataAccess.Core` via the API project) | Parties, sites and contacts provisioned through the real API; identifiers sourced from migration tables, not hard-coded |
| Evidence | Full-page failure screenshot + Playwright trace zip per test | Every failure is reproducible; traces replay with DOM snapshots |
| Results | `XunitXml.TestLogger` + `JunitXml.TestLogger` + TRX | xunit XML for parity with the API suite; JUnit for GitLab; TRX for the report generator |
| Reporting | Python `scripts/build_regression_report.py` → self-contained HTML (screenshots base64-embedded) | One file survives e-mail intact |
| Delivery | `scripts/send_regression_report.ps1` via Outlook COM | Corporate SMTP is blocked from the workstation |
| Execution | `run.bat`, `run-p1.bat`, `run-g1.bat`, `test.bat`; GitLab CI (`.gitlab-ci.yml`), `Dockerfile` (mcr.microsoft.com/playwright/dotnet:v1.49.0-jammy); Devin skills in `.devin/skills/` | Runs locally, on a schedule, or unattended by anyone on the team through one Devin command |
| Serialisation | Newtonsoft.Json 13 | Provisioned-data cache, config |
| Coverage tooling | coverlet.collector | Standard collector, not actively used |

Other build facts: `xunit.runner.json` disables collection parallelism (`parallelizeTestCollections=false`,
`longRunningTestSeconds=120`); the csproj references `CIDM_APIAutomation.csproj` via the `CidmApiDir`
MSBuild property (net6.0 project consumed from net8.0 with `DOTNET_ROLL_FORWARD=LatestMajor`);
post-build targets re-copy our `launchSettings.json`/`xunit.runner.json` last, **delete the old
`xunit.runner.visualstudio.dotnetcore.testadapter.dll` (2.4)** the API project drops (otherwise vstest
crashes with `MissingMethodException: LogRaw`), and mirror the API bin's `DCNDtls/`, `Resources/` and
`DBUtils/*.txt|csv` (rules + `PartyQuery.txt`) into our output.

---

## 5. Framework architecture

```
C:\repos\CDUI
├── CDUI_UIAutomation/                 the test project
│   ├── Config/    TestSettings (env vars + launchSettings, CDUI_ENV=G1 switch), TestData, ProvisionedData
│   ├── Core/      PlaywrightFixture (persistent Edge context, self-recovering RestartAsync),
│   │              BaseUiTest (page per test, tracing, failure screenshots, consent-modal locator handler),
│   │              AuthHelper (SSO detection, Okta form login, consent accept, 5s stability window,
│   │              Authenticated = side nav present), CidmApiClient, CidmDbClient, CidmEnvironment,
│   │              PartyProvisioner, ReportMailer
│   ├── Pages/     BasePage, HomePage, PartySearchPage, PartyDetailsPage, TicketListPage,
│   │              OdwIdTranslationPage, AddSitesPage; Components/ SideNav, DdsDropdown, DdsField
│   ├── Tests/     Smoke, Navigation, Tickets, PartySearch, Search, OdwTranslation, AccountSearch,
│   │              Regression (defect guards), Creation, Provisioning, DefectValidation, Discovery
│   ├── TestArtifacts/ (git-ignored) browser-profile, traces, screenshots, discovery dumps,
│   │              provisioned/parties.json, defect-validation/, CDUI_Regression_Report.html
│   ├── launchSettings.json / launchSettings.G1.json   (git-ignored; templates committed)
│   ├── run.bat · run-p1.bat · run-g1.bat · test.bat · xunit.runner.json · Dockerfile
├── scripts/   build_regression_report.py, send_regression_report.ps1, run_all_mfes.sh, run_missing_mfes.sh
├── tools/     mkreport.py (G1 report from console log), send_report.py
├── .devin/skills/  cdui-regression (parent) + cdui-homepage, cdui-ticket, cdui-create-ticket,
│                   cdui-party-search, cdui-odw-translation, cdui-account-search
├── .gitlab-ci.yml  auth-preflight → build → p1-regression / full-regression (manual) / nightly; G1 jobs
├── G1_Report/      G1_Test_Report.html + screenshots (2026-08-28 run)
└── *.md / *.html   README, MFE_TEST_INVENTORY, P1_REGRESSION, SEARCH_COVERAGE, TEST_REPORT,
                    TEAMS_MESSAGE, DEV_QUESTIONS, DEFECT_VALIDATION_510-519, RELATIONSHIP_TYPES,
                    RELATIONSHIP_HOWTO, VERIFY_STEPS, PIPELINE, DEMO_NOTES, AUTOMATION_OVERVIEW
```

Design conventions: mirror CIDM_APIAutomation (launchSettings-driven env vars, xUnit categories,
xunit XML results); one Playwright page per test; `[Trait("Category", …)]` per module,
`[Trait("Priority","P1")]` + `[Trait("Feature", …)]` for the tracker suite,
`[Trait("KnownDefect","MAVCDUI-nnn")]` on guards for still-open defects; prefer `name=` /
`data-testid` selectors, else label→input XPath; read-only unless the test lives in Creation/Provisioning.

---

## 6. Authentication (the part that took real debugging)

Chain: app → OIDC (`www-sit-g4.dell.com`) → Okta (`myaccess-uat.dell.com`) → silent Microsoft Entra
login (`prompt=none`). Works non-interactively only when **both** hold:

1. `CDUI_BROWSER_CHANNEL=msedge` (default on Windows). Bundled Chromium fails Desktop SSO.
2. `LaunchPersistentContextAsync` with the profile at `TestArtifacts/browser-profile`. Ambient auth is
   blocked in incognito contexts; the profile also keeps the session between runs. First run on a fresh
   machine may need one manual (headed) Okta login.

Facts and traps:
- The shell renders briefly **before** redirecting to SSO: auth checks need a 5s stability window, and
  "Authenticated" now requires `[data-testid="side-nav-test-id"]` (pre-redirect shell has no side nav).
- **Consent modal (Terms and Conditions, Accept/Decline)** appears on fresh sessions and whenever the
  profile cache is cleared. It blocks every interaction underneath. On 2026-09-04 it caused 24 of 25 tests
  to fail at their ~2m20s wait timeouts, which looks exactly like the app being down. Fix: `BaseUiTest`
  registers `Page.AddLocatorHandlerAsync` on `GetByText("Terms and Conditions")` that clicks Accept.
  Diagnosis rule: when a whole run fails at timeouts, **look at a failure screenshot first**.
- Accepting the consent modal can bounce the app to Home mid-wait; authenticate first, then navigate.
- `BypassCSP=true` is required in the context: Okta pages block eval, so a mid-run session refresh
  otherwise kills every `WaitForFunction` with `EvalError`.
- **Landing-page flake:** when the saved session needs a silent refresh, `/` hops through OIDC and lands
  on `/review-list`, so `.welcome-title` never appears (`Navigate_To_CDUI_Landing_Page` fails). Passes on
  a fresh session. Harness gap: `HomePage.GotoAsync` should re-navigate to `/` when the post-auth URL differs.
- **Pipeline / service-account SSO (2026-09-04, blocked):** the e-mail service account used by the API
  team's reports (`SMTP_USER` in the API repo's launchSettings) has a password that is **correct for AD**
  (SMTP AUTH succeeds), but Okta answers **"Unable to sign in"** for the CDUI app, so the account is not
  enabled/assigned for interactive Okta login. Only one attempt was made; **do not retry** (a lockout
  would break the API team's e-mail reports). Needs an Okta/IT request: activate the account, assign it
  to the CDUI (G4 OIDC) app, allow password authenticator with MFA/device-trust exemption, then a CDUI
  role in G4 - or a dedicated CDUI test account.
- Traps found while testing that: `$env:CDUI_BROWSER_CHANNEL = ''` in PowerShell **deletes** the
  variable so Edge silently returns; even bundled Chromium completes Windows integrated auth on a Dell
  laptop via the GPO intranet-zone list (three false-positive "service account signed in" runs greeted
  the personal user). Fixes: `CDUI_BROWSER_CHANNEL=bundled|chromium`, `CDUI_DISABLE_DSSO=true` (passes
  `--auth-schemes=basic,digest --auth-server-allowlist=_`), `AuthHelper.LastLoginUsedOktaForm`, and the
  preflight fails when the greeted name equals `Environment.UserName`. Wipe `CDUI_ARTIFACTS_DIR` before
  a service-account run and set `CDUI_TRACE=false` (traces record the typed password).
- Pipeline itself still blocked: the workspace is not a git repo, no Dell-network GitLab runner is
  assigned, Docker is not installed locally. Linux options are documented in PIPELINE.md (Okta form
  login with an MFA-exempt service account; Kerberos keytab; pre-seeded storageState for demos only).

---

## 7. Configuration and how to run

### 7.1 Environment variables (launchSettings.json → env; env wins)

| Variable | Default | Purpose |
|---|---|---|
| `TEST_ENVIRONMENT` / `CDUI_ENV` | `G4` | `CDUI_ENV=G1` reads `launchSettings.G1.json` first |
| `CDUI_BASE_URL` | `https://g4.cdui-np.kob.dell.com/` | Target |
| `CDUI_HEADLESS` | `true` in code, `false` in local launchSettings | Headed = `--start-maximized` + `NoViewport`; headless = 1920×1080 (team requirement: maximized) |
| `CDUI_CLEAR_CACHE` | `true` | Deletes profile cache dirs before every run, keeps SSO cookies (team practice after stale-cache MFE issues) |
| `CDUI_BROWSER_CHANNEL` | `msedge` on Windows, bundled Chromium elsewhere | `bundled`/`chromium` to force Playwright's browser |
| `CDUI_DISABLE_DSSO` | `false` | Blocks Negotiate/NTLM so the Okta form must appear |
| `CDUI_SSO_USER` / `CDUI_SSO_PASSWORD` | empty | Okta form login (CI / service account); never commit |
| `CDUI_SLOWMO_MS` | `0` | Per-action delay |
| `CDUI_TIMEOUT_MS` | `30000` | Default Playwright timeout |
| `CDUI_TRACE` | `true` | Trace zip per test |
| `CDUI_ARTIFACTS_DIR` | `TestArtifacts` | Profile, traces, screenshots |
| `CDUI_API_AUTOMATION_DIR` | `C:\repos\cidm_cilms_apiautomation\CIDM_APIAutomation` | API repo (launchSettings loaded by `CidmEnvironment`) |
| `CDUI_FORCE_PROVISION` | `false` | Ignore `TestArtifacts/provisioned/parties.json` |
| `CDUI_TEST_COMPANY_NAME` / `CDUI_TEST_COUNTRY` | `Dell` / `United States` | Fuzzy-search seed |
| `CDUI_TEST_PARTY_NUMBER`, `CDUI_TEST_MULTISITE_PARTY` | see §8 | Known parties |
| `CDUI_TEST_DCN`, `CDUI_TEST_DCN_BUID`, `CDUI_TEST_DCN_PARTY` | see §8 | Converted DCN bridge pair |
| `CDUI_TEST_UCID`, `CDUI_TEST_UCID_PARTY` | see §8 | UCID bridge pair |
| `CDUI_TEST_APID`, `CDUI_TEST_APID_PARTY` | see §8 | APID bridge pair |
| `CDUI_TEST_LOCATION_ID`, `CDUI_TEST_PARTNER_TRACK_ID` | see §8 | Create Ticket identifiers |

Machine prerequisite: no admin .NET SDK; .NET 8 SDK is per-user at `%USERPROFILE%\.dotnet`. Every shell:

```powershell
$env:DOTNET_ROOT = "$env:USERPROFILE\.dotnet"
$env:PATH = "$env:USERPROFILE\.dotnet;$env:PATH"
$env:DOTNET_ROLL_FORWARD = "LatestMajor"
```

### 7.2 Commands

```powershell
cd C:\repos\CDUI\CDUI_UIAutomation
dotnet build -v q --nologo
.\run.bat                      # full suite (xunit XML -> TestResults\cdui_ui_results.xml)
.\run.bat Smoke                # one Category
.\run-p1.bat                   # Priority=P1 (96 cases)
.\run-p1.bat Ticket            # one Feature: Homepage|Ticket|CreateStewardshipTicket|ODWIDTranslation|PartySearch|AccountSearch
.\run-g1.bat                   # P1 on G1
.\test.bat --list-tests        # forwards args verbatim to dotnet test

# The read-only regression filter (identical in the Devin skill and GitLab full-regression job):
dotnet test --no-build --filter "FullyQualifiedName!~Discovery&Category!=Provisioning&Category!=Creation&Category!=DefectValidation&Category!=DefectProbe&FullyQualifiedName!=CDUI_UIAutomation.Tests.Tickets.CreateStewardshipTicketTests.Create_General_Inquiry_Ticket_E2E" --logger "trx;LogFileName=regression.trx"

# Defect guards
dotnet test --filter "Category=DefectRegression&KnownDefect="     # green gate, must stay green
dotnet test --filter "KnownDefect!="                              # red until dev fixes land; a pass = defect fixed

# Investigation / write flows (deliberate, never scheduled)
dotnet test --filter "Category=DefectValidation"   # 10 JIRA replays (record verdicts, no assertions)
dotnet test --filter "Category=Provisioning"       # creates party + DCN + sites, queries Oracle
dotnet test --filter "Category=Creation"           # creates relationships / sites in G4

# Report + e-mail (from C:\repos\CDUI)
python scripts\build_regression_report.py [--area "ODW ID Translation"]
powershell -ExecutionPolicy Bypass -File scripts\send_regression_report.ps1 [-Draft] [-To x@dell.com]
```

Debugging: failure screenshots `TestArtifacts/screenshots/FAILED_<test>_<time>.png`; traces
`TestArtifacts/traces/<test>_<time>.zip` (`playwright.ps1 show-trace <zip>`).

### 7.3 Unattended workflow (Devin skills, `.devin/skills/`)

`cdui-regression`: build → read-only run (45-70 min) → `build_regression_report.py` → Outlook e-mail
to s.muneer@dell.com → chat summary with in-scope tallies and per-area breakdown. Per-MFE skills do the
same for one MFE (`--area`). Rules: fully unattended, read-only, never edit a test to make it pass, a
non-zero `dotnet test` exit is expected while known-defect guards are red.

Failure buckets used everywhere (report, skills, Teams messages):

| Bucket | Meaning |
|---|---|
| **known-defect** | A guard reporting MAVCDUI-510..519. Expected red. Not a regression. |
| **mfe-stall** | The screen never rendered (blank MFE). Application/environment defect, not a test defect. |
| **other** | Genuinely needs a human look. |

A healthy run = only known-defect failures plus whatever mfe-stalls the environment threw. A new
`other` failure is the finding worth escalating. If the positive control
`Sites_Tab_SiteId_Filter_Honours_NonMatching_Value` also fails, suspect harness/environment first.

Report script gotchas: `$PSScriptRoot` is empty inside a `param()` default under PowerShell 5.1
(resolve from `$MyInvocation.MyCommand.Path`); TRX `testName` is fully qualified while the console log
gives the bare method name (normalise or every lookup misses); the script falls back to
`TestArtifacts/full-regression.log` when a run is killed and merges every matching `.trx`.

---

## 8. Test data (G4 unless stated)

| Data | Value / source |
|---|---|
| Fresh parties + CPDn DCN + sites | `Core/PartyProvisioner` → `CidmApiClient.CreateUsCustomer()` (~5s: party + DCN + site + contact) and `AddUsSite(party, dcn, "Billing"/"Shipping")`; cached in `TestArtifacts/provisioned/parties.json` (P15804401241, P15804401298, P15804430482 / D11320928153 with billing S16006182285 + shipping S16006182298; 08/24 run minted P15804604218 / D11320928853) |
| Multi-site party | P15804430482 (US, 5 sites) |
| Converted DCN ↔ Party (US) | DCN `640369434444` / BUID `11` / P15798066869 (KSW SOLUTIONS). DCN triple format `640369434444-11-10000001234577212`. This party has **no** APID/UCID mappings |
| UCID ↔ Party | UCID `1003299042` ↔ P12836187399 (IS_PARTY_CONVERTED=C). Canonical source: `SELECT * FROM CPD_PARTY.T_UCID_CPD_DATA_MIGRATION WHERE ucid='…'` on **G4**. Sample UCID 1000000008 is not in G4 |
| APID ↔ Party (team-supplied 08/25) | `CDUI_TEST_APID=3614118606` / P15797091770; others 3614110623/P15795479753, 3614119289/P15802039218, 2658464064/P12777692118, 3597188700/P15780815173; P12310765294 has three APIDs 3614105990 / 3614105978 / 3614104540 (many-to-one verified) |
| Location IDs (team-supplied 08/25) | 18204218062, 18204217959 (11-digit numerics; not party site ids; Party Search returns nothing for them) |
| Partner Track ID | unknown; the field gives no feedback at all for unknown values (open dev question) |
| Known ticket | TKT1025348 (used by ticket filter tests); E2E-created TKT1025406, TKT1025407 |
| Party classes (verified) | Partner + Commercial: P15797091770, P15802039218, P12777692118, P12310765294. Direct + Commercial: P15804430482, P15795479753, P15798066869, P15804401298. API-created parties are always class **Direct** |
| Reporter's party for 514-516 | P12308413151 (Sites: Site ID `ZQXJ7`, Site Name "Clinic"; Contacts: "Ramesh") |
| Not usable | Party numbers from the API suite's `test_data.json` (P144672919xx) are not in G4; resolve live parties via fuzzy search (`Dell` + `United States`) |
| G1 | Most G4 identifier pairs do not exist in G1; fill `launchSettings.G1.json` (see G1 run, §11) |

CVS rule for site creation: contact responsibilities must match site purposes (align every
`$..responsibilities` array). Site POST = `POST /v1/customers/{party}/sites`, expect 201 with
SUCCESS/WARNING. Site rules auto-download needs `RULES_API_*` env. Cast `(object)` around
dynamic-returning CIDM methods before LINQ. Legacy DCNs are 12-digit numerics; CPDn
`dellCustomerNumber` is `D` + 11 digits.

JIRA access for defect work: `python scripts/defect_query.py --keys …` in the API repo (creds from its
launchSettings `JIRA_BASE_URL` + `JIRA_PAT_TOKEN`, `verify=False`). Descriptions on the 510-519 batch
were all empty; the real steps live in attachments (`/rest/api/2/issue/KEY?fields=attachment`, then GET
`content`) and the svc_prdema EMA comment. Always read attachments before judging a defect.

---

## 9. Benefits of the framework (what we get from it)

1. **Regression in ~50 minutes, unattended, read-only.** 111 in-scope checks run headless. The same
   coverage by hand is roughly 4-7 hours of a tester's day (estimate from the check count at 2-4 min per
   check, not a measured manual baseline).
2. **Evidence attached to every failure.** Repro steps, expected vs actual, the exact assertion and a
   full-page screenshot at the moment of failure, plus a replayable Playwright trace. Defect tickets
   arrive complete; developers stop asking how to reproduce.
3. **Defects verified independently in hours.** All ten MAVCDUI-510..519 defects raised by the manual
   E2E team were re-tested and confirmed within a day, including pinning 510 to a single ticket-type
   combination (Castle Hierarchy Update) that a dev testing the obvious path would close as
   "cannot reproduce", narrowing 514/515/516 to two column bindings (Site Name, Identifying Site) rather
   than "search is broken", and measuring that 511 and 519 are the same auto-hide behaviour (~30s / ~28s)
   in two components.
4. **A free signal when a fix ships.** Each confirmed defect has a guard asserting the correct behaviour:
   red while open, green the day it is fixed. On 2026-09-01 the guards proved 512 and 513 fixed in G4 and
   that 514's "Waiting to Deploy" Jira status was not yet reality.
5. **Catches what a manual pass retries past.** Intermittent MFE blank/stall failures (Account Search
   rendered only 252 chars of shell chrome in 60s, 4 of 4 tests failed) are recorded, not retried away.
   This P1 defect is not in JIRA yet (checked all 507 MAVCDUI tickets).
6. **Safe to run against G4 daily.** Creation/Provisioning/E2E-ticket flows are excluded, so repeated
   runs never pollute the live MDG queue.
7. **Resilient to UI rebuilds.** DDS random ids are never cached; controls resolve by label/role; auto-wait
   instead of sleeps; the fixture restarts the browser if it crashes mid-run.
8. **Reusable data plumbing.** Parties, DCNs and sites are minted through the real CIDM API in-process and
   identifiers come from the migration tables, so tests are not pinned to data that drifts.
9. **Repeatable by anyone.** One Devin command or one GitLab job builds, runs, reports and e-mails.
10. **Honest numbers.** The suite reports 120 regression checks, not 219, and keeps zero-assertion probes
    out of the coverage count. A red known-defect guard is the system working as designed; driving the
    pass rate to 100% would mean deleting the tests that prove the defects exist.

Evidence that the automation fixed its own gaps: an audit found the pre-existing suite would have caught
**0** of the ten defects (tests exercised only the passing path; `Cancel.ClickAsync()` was always the last
line; Access Request and the Billing Account tab had no tests). It now guards all ten.

---

## 10. Known defects and guard status

### 10.1 MAVCDUI-510..519 (raised 2026-08-26 by Kiran, validated 2026-08-27, guards re-swept 2026-09-01)

| Key | Area | Verdict | Detail | Guard status (G4, 09/01) |
|---|---|---|---|---|
| 510 | Create Ticket | Valid, one path only | "Customer Id is required" only on Castle Hierarchy → Castle Hierarchy Update; General Inquiry reads "Identifier is required" | Still reproduces. Jira = CANCELLED (won't-fix) yet guard red - needs decision |
| 511 | Create Ticket | Valid | "Input record not found" banner self-hides at ~30s, leaving Submit disabled with no reason | Still reproduces |
| 512 | Create Ticket | Confirmed; "expected" is a UX call | Cancel lands on `/tickets` instead of origin | **Fixed in G4** (two clean passes; trait removed) |
| 513 | Access Request | Valid | Cancel leaves for Ticket List, not the opener (Home) | **Fixed in G4** (trait removed) |
| 514 | Sites tab | Valid, narrower | Site Name + Identifying Site filters ignored; Site ID works on the same grid | Still reproduces (Jira "Waiting to Deploy", fix not in G4) |
| 515 | Billing Account tab | Valid, narrower | Site Name filter ignored | Still reproduces |
| 516 | Contacts tab | Valid, clearest | Site Name filter ignored (8 → 8 rows with "Ramesh") | Still reproduces |
| 517 | ODW | Valid | Reset Form clears inputs but leaves "Party not found" banner (click Reset immediately) | Still reproduces |
| 518 | Party Search | Valid, exact | No required markers until Search; then "Company Name is required" / "Country is required" | Still reproduces |
| 519 | ODW | Valid | "Party not found" banner self-hides ~28s | Still reproduces |

Deploy status in Jira ≠ G4 reality: always verify with the guards. Three verdicts (511, 517, 519) were
initially wrong (recorded not-reproduced) because of the banner-detection trap in §12.

### 10.2 Other standing findings for developers (DEV_QUESTIONS.md)

- **Dev question 19 - MFEs intermittently render blank / stall** (chronic: Account Search P1, Partner
  User Management P1, Sponsoring Distributor; intermittently VCID, User Search, DNB, Org Profile, UCID
  Search, Create Ticket, ODW, party details). Remote module never mounts within 60s, no error shown;
  matches the tracker's own "didn't load in Edge" note. Six unrelated tests once failed at an identical
  1m34s after passing 20 min earlier. **Not yet in JIRA; recommended to raise.**
- **Add Relationship accept-then-cancel** - answered and closed (08/25, Eduardo/PCG): the UI accepts and
  creates a ticket; the rules engine validates asynchronously and cancels it, reason in the ticket
  comments. Creation cannot be blocked up front because the relationship tabs come from PCG. Follow-up:
  TKT1025362/376 were cancelled with no comment. Only ARM To Customer validates synchronously in the
  dialog (inline error + Add disabled), proving up-front blocking is possible.
- Party details `activeTab` deep link intermittently ignored (resolved 08/25 per dev); duplicate DOM ids
  when tabs overflow into "More"; blank Legal Name on a CPDn-created party.
- New parties are not findable by fuzzy name search on creation day (identifiers work immediately;
  indexed by next day) - SLA question.
- Performance: Ticket List filter refetch can exceed 90s (270K+ tickets), ticket detail spinner 20-60s,
  ODW lookups slow; filter sweep once took 7m48s.
- Both "Is Federal Customer" radios carry `data-testid="is-federal-option-yes"`.
- Customer Country could be overridden to contradict the party's country (US→BH) without warning; the
  select is now DISABLED on ODW Inquiry / Party Hierarchy - possibly an intentional fix, confirm with dev.
- Ticket ID search "intermittently finds nothing" (MDG callout) could never be reproduced; older
  Cancelled tickets show an empty Action cell - intended?
- Asks: stable `data-testid` on key controls; MDG-lead test account + non-interactive login for CI;
  converted DCN pairs for EMEA/APJ/LATAM; Add Sites wizard scope and ticketing behaviour.

---

## 11. Run history and baselines

| Date | Run | Result | Notes |
|---|---|---|---|
| 2026-08-19 | First suite | 37 tests, all passing on live G4 | Incl. creation flows (relationships, sites, DCN translation) |
| 2026-08-20 | README count | 101 cases (75 functional + 26 discovery) | Search coverage sweep, user rules (maximized, cache cleared) |
| 2026-08-24 | Full run, 75 functional | 58 pass / 17 fail | On 08/25 **8 of the 17 were harness bugs**; app vindicated (see §12). Genuine: MFE blank/stall, perf |
| 2026-08-25 | Create Ticket matrix | 32 tests green | Dev answers on relationships; Location IDs + APID pairs supplied |
| 2026-08-26 | Inventory | 106 functional across 13 MFEs; P1 suite 96 | Kiran raised MAVCDUI-510..519 |
| 2026-08-27 | Defect validation + full regression baseline | 10/10 defects valid; 116 run / 91 pass / 25 fail; in-scope 108 / 90 / 18 = 83% | 11 known-defect guards red by design, 5 MFE stalls, 2 other (`Country_Mismatch_Is_Flagged`, `Every_Key_Ticket_Filter_Finds_The_Known_Ticket` - pinned ticket dropped off page one). Account Search 4/4 failed (blank). Devin skill + report scripts created |
| 2026-08-28 | G1 run (`G1_Report/`) | 127 total / 72 pass / 55 fail | Most failures are G4-only identifiers absent in G1 (APID/DCN/UCID pairs) plus blank MFEs (sponsoring-distributor, account-search, my-account) - fill `launchSettings.G1.json` |
| 2026-09-01 | Guard-flip sweep | 512, 513 fixed; 510/511/514-519 still reproduce | Traits removed for 512/513 |
| 2026-09-04 | Per-MFE runs (headless) | Homepage 12/12; Ticket 7/10; Create Ticket 31/38; Party Search 27/30; ODW 17/38 (merged trx); Account Search 0/3 | Consent-modal mass-failure trap found and fixed (24/25 timeouts → 17s/8s passes); service-account SSO investigation ended blocked on Okta |
| 2026-09-06 | Ticket MFE re-run | 8/10 | Failed: `Grid_Shows_The_MDG_Requested_Columns`, `Every_Key_Ticket_Filter_Finds_The_Known_Ticket` (repro still failing, Shouldly "failures"); `Navigate_To_CDUI_Landing_Page` hit the session-refresh flake. Under investigation |

Report conventions: `TEST_REPORT.md` at repo root (full-run narrative), `TEAMS_MESSAGE.md` (dev-facing
summary), `DEFECT_VALIDATION_510-519.md`, `DEMO_NOTES.md/.html` (manager demo), `AUTOMATION_OVERVIEW.html`
(capability/coverage one-pager), `CDUI_UIAutomation/TestArtifacts/CDUI_Regression_Report.html` (generated).

---

## 12. Hard-won lessons and gotchas (do not relearn these)

**Verification discipline**
- **Three reported "defects" were harness bugs (08/25):** ticket search "broken" (rows counted via
  `button[id^='ticket-table-actionmenu-']`, absent on older Cancelled tickets → read Ticket IDs from the
  `a` links containing "TKT"); description "ignores 300-char limit" (`FillAsync` bypasses React guards;
  real typing with `PressSequentiallyAsync` shows "320/300" and disables Submit); "Customer Country not
  enforced" (the app auto-populates it from the Party ID on blur). **Rule: real typing for validation
  tests, and cross-check every suspected app defect by hand or screenshot before telling anyone.**
- **Verify a "not reproducible" verdict as carefully as a "confirmed" one.** Three false NOT-REPRODUCED
  verdicts came from: a leaf-element-only DOM scan (missed banners containing an icon span) and a loose
  `not found` substring match on the whole page (the ODW grid's persistent "Content not found" empty state
  kept matching after the real banner had gone). Assert the exact wording ("Party not found",
  "Input record not found"). For 517, timing is the test: click Reset the instant the banner appears.
- Reading the reporter's own screenshots corrected 510 and revealed the exact columns/values for 514-516.

**Grid filters**
- Never seed a column filter from another column's value (filtering Purpose(s) by a Site ID fails for the
  wrong reason).
- Typing a new filter value ~1s after clearing the previous one is swallowed while the grid re-renders:
  clear, let the grid settle (~3s+), then type; prefer `PressSequentiallyAsync` over `FillAsync`.
- Exclude the filter-input row from row counts (`:not(:has(input,select))`).
- Keep a positive control (Site ID filter) next to the negative filter guards.

**DDS / selectors / waits**
- DDS element ids are random per re-render: never cache ids; `DdsField` resolves label → following input
  lazily.
- Escape inside a modal closes the modal, not the dropdown (`DdsDropdown` toggle-closes via the input).
- Some dropdowns (ODW BUID) are type-ahead with options hidden until filtered; some labels embed " *"
  ("Purposes *"), others mark required via CSS.
- Fuzzy Party Search results are a div-based DDS grid (not `<table>`); the header paints before rows:
  wait for `/P\d{9,}/` in body text, not for `role=row`.
- `activeTab` query param is not always honoured; overflowed tabs exist twice in the DOM with the same id
  (hidden `button[role=tab]` + `[role=menuitemcheckbox]` in More): check `aria-selected`, then a
  role-qualified click (`PartyDetailsPage.EnsureTabActiveAsync`).
- Ticket List per-column filters use `aria-label="Filter <col> entries"`; relationship tickets show Ticket
  Type "Party Relationship", Issue Type "Add Relationship"; filter Ticket Requestor by name.
- `/access-request` is a real route; Cancel is `button[data-testid=cancel-create-access-request-form]`;
  the MFE needs a long mount wait or you get a false "button missing".
- Ticket detail `/tickets/TKT####` loads slowly (wait for content); detail view does not show the requested
  relationship type; some tickets stick in Processing 30+ min.
- Cold-session render stalls on Create Ticket recover on reload (`OpenAsync` retries).

**Create Stewardship Ticket facts**
- Types: Castle Hierarchy → Castle Hierarchy Update (reduced field set: Ticket Type, Issue Type, plain
  Identifier, Description, FED - no Identifier Type, no Country); ODW Inquiry → Party Hierarchy | Sales
  Account | Partner Account; General Inquiry → General. Five identifier types: Party ID, UCID,
  DCN-BUID-Address ID, Partner Track ID, Location ID, each with its own hint (DCN hint "Use format:
  DCN-BUID-AddressID (e.g. 111-222-333)"). The identifier does a server lookup on blur: valid → Customer
  Country auto-fills; malformed → error + Submit disabled; well-formed unknown → "Input record not found".
  CAM `L…` site ids are not CDUI Location IDs.

**Party Search facts**
- Identifier vs fuzzy criteria are mutually exclusive (fuzzy inputs disable while an identifier is typed);
  Party ID / Site ID / DUNS are mutually exclusive single-select; identifier search jumps straight to the
  details view; multi-site parties are searchable by every site id; DUNS round-trips.

**Relationships (Add Relation modal)**
- `input[name=targetPartyId]` + `name=relationshipType`; invalid target → dialog closes with "Target
  party not found" toast, no ticket. Eight types for a Direct source (Partner To Sales, Sales To Partner,
  Partner To Finance, Partner To Supplier, Distributor To Reseller, Partner To EndCustomer, ARM To
  Customer, Bill To); only three for a Partner source; Distributor To Reseller is not offered for
  Partner→Partner despite its rule requiring both Partner.
- Working recipes (Partner source → Direct target): Partner To EndCustomer (P15797091770 → P15804430482,
  TKT1025651/647) and ARM To Customer (P12777692118 → P15798066869, TKT1025658 Completed; P12310765294 →
  P15804401298, TKT1025660). Partner To Sales needs Commercial + Partner source **and** matching site
  name + address. Success = ticket Status **Completed**; the relationship appears on the tab only after
  MDG approval. Full matrix in RELATIONSHIP_TYPES.md; steps in RELATIONSHIP_HOWTO.md.

**ODW ID Translation**
- Three bidirectional bridges with their own tables: DCN↔Party (DCN/BUID/Address ID ↔ Party Number/Party
  Site ID), APID↔Party (ARD bridge; grid: APID, Sales Relationship Company Name, Partner Track ID,
  Account Status…), UCID↔Party (grid: UCID Number/Name, Party Site ID, Party Contact Id, MAP). Fresh CPDn
  customers are in none of them (converted parties only, confirmed by team). Harvest identifiers by
  reverse-looking-up a converted party in each type. All three bridges have live US round-trip coverage;
  EMEA/APJ/LATAM pairs still needed.

**Build / runtime**
- Delete the referenced project's old xunit adapter after build (csproj target does it); mirror
  `DCNDtls/Resources/DBUtils` (csproj target); `DOTNET_ROLL_FORWARD=LatestMajor` for the net6.0
  reference; auto-mode tooling blocks loading service-account passwords into a shell - hand the user a
  runner script instead.

---

## 13. Team, contacts and context

| Person | Role / relevance |
|---|---|
| Shaik Muneer | E2E Testing; owns this framework; report recipient s.muneer@dell.com |
| Lavanya Kannepalli | MDG KT (2026-08); owns the regression tracker and MFE priorities |
| Kiran Kumar Avsn | Manual E2E; raised MAVCDUI-510..519 |
| Srikanth Gaddam, Manikarao Kulkarni | MDG team |
| Eduardo Dutra | Dev contact; answered the relationship auto-cancel question (PCG owns relationship tabs) |
| ARD / MDG | Sources for APID / UCID sample data |

Context: testing happens in G4 before prod. Upcoming app changes mentioned in KT: ticketing for
segment/status edits, identifying-site change capability.

---

## 14. Open items and next steps

1. **Raise the MFE blank/stall defect** (P1 Account Search unusable; not in JIRA).
2. **Pipeline SSO:** Okta/IT request for the service account (activate, assign CDUI app, password
   authenticator + MFA/device-trust exemption, CDUI role) or a dedicated CDUI test account; then a
   Dell-network GitLab runner + Docker. Do not re-run the Okta preflight until confirmed.
3. **510 decision:** Jira CANCELLED but guard red - keep red-by-design or relax.
4. **512/513 product decision:** should Cancel return to Home or to the Ticket List (now fixed in G4).
5. **Ticket MFE failures from 2026-09-06** (`Grid_Shows_The_MDG_Requested_Columns`,
   `Every_Key_Ticket_Filter_Finds_The_Known_Ticket`): investigate; the latter may be stale pinned-ticket
   data (queue > 270K).
6. **Landing-page flake:** make `HomePage.GotoAsync` re-navigate to `/` after a silent re-auth.
7. **Grow coverage:** Account Search once stable; P2 search MFEs beyond load-and-respond; Bulk Upload /
   Bulk Management / Rules Lookup / Access Review / Create Org Profile / Dashboard widgets; cross-region
   identifier pairs (EMEA, APJ, LATAM); Partner Track ID sample data.
8. **CI scheduling:** nightly P1 green gate plus a "has dev fixed it yet" job (`KnownDefect!=`).
9. Post findings as comments on 510-519 (especially the Castle Hierarchy detail on 510); ask the manual
   team to put repro steps in the Jira Description, not only in screenshots.
10. G1: fill `launchSettings.G1.json` with G1 identifiers so the G1 suite stops failing on G4-only data.

---

## 15. Where things live (quick index)

| Need | File |
|---|---|
| Setup, run, extend | `C:\repos\CDUI\README.md` |
| MFE-by-MFE test list | `MFE_TEST_INVENTORY.md`, `P1_REGRESSION.md`, `SEARCH_COVERAGE.md` |
| Capability / coverage one-pager (manager) | `AUTOMATION_OVERVIEW.html`, `DEMO_NOTES.md/.html` |
| Defects | `DEFECT_VALIDATION_510-519.md`, `DEV_QUESTIONS.md`, `TEST_REPORT.md`, `TEAMS_MESSAGE.md`, `VERIFY_STEPS.md` |
| Relationships | `RELATIONSHIP_TYPES.md`, `RELATIONSHIP_HOWTO.md` |
| Pipeline | `PIPELINE.md`, `.gitlab-ci.yml`, `CDUI_UIAutomation/Dockerfile` |
| Unattended runs | `.devin/skills/README.md` and the seven skills |
| Reports | `scripts/build_regression_report.py`, `scripts/send_regression_report.ps1`, `tools/mkreport.py`, `G1_Report/` |
| Guards | `CDUI_UIAutomation/Tests/Regression/DefectRegressionTests.cs` |
| Auth internals | `CDUI_UIAutomation/Core/AuthHelper.cs`, `Core/BaseUiTest.cs`, `Core/PlaywrightFixture.cs`, `Config/TestSettings.cs` |
| AI-assistant memory (Claude Code) | `%USERPROFILE%\.claude\projects\c--repos-CDUI\memory\cdui-ui-automation-project.md` |
