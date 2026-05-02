# ERP Commands & API Reference

## 1. ERP REST API Endpoints

### Base URL
```
https://{host}/fscmRestApi/resources/11.13.18.05/{resource}
```

### Authentication
```bash
# Basic Auth
curl -u "username:password" https://{host}/fscmRestApi/...

# Bearer Token (OAuth)
curl -H "Authorization: Bearer $TOKEN" https://{host}/fscmRestApi/...
```

---

### 1.1 General Ledger (GL)

```bash
# Journals
GET    /journals                           # List journals
GET    /journals/{JournalHeaderId}         # Get journal
POST   /journals                           # Create journal
PATCH  /journals/{JournalHeaderId}         # Update journal
DELETE /journals/{JournalHeaderId}         # Delete journal

# Journal Batches
GET    /journalBatches                     # List batches
POST   /journalBatches                     # Create batch

# Ledgers
GET    /ledgers                            # List ledgers
GET    /ledgers/{LedgerId}                # Get ledger

# Account Combinations
GET    /accountCombinations                # List combos
GET    /accountCombinations/{CombinationId} # Get combo

# Periods
GET    /accountingPeriods                  # List periods
PATCH  /accountingPeriods/{PeriodName}     # Update period (open/close)

# Account Balances
GET    /accountBalances                    # Get balances

# --- CURL EXAMPLES ---

# Create GL Journal
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/journals" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "LedgerId": 300000001234,
    "JournalBatchName": "REST-BATCH-001",
    "JournalName": "JE-001",
    "Description": "Revenue accrual",
    "Period": "Jan-24",
    "JournalCategoryName": "Adjustment",
    "CurrencyCode": "USD",
    "JournalLines": [
      {"DebitAmount":10000, "Segment1":"01","Segment2":"200","Segment3":"4100","Segment4":"0000"},
      {"CreditAmount":10000, "Segment1":"01","Segment2":"200","Segment3":"2100","Segment4":"0000"}
    ]
  }'

# Get Journal with lines
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/journals/{id}?expand=JournalLines" \
  -u "user:pass"

# Get Account Balances
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/accountBalances?\
q=LedgerId=300000001234;AccountCombination='01-200-4100-0000';Period='Jan-24'" \
  -u "user:pass"
```

### 1.2 Accounts Payable (AP)

```bash
# Invoices
GET    /invoices                           # List invoices
GET    /invoices/{InvoiceId}              # Get invoice
POST   /invoices                           # Create invoice
PATCH  /invoices/{InvoiceId}              # Update invoice
DELETE /invoices/{InvoiceId}              # Delete invoice
POST   /invoices/{InvoiceId}/action/validateInvoice  # Validate
POST   /invoices/{InvoiceId}/action/cancelInvoice    # Cancel

# Invoice Lines (child)
GET    /invoices/{InvoiceId}/child/invoiceLines
POST   /invoices/{InvoiceId}/child/invoiceLines
PATCH  /invoices/{InvoiceId}/child/invoiceLines/{LineNumber}

# Suppliers
GET    /suppliers                          # List suppliers
GET    /suppliers/{SupplierId}            # Get supplier
POST   /suppliers                          # Create supplier
PATCH  /suppliers/{SupplierId}            # Update supplier

# Supplier Sites (child)
GET    /suppliers/{SupplierId}/child/sites
POST   /suppliers/{SupplierId}/child/sites

# Payments
GET    /payablesPayments                   # List payments

# --- CURL EXAMPLES ---

# Create AP Invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/invoices" \
  -u "user:pass" -H "Content-Type: application/json" \
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
        "Description": "Consulting Services",
        "DistributionCombination": "01-200-6100-0000"
      }
    ]
  }'

# Get Invoices with filter
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/invoices?\
q=Supplier='Acme Corporation' AND InvoiceStatus='Validated'&\
orderby=InvoiceDate:desc&limit=25&onlyData=true" \
  -u "user:pass"

# Create Supplier
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/suppliers" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "Supplier": "New Vendor LLC",
    "SupplierType": "Vendor",
    "TaxOrganizationType": "Corporation",
    "TaxpayerId": "98-7654321"
  }'
```

### 1.3 Accounts Receivable (AR)

```bash
# Receivables Invoices
GET    /receivablesInvoices                # List AR invoices
GET    /receivablesInvoices/{InvoiceId}   # Get AR invoice
POST   /receivablesInvoices                # Create AR invoice
PATCH  /receivablesInvoices/{InvoiceId}   # Update AR invoice

# Standard Receipts
GET    /standardReceipts                   # List receipts
POST   /standardReceipts                   # Create receipt

# Customers (Hub Organizations)
GET    /hubOrganizations                   # List customers
POST   /hubOrganizations                   # Create customer
PATCH  /hubOrganizations/{PartyId}        # Update customer

# --- CURL EXAMPLES ---

# Create AR Invoice
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/receivablesInvoices" \
  -u "user:pass" -H "Content-Type: application/json" \
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

# Get AR Aging
curl -X GET "https://{host}/fscmRestApi/resources/11.13.18.05/receivablesInvoices?\
q=Status='Open' AND DueDate<'2024-01-01'&fields=TransactionNumber,CustomerName,Amount,DueDate" \
  -u "user:pass"
```

### 1.4 Fixed Assets (FA)

```bash
# Fixed Assets
GET    /fixedAssets                        # List assets
GET    /fixedAssets/{AssetId}             # Get asset
POST   /fixedAssets                        # Add asset
PATCH  /fixedAssets/{AssetId}             # Update asset

# Asset Books (child)
GET    /fixedAssets/{AssetId}/child/assetBooks

# --- CURL EXAMPLES ---

# Add Asset
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/fixedAssets" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "AssetNumber": "ASSET-2024-001",
    "Description": "Dell Server R740",
    "AssetCategory": "Computer Equipment",
    "AssetType": "Capitalized",
    "DatePlacedInService": "2024-01-15",
    "AssetCost": 15000.00,
    "AssetBook": "CORPORATE",
    "LifeInMonths": 60
  }'
```

### 1.5 Procurement (PO)

```bash
# Purchase Orders
GET    /purchaseOrders                     # List POs
GET    /purchaseOrders/{POHeaderId}       # Get PO
POST   /purchaseOrders                     # Create PO
PATCH  /purchaseOrders/{POHeaderId}       # Update PO

# Purchase Requisitions
GET    /purchaseRequisitions               # List requisitions
POST   /purchaseRequisitions               # Create requisition

# PO Lines (child)
GET    /purchaseOrders/{POHeaderId}/child/lines

# --- CURL EXAMPLES ---

# Create Purchase Order
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/purchaseOrders" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OrderNumber": "PO-2024-001",
    "Supplier": "Acme Corporation",
    "SupplierSite": "Main Site",
    "ProcurementBU": "US Business Unit",
    "lines": [
      {
        "LineNumber": 1,
        "ItemDescription": "Laptop Dell XPS 15",
        "Quantity": 10,
        "UnitPrice": 1500.00,
        "UOM": "Each"
      }
    ]
  }'
```

### 1.6 Cash Management (CM)

```bash
# Bank Accounts
GET    /cashBankAccounts                   # List bank accounts
GET    /cashBankAccounts/{BankAccountId}  # Get bank account

# Bank Statements
GET    /bankStatements                     # List statements
```

---

## 2. Query Parameter Reference

```bash
# Filter (q parameter)
?q=field='value'                    # Equals (string)
?q=field=123                        # Equals (number)
?q=field!='value'                   # Not equals
?q=field>100                        # Greater than
?q=field<100                        # Less than
?q=field>=100                       # Greater or equal
?q=field<=100                       # Less or equal
?q=field LIKE '%text%'              # Contains
?q=field LIKE 'prefix%'            # Starts with
?q=field LIKE '%suffix'            # Ends with
?q=field IN ('A','B','C')          # In list
?q=field IS NULL                    # Is null
?q=field IS NOT NULL                # Is not null
?q=cond1 AND cond2                  # Logical AND
?q=cond1 OR cond2                   # Logical OR

# Sort
?orderby=field1:asc                 # Ascending
?orderby=field1:desc                # Descending
?orderby=field1:desc,field2:asc     # Multiple sort

# Pagination
?limit=25                           # Records per page (max usually 500)
?offset=0                           # Start position
?totalResults=true                  # Include total count in response

# Field Selection
?fields=Field1,Field2,Field3        # Only return these fields

# Expand Children
?expand=childResource1              # Include child records
?expand=child1,child2               # Multiple children
?expand=child1,child1.grandchild    # Nested expand

# Only Data (no links/metadata)
?onlyData=true

# Finders
?finder=FindByName;pName=Acme      # Use predefined finder

# Describe (metadata)
GET /resource/describe               # Get resource metadata
```

---

## 3. UCM / Content Server API

### 3.1 Upload File to UCM

```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OperationName": "uploadFileToUCM",
    "DocumentContent": "<BASE64_ENCODED_ZIP_CONTENT>",
    "DocumentAccount": "fin$/journal$/import$",
    "ContentType": "application/zip",
    "FileName": "GlInterface.zip"
  }'

# Response:
# { "DocumentId": "UCM12345" }
```

### 3.2 Download File from UCM

```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OperationName": "downloadFileFromUCM",
    "DocumentId": "UCM12345"
  }'

# Response contains DocumentContent (base64) and FileName
```

### 3.3 UCM Document Accounts

```
GL:   fin$/journal$/import$
AP:   fin$/payables$/import$
AR:   fin$/receivables$/import$
FA:   fin$/fixedAssets$/import$
PO:   prc$/purchaseOrder$/import$
CM:   fin$/cashManagement$/import$
```

### 3.4 SOAP Upload (GenericSoapPort)

```xml
<!-- WSDL: https://{host}/idcws/GenericSoapPort?WSDL -->
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
  xmlns:ucm="http://www.oracle.com/UCM">
  <soapenv:Body>
    <ucm:GenericRequest webKey="cs">
      <ucm:Service IdcService="CHECKIN_UNIVERSAL">
        <ucm:Document>
          <ucm:Field name="dDocTitle">GlInterface</ucm:Field>
          <ucm:Field name="dDocType">Application</ucm:Field>
          <ucm:Field name="dDocAccount">fin$/journal$/import$</ucm:Field>
          <ucm:Field name="dSecurityGroup">FAFusionImportExport</ucm:Field>
          <ucm:File name="primaryFile" href="GlInterface.zip">
            <ucm:Contents>BASE64_CONTENT_HERE</ucm:Contents>
          </ucm:File>
        </ucm:Document>
      </ucm:Service>
    </ucm:GenericRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

---

## 4. ESS Job Submission API

### 4.1 Submit Job

```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OperationName": "submitESSJobRequest",
    "JobPackageName": "/oracle/apps/ess/financials/generalLedger/programs/common",
    "JobDefName": "ImportJournals",
    "ESSParameters": "300000001234,Jan-24,Spreadsheet"
  }'

# Response: { "ReqstId": "12345678" }
```

### 4.2 Get Job Status

```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OperationName": "getESSJobStatus",
    "ReqstId": "12345678"
  }'

# Response: { "RequestStatus": "SUCCEEDED" }
# Values: WAIT | RUNNING | SUCCEEDED | ERROR | WARNING | CANCELLED
```

### 4.3 Download Job Output

```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OperationName": "downloadESSJobExecutionDetails",
    "ReqstId": "12345678"
  }'
```

### 4.4 Common ESS Jobs

| Module | Package | Job Name | Parameters |
|--------|---------|----------|------------|
| GL | /oracle/apps/ess/financials/generalLedger/programs/common | ImportJournals | LedgerId,Period,Source |
| GL | /oracle/apps/ess/financials/generalLedger/programs/common | PostJournals | LedgerId,Period,Source,Category |
| AP | /oracle/apps/ess/financials/payables/invoices/transactions | APXIIMPT | BU,Source,Group,Hold,HoldReason,GLDate,Purge |
| AP | /oracle/apps/ess/financials/payables/invoices/transactions | ValidateInvoices | BU,InvoiceGroup |
| AR | /oracle/apps/ess/financials/receivables/transactions | ARXIIMPT | BU,BatchSource |
| FA | /oracle/apps/ess/financials/fixedAssets/depreciation | CalculateDepreciation | BookTypeCode,Period |
| FA | /oracle/apps/ess/financials/fixedAssets/massAdditions | FAMASS_CREATE | BookTypeCode |
| PO | /oracle/apps/ess/procurement/purchaseOrders | POXPOPDOI | BU,BuyerId,DefaultBuyer |
| CM | /oracle/apps/ess/financials/cashManagement/bankStatement | CEBNKSTMTIMP | BankAccountId |
| SLA | /oracle/apps/ess/financials/commonModules/shared/common | CreateAccounting | Subledger,LedgerId,Period,Mode,Y |

**Parameter Notes:**
- Parameters are comma-separated in order
- Use `#` for null/empty: `"param1,#,param3,#,param5"`
- Strings don't need quotes in ESSParameters

---

## 5. BIP Report API

### 5.1 REST API

```bash
# Run report
curl -X POST "https://{host}/xmlpserver/services/rest/v1/reports" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "reportAbsolutePath": "/Custom/Financials/AP/InvoiceRegister.xdo",
    "outputFormat": "csv",
    "parameterNameValues": {
      "listOfParamNameValues": {
        "item": [
          {"name":"p_bu","values":{"item":["US Business Unit"]}},
          {"name":"p_from_date","values":{"item":["2024-01-01"]}},
          {"name":"p_to_date","values":{"item":["2024-01-31"]}}
        ]
      }
    }
  }'

# Response: { "reportBytes": "BASE64_ENCODED_OUTPUT" }
```

### 5.2 Via ERP Integrations

```bash
curl -X POST "https://{host}/fscmRestApi/resources/11.13.18.05/erpintegrations" \
  -u "user:pass" -H "Content-Type: application/json" \
  -d '{
    "OperationName": "runReport",
    "ReportPath": "/Custom/Financials/AP/InvoiceRegister.xdo",
    "OutputFormat": "CSV",
    "ReportParameters": "p_bu=US Business Unit,p_from=2024-01-01,p_to=2024-01-31"
  }'
```

### 5.3 Output Formats

```
PDF   - application/pdf
CSV   - text/csv
XLSX  - application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
XML   - application/xml
HTML  - text/html
RTF   - application/rtf
```

---

## 6. Business Events Reference

### 6.1 AP Events

```
oracle.apps.fin.ap.invoices.invoiceApproved
oracle.apps.fin.ap.invoices.invoiceCreated
oracle.apps.fin.ap.invoices.invoiceValidated
oracle.apps.fin.ap.invoices.invoiceCancelled
oracle.apps.fin.ap.payments.paymentCreated
oracle.apps.fin.ap.payments.paymentCompleted
```

### 6.2 AR Events

```
oracle.apps.fin.ar.transactions.transactionCreated
oracle.apps.fin.ar.transactions.transactionCompleted
oracle.apps.fin.ar.receipts.receiptCreated
oracle.apps.fin.ar.receipts.receiptApplied
```

### 6.3 PO Events

```
oracle.apps.prc.po.purchaseOrderApproved
oracle.apps.prc.po.purchaseOrderCreated
oracle.apps.prc.po.purchaseOrderClosed
oracle.apps.prc.po.receiptCreated
oracle.apps.prc.req.requisitionApproved
```

### 6.4 GL Events

```
oracle.apps.fin.gl.journals.journalPosted
oracle.apps.fin.gl.journals.journalApproved
oracle.apps.fin.gl.periods.periodOpened
oracle.apps.fin.gl.periods.periodClosed
```

### 6.5 Event Payload Structure

```json
{
  "id": "event-uuid",
  "source": "/oracle/apps/fin/ap/invoices",
  "type": "oracle.apps.fin.ap.invoices.invoiceApproved",
  "time": "2024-01-15T10:30:00Z",
  "data": {
    "InvoiceId": 300000012345,
    "InvoiceNumber": "INV-2024-001",
    "Supplier": "Acme Corp",
    "InvoiceAmount": 5000.00,
    "BusinessUnit": "US Business Unit",
    "ApprovedBy": "manager@company.com"
  }
}
```

---

## 7. FBDI Template Quick Reference

### CSV Format Rules

```
- UTF-8 encoding
- Comma-delimited
- Double-quote text qualifier for values containing commas
- Date format: YYYY-MM-DD or YYYY/MM/DD
- Number format: No thousands separator, period for decimal
- First row: Column headers (must match template exactly)
- Blank required columns: Leave empty (,,) not NULL
- File must be zipped before upload to UCM
```

### GL Journal Import (GlInterface.csv)

```csv
STATUS,LEDGER_ID,EFFECTIVE_DATE,JOURNAL_SOURCE,JOURNAL_CATEGORY,CURRENCY_CODE,DATE_CREATED,ACTUAL_FLAG,JOURNAL_BATCH_NAME,JOURNAL_NAME,LINE_DESCRIPTION,ENTERED_DR,ENTERED_CR,SEGMENT1,SEGMENT2,SEGMENT3,SEGMENT4,SEGMENT5
NEW,300000001234,2024-01-31,Spreadsheet,Adjustment,USD,2024-01-30,A,BATCH-001,JE-001,Debit entry,10000,,01,200,4100,0000,000
NEW,300000001234,2024-01-31,Spreadsheet,Adjustment,USD,2024-01-30,A,BATCH-001,JE-001,Credit entry,,10000,01,200,2100,0000,000
```

### AP Invoice Import (ApInvoicesInterface.csv)

```csv
INVOICE_NUM,INVOICE_TYPE_LOOKUP_CODE,INVOICE_DATE,VENDOR_NAME,VENDOR_SITE_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,SOURCE,ORG_ID,DESCRIPTION,GL_DATE
INV-001,STANDARD,2024-01-15,Acme Corp,Main Site,5000.00,USD,Manual Invoice Entry,300000001,Consulting,2024-01-31
```

### AR AutoInvoice Import (RaInterfaceLinesAll.csv)

```csv
INTERFACE_LINE_ID,BATCH_SOURCE_NAME,LINE_TYPE,DESCRIPTION,CURRENCY_CODE,AMOUNT,ORIG_SYSTEM_BILL_CUSTOMER_REF,ORIG_SYSTEM_BILL_ADDRESS_REF,CONVERSION_TYPE,CONVERSION_RATE,TERM_NAME,INTERFACE_LINE_CONTEXT
1,Manual,LINE,Services,USD,10000,CUST-001,ADDR-001,,,Net 30,
```

---

## 8. Diagnostic SQL Queries

```sql
-- Check FBDI interface table (GL)
SELECT * FROM gl_interface
WHERE status = 'NEW' AND ledger_id = :ledger_id
ORDER BY date_created DESC;

-- Check AP invoice import errors
SELECT * FROM ap_invoices_interface
WHERE status != 'PROCESSED'
ORDER BY creation_date DESC;

-- Check ESS job history
SELECT request_id, definition, submission_date, completion_date, state
FROM ess_request_history
WHERE definition LIKE '%ImportJournals%'
ORDER BY submission_date DESC;

-- Get account combination
SELECT code_combination_id, segment1, segment2, segment3, segment4,
       enabled_flag, start_date_active, end_date_active
FROM gl_code_combinations
WHERE segment1 = '01' AND segment3 = '4100';

-- Check open AP invoices for supplier
SELECT invoice_num, invoice_date, invoice_amount, amount_paid,
       invoice_amount - amount_paid as balance, approval_status
FROM ap_invoices_all
WHERE vendor_id = :vendor_id AND cancelled_flag = 'N'
  AND invoice_amount != amount_paid;

-- AR aging summary
SELECT customer_name, SUM(amount_due_remaining) as outstanding,
  SUM(CASE WHEN due_date >= SYSDATE THEN amount_due_remaining ELSE 0 END) as current_amt,
  SUM(CASE WHEN due_date < SYSDATE AND due_date >= SYSDATE-30 THEN amount_due_remaining ELSE 0 END) as days_30,
  SUM(CASE WHEN due_date < SYSDATE-30 AND due_date >= SYSDATE-60 THEN amount_due_remaining ELSE 0 END) as days_60,
  SUM(CASE WHEN due_date < SYSDATE-60 THEN amount_due_remaining ELSE 0 END) as days_90_plus
FROM ar_payment_schedules_all aps
JOIN hz_cust_accounts hca ON aps.customer_id = hca.cust_account_id
JOIN hz_parties hp ON hca.party_id = hp.party_id
WHERE aps.status = 'OP'
GROUP BY customer_name
ORDER BY outstanding DESC;
```
