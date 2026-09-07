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
