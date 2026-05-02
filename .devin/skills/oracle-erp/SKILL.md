---
name: oracle-erp
description: Oracle ERP Cloud expert reference - GL, AP, AR, FA, CM, REST APIs, FBDI, BIP reports, ESS jobs, flexfields, security
---

# Oracle ERP Cloud Skill

## Quick Reference

### Module Overview
| Module | Key Operations |
|--------|---------------|
| GL | Journals, posting, period management, allocations |
| AP | Invoices, suppliers, payments, matching |
| AR | Customers, invoices, receipts, collections |
| FA | Assets, depreciation, transfers, retirements |
| CM | Bank accounts, statements, reconciliation |
| PO | Requisitions, purchase orders, receiving |
| EXP | Expense reports, policies, reimbursements |

### REST API Base URL
```
https://{host}/fscmRestApi/resources/11.13.18.05/{resource}
Auth: Basic (user:pass) or Bearer token (OAuth)
```

### Common REST Resources
| Resource | Module | Operations |
|----------|--------|-----------|
| `journals` | GL | CRUD, post |
| `ledgers` | GL | Read |
| `invoices` | AP | CRUD, validate, cancel |
| `suppliers` | AP | CRUD |
| `receivablesInvoices` | AR | CRUD |
| `standardReceipts` | AR | CRUD |
| `hubOrganizations` | TCA | Customer CRUD |
| `fixedAssets` | FA | CRUD, retire |
| `purchaseOrders` | PO | CRUD |
| `purchaseRequisitions` | PO | CRUD |
| `cashBankAccounts` | CM | Read |
| `erpintegrations` | Common | UCM upload, ESS jobs, BIP reports |

### Query Parameters
```
?q=field='value'                -- Filter (eq, !=, >, <, LIKE, IN, IS NULL)
?q=cond1 AND cond2              -- Multiple conditions
?orderby=field:desc             -- Sort
?limit=25&offset=0              -- Pagination
?fields=f1,f2,f3                -- Select fields
?expand=childResource           -- Include children
?onlyData=true                  -- No metadata
?totalResults=true              -- Include count
?finder=FinderName;p=value      -- Predefined finder
```

### FBDI Data Loading Process
```
Step 1: Prepare CSV (match FBDI template columns exactly)
Step 2: Zip the CSV file
Step 3: Upload ZIP to UCM
  POST /erpintegrations { "OperationName": "uploadFileToUCM",
    "DocumentContent": "<base64>", "DocumentAccount": "fin$/module$/import$",
    "FileName": "file.zip" }
Step 4: Submit ESS import job
  POST /erpintegrations { "OperationName": "submitESSJobRequest",
    "JobPackageName": "...", "JobDefName": "...", "ESSParameters": "..." }
Step 5: Poll job status
  POST /erpintegrations { "OperationName": "getESSJobStatus",
    "ReqstId": "12345" }
  Status: WAIT | RUNNING | SUCCEEDED | ERROR | WARNING
```

### UCM Document Accounts
```
GL:  fin$/journal$/import$
AP:  fin$/payables$/import$
AR:  fin$/receivables$/import$
FA:  fin$/fixedAssets$/import$
PO:  prc$/purchaseOrder$/import$
CM:  fin$/cashManagement$/import$
```

### Common ESS Jobs
| Module | Job | Package (shortened) |
|--------|-----|---------------------|
| GL Import | ImportJournals | .../generalLedger/programs/common |
| GL Post | PostJournals | .../generalLedger/programs/common |
| AP Import | APXIIMPT | .../payables/invoices/transactions |
| AP Validate | ValidateInvoices | .../payables/invoices/transactions |
| AR Import | ARXIIMPT | .../receivables/transactions |
| FA Deprec | CalculateDepreciation | .../fixedAssets/depreciation |
| FA Mass Add | FAMASS_CREATE | .../fixedAssets/massAdditions |
| PO Import | POXPOPDOI | .../procurement/purchaseOrders |
| CM BankStmt | CEBNKSTMTIMP | .../cashManagement/bankStatement |
| SLA Create | CreateAccounting | .../commonModules/shared/common |

ESS Parameters: comma-separated, `#` for null

### BIP Report Execution
```bash
# Via REST
POST /xmlpserver/services/rest/v1/reports
{ "reportAbsolutePath": "/Custom/Financials/AP/Report.xdo",
  "outputFormat": "csv",
  "parameterNameValues": { "listOfParamNameValues": { "item": [
    { "name": "p_bu", "values": { "item": ["US BU"] } }
  ] } } }

# Via erpintegrations
POST /erpintegrations
{ "OperationName": "runReport",
  "ReportPath": "/Custom/Financials/AP/Report.xdo",
  "OutputFormat": "CSV" }
```

### Business Events (for OIC subscription)
```
AP: oracle.apps.fin.ap.invoices.invoiceApproved
    oracle.apps.fin.ap.invoices.invoiceCreated
    oracle.apps.fin.ap.payments.paymentCompleted
AR: oracle.apps.fin.ar.transactions.transactionCreated
    oracle.apps.fin.ar.receipts.receiptCreated
PO: oracle.apps.prc.po.purchaseOrderApproved
    oracle.apps.prc.po.receiptCreated
GL: oracle.apps.fin.gl.journals.journalPosted
```

### GL Quick Reference
```
Chart of Accounts: Company-Dept-Account-SubAcct-Interco
Ledger: Primary (main), Secondary (alternate GAAP), Reporting Currency
Journal: Batch > Journal > Lines (DR/CR must balance)
Period Status: Never Opened > Future Entry > Open > Closed > Permanently Closed
```

### AP Quick Reference
```
Invoice Types: Standard, Credit Memo, Debit Memo, Prepayment
Matching: 2-way (PO), 3-way (PO+Receipt), 4-way (PO+Receipt+Inspection)
Flow: Create > Validate > Approve > Account > Pay
Payment Methods: Check, EFT/ACH, Wire, ISO 20022
```

### AR Quick Reference
```
Transaction Types: Invoice, Credit Memo, Debit Memo, Chargeback
Receipt Types: Manual, Automatic (AutoReceipt), Lockbox
Application: Apply to invoice, On-Account, Write-off, Refund
Aging: Current, 31-60, 61-90, 91-120, 120+ days
```

### FA Quick Reference
```
Depreciation: Straight-Line, Declining Balance, DDB, Units of Production
Books: Corporate (financial), Tax (different rates/lives)
Lifecycle: Addition > Depreciation > Transfer/Adjust > Retirement
CIP: Construction in Progress (not yet depreciating)
```

### Flexfields
```
DFF (Descriptive): Custom fields on standard forms
  - Context segment determines visible fields
  - Global segments always visible
KFF (Key): Accounting Flexfield (CoA structure), Item Flexfield
EFF (Extensible): Categorized custom attributes with grouping

REST: ?expand=InvoiceDFF to read, include DFF in body to write
Setup: Manage Descriptive Flexfields > find > add segments > Deploy
```

### Security Roles
```
Key Roles: GL Manager, AP Manager, AR Manager, Asset Accountant,
           Cash Manager, Procurement Manager, Buyer, Integration Specialist

Role Structure: Job Role > Duty Role > Privilege > Resource
Custom Roles: Security Console > Create Role > Add duties > Assign

SoD Conflicts: Create Supplier + Approve Payment,
               Create Invoice + Approve Invoice,
               Create Journal + Post Journal
```

### Multi-Org Architecture
```
Enterprise > Legal Entity > Business Unit (AP/AR/PO)
                          > Ledger (GL)
                          > Inventory Org
BU controls which transactions belong to which operating unit
```

### CURL Templates

```bash
# Create AP Invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/invoices" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{"InvoiceNumber":"INV-001","InvoiceType":"Standard",
    "InvoiceDate":"2024-01-15","Supplier":"Acme Corp",
    "InvoiceAmount":5000,"InvoiceCurrencyCode":"USD",
    "BusinessUnit":"US BU","Source":"Manual Invoice Entry",
    "invoiceLines":[{"LineNumber":1,"LineType":"Item","Amount":5000,
    "Description":"Services","DistributionCombination":"01-200-6100-0000"}]}'

# Upload to UCM
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{"OperationName":"uploadFileToUCM","DocumentContent":"<base64>",
    "DocumentAccount":"fin$/payables$/import$","FileName":"import.zip"}'

# Submit ESS Job
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{"OperationName":"submitESSJobRequest",
    "JobPackageName":"/oracle/apps/ess/financials/payables/invoices/transactions",
    "JobDefName":"APXIIMPT","ESSParameters":"US BU,Manual Invoice Entry,#"}'

# Check Job Status
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{"OperationName":"getESSJobStatus","ReqstId":"12345678"}'
```

### Common REST API Errors
| Code | Meaning | Fix |
|------|---------|-----|
| 400 | Bad Request | Check payload, required fields |
| 401 | Unauthorized | Verify credentials |
| 403 | Forbidden | Check user roles/privileges |
| 404 | Not Found | Verify resource name/ID |
| 409 | Conflict | Duplicate or version conflict |
| 500 | Server Error | Check payload format, contact support |

### Full Documentation
- See `docs/ERP-Complete-Guide.md` for comprehensive guide
- See `docs/ERP-Commands-Reference.md` for API reference
