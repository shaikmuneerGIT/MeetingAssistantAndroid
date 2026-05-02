# Oracle Integration Cloud (OIC) - Complete Guide

## Table of Contents

1. [Overview & Architecture](#1-overview--architecture)
2. [Connections & Adapters](#2-connections--adapters)
3. [Integration Patterns](#3-integration-patterns)
4. [Orchestration Actions](#4-orchestration-actions)
5. [Data Mapping (XSLT)](#5-data-mapping-xslt)
6. [Lookups](#6-lookups)
7. [Libraries](#7-libraries)
8. [Stage File Operations](#8-stage-file-operations)
9. [Error Handling & Fault Policies](#9-error-handling--fault-policies)
10. [Monitoring & Tracking](#10-monitoring--tracking)
11. [Security](#11-security)
12. [Scheduled Orchestrations](#12-scheduled-orchestrations)
13. [PL/SQL Integration](#13-plsql-integration)
14. [FBDI Integration](#14-fbdi-integration)
15. [BIP Reports](#15-bip-reports)
16. [OIC REST API](#16-oic-rest-api)
17. [Connectivity Agent](#17-connectivity-agent)
18. [Process Automation](#18-process-automation)
19. [Best Practices](#19-best-practices)
20. [Troubleshooting](#20-troubleshooting)

---

## 1. Overview & Architecture

### What is OIC?

Oracle Integration Cloud (OIC) is a fully managed integration platform (iPaaS) that connects SaaS and on-premises applications. It provides:

- **Application Integration** - Pre-built adapters for 70+ applications
- **Process Automation** - Structured and dynamic workflows
- **Visual Builder** - Low-code app development
- **File Server** - Embedded SFTP for file-based integrations

### OIC Generations

| Feature | OIC Gen2 | OIC Gen3 |
|---------|----------|----------|
| Infrastructure | Oracle Cloud Infrastructure (OCI) | OCI with improved isolation |
| Pricing | Message packs | Message packs + OCPU-based |
| Projects | Not available | Project-based organization |
| Monitoring | Standard dashboard | Enhanced observability |
| Deployment | Direct activation | Project-based deployment |

### Architecture Components

```
┌─────────────────────────────────────────────────────┐
│                  OIC Instance                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │  Design   │  │  Runtime  │  │  Monitoring &    │  │
│  │  Time     │  │  Engine   │  │  Management      │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │ Adapters  │  │ Mappings │  │  Error Hospital  │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │ Lookups   │  │Libraries │  │  Agent Framework │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
└─────────────────────────────────────────────────────┘
         │                              │
    Cloud Apps                   On-Premises Apps
   (ERP, HCM,                  (DB, File, SAP)
    Salesforce)                via Connectivity Agent
```

### Pricing Tiers

- **Standard**: App-driven and scheduled integrations, basic adapters
- **Enterprise**: All Standard features + File Server, Process Automation, advanced adapters
- **Premium**: All Enterprise features + advanced analytics

---

## 2. Connections & Adapters

### 2.1 REST Adapter

The REST adapter connects to any RESTful API.

**Configuration:**
```
Connection Properties:
  - Connection Type: REST API Base URL
  - Connection URL: https://api.example.com/v1
  - Security Policy: OAuth 2.0 / Basic Auth / API Key / OCI Signature
  - TLS Version: TLSv1.2

Custom Headers (optional):
  - X-Custom-Header: value
  - Accept: application/json
```

**Trigger Configuration:**
- Endpoint URI: /orders/{orderId}
- HTTP Method: GET, POST, PUT, PATCH, DELETE
- Request/Response payload: JSON or XML
- Query parameters
- Custom headers at endpoint level
- Pagination: Offset-based, cursor-based, or page-number

**Invoke Configuration:**
- Configure endpoint URI with path parameters
- Define request/response JSON schema (sample payload or JSON Schema)
- Configure pagination for large result sets
- Add custom headers per invocation

### 2.2 SOAP Adapter

**Configuration:**
```
Connection Properties:
  - WSDL URL: https://service.example.com/wsdl
  - SAML/WS-Security policies
  - MTOM for binary attachments
  - Security: Username Token, SAML, OAuth
```

**Usage:**
- Import WSDL at connection level
- Select port type and operation at invoke level
- Map SOAP header elements separately
- Handle SOAP faults in error handler

### 2.3 Oracle ERP Cloud Adapter

The most critical adapter for ERP integrations.

**Connection Properties:**
```
  - ERP Cloud Host: https://fa-xxxx.oraclecloud.com
  - Security: Username/Password
  - Interface Catalog URL: auto-populated
```

**Capabilities:**

| Feature | Description |
|---------|-------------|
| Business Events | Subscribe to ERP business events (PO approved, Invoice created, etc.) |
| Business Objects (CRUD) | REST-based CRUD on ERP objects |
| FBDI Import | Upload CSV, submit import job, get callback |
| BIP Reports | Run BI Publisher reports and retrieve output |
| Bulk Import | Large-volume data loading |
| Callback | Receive async import completion notifications |

**Business Events Trigger Example:**
```
Event: oracle.apps.fin.ap.invoices.invoiceApproved
Filter: <xpathExpr xmlns:ns0="..." >ns0:BusinessUnit = 'US BU'</xpathExpr>
```

**FBDI Import Pattern:**
```
1. Stage File: Write CSV → zip
2. Invoke ERP Adapter: Upload to UCM (WebCenter Content)
3. Invoke ERP Adapter: Submit ESS Import Job
4. Invoke ERP Adapter: Get ESS Job Status (polling or callback)
5. Handle success/failure
```

### 2.4 Oracle HCM Cloud Adapter

```
Connection Properties:
  - HCM Host: https://hcm-xxxx.oraclecloud.com
  - Security: Username/Password
```

**Capabilities:**
- HCM Data Loader (HDL) file upload
- HCM Extract (HCM Extracts / BIP reports)
- Business Events subscription
- CRUD on HCM business objects
- Atom feed subscription

### 2.5 FTP Adapter

```
Connection Properties:
  - Host: ftp.example.com
  - Port: 22 (SFTP) / 21 (FTP)
  - Security: SFTP with key / FTP with TLS
  - Private Key: upload .pem file
```

**Operations:**
- Read File (single file, pattern-based)
- Write File
- List Files (directory listing)
- Download/Upload with transfer mode (binary/ASCII)

**Polling (Scheduled):**
```
  - Directory: /inbound/orders
  - File Pattern: *.csv
  - Max Files: 10
  - Post-read action: Move to /processed or Delete
```

### 2.6 Database Adapter

```
Connection Properties:
  - Host: db-host.example.com
  - Port: 1521
  - SID/Service Name: ORCL
  - Security: Username/Password
  - Requires: Connectivity Agent for on-premises databases
```

**Operations:**
- Run a SQL Statement (SELECT, INSERT, UPDATE, DELETE)
- Call a Stored Procedure / Function
- Polling (query-based polling with logical delete or physical delete)
- Bulk operations (batch INSERT)

**Stored Procedure Example:**
```sql
-- PL/SQL Package called via DB Adapter
CREATE OR REPLACE PACKAGE order_pkg AS
  PROCEDURE process_order(
    p_order_id   IN  NUMBER,
    p_status     OUT VARCHAR2,
    p_message    OUT VARCHAR2
  );
END order_pkg;
```

### 2.7 File Adapter (Stage File)

The File Adapter works with OIC's internal staging area (not external FTP).

**Operations:**
- Read Entire File
- Read File in Segments (for large files)
- Write File
- Zip File / Unzip File
- List Files in a directory

### 2.8 Other Key Adapters

| Adapter | Use Case |
|---------|----------|
| **Oracle ATP/ADW** | Connect to Autonomous Database |
| **Oracle SCM Cloud** | Supply chain events, POs, shipments |
| **Kafka** | Event streaming with Apache Kafka |
| **JMS** | Java Message Service queues/topics |
| **SAP** | SAP ECC/S4HANA via IDoc, BAPI, RFC |
| **Salesforce** | SFDC CRUD, events, bulk API |
| **ServiceNow** | Incidents, requests, CMDB |
| **MS SQL Server** | SQL Server via connectivity agent |
| **LDAP** | Directory services integration |
| **Oracle Commerce** | Commerce Cloud catalog, orders |

### 2.9 Connectivity Agent

Required for on-premises systems (databases, file servers, SAP, etc.).

**Installation:**
```bash
# Download agent installer from OIC console
# Unzip and configure
cd agent_install_dir
# Edit InstallerProfile.cfg
java -jar connectivityagent.jar

# Key configuration (InstallerProfile.cfg):
oic_URL=https://your-oic-instance.integration.ocp.oraclecloud.com
agent_GROUP_ID=your_agent_group
oic_USER=your_username
oic_PASSWORD=your_password
```

**Agent Group Architecture:**
```
OIC Cloud  ──────── Agent Group ──────── On-Premises
                    ├── Agent 1 (primary)
                    └── Agent 2 (HA standby)
```

---

## 3. Integration Patterns

### 3.1 App Driven Orchestration

Triggered by an external HTTP request (REST/SOAP).

```
Trigger → Actions → Response

Use cases:
  - Synchronous API calls
  - Real-time data lookup
  - Webhook handlers
  - Request/reply operations
```

**REST Trigger Configuration:**
```
Endpoint URI: /api/v1/orders
Method: POST
Request Body: JSON
Response Body: JSON
HTTP Status: 200, 201, 400, 500
```

### 3.2 Scheduled Orchestration

Runs on a defined schedule (cron-based).

```
Schedule → Actions → Complete

Use cases:
  - Batch data sync
  - Periodic file processing
  - Report generation
  - Data cleanup jobs
```

**Schedule Options:**
```
Frequency:
  - Every X minutes/hours
  - Daily at specific time
  - Weekly on specific days
  - Monthly on specific date
  - Custom cron: 0 30 2 * * ?  (2:30 AM daily)

Parameters:
  - Define named parameters (string type)
  - Pass values at runtime or via schedule
  - Access in integration: $parameters.paramName
```

### 3.3 Event-Based Integration

Triggered by business events from SaaS applications.

```
Business Event → Trigger → Actions

Use cases:
  - ERP invoice approved → sync to external system
  - HCM employee hired → provision accounts
  - SCM PO received → update warehouse
```

### 3.4 File-Based Integration

Process files from FTP/SFTP or staged files.

```
Scheduled/FTP Poll → Read File → Transform → Load

Use cases:
  - CSV file import to ERP (FBDI)
  - EDI file processing
  - Bank statement import
  - Flat file transformations
```

### 3.5 Publish/Subscribe Pattern

Decouple producers from consumers using OIC's messaging.

```
Publisher Integration → OIC Messaging → Subscriber Integration(s)

Use cases:
  - One event triggers multiple downstream systems
  - Loose coupling between integrations
  - Fan-out processing
```

### 3.6 Fire-and-Forget Pattern

Invoke an endpoint without waiting for response.

```
Trigger → Async Invoke (no wait) → Return immediately

Use cases:
  - Audit logging
  - Non-critical notifications
  - Performance optimization
```

---

## 4. Orchestration Actions

### 4.1 Invoke

Call an external service (REST, SOAP, ERP, etc.).

```
Properties:
  - Connection: Select configured connection
  - Operation: GET, POST, PUT, PATCH, DELETE
  - Endpoint URI: /resource/{id}
  - Request/Response mapping via data mapper
```

### 4.2 Map (Data Mapper)

Transform data between source and target schemas using XSLT.

```
Source Schema → XSLT Mapping → Target Schema

Features:
  - Drag-and-drop field mapping
  - XPath expressions for transformations
  - Lookup integration
  - Conditional mapping (if/then)
  - For-each for repeating elements
```

### 4.3 Assign

Set variable values.

```xml
<!-- Assign simple value -->
<variable name="status" value="'PROCESSED'" />

<!-- Assign from expression -->
<variable name="fullName" value="concat($firstName, ' ', $lastName)" />

<!-- Assign from response -->
<variable name="orderId" value="$InvokeResponse/id" />
```

### 4.4 Switch (Conditional)

Branch logic based on conditions.

```
Switch
  ├── Branch 1: $status = 'NEW'        → Process new order
  ├── Branch 2: $status = 'UPDATED'    → Update existing
  └── Otherwise                         → Log and skip
```

**Expression Examples:**
```xpath
$variable = 'value'
$amount > 1000
contains($name, 'Oracle')
$count >= 1 and $count <= 100
string-length($field) > 0
```

### 4.5 For-Each

Loop over repeating elements.

```
For-Each: $response/orders/order
  Current Element: $currentOrder
  Actions:
    - Map: transform $currentOrder
    - Invoke: POST to target system
    - Assign: increment counter
```

### 4.6 While

Loop with a condition.

```
While: $hasMorePages = true()
  Actions:
    - Invoke: GET next page
    - Assign: update $hasMorePages
    - Stage File: append results
```

### 4.7 Scope

Group actions with error handling.

```
Scope: "ProcessOrder"
  ├── Normal Flow:
  │   ├── Invoke: Get Order
  │   ├── Map: Transform
  │   └── Invoke: Create in Target
  └── Fault Handler:
      ├── Logger: Log error details
      └── Notification: Send alert email
```

### 4.8 Stage File

Read/write files in OIC's staging area. See [Section 8](#8-stage-file-operations).

### 4.9 Notification

Send email notifications.

```
Properties:
  - To: email@example.com
  - Subject: "Integration Alert: ${integrationName}"
  - Body: HTML or plain text with variable substitution
  - From: noreply@oic-instance.com
```

### 4.10 Logger

Log messages to the activity stream.

```
Logger Levels:
  - Information
  - Warning
  - Error

Message: "Processing order ${orderId}, amount: ${amount}"
```

### 4.11 JavaScript

Execute inline JavaScript for complex logic.

```javascript
// Available in JavaScript action
function main(input) {
  var data = JSON.parse(input);

  // String manipulation
  var formatted = data.firstName.toUpperCase() + ' ' + data.lastName.toUpperCase();

  // Date formatting
  var now = new Date();
  var dateStr = now.toISOString().split('T')[0]; // YYYY-MM-DD

  // Number formatting
  var amount = parseFloat(data.amount).toFixed(2);

  // Array processing
  var items = data.lineItems.map(function(item) {
    return {
      sku: item.sku,
      total: item.quantity * item.unitPrice
    };
  });

  // Return JSON string
  return JSON.stringify({
    name: formatted,
    date: dateStr,
    amount: amount,
    items: items
  });
}
```

### 4.12 Other Actions

| Action | Description |
|--------|-------------|
| **Wait** | Pause execution for specified duration |
| **Stop** | Terminate integration execution |
| **Return** | Return response (app-driven) |
| **Callback** | Send async callback response |
| **Fault** | Throw a fault/error |
| **Re-throw** | Re-throw caught fault in fault handler |

---

## 5. Data Mapping (XSLT)

### 5.1 Mapper Overview

OIC uses XSLT 1.0 (with XPath 1.0 extensions) for data transformation.

### 5.2 String Functions

```xpath
<!-- Concatenation -->
concat($firstName, ' ', $lastName)

<!-- Substring -->
substring($date, 1, 10)          <!-- First 10 chars -->
substring-after($email, '@')     <!-- After @ sign -->
substring-before($name, ',')     <!-- Before comma -->

<!-- String length -->
string-length($field)

<!-- Contains -->
contains($description, 'urgent')

<!-- Starts-with -->
starts-with($code, 'PO-')

<!-- Translate (character replacement) -->
translate($phone, '()-. ', '')   <!-- Remove formatting -->
translate($text, 'abc', 'ABC')   <!-- To uppercase specific chars -->

<!-- Normalize space -->
normalize-space($input)          <!-- Trim and collapse whitespace -->

<!-- Upper/Lower case (OIC extensions) -->
upper-case($text)
lower-case($text)
```

### 5.3 Date Functions

```xpath
<!-- Current date/time -->
current-dateTime()
current-date()

<!-- Format date -->
format-dateTime($dateField, '[Y0001]-[M01]-[D01]T[H01]:[m01]:[s01]')

<!-- Date arithmetic -->
xsd:dateTime($date) + xsd:dayTimeDuration('P1D')     <!-- Add 1 day -->
xsd:dateTime($date) + xsd:dayTimeDuration('PT2H')    <!-- Add 2 hours -->
xsd:dateTime($date) - xsd:dayTimeDuration('P30D')    <!-- Subtract 30 days -->

<!-- Extract date components -->
year-from-dateTime($date)
month-from-dateTime($date)
day-from-dateTime($date)
hours-from-dateTime($date)
```

### 5.4 Number / Math Functions

```xpath
<!-- Rounding -->
round($amount)
floor($amount)
ceiling($amount)

<!-- Sum -->
sum($items/item/amount)

<!-- Number formatting -->
format-number($amount, '#,##0.00')

<!-- Type conversion -->
number($stringValue)
string($numberValue)
```

### 5.5 Conditional Mapping

```xpath
<!-- If-then-else using XPath -->
<xsl:choose>
  <xsl:when test="$status = 'ACTIVE'">
    <xsl:value-of select="'Y'" />
  </xsl:when>
  <xsl:otherwise>
    <xsl:value-of select="'N'" />
  </xsl:otherwise>
</xsl:choose>

<!-- Inline if (OIC expression) -->
if ($amount > 0) then 'Credit' else 'Debit'
```

### 5.6 Node-Set Functions

```xpath
count($items/item)              <!-- Count elements -->
position()                      <!-- Current position in loop -->
last()                          <!-- Last position -->
not(boolean_expr)               <!-- Negate boolean -->
true() / false()                <!-- Boolean constants -->
```

### 5.7 Lookup in Mapping

```xpath
<!-- Use a lookup in mapping -->
lookupValue('LookupName', 'SourceColumn', $sourceValue, 'TargetColumn', 'DefaultValue')

<!-- Example: Map country code to country name -->
lookupValue('CountryCodeLookup', 'Code', $countryCode, 'Name', 'Unknown')
```

---

## 6. Lookups

### Creating Lookups

```
Navigate: Design → Lookups → Create

Structure:
  Lookup Name: COUNTRY_CODE_MAP
  Columns: SourceSystem | TargetSystem

  Rows:
    US  | United States
    UK  | United Kingdom
    IN  | India
    AU  | Australia
```

### Import/Export

- Export as CSV for bulk editing
- Import CSV to update lookup values
- Useful for environment migration

### Using Lookups in Mappings

```xpath
lookupValue('COUNTRY_CODE_MAP', 'SourceSystem', $code, 'TargetSystem', 'Unknown')
```

### Best Practices
- Use lookups for code/value translations between systems
- Keep lookup names descriptive with consistent naming: `MODULE_PURPOSE_MAP`
- Export and version control lookup CSVs
- Use default values to handle unmapped codes

---

## 7. Libraries

### Creating a Library

```
Navigate: Design → Libraries → Create

Library Name: CommonUtilities
Version: 1.0

Functions:
  - formatDate(dateStr): formats dates consistently
  - validateEmail(email): returns true/false
  - generateId(prefix): creates unique IDs
```

### JavaScript Library Function Example

```javascript
/**
 * Format an Oracle date string to ISO format
 */
function formatOracleDate(oraDate) {
  if (!oraDate) return '';
  // Oracle format: 2024-01-15T00:00:00+00:00
  return oraDate.split('T')[0];
}

/**
 * Calculate tax amount
 */
function calculateTax(amount, taxRate) {
  return (parseFloat(amount) * parseFloat(taxRate) / 100).toFixed(2);
}

/**
 * Mask sensitive data (e.g., SSN)
 */
function maskSSN(ssn) {
  if (!ssn || ssn.length < 4) return '***';
  return '***-**-' + ssn.slice(-4);
}
```

### Registering and Using

1. Create library with JS functions
2. Register (publish) the library
3. In integration, add library dependency
4. Call functions in JavaScript actions or mappings

---

## 8. Stage File Operations

### 8.1 Read Entire File

```
Operation: Read Entire File
File Name: orders.csv
Directory: /input

Schema: Define CSV/JSON/XML structure
  - CSV: specify delimiter, qualifier, header row
  - JSON: provide sample JSON
  - XML: provide XSD/sample XML
```

### 8.2 Read File in Segments

For large files, read in configurable chunk sizes.

```
Operation: Read File in Segments
Segment Size: 200 records
File Name: large_file.csv
Directory: /input

Processing:
  For each segment → transform → invoke target
```

### 8.3 Write File

```
Operation: Write File
File Name: output_${timestamp}.csv
Directory: /output
Append: Yes/No

Content: mapped from integration data
```

### 8.4 Zip / Unzip

```
Zip:
  Input: /staging/file1.csv, /staging/file2.csv
  Output: /staging/archive.zip

Unzip:
  Input: /staging/archive.zip
  Output Directory: /staging/unzipped/
```

### 8.5 List Files

```
Operation: List Files
Directory: /input
File Pattern: *.csv

Returns: list of file names, sizes, dates
Use in For-Each to process each file
```

---

## 9. Error Handling & Fault Policies

### 9.1 Scope-Based Error Handling

```
Scope: "ProcessOrder"
  ├── Main Flow:
  │   ├── Invoke REST API
  │   └── Map Response
  └── Fault Handler:
      ├── Assign: $errorMessage = fault-message
      ├── Logger: Log error
      ├── Notification: Alert admin
      └── Re-throw (optional)
```

### 9.2 Global Fault Handler

Catches any unhandled errors in the integration.

```
Global Fault:
  ├── Logger: Log integration failure
  ├── Assign: Set error response
  └── Return: HTTP 500 with error details
```

### 9.3 Fault Object Properties

```xpath
$fault/faultCode          <!-- Error code -->
$fault/faultString        <!-- Error message -->
$fault/faultDetail        <!-- Detailed error info -->
$fault/faultActor         <!-- Component that caused error -->
```

### 9.4 Error Hospital

- Failed integration instances appear in Error Hospital
- Actions: Resubmit, Discard, Download payload
- Filter by: integration name, date range, error type
- Bulk resubmit for batch recovery

### 9.5 Retry Patterns

```
Try (max 3 attempts):
  Invoke external service
Catch:
  If retryCount < 3:
    Wait 30 seconds
    Retry
  Else:
    Log failure
    Send notification
```

---

## 10. Monitoring & Tracking

### 10.1 Activity Stream

Every integration instance logs activities:
- Request received
- Each action executed
- Variable values at each step
- Errors and faults
- Response sent

### 10.2 Tracking Variables

Define business identifiers to search instances:

```
Primary Tracking Variable: OrderId
Secondary: CustomerName
Tertiary: OrderDate

Usage: Track → search by OrderId to find specific instance
```

### 10.3 Dashboard

```
Monitoring Dashboard shows:
  - Total instances (24h / 7d / 30d)
  - Success / Failed / In-progress counts
  - Integration-level statistics
  - Message volume trends
  - Error rate analysis
```

### 10.4 Resubmit Failed Instances

```
Steps:
  1. Navigate: Monitoring → Errors
  2. Filter by integration name and date
  3. Select failed instance(s)
  4. View error details and payload
  5. (Optional) Modify payload
  6. Resubmit
```

---

## 11. Security

### 11.1 OAuth 2.0

**Client Credentials Grant:**
```
Token URL: https://idcs-xxx.identity.oraclecloud.com/oauth2/v1/token
Client ID: your_client_id
Client Secret: your_client_secret
Scope: https://api.example.com/.default
```

**Authorization Code Grant:**
```
Auth URL: https://provider.com/authorize
Token URL: https://provider.com/token
Redirect URI: https://oic-instance/icsapis/agent/oauth/callback
Client ID: xxx
Client Secret: xxx
Scope: read write
```

**Resource Owner Password Grant:**
```
Token URL: https://idcs-xxx/oauth2/v1/token
Username: user@example.com
Password: ********
Client ID: xxx
Client Secret: xxx
```

### 11.2 Basic Authentication

```
Username: integration_user
Password: ********
Security Policy: Basic Authentication
```

### 11.3 API Key

```
Header Name: X-API-Key
API Key Value: stored in OIC credentials
```

### 11.4 OCI Signature (Version 1)

```
Tenancy OCID: ocid1.tenancy.oc1..xxx
User OCID: ocid1.user.oc1..xxx
Fingerprint: aa:bb:cc:dd:...
Private Key: uploaded PEM file
Region: us-phoenix-1
```

### 11.5 SSL Certificates

```
Upload certificates:
  - Navigate: Settings → Certificates
  - Types: Trust certificate, Identity certificate
  - Format: .cer, .pem, .crt
  - Use: For HTTPS connections to systems with custom CAs
```

---

## 12. Scheduled Orchestrations

### 12.1 Defining Parameters

```
Parameter Name: startDate
Type: string
Default: (none - set at schedule time)

Parameter Name: batchSize
Type: string
Default: 100

Access in integration:
  $Parameters.startDate
  $Parameters.batchSize
```

### 12.2 Schedule Frequency

```
Options:
  - Run once at specific date/time
  - Every N minutes (min: 1)
  - Every N hours
  - Daily at HH:MM
  - Weekly: Mon-Sun at HH:MM
  - Monthly: 1st-28th at HH:MM
  - Custom iCal expression

iCal Examples:
  FREQ=DAILY;BYHOUR=2;BYMINUTE=30          # Daily at 2:30 AM
  FREQ=WEEKLY;BYDAY=MO,WE,FR;BYHOUR=6     # Mon/Wed/Fri at 6 AM
  FREQ=MONTHLY;BYMONTHDAY=1;BYHOUR=0       # 1st of month at midnight
```

### 12.3 Scheduled Integration Pattern

```
Schedule Trigger (with parameters)
  → Invoke Source System (get data since $lastRunDate)
  → For-Each record:
      → Map/Transform
      → Invoke Target System
  → Logger: "Processed ${count} records"
```

---

## 13. PL/SQL Integration

### 13.1 Database Adapter for PL/SQL

```
Connection: On-premises Oracle DB (via Connectivity Agent)
Operation: Call Stored Procedure

Package: APPS.XX_ORDER_PKG
Procedure: PROCESS_ORDER

Parameters:
  IN:  p_order_id   NUMBER
  IN:  p_action      VARCHAR2
  OUT: p_status      VARCHAR2
  OUT: p_message     VARCHAR2
  OUT: p_ref_cursor  REF CURSOR (for result sets)
```

### 13.2 Calling Functions

```sql
-- Function returning a value
CREATE OR REPLACE FUNCTION get_next_sequence
RETURN NUMBER IS
  v_seq NUMBER;
BEGIN
  SELECT my_sequence.NEXTVAL INTO v_seq FROM dual;
  RETURN v_seq;
END;
```

### 13.3 REF CURSOR Output

```sql
-- Procedure with REF CURSOR for multi-row results
CREATE OR REPLACE PROCEDURE get_pending_orders(
  p_status    IN  VARCHAR2,
  p_orders    OUT SYS_REFCURSOR
) IS
BEGIN
  OPEN p_orders FOR
    SELECT order_id, customer_name, order_date, amount
    FROM orders
    WHERE status = p_status;
END;
```

OIC maps REF CURSOR output to repeating XML elements for iteration.

---

## 14. FBDI Integration

### 14.1 FBDI Process Flow

```
1. Prepare CSV Data
     ↓
2. Write CSV using Stage File
     ↓
3. Zip the CSV file (Stage File → Zip)
     ↓
4. Upload ZIP to UCM (ERP Adapter → UCM)
     ↓
5. Submit ESS Import Job (ERP Adapter → Submit Job)
     ↓
6. Monitor Job Status (Poll or Callback)
     ↓
7. Handle Results (Success/Failure)
```

### 14.2 CSV Format Example (AP Invoice Import)

```csv
INVOICE_ID,INVOICE_NUM,INVOICE_TYPE_LOOKUP_CODE,INVOICE_DATE,VENDOR_NAME,VENDOR_SITE_CODE,INVOICE_AMOUNT,INVOICE_CURRENCY_CODE,SOURCE,BUSINESS_UNIT
,INV-2024-001,STANDARD,2024-01-15,Acme Corp,Main Site,5000.00,USD,Manual Invoice Entry,US Business Unit
```

### 14.3 Upload to UCM

```
ERP Adapter Configuration:
  Operation: Upload File to UCM
  Document Account: fin$/payables$/import$

Request Mapping:
  - fileName: invoices.zip
  - contentType: application/zip
  - content: base64 encoded zip file (from Stage File reference)
  - documentAccount: fin$/payables$/import$
```

### 14.4 Submit ESS Import Job

```
ERP Adapter Configuration:
  Operation: Submit ESS Job Request

Job Details (AP Invoice Import):
  Job Package: /oracle/apps/ess/financials/payables/invoices/transactions
  Job Name: APXIIMPT
  Parameters:
    1. Business Unit
    2. Source
    3. Group (optional)
    4. Hold Name (optional)
    5. Hold Reason (optional)
    6. GL Date (optional)
    7. Purge (optional - Y/N)
```

### 14.5 Check Job Status

```
ERP Adapter Configuration:
  Operation: Get ESS Job Status

Response Fields:
  - requestStatus: SUCCEEDED / FAILED / WARNING / RUNNING
  - requestId: ESS job request ID

Polling Pattern:
  While $status = 'RUNNING':
    Wait 30 seconds
    Get ESS Job Status
```

### 14.6 Common FBDI Templates by Module

| Module | Template | ESS Job |
|--------|----------|---------|
| GL | JournalImportTemplate.xlsm | ImportJournals |
| AP | ApInvoicesInterface.xlsm | APXIIMPT |
| AR | AutoInvoiceImportTemplate.xlsm | ARXIIMPT |
| FA | FaAdditionsInterface.xlsm | FAMASS_CREATE |
| PO | PoImportTemplate.xlsm | POXPOPDOI |
| CM | BankStatementImport.xlsm | CEBNKSTMTIMP |

---

## 15. BIP Reports

### 15.1 Running Reports via ERP Adapter

```
ERP Adapter Configuration:
  Operation: Run BI Publisher Report

Report Path: /Custom/Financials/AP/XX_AP_Invoice_Report.xdo
Parameters:
  - p_business_unit: US Business Unit
  - p_date_from: 2024-01-01
  - p_date_to: 2024-01-31

Output Format: CSV / PDF / Excel / XML
```

### 15.2 Common Report Paths

```
GL Reports:
  /xmlpserver/reports/custom/GL/JournalReport.xdo
  /Custom/Financials/GL/AccountBalances.xdo

AP Reports:
  /Custom/Financials/AP/InvoiceRegister.xdo
  /Custom/Financials/AP/PaymentRegister.xdo

AR Reports:
  /Custom/Financials/AR/TransactionReport.xdo
  /Custom/Financials/AR/AgingReport.xdo

FBDI Error Reports:
  /Custom/Financials/AP/APInvoiceImportErrors.xdo
```

### 15.3 Report Output Handling

```
1. Invoke: Run BIP Report
2. Response: Base64-encoded report output
3. Stage File: Decode and write to file
4. FTP: Upload to external system (or process in-memory)
```

---

## 16. OIC REST API

### 16.1 Authentication

```bash
# Basic Auth
curl -u "username:password" https://oic-instance/ic/api/...

# OAuth 2.0
curl -H "Authorization: Bearer $ACCESS_TOKEN" https://oic-instance/ic/api/...
```

### 16.2 Key Endpoints

```bash
# List integrations
GET /ic/api/integration/v1/integrations

# Get integration details
GET /ic/api/integration/v1/integrations/{id}|{version}

# Activate integration
POST /ic/api/integration/v1/integrations/{id}|{version}/activate

# Deactivate integration
POST /ic/api/integration/v1/integrations/{id}|{version}/deactivate

# List connections
GET /ic/api/integration/v1/connections

# Test connection
POST /ic/api/integration/v1/connections/{id}/test

# List lookups
GET /ic/api/integration/v1/lookups

# Get monitoring data
GET /ic/api/integration/v1/monitoring/instances

# Run scheduled integration
POST /ic/api/integration/v1/integrations/{id}|{version}/schedule/start

# Import/Export
POST /ic/api/integration/v1/integrations/archive   (export)
POST /ic/api/integration/v1/integrations/import     (import)
```

---

## 17. Connectivity Agent

### 17.1 Prerequisites

- Java 8+ (JDK)
- Network access to OIC cloud endpoint (port 443)
- Network access to on-premises systems
- Minimum 4 GB RAM

### 17.2 Installation Steps

```bash
# 1. Download agent from OIC Console → Settings → Agents → Download
# 2. Create agent group in OIC Console
# 3. Extract and configure

cd /opt/oracle/agent
unzip oic_connectivity_agent.zip

# 4. Edit InstallerProfile.cfg
cat InstallerProfile.cfg
# oic_URL=https://your-instance.integration.ocp.oraclecloud.com
# agent_GROUP_ID=ONPREM_AGENT_GROUP

# 5. Run installer
java -jar connectivityagent.jar

# 6. Start agent
nohup ./agent.sh &
```

### 17.3 Agent Health Check

```
OIC Console → Settings → Agents
  - Status: Running / Stopped / Disconnected
  - Last heartbeat timestamp
  - Agent version
```

---

## 18. Process Automation

### 18.1 Overview

OIC Process Automation enables structured and dynamic business processes.

### 18.2 Components

- **Structured Processes**: BPMN-based workflows with defined paths
- **Dynamic Processes**: Ad-hoc task-based workflows
- **Forms**: Web forms for human tasks (Oracle Visual Builder-based)
- **Decisions**: DMN-based business rules
- **Connectors**: Integration with OIC integrations

### 18.3 Process Flow Example

```
Start → Submit Request (Form)
  → Manager Approval (Human Task)
  → Gateway: Approved?
    → Yes → Auto-Process (Integration Call)
      → Notification → End
    → No → Notify Requester → End
```

---

## 19. Best Practices

### 19.1 Naming Conventions

```
Integrations:
  [SOURCE]_[TARGET]_[PURPOSE]_[PATTERN]
  Example: ERP_SFDC_SYNC_INVOICES_SCHED
  Example: WEBHOOK_ERP_CREATE_PO_APPDRIVEN

Connections:
  [SYSTEM]_[ENV]_[TYPE]
  Example: ORACLE_ERP_PROD_REST
  Example: SAP_DEV_SOAP

Lookups:
  [MODULE]_[PURPOSE]_MAP
  Example: AP_PAYMENT_METHOD_MAP
  Example: GL_ACCOUNT_SEGMENT_MAP
```

### 19.2 Error Handling Patterns

1. Always wrap external calls in Scope actions
2. Log meaningful error messages with business context
3. Use fault handlers for graceful degradation
4. Implement retry logic for transient failures
5. Send notifications for critical failures
6. Include correlation IDs for end-to-end tracing

### 19.3 Performance Optimization

- Minimize the number of invokes in loops (batch when possible)
- Use Stage File segments for large files (200-500 records per segment)
- Avoid unnecessary mapping steps
- Use asynchronous patterns for non-blocking flows
- Set appropriate timeout values
- Use connection caching (default in OIC)

### 19.4 Security Best Practices

- Never hardcode credentials; use OIC connection security
- Use OAuth 2.0 over Basic Auth when possible
- Rotate API keys and passwords regularly
- Use least-privilege roles for integration users
- Enable TLS 1.2+ for all connections
- Audit integration access logs

### 19.5 Environment Migration

```
DEV → TEST → PROD Migration:

1. Export integration package (.iar) from DEV
2. Import to TEST
3. Update connections (point to TEST endpoints)
4. Test thoroughly
5. Export from TEST
6. Import to PROD
7. Update connections (point to PROD endpoints)
8. Activate

Use: Packages for grouping related integrations
```

---

## 20. Troubleshooting

### 20.1 Common Errors

| Error | Cause | Solution |
|-------|-------|----------|
| `CASDK-0004` | Connection timeout | Check network, increase timeout |
| `CASDK-0005` | SSL handshake failure | Upload CA certificate |
| `ORA-20000` | DB adapter error | Check SQL, permissions |
| `HTTP 401` | Authentication failure | Verify credentials, token |
| `HTTP 403` | Authorization failure | Check user roles/permissions |
| `HTTP 429` | Rate limit exceeded | Add retry with backoff |
| `OICS-INTG-ERR` | Integration error | Check activity stream |
| `FBDI-001` | FBDI import failed | Check error report in BIP |
| `Agent disconnected` | Connectivity agent down | Restart agent, check logs |

### 20.2 Debugging Techniques

1. **Activity Stream**: View step-by-step execution with payloads
2. **Logger Action**: Add temporary loggers for variable inspection
3. **Tracking Variables**: Set business identifiers for search
4. **Test Mode**: Use connection test to verify connectivity
5. **Postman**: Test REST endpoints independently before OIC integration
6. **Agent Logs**: Check `$AGENT_HOME/logs/` for connectivity agent issues

### 20.3 Payload Size Limits

```
REST Trigger: 10 MB (default), configurable up to 100 MB
SOAP Trigger: 10 MB
Stage File: 1 GB
FTP: No OIC limit (FTP server limits apply)
Database: Depends on query result size
```

### 20.4 Timeout Settings

```
Default invoke timeout: 120 seconds
Maximum invoke timeout: 300 seconds
Scheduled integration max runtime: 6 hours
Agent heartbeat: 60 seconds
```

---

*This guide covers Oracle Integration Cloud comprehensively. For the latest updates, refer to [Oracle OIC Documentation](https://docs.oracle.com/en/cloud/paas/integration-cloud/).*
