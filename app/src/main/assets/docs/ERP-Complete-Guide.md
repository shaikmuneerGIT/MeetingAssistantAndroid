# Oracle ERP Cloud - Complete Guide

## Table of Contents

1. [Overview & Architecture](#1-overview--architecture)
2. [General Ledger (GL)](#2-general-ledger-gl)
3. [Accounts Payable (AP)](#3-accounts-payable-ap)
4. [Accounts Receivable (AR)](#4-accounts-receivable-ar)
5. [Fixed Assets (FA)](#5-fixed-assets-fa)
6. [Cash Management (CM)](#6-cash-management-cm)
7. [Procurement](#7-procurement)
8. [Expenses](#8-expenses)
9. [REST APIs](#9-rest-apis)
10. [FBDI (File-Based Data Import)](#10-fbdi-file-based-data-import)
11. [ESS Jobs (Scheduled Processes)](#11-ess-jobs-scheduled-processes)
12. [BIP Reports (BI Publisher)](#12-bip-reports-bi-publisher)
13. [Flexfields](#13-flexfields)
14. [Security](#14-security)
15. [Workflows & Approvals](#15-workflows--approvals)
16. [Subledger Accounting (SLA)](#16-subledger-accounting-sla)
17. [Tax Configuration](#17-tax-configuration)
18. [Multi-Org Architecture](#18-multi-org-architecture)
19. [OTBI Reports](#19-otbi-reports)
20. [Personalizations](#20-personalizations)
21. [Integrations](#21-integrations)
22. [Best Practices](#22-best-practices)
23. [Troubleshooting](#23-troubleshooting)

---

## 1. Overview & Architecture

### Oracle Fusion Cloud ERP

Oracle ERP Cloud is a comprehensive SaaS enterprise resource planning suite covering financials, procurement, project management, and more.

### Core Modules

| Module | Abbreviation | Purpose |
|--------|-------------|---------|
| General Ledger | GL | Chart of accounts, journals, financial reporting |
| Accounts Payable | AP | Supplier invoices, payments |
| Accounts Receivable | AR | Customer invoices, receipts, collections |
| Fixed Assets | FA | Asset lifecycle, depreciation |
| Cash Management | CM | Bank accounts, reconciliation |
| Procurement | PO | Requisitions, purchase orders, receiving |
| Expenses | EXP | Employee expense reports |
| Project Management | PPM | Project costing, billing, budgets |

### Environment Types

```
PROD  → Production (live data, end users)
TEST  → Test/UAT (user acceptance testing)
DEV   → Development (configuration, customization)

Update Cycle: Quarterly mandatory updates
Patches: Monthly optional patches
```

### Architecture Overview

```
┌──────────────────────────────────────────────────────────┐
│                    Oracle ERP Cloud                       │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌──────────────┐  │
│  │   GL    │ │   AP    │ │   AR    │ │  Procurement │  │
│  └─────────┘ └─────────┘ └─────────┘ └──────────────┘  │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌──────────────┐  │
│  │   FA    │ │   CM    │ │  EXP    │ │     PPM      │  │
│  └─────────┘ └─────────┘ └─────────┘ └──────────────┘  │
│  ┌────────────────────────────────────────────────────┐  │
│  │  Subledger Accounting (SLA) | Tax | Intercompany  │  │
│  └────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────┐  │
│  │  REST APIs | FBDI | BIP | ESS | Business Events   │  │
│  └────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────┐  │
│  │  Security (Roles/Duties) | Workflow | OTBI/SmartView│ │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
           │                    │                │
       OIC/VBCS            External          BI Tools
      Integrations          Systems         (OTBI, BIP)
```

---

## 2. General Ledger (GL)

### 2.1 Chart of Accounts (CoA)

```
Structure:
  Segment 1: Company        (e.g., 01, 02, 03)
  Segment 2: Department     (e.g., 100, 200, 300)
  Segment 3: Account        (e.g., 1000-Assets, 2000-Liabilities, 4000-Revenue)
  Segment 4: Sub-Account    (e.g., 0000)
  Segment 5: Intercompany   (e.g., 000)
  Segment 6: Future         (e.g., 000000)

Example: 01-200-4100-0000-000-000000
  Company 01, Dept 200, Revenue Account 4100
```

### 2.2 Value Sets

```
Types:
  - Independent: Standalone values (e.g., Company codes)
  - Dependent: Values depend on parent (e.g., Dept depends on Company)
  - Table-validated: Values from a DB table

Properties:
  - Format Type: Character, Number
  - Maximum Length
  - Security enabled (Yes/No)
  - Hierarchies enabled (Yes/No)
```

### 2.3 Ledgers

```
Primary Ledger:
  - Main accounting ledger
  - Single CoA, currency, calendar, accounting method
  - Example: US Primary Ledger (USD, Jan-Dec calendar)

Secondary Ledger:
  - Alternate accounting standards (IFRS vs US GAAP)
  - Different CoA or calendar

Reporting Currency:
  - Translated financials in another currency
  - Example: EUR reporting for US primary ledger
```

### 2.4 Journal Entries

**Manual Journal:**
```
Journal Batch: JAN-2024-ADJ
Journal Name: Revenue Accrual Jan 2024
Category: Adjustment
Period: Jan-24
Currency: USD

Lines:
  Line 1: DR  01-200-4100-0000  Revenue        $10,000.00
  Line 2: CR  01-200-2100-0000  Accrued Revenue $10,000.00
```

**Journal Import (via FBDI):**
```csv
STATUS,LEDGER_ID,EFFECTIVE_DATE,JOURNAL_SOURCE,JOURNAL_CATEGORY,CURRENCY_CODE,JOURNAL_BATCH_NAME,JOURNAL_NAME,DEBIT_AMOUNT,CREDIT_AMOUNT,SEGMENT1,SEGMENT2,SEGMENT3,SEGMENT4
NEW,300000001234,2024-01-31,Manual,Adjustment,USD,JAN-ADJ-001,Rev Accrual,10000,,01,200,4100,0000
NEW,300000001234,2024-01-31,Manual,Adjustment,USD,JAN-ADJ-001,Rev Accrual,,10000,01,200,2100,0000
```

### 2.5 Period Management

```
Period Statuses:
  - Never Opened: Not yet available
  - Future Entry: Can create journals but not post
  - Open: Full journal entry and posting
  - Closed: No new journals (can reopen)
  - Permanently Closed: Cannot reopen

Operations:
  - Open Period: Open/Close Periods task
  - Close Period: Run period close processes first
  - Reopen: Reopen closed period for adjustments
```

### 2.6 GL REST APIs

```bash
# List Journals
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/journals?q=Status='P'&limit=25" \
  -H "Authorization: Bearer $TOKEN"

# Create Journal
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/journals" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "LedgerId": 300000001234,
    "JournalBatchName": "API-BATCH-001",
    "JournalName": "API Journal",
    "Description": "Created via REST API",
    "Period": "Jan-24",
    "JournalCategoryName": "Adjustment",
    "CurrencyCode": "USD",
    "JournalLines": [
      {
        "DebitAmount": 5000,
        "Segment1": "01", "Segment2": "200",
        "Segment3": "4100", "Segment4": "0000"
      },
      {
        "CreditAmount": 5000,
        "Segment1": "01", "Segment2": "200",
        "Segment3": "2100", "Segment4": "0000"
      }
    ]
  }'

# Get Account Balances
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/accountBalances?\
q=LedgerId=300000001234;AccountCombination='01-200-4100-0000';Period='Jan-24'" \
  -H "Authorization: Bearer $TOKEN"
```

### 2.7 Key GL Processes

| Process | ESS Job | Description |
|---------|---------|-------------|
| Import Journals | ImportJournals | Import journals from interface |
| Post Journals | PostJournals | Post journals to balances |
| Open Period | OpenPeriod | Open a GL period |
| Close Period | ClosePeriod | Close a GL period |
| Revaluation | Revaluation | Revalue foreign currency balances |
| Translation | Translation | Translate to reporting currency |
| Create Accounting | CreateAccounting | Create SLA accounting entries |
| Allocations | GenerateAllocations | Run allocation rules |

---

## 3. Accounts Payable (AP)

### 3.1 Supplier Management

```
Supplier Record:
  ├── Supplier Header
  │   ├── Name, Tax ID, Classification
  │   ├── Supplier Type (Vendor, Contractor, etc.)
  │   └── Status (Active, Inactive, On Hold)
  ├── Supplier Sites
  │   ├── Address, Payment Method
  │   ├── Payment Terms, Tax settings
  │   └── Bank Accounts (for payments)
  ├── Contacts
  │   └── Name, Email, Phone, Role
  └── Qualifications / Certifications
```

**Supplier REST API:**
```bash
# Create Supplier
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/suppliers" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "Supplier": "Acme Corporation",
    "SupplierType": "Vendor",
    "TaxOrganizationType": "Corporation",
    "TaxpayerId": "12-3456789"
  }'

# Get Suppliers
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/suppliers?\
q=Supplier LIKE 'Acme%'&fields=Supplier,SupplierId,SupplierType&limit=25" \
  -H "Authorization: Bearer $TOKEN"
```

### 3.2 Invoice Processing

**Invoice Types:**
```
Standard     → Regular vendor invoice
Credit Memo  → Supplier credit
Debit Memo   → Charge to supplier
Prepayment   → Advance payment to supplier
Mixed        → Contains both debit and credit lines
```

**Invoice Lifecycle:**
```
Create → Validate → Approve (optional) → Account → Pay

Statuses:
  - Incomplete: Missing required data
  - Needs Revalidation: Changed after validation
  - Validated: Ready for approval/payment
  - Approved: Approved by workflow
  - On Hold: Payment held
  - Paid: Fully paid
  - Cancelled: Cancelled
```

**Invoice Matching:**
```
2-Way Match: Invoice ↔ PO (quantity, price)
3-Way Match: Invoice ↔ PO ↔ Receipt (quantity received)
4-Way Match: Invoice ↔ PO ↔ Receipt ↔ Inspection

Match Tolerances:
  - Price tolerance: ±5%
  - Quantity tolerance: ±10%
  - Amount tolerance: ±$100
```

### 3.3 AP REST APIs

```bash
# Create Invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/invoices" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "InvoiceNumber": "INV-2024-001",
    "InvoiceType": "Standard",
    "InvoiceDate": "2024-01-15",
    "Supplier": "Acme Corporation",
    "SupplierSite": "Main Site",
    "InvoiceAmount": 5000.00,
    "InvoiceCurrencyCode": "USD",
    "BusinessUnit": "US Business Unit",
    "Source": "Manual Invoice Entry",
    "invoiceLines": [
      {
        "LineNumber": 1,
        "LineType": "Item",
        "Amount": 5000.00,
        "Description": "Consulting Services Jan 2024",
        "DistributionCombination": "01-200-6100-0000"
      }
    ]
  }'

# Get Invoice
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/invoices/{invoiceId}?\
expand=invoiceLines,invoiceInstallments&fields=InvoiceNumber,InvoiceAmount,InvoiceStatus" \
  -H "Authorization: Bearer $TOKEN"

# Validate Invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/invoices/{invoiceId}/action/validateInvoice" \
  -H "Authorization: Bearer $TOKEN"
```

### 3.4 Payment Processing

```
Payment Methods:
  - Check (printed or manual)
  - EFT/ACH (electronic transfer)
  - Wire Transfer
  - ISO 20022 (XML-based payment format)

Payment Batch Process:
  1. Select Invoices for Payment (based on due date, discount)
  2. Build Payment Batch
  3. Format Payment File (EFT, ISO 20022)
  4. Approve Payment Batch
  5. Confirm Payment (mark invoices as paid)
```

### 3.5 AP FBDI Templates

| Template | Purpose | Key Columns |
|----------|---------|-------------|
| ApInvoicesInterface | Import invoices | InvoiceNum, Amount, Supplier, Date |
| ApSuppliersInterface | Import suppliers | SupplierName, TaxId, Type |
| ApPaymentsInterface | Import payments | PaymentNum, Amount, Method |
| ApInvoiceHoldsInterface | Import holds | InvoiceNum, HoldName, HoldReason |

---

## 4. Accounts Receivable (AR)

### 4.1 Customer Management

```
Customer Record (TCA - Trading Community Architecture):
  ├── Party (Organization or Person)
  │   ├── Party Name, DUNS Number
  │   └── Party Addresses
  ├── Customer Account
  │   ├── Account Number, Status
  │   ├── Credit Limit, Payment Terms
  │   └── Profile (collector, dunning)
  ├── Account Sites
  │   ├── Bill-To Site (invoicing address)
  │   └── Ship-To Site (delivery address)
  └── Contacts
      └── Name, Email, Phone, Role
```

### 4.2 AR Transactions

```
Transaction Types:
  - Invoice (INV): Standard customer invoice
  - Credit Memo (CM): Credit to customer
  - Debit Memo (DM): Additional charge to customer
  - Chargeback (CB): Dispute-related charge

Transaction Lifecycle:
  Create → Complete → Print/Send → Apply Receipt → Close
```

### 4.3 Receipt Processing

```
Receipt Types:
  - Manual: Entered by AR clerk
  - Automatic (AutoReceipts): System-generated for direct debit
  - Lockbox: Bank-provided receipt file import

Receipt Application:
  - Apply to specific invoice(s)
  - On-Account (unapplied credit)
  - Write-off (small balance write-off)
  - Refund

AutoApply:
  - Match receipts to invoices by customer, amount, reference
  - Uses matching rules for automatic application
```

### 4.4 AR REST APIs

```bash
# Create Customer (Party + Account)
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/hubOrganizations" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "OrganizationName": "Global Corp",
    "TaxpayerIdentificationNumber": "98-7654321"
  }'

# Create AR Invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/receivablesInvoices" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "TransactionNumber": "AR-2024-001",
    "TransactionDate": "2024-01-15",
    "TransactionType": "Invoice",
    "BillToCustomerName": "Global Corp",
    "BusinessUnit": "US Business Unit",
    "InvoiceCurrencyCode": "USD",
    "receivablesInvoiceLines": [
      {
        "LineNumber": 1,
        "Description": "Professional Services",
        "Quantity": 1,
        "UnitSellingPrice": 10000.00
      }
    ]
  }'

# Get Receipts
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/standardReceipts?\
q=CustomerName='Global Corp'&orderby=ReceiptDate:desc" \
  -H "Authorization: Bearer $TOKEN"
```

### 4.5 Collections & Dunning

```
Dunning Process:
  1. Define dunning letter templates
  2. Set dunning rules (days overdue thresholds)
  3. Run dunning generation process
  4. Review and send dunning letters
  5. Track customer responses

Aging Buckets:
  - Current (0-30 days)
  - 31-60 days
  - 61-90 days
  - 91-120 days
  - 120+ days
```

---

## 5. Fixed Assets (FA)

### 5.1 Asset Categories & Books

```
Asset Category:
  - Category: Computer Equipment
  - Default Life: 60 months
  - Default Depreciation Method: Straight Line
  - Default Accounts: Asset, Depreciation Expense, Accumulated Depreciation

Asset Books:
  - Corporate Book: Financial reporting depreciation
  - Tax Book: Tax depreciation (may differ from corporate)
```

### 5.2 Asset Lifecycle

```
Addition → Depreciation → Transfer/Adjust → Retirement

Statuses:
  - New: Just added, pending review
  - Active: In service, depreciating
  - Fully Depreciated: Zero net book value
  - Retired: Removed from service
  - CIP: Construction in Progress (not yet in service)
```

### 5.3 Depreciation Methods

```
Straight Line:
  Annual Depreciation = (Cost - Salvage Value) / Useful Life

Declining Balance:
  Annual Depreciation = Net Book Value × Rate

Double Declining Balance:
  Annual Depreciation = Net Book Value × (2 / Useful Life)

Units of Production:
  Depreciation = (Cost - Salvage) × (Units Used / Total Units)

Sum of Years Digits:
  Factor = Remaining Life / Sum of Years
  Annual Depreciation = (Cost - Salvage) × Factor
```

### 5.4 FA REST APIs

```bash
# Add Asset
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/fixedAssets" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "AssetNumber": "ASSET-2024-001",
    "Description": "Dell Server PowerEdge R740",
    "AssetCategory": "Computer Equipment",
    "AssetType": "Capitalized",
    "DatePlacedInService": "2024-01-15",
    "AssetCost": 15000.00,
    "AssetBook": "CORPORATE",
    "LifeInMonths": 60
  }'

# Get Asset Details
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/fixedAssets?\
q=AssetNumber='ASSET-2024-001'&expand=assetBooks,assetDistributions" \
  -H "Authorization: Bearer $TOKEN"

# Retire Asset
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/fixedAssets/{assetId}/action/retire" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"RetirementDate": "2024-12-31", "ProceedsOfSale": 2000.00}'
```

### 5.5 Key FA Processes

| Process | Description |
|---------|-------------|
| Calculate Depreciation | Run monthly depreciation calculation |
| Create Accounting | Generate SLA accounting entries |
| Mass Additions Create | Create assets from AP/PO mass additions |
| Asset Transfer | Transfer assets between departments/locations |
| Retire Assets | Remove assets from service |
| Tax Reserve Adjustments | Adjust tax book depreciation |

---

## 6. Cash Management (CM)

### 6.1 Bank Account Setup

```
Bank:
  └── Branch:
      └── Bank Account:
          ├── Account Number, IBAN
          ├── Currency
          ├── Account Type (Checking, Savings)
          ├── GL Cash Account mapping
          └── Legal Entity assignment
```

### 6.2 Bank Statements

**Formats Supported:**
```
BAI2      → US standard bank statement format
MT940     → SWIFT format (international)
CAMT.053  → ISO 20022 XML format
CSV       → Custom delimited format
```

**Import Process:**
```
1. Receive bank statement file (FTP/email/portal)
2. Load into CM (manual upload or automated via OIC)
3. Run Auto-Reconciliation
4. Review and manually reconcile unmatched items
5. Create accounting for bank charges/fees
```

### 6.3 Bank Reconciliation

```
Auto-Reconciliation Rules:
  - Match by: Amount + Reference Number
  - Match by: Amount + Date (tolerance ±2 days)
  - Match by: Amount + Check Number
  - One-to-One or One-to-Many matching

Manual Reconciliation:
  - Match bank lines to system transactions
  - Create journal entries for bank fees
  - Handle NSF (bounced) checks
```

### 6.4 CM REST APIs

```bash
# Get Bank Accounts
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/cashBankAccounts?\
q=LegalEntityName='US Legal Entity'" \
  -H "Authorization: Bearer $TOKEN"

# Import Bank Statement
# Usually done via FBDI (BankStatementImport template) + ESS job
```

---

## 7. Procurement

### 7.1 Requisitions

```
Requisition Types:
  - Purchase Requisition: Standard procurement request
  - Internal Requisition: Inter-org transfer request

Requisition Lifecycle:
  Create → Submit → Approve → Process (create PO) → Close
```

### 7.2 Purchase Orders

```
PO Types:
  - Standard PO: One-time purchase with specific items
  - Blanket PO: Agreement for recurring purchases (qty/amount limits)
  - Contract PO: Terms and conditions only (no items/prices)

PO Lifecycle:
  Create → Submit → Approve → Dispatch → Receive → Close

PO Statuses:
  - Incomplete, Requires Reapproval, In Process
  - Pre-Approved, Approved, Rejected
  - On Hold, Closed, Cancelled, Finally Closed
```

### 7.3 Receiving

```
Receipt Types:
  - Standard Receipt: Receive goods against PO
  - Direct Delivery: Receive and deliver in one step
  - Inspection Required: Receive → Inspect → Accept/Reject

3-Way Match flow:
  PO Created → Goods Received → Invoice Matched to PO + Receipt
```

### 7.4 Procurement REST APIs

```bash
# Create Purchase Order
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/purchaseOrders" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "OrderNumber": "PO-2024-001",
    "Supplier": "Acme Corporation",
    "SupplierSite": "Main Site",
    "BuyerEmail": "buyer@company.com",
    "ProcurementBU": "US Business Unit",
    "lines": [
      {
        "LineNumber": 1,
        "ItemDescription": "Laptop Dell XPS 15",
        "Quantity": 10,
        "UnitPrice": 1500.00,
        "UOM": "Each",
        "RequestedDeliveryDate": "2024-02-15"
      }
    ]
  }'

# Get Purchase Orders
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/purchaseOrders?\
q=Supplier='Acme Corporation' AND Status='APPROVED'&expand=lines" \
  -H "Authorization: Bearer $TOKEN"

# Create Requisition
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/purchaseRequisitions" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "RequisitioningBU": "US Business Unit",
    "PreparerEmail": "user@company.com",
    "Description": "IT Equipment Request",
    "lines": [
      {
        "LineNumber": 1,
        "ItemDescription": "Monitor 27 inch",
        "Quantity": 5,
        "UnitPrice": 500.00,
        "UOMCode": "Ea"
      }
    ]
  }'
```

---

## 8. Expenses

### 8.1 Expense Reports

```
Expense Report:
  ├── Header
  │   ├── Purpose, Business Justification
  │   ├── Trip dates, destination
  │   └── Default expense account
  ├── Expense Lines
  │   ├── Expense Type (airfare, hotel, meals, mileage)
  │   ├── Date, Amount, Currency
  │   ├── Receipt attached (image/PDF)
  │   └── Itemization (e.g., hotel per-night breakdown)
  └── Approval
      ├── Manager approval
      ├── Finance approval (if above threshold)
      └── Audit selection (random or rule-based)

Lifecycle:
  Draft → Submit → Approve → Audit (optional) → Pay
```

### 8.2 Expense Policies

```
Policy Rules:
  - Per-diem rates by location
  - Maximum amounts by expense type
  - Receipt required thresholds
  - Duplicate detection
  - Policy violations (warning vs blocking)
```

---

## 9. REST APIs

### 9.1 Base URL Pattern

```
https://{host}/fscmRestApi/resources/{version}/{resourceName}

Example:
https://fa-xxxx.oraclecloud.com/fscmRestApi/resources/11.13.18.05/invoices
```

### 9.2 Authentication

```bash
# Basic Authentication
curl -u "username:password" https://{host}/fscmRestApi/resources/...

# JWT Token (via OAuth)
curl -X POST "https://{idcs-host}/oauth2/v1/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=XXX&client_secret=YYY&scope=https://{host}:443urn:opc:resource:consumer::all"

curl -H "Authorization: Bearer $JWT_TOKEN" https://{host}/fscmRestApi/resources/...
```

### 9.3 Query Parameters

```bash
# Filter
?q=Status='APPROVED' AND Amount>1000

# Filter operators
?q=field eq 'value'         # equals
?q=field != 'value'         # not equals
?q=field > 100              # greater than
?q=field < 100              # less than
?q=field >= 100             # greater than or equals
?q=field <= 100             # less than or equals
?q=field LIKE '%text%'      # contains
?q=field IN ('A','B','C')   # in list
?q=field IS NULL            # is null
?q=field IS NOT NULL        # is not null

# Sort
?orderby=CreationDate:desc,InvoiceNumber:asc

# Pagination
?limit=25&offset=0          # Page 1 (items 1-25)
?limit=25&offset=25         # Page 2 (items 26-50)

# Field selection
?fields=InvoiceId,InvoiceNumber,InvoiceAmount,Status

# Expand child resources
?expand=invoiceLines,invoiceInstallments,attachments

# Include total count
?totalResults=true

# Only data (no links/metadata)
?onlyData=true

# Finders (predefined queries)
?finder=FindByStatus;Status=APPROVED

# Describe (metadata)
GET /fscmRestApi/resources/11.13.18.05/invoices/describe
```

### 9.4 CRUD Operations

```bash
# CREATE (POST)
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/{resource}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{ ... }'

# READ (GET) - List
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/{resource}?limit=25" \
  -H "Authorization: Bearer $TOKEN"

# READ (GET) - Single
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/{resource}/{id}" \
  -H "Authorization: Bearer $TOKEN"

# UPDATE (PATCH)
curl -X PATCH "https://{host}/fscmRestApi/resources/11.13.18.05/{resource}/{id}" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{ "Status": "APPROVED" }'

# DELETE
curl -X DELETE "https://{host}/fscmRestApi/resources/11.13.18.05/{resource}/{id}" \
  -H "Authorization: Bearer $TOKEN"
```

### 9.5 Common REST Resources

| Resource | Endpoint | Module |
|----------|----------|--------|
| `invoices` | Payables Invoices | AP |
| `suppliers` | Suppliers | AP |
| `purchaseOrders` | Purchase Orders | PO |
| `purchaseRequisitions` | Requisitions | PO |
| `receivablesInvoices` | AR Invoices | AR |
| `standardReceipts` | AR Receipts | AR |
| `journals` | GL Journals | GL |
| `ledgers` | GL Ledgers | GL |
| `fixedAssets` | Fixed Assets | FA |
| `cashBankAccounts` | Bank Accounts | CM |
| `hubOrganizations` | Customers/Parties | TCA |
| `expenseReports` | Expense Reports | EXP |
| `erpintegrations` | File Upload/Download | Common |
| `essJobs` | Scheduled Processes | Common |

### 9.6 Attachments via REST

```bash
# Upload attachment to invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/invoices/{invoiceId}/child/attachments" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "DatatypeCode": "FILE",
    "Title": "Invoice Scan",
    "Description": "Scanned PDF of original invoice",
    "FileName": "invoice_scan.pdf",
    "FileContents": "<base64-encoded-content>",
    "ContentType": "application/pdf",
    "CategoryName": "MISC"
  }'
```

### 9.7 Flexfields via REST

```bash
# DFF values in request
{
  "InvoiceNumber": "INV-001",
  "InvoiceAmount": 5000,
  "InvoiceDFF": [
    {
      "approvalStatus": "Pending",
      "projectCode": "PRJ-100",
      "__FLEX_Context": "Standard"
    }
  ]
}

# DFF values in query
?expand=InvoiceDFF
```

---

## 10. FBDI (File-Based Data Import)

### 10.1 What is FBDI?

FBDI is Oracle's bulk data loading mechanism using CSV files:

```
Process Flow:
  1. Download FBDI template (XLSM) from Oracle docs
  2. Populate CSV data per template format
  3. Zip the CSV file(s)
  4. Upload ZIP to UCM (WebCenter Content)
  5. Submit ESS import job
  6. Monitor job status
  7. Review error reports
```

### 10.2 Step-by-Step Process

**Step 1: Prepare CSV**
```csv
# GL Journal Import (GlInterface.csv)
STATUS,LEDGER_ID,EFFECTIVE_DATE,JOURNAL_SOURCE,JOURNAL_CATEGORY,CURRENCY_CODE,DATE_CREATED,ACTUAL_FLAG,JOURNAL_BATCH_NAME,JOURNAL_NAME,LINE_DESCRIPTION,ENTERED_DR,ENTERED_CR,SEGMENT1,SEGMENT2,SEGMENT3,SEGMENT4
NEW,300000001234,2024-01-31,Spreadsheet,Adjustment,USD,2024-01-30,A,IMPORT-001,JE-001,Revenue Entry,10000,,01,200,4100,0000
NEW,300000001234,2024-01-31,Spreadsheet,Adjustment,USD,2024-01-30,A,IMPORT-001,JE-001,Accrual Entry,,10000,01,200,2100,0000
```

**Step 2: Upload to UCM (via REST)**
```bash
# Upload file to UCM
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "OperationName": "uploadFileToUCM",
    "DocumentContent": "<base64-encoded-zip>",
    "DocumentAccount": "fin$/journal$/import$",
    "ContentType": "application/zip",
    "FileName": "GlInterface.zip"
  }'
```

**Step 3: Submit ESS Import Job**
```bash
# Submit import job
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "OperationName": "submitESSJobRequest",
    "JobPackageName": "/oracle/apps/ess/financials/generalLedger/programs/common",
    "JobDefName": "ImportJournals",
    "ESSParameters": "300000001234,Jan-24,Spreadsheet"
  }'

# Response:
# { "ReqstId": "12345678" }
```

**Step 4: Check Job Status**
```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "OperationName": "getESSJobStatus",
    "ReqstId": "12345678"
  }'

# Response:
# { "RequestStatus": "SUCCEEDED" }
# Possible: WAIT, RUNNING, SUCCEEDED, ERROR, WARNING
```

### 10.3 FBDI Templates by Module

| Module | Template File | UCM Account | ESS Job |
|--------|--------------|-------------|---------|
| GL Journals | GlInterface.csv | fin$/journal$/import$ | ImportJournals |
| AP Invoices | ApInvoicesInterface.csv | fin$/payables$/import$ | APXIIMPT |
| AP Suppliers | ApSuppliersInterface.csv | fin$/payables$/import$ | ImportSuppliers |
| AR Invoices | RaInterfaceLinesAll.csv | fin$/receivables$/import$ | ARXIIMPT |
| AR Receipts | ArReceiptsInterface.csv | fin$/receivables$/import$ | ARLOCKBOX |
| FA Additions | FaAdditionsInterface.csv | fin$/fixedAssets$/import$ | FAMASS_CREATE |
| PO Orders | PoHeadersInterfaceAll.csv | prc$/purchaseOrder$/import$ | POXPOPDOI |
| PO Receipts | RcvHeadersInterface.csv | prc$/receipt$/import$ | RCVTXNLOADER |
| CM Bank Stmt | CeBankStmtInterface.csv | fin$/cashManagement$/import$ | CEBNKSTMTIMP |

### 10.4 Common FBDI Errors

| Error | Cause | Fix |
|-------|-------|-----|
| `Invalid Account Combination` | Bad CoA segment values | Verify segment values in value sets |
| `Period not open` | Target period closed | Open the GL period |
| `Supplier not found` | Supplier name mismatch | Check exact supplier name in system |
| `Duplicate invoice number` | Invoice already exists | Use unique invoice number |
| `Invalid currency` | Currency code wrong | Use ISO currency code (USD, EUR) |
| `File format error` | Wrong CSV structure | Match template column order exactly |

---

## 11. ESS Jobs (Scheduled Processes)

### 11.1 Common ESS Jobs by Module

**General Ledger:**
```
Import Journals
  Package: /oracle/apps/ess/financials/generalLedger/programs/common
  Job: ImportJournals
  Params: LedgerId, Period, Source

Post Journals
  Package: /oracle/apps/ess/financials/generalLedger/programs/common
  Job: PostJournals
  Params: LedgerId, Period, Source, Category

Create Accounting
  Package: /oracle/apps/ess/financials/commonModules/shared/common
  Job: CreateAccounting
  Params: Subledger, LedgerId, Period, Mode
```

**Accounts Payable:**
```
Import Payables Invoices
  Package: /oracle/apps/ess/financials/payables/invoices/transactions
  Job: APXIIMPT
  Params: BusinessUnit, Source, Group

Validate Payables Invoices
  Package: /oracle/apps/ess/financials/payables/invoices/transactions
  Job: ValidateInvoices
  Params: BusinessUnit, InvoiceGroup

Select and Build Payments
  Package: /oracle/apps/ess/financials/payables/payments
  Job: IBY_FD_PAYMENT_FORMAT
  Params: PaymentBatchName
```

**Accounts Receivable:**
```
Import AutoInvoice
  Package: /oracle/apps/ess/financials/receivables/transactions
  Job: ARXIIMPT
  Params: BusinessUnit, BatchSource

Create Receipts
  Package: /oracle/apps/ess/financials/receivables/receipts
  Job: CreateReceipts
  Params: BusinessUnit, ReceiptMethod

Auto Apply Cash Receipts
  Package: /oracle/apps/ess/financials/receivables/receipts
  Job: ARXAAP
  Params: BusinessUnit
```

**Fixed Assets:**
```
Calculate Depreciation
  Package: /oracle/apps/ess/financials/fixedAssets/depreciation
  Job: CalculateDepreciation
  Params: BookTypeCode, Period

Mass Additions Create
  Package: /oracle/apps/ess/financials/fixedAssets/massAdditions
  Job: FAMASS_CREATE
  Params: BookTypeCode
```

### 11.2 Submit ESS Job via REST

```bash
# Generic ESS Job Submission
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "OperationName": "submitESSJobRequest",
    "JobPackageName": "/oracle/apps/ess/financials/generalLedger/programs/common",
    "JobDefName": "ImportJournals",
    "ESSParameters": "300000001234,Jan-24,Spreadsheet"
  }'

# Parameters are comma-separated in order
# Use # for null/empty parameters
# Example: "param1,#,param3,#,param5"
```

### 11.3 Monitor ESS Job

```bash
# Get job status
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"OperationName":"getESSJobStatus","ReqstId":"12345678"}'

# Status values:
# WAIT      - Queued
# RUNNING   - In progress
# SUCCEEDED - Completed successfully
# ERROR     - Failed
# WARNING   - Completed with warnings
# PAUSED    - Paused
# CANCELLED - Cancelled

# Download job output/log
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"OperationName":"downloadESSJobExecutionDetails","ReqstId":"12345678"}'
```

---

## 12. BIP Reports (BI Publisher)

### 12.1 Running Reports via REST

```bash
# Run BIP Report
curl -X POST "https://{host}/xmlpserver/services/rest/v1/reports" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "reportAbsolutePath": "/Custom/Financials/AP/InvoiceRegister.xdo",
    "outputFormat": "csv",
    "parameterNameValues": {
      "listOfParamNameValues": {
        "item": [
          {"name": "p_business_unit", "values": {"item": ["US Business Unit"]}},
          {"name": "p_date_from", "values": {"item": ["2024-01-01"]}},
          {"name": "p_date_to", "values": {"item": ["2024-01-31"]}}
        ]
      }
    }
  }'

# Response contains base64-encoded report output
```

### 12.2 Running Reports via ERP Integration Service

```bash
# Via erpintegrations resource
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "OperationName": "runReport",
    "ReportPath": "/Custom/Financials/AP/InvoiceRegister.xdo",
    "OutputFormat": "CSV",
    "ReportParameters": "p_business_unit=US Business Unit,p_date_from=2024-01-01"
  }'
```

### 12.3 Output Formats

```
PDF   - Portable Document Format
CSV   - Comma Separated Values
XLSX  - Microsoft Excel
XML   - Raw XML data
HTML  - Web page format
RTF   - Rich Text Format
```

### 12.4 Common BIP Report Paths

```
GL:
  /Custom/Financials/GL/JournalReport.xdo
  /Custom/Financials/GL/TrialBalanceReport.xdo
  /Custom/Financials/GL/AccountAnalysis.xdo

AP:
  /Custom/Financials/AP/InvoiceRegister.xdo
  /Custom/Financials/AP/PaymentRegister.xdo
  /Custom/Financials/AP/APAging.xdo
  /Custom/Financials/AP/InvoiceImportErrors.xdo

AR:
  /Custom/Financials/AR/TransactionRegister.xdo
  /Custom/Financials/AR/AgingReport.xdo
  /Custom/Financials/AR/ReceiptJournal.xdo

FA:
  /Custom/Financials/FA/AssetSummaryReport.xdo
  /Custom/Financials/FA/DepreciationExpenseReport.xdo

FBDI Error Reports:
  /Custom/Financials/AP/APInvoiceImportErrors.xdo
  /Custom/Financials/GL/GLJournalImportErrors.xdo
```

---

## 13. Flexfields

### 13.1 Descriptive Flexfields (DFF)

```
Purpose: Add custom fields to standard ERP forms

Structure:
  - Context Segment: Determines which additional segments display
  - Global Segments: Always visible regardless of context
  - Context-Sensitive Segments: Visible only for specific context value

Example: Invoice DFF
  Context: Invoice Category
    - "Standard": fields → Project Code, Task Code
    - "Intercompany": fields → Source Company, Target Company
  Global: Approval Status (always visible)
```

### 13.2 Key Flexfields (KFF)

```
Accounting Flexfield:
  - Defines Chart of Accounts structure
  - Segments: Company, Department, Account, Sub-Account, etc.
  - Each segment has a Value Set

Item Flexfield:
  - Defines item identification structure
  - Used in Inventory and Procurement

Location Flexfield:
  - Defines location/address structure
```

### 13.3 Extensible Flexfields (EFF)

```
Purpose: Categorized custom attributes with grouping

Structure:
  Category → Group → Attribute (field)

Use cases:
  - Asset additional information
  - Item specifications
  - Project attributes

More flexible than DFF - supports multiple contexts simultaneously
```

### 13.4 Configuring Flexfields

```
Setup Path:
  Setup and Maintenance → Search: "Manage Descriptive Flexfields"

Steps:
  1. Find the flexfield (e.g., AP Invoice Header DFF)
  2. Add context values
  3. Add segments (fields) per context
  4. Define value sets for each segment
  5. Deploy the flexfield
  6. Test in the UI

Deployment Status:
  - Edited: Changes not yet active
  - Deployed: Changes active
  - Error: Deployment failed
```

---

## 14. Security

### 14.1 Role Hierarchy

```
Job Role (assigned to users)
  └── Duty Role (collection of privileges)
      └── Privilege (atomic permission)
          └── Resource (data/function secured)
```

### 14.2 Key ERP Roles

| Role | Description |
|------|-------------|
| General Accounting Manager | Full GL access |
| General Accountant | GL journal entry, inquiry |
| Accounts Payable Manager | Full AP access |
| Accounts Payable Specialist | Invoice entry, matching |
| Accounts Receivable Manager | Full AR access |
| Accounts Receivable Specialist | Receipt entry, adjustments |
| Asset Accountant | Fixed asset management |
| Cash Manager | Bank reconciliation, cash management |
| Procurement Manager | Full procurement access |
| Buyer | Create POs, manage suppliers |
| Financial Application Administrator | Setup and configuration |
| Integration Specialist | API and integration access |

### 14.3 Custom Roles

```
Steps to create:
  1. Security Console → Roles → Create Role
  2. Name: XX_Custom_AP_Viewer
  3. Add duty roles or individual privileges
  4. Define data security policies (BU-level access)
  5. Assign to users

Data Security Policy:
  - Condition: Business Unit = 'US Business Unit'
  - Grants: View-only access to AP invoices
```

### 14.4 Segregation of Duties (SoD)

```
Key SoD conflicts:
  - Create Supplier + Approve Payments (conflict)
  - Create Invoice + Approve Invoice (conflict)
  - Create Journal + Post Journal (conflict)
  - Create PO + Receive Goods (conflict)

Resolution:
  - Different users for conflicting duties
  - Document exceptions with justification
  - Regular SoD audit reviews
```

---

## 15. Workflows & Approvals

### 15.1 BPM Workflows

```
Common approval workflows:
  - AP Invoice Approval
  - PO Approval
  - Requisition Approval
  - Journal Approval
  - Expense Report Approval

Configuration:
  Setup → Approval Rules → Define rules
  - By amount threshold
  - By department/business unit
  - By category/type
  - Sequential or parallel approval
```

### 15.2 Approval Rules

```
Example: PO Approval
  Rule 1: Amount < $5,000 → Auto-approve
  Rule 2: Amount $5,000 - $50,000 → Manager approval
  Rule 3: Amount $50,000 - $250,000 → Director approval
  Rule 4: Amount > $250,000 → VP + CFO approval (sequential)

Approval Chain:
  Requester → Manager → Director → VP → CFO
  (based on reporting hierarchy or custom list)
```

### 15.3 Notifications

```
Notification Channels:
  - Email: HTML notification with action buttons
  - Bell Notifications: In-app notifications
  - Worklist: BPM task list in ERP UI

Email Content:
  - Subject: "PO #12345 requires your approval"
  - Body: PO details, line items, total amount
  - Actions: Approve, Reject, Request Info
```

---

## 16. Subledger Accounting (SLA)

### 16.1 Overview

SLA generates accounting entries from subledger transactions (AP, AR, FA, etc.).

```
Transaction (AP Invoice) → SLA Rules → Journal Entry (GL)

Components:
  - Accounting Method: Collection of rules
  - Journal Line Rules: How to create debit/credit lines
  - Account Derivation Rules: Which GL account to use
  - Description Rules: Journal line descriptions
  - Mapping Sets: Value mappings for account derivation
```

### 16.2 Create Accounting Process

```bash
# Submit Create Accounting ESS job
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "OperationName": "submitESSJobRequest",
    "JobPackageName": "/oracle/apps/ess/financials/commonModules/shared/common",
    "JobDefName": "CreateAccounting",
    "ESSParameters": "Payables,300000001234,Jan-24,Final,Y"
  }'
```

---

## 17. Tax Configuration

### 17.1 Tax Setup

```
Tax Regime → Tax → Tax Status → Tax Rate → Tax Rules

Example:
  Regime: US Sales Tax
    Tax: State Tax
      Status: Standard
        Rate: 8.25% (California)
    Tax: County Tax
      Status: Standard
        Rate: 1.0% (LA County)

Withholding Tax:
  - Define withholding tax types
  - Assign to suppliers
  - Auto-calculate on invoice payment
```

---

## 18. Multi-Org Architecture

### 18.1 Organization Hierarchy

```
Enterprise
  └── Legal Entity (LE)
      ├── Business Unit (BU) - AP, AR, PO
      ├── Ledger (GL)
      ├── Inventory Organization
      └── HR Organization

Example:
  Global Corp (Enterprise)
    ├── US Legal Entity
    │   ├── US Business Unit (AP, AR, PO)
    │   ├── US Primary Ledger (USD)
    │   └── US Warehouse (Inventory)
    └── UK Legal Entity
        ├── UK Business Unit (AP, AR, PO)
        ├── UK Primary Ledger (GBP)
        └── UK Warehouse (Inventory)
```

### 18.2 Cross-Reference Mapping

```
Legal Entity ↔ Ledger: 1-to-1 or Many-to-1
Business Unit ↔ Legal Entity: Many-to-1
Business Unit ↔ Ledger: Many-to-1

Business Unit controls:
  - AP: Which suppliers and invoices are managed
  - AR: Which customers and transactions are managed
  - PO: Which purchase orders are created
  - GL: Which ledger entries are posted to
```

---

## 19. OTBI Reports

### 19.1 Overview

Oracle Transactional Business Intelligence (OTBI) provides real-time operational reporting.

```
Access: Navigator → Reports and Analytics → Create Analysis

Subject Areas by Module:
  GL:
    - Financials - GL Balances Real Time
    - Financials - GL Journals Real Time

  AP:
    - Financials - AP Invoices Real Time
    - Financials - AP Payments Real Time
    - Financials - AP Aging Real Time

  AR:
    - Financials - AR Transactions Real Time
    - Financials - AR Receipts Real Time
    - Financials - AR Aging Real Time

  FA:
    - Financials - Assets Real Time

  PO:
    - Procurement - Purchase Orders Real Time
    - Procurement - Requisitions Real Time
```

### 19.2 Creating an OTBI Analysis

```
Steps:
  1. Select subject area (e.g., AP Invoices Real Time)
  2. Add columns (Invoice Number, Supplier, Amount, Status)
  3. Add filters (Business Unit = 'US BU', Status = 'Validated')
  4. Configure views (table, chart, pivot table)
  5. Save and share

Prompts:
  - Date range prompts
  - Business Unit prompt
  - Status prompt
  - Supplier/Customer prompt
```

---

## 20. Personalizations

### 20.1 Page Composer

```
Customize UI pages:
  - Show/hide fields
  - Rearrange field order
  - Change field labels
  - Add tips/help text
  - Add custom links

Steps:
  1. Activate Sandbox
  2. Navigate to target page
  3. Settings → Edit Pages
  4. Make changes (drag/drop, show/hide)
  5. Test in sandbox
  6. Publish sandbox to apply
```

### 20.2 Application Composer

```
Extend ERP with custom objects and logic:
  - Custom Objects (custom tables)
  - Custom Fields on standard objects
  - Custom Links
  - Server Scripts (Groovy)
  - Object Workflows
  - Custom Global Functions

Groovy Script Example:
  // Validation script on Invoice DFF
  if (InvoiceAmount > 50000 && ApprovalStatus == null) {
    throw new oracle.jbo.ValidationException("Approval required for invoices > $50,000")
  }
```

---

## 21. Integrations

### 21.1 ERP → OIC Patterns

```
Pattern 1: Real-time API calls
  OIC receives REST request → calls ERP REST API → returns response

Pattern 2: FBDI batch import
  OIC scheduled → read source file → transform to FBDI CSV → upload to UCM → submit ESS job

Pattern 3: Business events
  ERP event (invoice approved) → triggers OIC integration → sync to external system

Pattern 4: BIP report extraction
  OIC scheduled → run BIP report → download output → send to target
```

### 21.2 ERP → VBCS Patterns

```
Pattern 1: Custom dashboard
  VBCS app → ERP REST APIs → display data in charts/tables

Pattern 2: Custom forms
  VBCS form → validate input → call ERP REST API to create record

Pattern 3: Approval UI
  VBCS app → show pending approvals → approve/reject via ERP API
```

### 21.3 Third-Party Integration

```
Inbound to ERP:
  - External system → OIC → FBDI/REST → ERP
  - Bank files → OIC → CM Bank Statement Import
  - EDI → OIC → Transform → PO/Invoice Import

Outbound from ERP:
  - ERP Business Event → OIC → External System
  - BIP Report → OIC → FTP/Email/API
  - Scheduled extract → OIC → Data warehouse
```

---

## 22. Best Practices

### 22.1 Data Loading

- Use FBDI for bulk data (>100 records)
- Use REST API for real-time/small volume (<100 records)
- Always validate data before loading (check required fields, value sets)
- Use staging tables for complex transformations
- Schedule imports during off-peak hours

### 22.2 Performance

- Limit REST API `$expand` to necessary child resources only
- Use `$fields` to return only needed columns
- Implement pagination for large data sets
- Cache reference data (LOVs, value sets)
- Use async processing for large volumes

### 22.3 Security

- Follow least-privilege principle for roles
- Audit custom roles regularly
- Use dedicated integration user accounts
- Rotate API credentials periodically
- Enable audit logging for sensitive operations

### 22.4 Testing

- Test in DEV/TEST before PROD
- Create test scripts for integration flows
- Validate FBDI templates with small data sets first
- Test approval workflows end-to-end
- Verify security roles with different user profiles

---

## 23. Troubleshooting

### 23.1 Common Errors by Module

**GL:**
| Error | Cause | Solution |
|-------|-------|----------|
| Invalid period | Period not open | Open the target period |
| Invalid account combo | Bad segment values | Verify value sets and cross-validation rules |
| Unbalanced journal | DR != CR | Ensure total debits equal total credits |

**AP:**
| Error | Cause | Solution |
|-------|-------|----------|
| Supplier not found | Name mismatch | Use exact supplier name from system |
| Invoice validation hold | Missing info or match failure | Check holds report, resolve issue |
| Duplicate invoice | Same supplier + invoice# | Use unique invoice numbers |

**AR:**
| Error | Cause | Solution |
|-------|-------|----------|
| Customer not found | Invalid account number | Verify customer exists in TCA |
| Invalid transaction type | Wrong type/source combo | Check transaction type setup |

**FBDI:**
| Error | Cause | Solution |
|-------|-------|----------|
| File format error | Wrong column order | Match template exactly |
| UCM upload failed | Auth or network error | Check credentials and connectivity |
| ESS job failed | Data validation error | Check BIP error report |

### 23.2 Diagnostic Tools

```
1. Scheduled Processes Monitor
   - Navigator → Tools → Scheduled Processes
   - View job status, output, and logs

2. BIP Error Reports
   - Run import error reports per module
   - Shows row-level error details

3. Audit Reports
   - Navigator → Tools → Audit Reports
   - Track who changed what and when

4. REST API Describe
   - GET /fscmRestApi/resources/11.13.18.05/{resource}/describe
   - Shows available fields, children, actions

5. Support: Diagnostic Tests
   - Run Oracle-provided diagnostic tests
   - Generate diagnostic reports for SR
```

### 23.3 Common REST API Errors

| HTTP Code | Meaning | Common Cause |
|-----------|---------|--------------|
| 400 | Bad Request | Invalid payload, missing required field |
| 401 | Unauthorized | Invalid/expired credentials |
| 403 | Forbidden | Insufficient role/privileges |
| 404 | Not Found | Wrong resource name or ID |
| 405 | Method Not Allowed | PATCH on non-updatable resource |
| 409 | Conflict | Duplicate record, version conflict |
| 500 | Internal Server Error | Server-side error, contact Oracle support |
| 503 | Service Unavailable | Maintenance or overload |

---

*This guide covers Oracle ERP Cloud comprehensively. For the latest updates, refer to [Oracle ERP Cloud Documentation](https://docs.oracle.com/en/cloud/saas/financials/).*
