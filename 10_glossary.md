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
