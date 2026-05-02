---
name: oracle-oic
description: Oracle Integration Cloud (OIC) expert reference - integrations, connections, mappings, adapters, error handling, FBDI, BIP, REST APIs
---

# Oracle Integration Cloud (OIC) Skill

## Quick Reference

### Integration Patterns
- **App Driven Orchestration**: REST/SOAP trigger -> actions -> response (real-time)
- **Scheduled Orchestration**: Cron/timer -> actions (batch processing)
- **Event-Based**: Business event subscription -> actions (ERP/HCM events)
- **File-Based**: FTP poll or stage file -> read -> transform -> load

### Key Adapters
| Adapter | Auth | Use |
|---------|------|-----|
| REST | OAuth/Basic/APIKey | Any REST API |
| SOAP | WS-Security/Basic | SOAP web services |
| Oracle ERP Cloud | Username/Password | FBDI, BIP, Events, CRUD |
| Oracle HCM Cloud | Username/Password | HDL, Extracts, Events |
| FTP/SFTP | Key/Password | File transfers |
| Database | Username/Password + Agent | SQL, stored procs |
| Kafka | SASL/SSL | Event streaming |
| SAP | Username/Password + Agent | IDoc, BAPI, RFC |

### Connection Setup Pattern
```
1. Create connection (adapter type + URL + credentials)
2. Test connection
3. Use in integration (trigger or invoke)
```

### FBDI Import Pattern (ERP Data Loading)
```
1. Stage File: Write CSV data
2. Stage File: Zip the CSV
3. ERP Adapter: Upload to UCM (documentAccount: fin$/module$/import$)
4. ERP Adapter: Submit ESS Job (JobPackageName + JobDefName + params)
5. Loop: Check ESS Job Status until SUCCEEDED/ERROR
6. Handle result
```

### Common FBDI Jobs
| Module | Job | Params |
|--------|-----|--------|
| GL | ImportJournals | LedgerId,Period,Source |
| AP | APXIIMPT | BU,Source,Group |
| AR | ARXIIMPT | BU,BatchSource |
| FA | FAMASS_CREATE | BookTypeCode |
| PO | POXPOPDOI | BU,BuyerId |

### BIP Report Pattern
```
ERP Adapter -> Run BIP Report -> base64 output -> Stage File: decode/write
Report Path: /Custom/Financials/{Module}/ReportName.xdo
Output: CSV, PDF, Excel, XML
```

### Stage File Operations
```
Read Entire File     - Read CSV/JSON/XML with schema
Read in Segments     - Chunked read for large files (200 records/segment)
Write File           - Write output (append or overwrite)
Zip / Unzip          - Archive operations
List Files           - Directory listing with pattern
```

### XPath Quick Reference
```xpath
concat($a, ' ', $b)                    -- String concatenation
substring($str, 1, 10)                 -- Substring
contains($str, 'text')                 -- Contains check
string-length($str)                    -- Length
upper-case($str) / lower-case($str)    -- Case conversion
translate($str, 'abc', 'ABC')          -- Character replacement
normalize-space($str)                  -- Trim whitespace

current-dateTime()                     -- Now
format-dateTime($d, '[Y]-[M01]-[D01]') -- Format date
xsd:dateTime($d) + xsd:dayTimeDuration('P1D')  -- Add 1 day

round($n) / floor($n) / ceiling($n)   -- Rounding
sum($items/amount)                     -- Sum
format-number($n, '#,##0.00')          -- Number format
count($nodes)                          -- Count nodes

lookupValue('Name', 'SrcCol', $val, 'TgtCol', 'Default')  -- Lookup
```

### JavaScript Action Template
```javascript
function main(input) {
  var data = JSON.parse(input);
  // Process data...
  return JSON.stringify({ result: "value" });
}
```

### Error Handling Pattern
```
Scope: "ProcessData"
  Main Flow:
    Invoke -> Map -> Invoke
  Fault Handler:
    Assign: $errorMsg = fault-message
    Logger: Log error
    Notification: Alert email
    Re-throw (optional)
```

### Monitoring
- **Tracking Variables**: Set business IDs (OrderId, CustomerName) for search
- **Activity Stream**: Step-by-step execution log with payloads
- **Error Hospital**: Resubmit/discard failed instances

### OIC REST API (Management)
```
GET  /ic/api/integration/v1/integrations               -- List
POST /ic/api/integration/v1/integrations/{id}/activate  -- Activate
POST /ic/api/integration/v1/integrations/{id}/deactivate -- Deactivate
GET  /ic/api/integration/v1/monitoring/instances         -- Monitor
POST /ic/api/integration/v1/integrations/{id}/schedule/start -- Run scheduled
```

### Naming Conventions
```
Integrations: SOURCE_TARGET_PURPOSE_PATTERN (e.g., ERP_SFDC_SYNC_INV_SCHED)
Connections:  SYSTEM_ENV_TYPE (e.g., ORACLE_ERP_PROD_REST)
Lookups:      MODULE_PURPOSE_MAP (e.g., AP_PAYMENT_METHOD_MAP)
```

### Troubleshooting
| Error | Fix |
|-------|-----|
| Connection timeout | Check network, increase timeout |
| SSL handshake fail | Upload CA certificate |
| HTTP 401/403 | Verify credentials/roles |
| HTTP 429 | Add retry with backoff |
| FBDI import fail | Check BIP error report |
| Agent disconnected | Restart agent, check logs |

### Connectivity Agent
```bash
# Install: java -jar connectivityagent.jar
# Config: InstallerProfile.cfg (oic_URL, agent_GROUP_ID)
# Start: ./agent.sh &
# Logs: $AGENT_HOME/logs/agent-diagnostics.log
```

### Payload Size Limits
- REST trigger: 10 MB (up to 100 MB configurable)
- Stage File: 1 GB
- Default timeout: 120s (max 300s)
- Scheduled max runtime: 6 hours

### Full Documentation
- See `docs/OIC-Complete-Guide.md` for comprehensive guide
- See `docs/OIC-Commands-Reference.md` for API reference
