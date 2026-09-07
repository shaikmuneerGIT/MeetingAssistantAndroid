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
