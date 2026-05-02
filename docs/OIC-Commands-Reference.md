# OIC Commands & API Reference

## 1. OIC Management REST API

### 1.1 Integration Management

```bash
# List all integrations
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/integrations"

# Get specific integration
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}"

# Activate integration
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/activate" \
  -H "X-HTTP-Method-Override: PATCH"

# Deactivate integration
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/deactivate" \
  -H "X-HTTP-Method-Override: PATCH"

# Delete integration
curl -u "user:pass" -X DELETE \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}"

# Export integration (.iar)
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/archive" \
  -o integration_export.iar

# Import integration
curl -u "user:pass" -X PUT \
  "https://{oic-host}/ic/api/integration/v1/integrations/archive" \
  -H "Content-Type: application/octet-stream" \
  --data-binary @integration_export.iar
```

### 1.2 Connection Management

```bash
# List all connections
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/connections"

# Get connection details
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/connections/{id}"

# Test connection
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/connections/{id}/test"

# Create connection
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/connections" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "MY_REST_CONN",
    "name": "My REST Connection",
    "adapterType": "rest",
    "connectionProperties": [
      {"propertyName": "connectionUrl", "propertyValue": "https://api.example.com"}
    ],
    "securityPolicy": "BASIC_AUTH",
    "securityProperties": [
      {"propertyName": "username", "propertyValue": "apiuser"},
      {"propertyName": "password", "propertyValue": "password123"}
    ]
  }'

# Update connection
curl -u "user:pass" -X PUT \
  "https://{oic-host}/ic/api/integration/v1/connections/{id}" \
  -H "Content-Type: application/json" \
  -d '{ ... }'

# Delete connection
curl -u "user:pass" -X DELETE \
  "https://{oic-host}/ic/api/integration/v1/connections/{id}"
```

### 1.3 Lookup Management

```bash
# List all lookups
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/lookups"

# Get lookup
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/lookups/{lookupName}"

# Create/Update lookup
curl -u "user:pass" -X PUT \
  "https://{oic-host}/ic/api/integration/v1/lookups/{lookupName}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "COUNTRY_MAP",
    "columns": ["SourceSystem", "TargetSystem"],
    "rows": [
      {"SourceSystem": "US", "TargetSystem": "United States"},
      {"SourceSystem": "UK", "TargetSystem": "United Kingdom"}
    ]
  }'

# Delete lookup
curl -u "user:pass" -X DELETE \
  "https://{oic-host}/ic/api/integration/v1/lookups/{lookupName}"

# Export lookup (CSV)
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/lookups/{lookupName}/archive" \
  -o lookup.csv

# Import lookup (CSV)
curl -u "user:pass" -X PUT \
  "https://{oic-host}/ic/api/integration/v1/lookups/{lookupName}/archive" \
  -H "Content-Type: text/csv" \
  --data-binary @lookup.csv
```

### 1.4 Monitoring

```bash
# Get integration instances
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/monitoring/instances?\
integrationId={id}&status=FAILED&from=2024-01-01T00:00:00Z&limit=25"

# Get instance details
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/monitoring/instances/{instanceId}"

# Get activity stream
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/monitoring/instances/{instanceId}/activityStream"

# Resubmit failed instance
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/monitoring/instances/{instanceId}/resubmit"

# Get error details
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/monitoring/errors?\
integrationId={id}&from=2024-01-01T00:00:00Z"

# Get dashboard metrics
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/monitoring/dashboard"
```

### 1.5 Scheduled Job Management

```bash
# Start scheduled integration now
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/schedule/start" \
  -H "Content-Type: application/json" \
  -d '{"scheduleParams": {"param1": "value1", "param2": "value2"}}'

# Pause schedule
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/schedule/pause"

# Resume schedule
curl -u "user:pass" -X POST \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/schedule/resume"

# Get schedule info
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/schedule"

# Update schedule
curl -u "user:pass" -X PUT \
  "https://{oic-host}/ic/api/integration/v1/integrations/{id}|{version}/schedule" \
  -H "Content-Type: application/json" \
  -d '{
    "scheduleType": "RECURRING",
    "frequency": {"recurringFrequency": "DAILY", "startTime": "02:30"}
  }'
```

### 1.6 Package Management

```bash
# List packages
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/packages"

# Export package
curl -u "user:pass" -X GET \
  "https://{oic-host}/ic/api/integration/v1/packages/{packageName}/archive" \
  -o package.par

# Import package
curl -u "user:pass" -X PUT \
  "https://{oic-host}/ic/api/integration/v1/packages/archive" \
  -H "Content-Type: application/octet-stream" \
  --data-binary @package.par
```

---

## 2. XPath / XSLT Function Reference

### 2.1 String Functions

| Function | Syntax | Example |
|----------|--------|---------|
| concat | `concat(str1, str2, ...)` | `concat($first, ' ', $last)` → "John Doe" |
| substring | `substring(str, start, len)` | `substring('Hello', 1, 3)` → "Hel" |
| substring-before | `substring-before(str, delim)` | `substring-before('2024-01-15', '-')` → "2024" |
| substring-after | `substring-after(str, delim)` | `substring-after('user@mail.com', '@')` → "mail.com" |
| string-length | `string-length(str)` | `string-length('Hello')` → 5 |
| contains | `contains(str, sub)` | `contains('Hello World', 'World')` → true |
| starts-with | `starts-with(str, prefix)` | `starts-with('PO-123', 'PO-')` → true |
| translate | `translate(str, from, to)` | `translate('abc', 'abc', 'ABC')` → "ABC" |
| normalize-space | `normalize-space(str)` | `normalize-space(' a  b ')` → "a b" |
| upper-case | `upper-case(str)` | `upper-case('hello')` → "HELLO" |
| lower-case | `lower-case(str)` | `lower-case('HELLO')` → "hello" |
| replace | `replace(str, pattern, repl)` | `replace('2024/01/15', '/', '-')` → "2024-01-15" |
| trim | `normalize-space(str)` | Trim whitespace |

### 2.2 Date/Time Functions

| Function | Syntax | Example |
|----------|--------|---------|
| current-dateTime | `current-dateTime()` | "2024-01-15T10:30:00Z" |
| current-date | `current-date()` | "2024-01-15" |
| format-dateTime | `format-dateTime($dt, pattern)` | `format-dateTime($d,'[Y]-[M01]-[D01]')` |
| year-from-dateTime | `year-from-dateTime($dt)` | 2024 |
| month-from-dateTime | `month-from-dateTime($dt)` | 1 |
| day-from-dateTime | `day-from-dateTime($dt)` | 15 |
| hours-from-dateTime | `hours-from-dateTime($dt)` | 10 |
| add-dayTimeDuration | `xsd:dateTime($d) + xsd:dayTimeDuration('P1D')` | Add 1 day |
| subtract-dayTimeDuration | `xsd:dateTime($d) - xsd:dayTimeDuration('P30D')` | Subtract 30 days |

**Date Arithmetic Patterns:**
```xpath
<!-- Add days -->
xsd:dateTime($date) + xsd:dayTimeDuration('P5D')      <!-- +5 days -->
xsd:dateTime($date) + xsd:dayTimeDuration('PT2H30M')  <!-- +2h 30m -->

<!-- Subtract days -->
xsd:dateTime($date) - xsd:dayTimeDuration('P1D')      <!-- -1 day -->

<!-- Duration format: P[n]Y[n]M[n]DT[n]H[n]M[n]S -->
P1Y        <!-- 1 year -->
P6M        <!-- 6 months -->
P30D       <!-- 30 days -->
PT2H       <!-- 2 hours -->
PT30M      <!-- 30 minutes -->
P1DT12H    <!-- 1 day 12 hours -->
```

### 2.3 Math Functions

| Function | Syntax | Example |
|----------|--------|---------|
| sum | `sum(nodeset)` | `sum($items/amount)` |
| round | `round(num)` | `round(3.7)` → 4 |
| floor | `floor(num)` | `floor(3.7)` → 3 |
| ceiling | `ceiling(num)` | `ceiling(3.2)` → 4 |
| number | `number(str)` | `number('123')` → 123 |
| format-number | `format-number(num, pattern)` | `format-number(1234.5, '#,##0.00')` → "1,234.50" |
| abs (OIC ext) | `abs($val)` | `abs(-5)` → 5 |
| mod | `$val mod $divisor` | `10 mod 3` → 1 |

### 2.4 Boolean / Node-Set Functions

| Function | Syntax | Example |
|----------|--------|---------|
| count | `count(nodeset)` | `count($items/item)` |
| position | `position()` | Current position in for-each |
| last | `last()` | Last position in for-each |
| not | `not(expr)` | `not($status = 'ACTIVE')` |
| true/false | `true()` / `false()` | Boolean constants |
| boolean | `boolean(expr)` | Convert to boolean |
| string | `string(val)` | Convert to string |

### 2.5 Lookup Function

```xpath
lookupValue('LookupName', 'SourceColumn', $sourceValue, 'TargetColumn', 'DefaultValue')

<!-- Examples -->
lookupValue('CURRENCY_MAP', 'Code', $currCode, 'Name', 'Unknown')
lookupValue('BU_MAP', 'SourceBU', $businessUnit, 'TargetBU', $businessUnit)
```

---

## 3. JavaScript Functions (OIC Libraries)

```javascript
// Available built-in objects in OIC JavaScript actions
// input: string (JSON) passed to the function
// output: return string (JSON)

function main(input) {
  var data = JSON.parse(input);

  // String operations
  var upper = data.name.toUpperCase();
  var trimmed = data.value.trim();
  var replaced = data.text.replace(/[^a-zA-Z0-9]/g, '');

  // Date operations
  var now = new Date();
  var isoDate = now.toISOString();                     // 2024-01-15T10:30:00.000Z
  var dateOnly = now.toISOString().split('T')[0];      // 2024-01-15
  var formatted = (now.getMonth()+1) + '/' + now.getDate() + '/' + now.getFullYear();

  // Array operations
  var filtered = data.items.filter(function(i) { return i.status === 'ACTIVE'; });
  var mapped = data.items.map(function(i) { return { id: i.id, total: i.qty * i.price }; });
  var total = data.items.reduce(function(sum, i) { return sum + i.amount; }, 0);
  var found = data.items.find(function(i) { return i.id === 123; });

  // JSON operations
  var obj = { key: 'value', nested: { a: 1 } };
  var jsonStr = JSON.stringify(obj);
  var parsed = JSON.parse(jsonStr);

  // Math
  var rounded = Math.round(data.amount * 100) / 100;  // 2 decimal places
  var max = Math.max(1, 2, 3);
  var random = Math.floor(Math.random() * 1000);

  // Base64
  var encoded = java.util.Base64.getEncoder().encodeToString(
    new java.lang.String(data.text).getBytes("UTF-8"));

  return JSON.stringify({ result: "processed" });
}
```

---

## 4. Stage File Operations Syntax

```
Read Entire File:
  File Name: ${filename}
  Directory: /input
  Schema: CSV (delimiter=",", qualifier="\"", header=true)

Read File in Segments:
  Segment Size: 200 records
  File Name: ${filename}
  Directory: /input

Write File:
  File Name: output_${timestamp}.csv
  Directory: /output
  Append to existing: Yes/No

Zip File:
  File Name: archive.zip
  Directory: /staging
  Files to zip: /staging/file1.csv, /staging/file2.csv

Unzip File:
  File Name: incoming.zip
  Directory: /input
  Output Directory: /input/unzipped

List Files:
  Directory: /input
  File Pattern: *.csv
  Returns: fileName, fileSize, lastModified
```

---

## 5. Connectivity Agent Commands

```bash
# Install agent
cd /opt/oracle/agent
java -jar connectivityagent.jar

# Start agent
./agent.sh &

# Stop agent
./agent.sh stop

# Check agent status
./agent.sh status

# View agent logs
tail -f /opt/oracle/agent/logs/agent-diagnostics.log

# Agent configuration file
cat /opt/oracle/agent/agenthome/conf/InstallerProfile.cfg

# Key config properties:
# oic_URL=https://oic-instance.integration.ocp.oraclecloud.com
# agent_GROUP_ID=YOUR_AGENT_GROUP
# oic_USER=username
# oic_PASSWORD=password (encrypted)

# Restart agent after config changes
./agent.sh stop && sleep 5 && ./agent.sh start &

# Test connectivity from agent host
curl -v https://oic-instance.integration.ocp.oraclecloud.com/ic/api/integration/v1/connections
```

---

## 6. Common Payload Templates

### REST Trigger Request
```json
{
  "orderId": "ORD-001",
  "customerName": "Acme Corp",
  "orderDate": "2024-01-15",
  "amount": 5000.00,
  "lineItems": [
    {"sku": "ITEM-1", "qty": 10, "price": 250.00},
    {"sku": "ITEM-2", "qty": 5, "price": 500.00}
  ]
}
```

### Error Response Template
```json
{
  "errorCode": "ERR-001",
  "errorMessage": "Validation failed: Missing required field 'customerName'",
  "timestamp": "2024-01-15T10:30:00Z",
  "integrationId": "ORDER_SYNC_01.00.0000",
  "instanceId": "abc-123-def"
}
```

### Callback Response Template
```json
{
  "requestId": "12345678",
  "status": "SUCCEEDED",
  "processedCount": 150,
  "errorCount": 2,
  "errorDetails": [
    {"row": 45, "field": "amount", "error": "Invalid number format"},
    {"row": 89, "field": "date", "error": "Date out of range"}
  ]
}
```
