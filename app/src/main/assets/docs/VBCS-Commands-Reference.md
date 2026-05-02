# VBCS Commands & API Reference

## 1. VBCS REST API

### 1.1 Application Management

```bash
# List applications
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications"

# Get application details
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications/{appId}"

# Create application
curl -u "user:pass" -X POST \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications" \
  -H "Content-Type: application/json" \
  -d '{"name": "MyApp", "description": "Custom Application"}'

# Delete application
curl -u "user:pass" -X DELETE \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications/{appId}"

# Publish (stage → live)
curl -u "user:pass" -X POST \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications/{appId}/publish"

# Stage application
curl -u "user:pass" -X POST \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications/{appId}/stage"

# Export application (.vba)
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications/{appId}/archive" \
  -o myapp.vba

# Import application
curl -u "user:pass" -X POST \
  "https://{vbcs-host}/ic/builder/rt/api/v2/applications/archive" \
  -H "Content-Type: application/octet-stream" \
  --data-binary @myapp.vba
```

### 1.2 Business Object REST APIs

```bash
# List records
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order?limit=25&offset=0"

# Get single record
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order/{id}"

# Create record
curl -u "user:pass" -X POST \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order" \
  -H "Content-Type: application/json" \
  -d '{"orderNumber":"ORD-001","customerName":"Acme","amount":5000}'

# Update record
curl -u "user:pass" -X PATCH \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order/{id}" \
  -H "Content-Type: application/json" \
  -d '{"status":"APPROVED"}'

# Delete record
curl -u "user:pass" -X DELETE \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order/{id}"

# Query with filter
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order?\
q=status='ACTIVE' AND amount>1000&orderBy=orderDate:desc&limit=25"

# Get child records
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order/{id}/child/OrderLine"

# Expand children
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order/{id}?expand=OrderLine"

# Total count
curl -u "user:pass" -X GET \
  "https://{vbcs-host}/ic/builder/rt/{appId}/1.0/resources/data/Order?\
q=status='ACTIVE'&totalResults=true"
```

---

## 2. Action Chain Actions Reference

### 2.1 Assign Variables
```json
{
  "id": "assignVars",
  "module": "vb/action/builtin/assignVariablesAction",
  "parameters": {
    "$page.variables.name": { "source": "John" },
    "$page.variables.count": { "source": "{{ $page.variables.count + 1 }}" },
    "$page.variables.order": { "source": "{{ $chain.results.callRest.body }}" },
    "$page.variables.items": { "source": [], "reset": "empty" }
  }
}
```

### 2.2 Call REST Endpoint
```json
{
  "id": "callRest",
  "module": "vb/action/builtin/restAction",
  "parameters": {
    "endpoint": "businessObjects/getall_Order",
    "uriParams": { "Order_Id": "{{ $page.variables.orderId }}" },
    "body": "{{ $page.variables.formData }}",
    "headers": { "X-Custom-Header": "value" },
    "requestType": "json"
  },
  "outcomes": {
    "success": "handleSuccess",
    "failure": "handleError"
  }
}
```

### 2.3 Navigate to Page
```json
{
  "id": "navToDetail",
  "module": "vb/action/builtin/navigateToPageAction",
  "parameters": {
    "page": "main-order-detail",
    "params": {
      "orderId": "{{ $variables.selectedId }}",
      "mode": "edit"
    },
    "history": "push"
  }
}
```

### 2.4 Navigate Back
```json
{
  "id": "goBack",
  "module": "vb/action/builtin/navigateBackAction",
  "parameters": {}
}
```

### 2.5 Call Module Function
```json
{
  "id": "callFn",
  "module": "vb/action/builtin/callModuleFunctionAction",
  "parameters": {
    "module": "{{ $page.functions }}",
    "functionName": "calculateTotal",
    "params": ["{{ $variables.lineItems }}", "{{ $variables.taxRate }}"]
  },
  "outcomes": { "success": "assignResult" }
}
```

### 2.6 Call Action Chain
```json
{
  "id": "callChain",
  "module": "vb/action/builtin/callChainAction",
  "parameters": {
    "id": "flow:validateAndSave",
    "params": { "orderId": "{{ $variables.orderId }}" }
  },
  "outcomes": { "success": "next", "failure": "handleError" }
}
```

### 2.7 Call Component Method
```json
{
  "id": "openDialog",
  "module": "vb/action/builtin/callComponentMethodAction",
  "parameters": {
    "component": "{{ document.getElementById('confirmDialog') }}",
    "method": "open",
    "params": []
  }
}

// Close dialog
{
  "id": "closeDialog",
  "module": "vb/action/builtin/callComponentMethodAction",
  "parameters": {
    "component": "{{ document.getElementById('confirmDialog') }}",
    "method": "close",
    "params": []
  }
}

// Form validation
{
  "id": "validate",
  "module": "vb/action/builtin/callComponentMethodAction",
  "parameters": {
    "component": "{{ document.getElementById('myForm') }}",
    "method": "valid",
    "params": []
  }
}

// Show form messages
{
  "id": "showMessages",
  "module": "vb/action/builtin/callComponentMethodAction",
  "parameters": {
    "component": "{{ document.getElementById('myForm') }}",
    "method": "showMessages",
    "params": []
  }
}
```

### 2.8 Fire Event
```json
{
  "id": "fireCustomEvent",
  "module": "vb/action/builtin/fireCustomEventAction",
  "parameters": {
    "name": "orderCreated",
    "payload": { "orderId": "{{ $variables.newOrderId }}" }
  }
}
```

### 2.9 Fire Notification (Toast)
```json
{
  "id": "successToast",
  "module": "vb/action/builtin/fireNotificationEventAction",
  "parameters": {
    "summary": "Success",
    "message": "Record saved successfully",
    "displayMode": "transient",
    "type": "confirmation"
  }
}
// type: confirmation | info | warning | error
// displayMode: transient | persist
```

### 2.10 Fire Data Provider Event (Refresh)
```json
{
  "id": "refreshTable",
  "module": "vb/action/builtin/fireDataProviderEventAction",
  "parameters": {
    "target": "{{ $page.variables.ordersListSDP }}",
    "refresh": true
  }
}
```

### 2.11 If / Else
```json
{
  "id": "checkCondition",
  "module": "vb/action/builtin/ifAction",
  "parameters": {
    "condition": "{{ $variables.amount > 10000 }}"
  },
  "outcomes": {
    "true": "requireApproval",
    "false": "autoApprove"
  }
}
```

### 2.12 For Each
```json
{
  "id": "processItems",
  "module": "vb/action/builtin/forEachAction",
  "parameters": {
    "items": "{{ $variables.lineItems }}",
    "currentItem": "$current"
  },
  "actions": { "processOne": { "module": "..." } }
}
```

### 2.13 Switch
```json
{
  "id": "switchAction",
  "module": "vb/action/builtin/switchAction",
  "parameters": {
    "expression": "{{ $variables.status }}"
  },
  "outcomes": {
    "DRAFT": "handleDraft",
    "ACTIVE": "handleActive",
    "CLOSED": "handleClosed",
    "default": "handleOther"
  }
}
```

### 2.14 Reset Variables
```json
{
  "id": "resetForm",
  "module": "vb/action/builtin/resetVariablesAction",
  "parameters": {
    "variables": [
      "$page.variables.formData",
      "$page.variables.errorMessage"
    ]
  }
}
```

### 2.15 Open URL
```json
{
  "id": "openLink",
  "module": "vb/action/builtin/openUrlAction",
  "parameters": {
    "url": "https://example.com/report/{{ $variables.reportId }}",
    "windowName": "_blank"
  }
}
```

### 2.16 Return
```json
{
  "id": "returnResult",
  "module": "vb/action/builtin/returnAction",
  "parameters": {
    "outcome": "success",
    "payload": { "orderId": "{{ $variables.newId }}" }
  }
}
```

---

## 3. System Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `$page.variables.xxx` | Page-scoped variable | `$page.variables.orderId` |
| `$flow.variables.xxx` | Flow-scoped variable | `$flow.variables.filter` |
| `$application.variables.xxx` | App-scoped variable | `$application.variables.config` |
| `$page.constants.xxx` | Page constant | `$page.constants.MAX_ITEMS` |
| `$flow.constants.xxx` | Flow constant | `$flow.constants.API_VERSION` |
| `$application.constants.xxx` | App constant | `$application.constants.APP_NAME` |
| `$variables` | Current scope variables | Shorthand |
| `$constants` | Current scope constants | Shorthand |
| `$chain.variables.xxx` | Action chain variable | `$chain.variables.temp` |
| `$chain.results.xxx` | Action chain results | `$chain.results.callRest.body` |
| `$application.user` | Current user object | `$application.user.userId` |
| `$application.user.userId` | User ID | "john.doe" |
| `$application.user.email` | User email | "john@example.com" |
| `$application.user.roles` | User roles array | ["Admin","User"] |
| `$application.user.fullName` | Display name | "John Doe" |
| `$application.path.app` | App base URL | "/ic/builder/rt/app/1.0" |
| `$responsive.smUp` | Screen >= small | true/false |
| `$responsive.mdUp` | Screen >= medium | true/false |
| `$responsive.lgUp` | Screen >= large | true/false |
| `$responsive.xlUp` | Screen >= x-large | true/false |
| `$current.data` | Current item in loop | `$current.data.name` |
| `$current.index` | Current index in loop | 0, 1, 2... |
| `$current.key` | Current key in loop | Row key value |
| `$page.functions` | Page module reference | For callModuleFunction |
| `$flow.functions` | Flow module reference | For callModuleFunction |
| `$application.functions` | App module reference | For callModuleFunction |

---

## 4. Expression Language Reference

### 4.1 Binding Syntax

```html
<!-- One-way (read-only) -->
[[ expression ]]

<!-- Two-way (read-write) -->
{{ expression }}

<!-- Examples -->
[[ $variables.name ]]                         <!-- Simple binding -->
[[ $variables.count > 0 ]]                    <!-- Boolean expression -->
[[ $variables.firstName + ' ' + $variables.lastName ]]  <!-- Concatenation -->
[[ $variables.amount > 0 ? 'Credit' : 'Debit' ]]       <!-- Ternary -->
[[ $variables.items.length ]]                  <!-- Array length -->
[[ $variables.items[0].name ]]                 <!-- Array index -->
[[ $variables.order.customer.name ]]           <!-- Nested object -->
[[ $current.data.fieldName ]]                  <!-- Iterator item -->
```

### 4.2 Operators

```
Arithmetic: +  -  *  /  %
Comparison: ==  !=  ===  !==  >  <  >=  <=
Logical:    &&  ||  !
Ternary:    condition ? trueVal : falseVal
Nullish:    value ?? defaultValue
```

### 4.3 Common Expressions

```javascript
// Conditional visibility
[[ $variables.showDetails === true ]]
[[ $variables.items.length > 0 ]]
[[ $variables.status !== 'CLOSED' ]]
[[ $application.user.roles.indexOf('Admin') !== -1 ]]

// Computed values
[[ $variables.quantity * $variables.unitPrice ]]
[[ '$' + $variables.amount.toFixed(2) ]]
[[ $variables.items.filter(i => i.active).length ]]

// CSS class binding
[[ $variables.isError ? 'error-text' : 'normal-text' ]]
[[ 'status-' + $variables.status.toLowerCase() ]]
```

---

## 5. Business Object Query Syntax

```bash
# Equals
?q=status='ACTIVE'

# Not equals
?q=status!='DRAFT'

# Greater/less than
?q=amount>1000
?q=amount>=1000
?q=amount<5000
?q=orderDate>'2024-01-01'

# LIKE (contains)
?q=customerName LIKE '%Oracle%'
?q=orderNumber LIKE 'ORD-%'

# AND / OR
?q=status='ACTIVE' AND amount>500
?q=status='ACTIVE' OR status='PENDING'

# IN
?q=status IN ('ACTIVE','PENDING','APPROVED')

# IS NULL / IS NOT NULL
?q=completedDate IS NULL
?q=assignee IS NOT NULL

# Combined
?q=status='ACTIVE' AND amount>1000 AND customerName LIKE '%Corp%'

# Sort
?orderBy=orderDate:desc
?orderBy=status:asc,orderDate:desc

# Pagination
?limit=25&offset=0     # Page 1
?limit=25&offset=25    # Page 2
?limit=25&offset=50    # Page 3

# Field selection
?fields=id,orderNumber,status,amount

# Expand children
?expand=OrderLine
?expand=OrderLine,Attachments

# Total count
?totalResults=true

# Combined query
?q=status='ACTIVE'&orderBy=orderDate:desc&limit=25&offset=0&fields=id,orderNumber,amount&totalResults=true
```

---

## 6. Page Lifecycle Events

| Event | When | Use Case |
|-------|------|----------|
| `vbBeforeEnter` | Before page renders | Validate access, redirect if needed |
| `vbEnter` | Page rendered | Load initial data, set defaults |
| `vbBeforeExit` | Before leaving page | Check unsaved changes, confirm |
| `vbExit` | Page is being left | Cleanup, stop timers |
| `vbAfterNavigate` | After navigation completes | Post-navigation setup |
| `vbNotification` | Notification event fires | Display toast messages |

### Flow Lifecycle Events

| Event | When |
|-------|------|
| `vbBeforeEnter` | Before flow is entered |
| `vbEnter` | Flow has been entered |
| `vbBeforeExit` | Before flow is exited |
| `vbExit` | Flow is being exited |

### Application Lifecycle Events

| Event | When |
|-------|------|
| `vbBeforeEnter` | App starting |
| `vbEnter` | App started |
| `vbNotification` | Global notification |

---

## 7. JET Component Key Properties

### oj-table
```html
<oj-table
  data="{{sdpVariable}}"
  columns='[{"headerText":"Name","field":"name","sortable":"enabled"}]'
  selection-mode='{"row":"single"|"multiple"|"none"}'
  scroll-policy="loadMoreOnScroll"|"loadAll"
  scroll-policy-options='{"fetchSize":25}'
  on-first-selected-row-changed="[[listener]]"
  edit-mode="none"|"rowEdit"
  style="width:100%;max-height:400px"
/>
```

### oj-input-text
```html
<oj-input-text
  label-hint="Label"
  value="{{variable}}"
  placeholder="Enter value"
  required="true"|"false"
  readonly="true"|"false"
  disabled="true"|"false"
  max-length="100"
  validators='[{"type":"regExp","options":{"pattern":"...","messageDetail":"..."}}]'
  on-value-changed="[[listener]]"
/>
```

### oj-select-single
```html
<oj-select-single
  label-hint="Label"
  value="{{variable}}"
  data="{{sdpOrAdpVariable}}"
  item-text="[labelField]"
  value-attribute="valueField"
  required="true"|"false"
  placeholder="Select..."
  on-value-changed="[[listener]]"
/>
```

### oj-dialog
```html
<oj-dialog
  id="myDialog"
  dialog-title="Title"
  cancel-behavior="icon"|"escape"|"none"
  style="width:500px;min-height:200px"
>
  <div slot="body">Content</div>
  <div slot="footer">
    <oj-button on-oj-action="[[cancel]]">Cancel</oj-button>
    <oj-button chroming="callToAction" on-oj-action="[[confirm]]">OK</oj-button>
  </div>
</oj-dialog>
<!-- Open: document.getElementById('myDialog').open() -->
<!-- Close: document.getElementById('myDialog').close() -->
```

### oj-chart
```html
<oj-chart
  type="bar"|"line"|"pie"|"area"|"scatter"|"bubble"|"stock"
  data="{{chartDP}}"
  orientation="vertical"|"horizontal"
  stack="on"|"off"
  x-axis='{"title":"X Label"}'
  y-axis='{"title":"Y Label"}'
  legend='{"rendered":"on","position":"bottom"}'
  style="width:100%;height:300px"
/>
```

---

## 8. CSS Variables (Redwood Theme)

```css
/* Core colors */
--oj-core-text-color-primary          /* Main text color */
--oj-core-text-color-secondary        /* Secondary text */
--oj-core-bg-color-primary            /* Main background */
--oj-core-bg-color-secondary          /* Card/panel background */
--oj-core-divider-color               /* Border/divider */
--oj-core-danger-color                /* Error/danger red */
--oj-core-success-color               /* Success green */
--oj-core-warning-color               /* Warning yellow/orange */
--oj-core-info-color                  /* Info blue */

/* Button colors */
--oj-button-calltoaction-bg-color
--oj-button-calltoaction-bg-color-hover
--oj-button-calltoaction-bg-color-active
--oj-button-calltoaction-text-color
--oj-button-outlined-chrome-border-color

/* Input field */
--oj-text-field-bg-color
--oj-text-field-border-color
--oj-text-field-border-color-focus
--oj-text-field-text-color

/* Collection (table/list) */
--oj-collection-bg-color
--oj-collection-header-bg-color
--oj-collection-cell-padding

/* Spacing */
--oj-core-spacing-1x   /* 4px */
--oj-core-spacing-2x   /* 8px */
--oj-core-spacing-3x   /* 12px */
--oj-core-spacing-4x   /* 16px */
--oj-core-spacing-6x   /* 24px */
--oj-core-spacing-8x   /* 32px */

/* Typography */
--oj-typography-heading-xs-font-size
--oj-typography-heading-sm-font-size
--oj-typography-heading-md-font-size
--oj-typography-heading-lg-font-size
--oj-typography-body-md-font-size
--oj-typography-body-sm-font-size
```
