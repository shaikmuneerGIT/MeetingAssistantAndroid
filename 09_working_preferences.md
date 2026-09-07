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
