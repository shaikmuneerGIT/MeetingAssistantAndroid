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
