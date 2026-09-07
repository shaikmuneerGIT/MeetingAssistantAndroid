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
