---
name: oracle-vbcs
description: Oracle Visual Builder Cloud Service (VBCS) expert reference - UI components, action chains, service connections, JavaScript, data binding, business objects
---

# Oracle Visual Builder Cloud Service (VBCS) Skill

## Quick Reference

### App Structure
```
Application
  └── Web App
      ├── Flows (group related pages)
      │   └── Pages (UI screens)
      ├── Variables (page / flow / app scope)
      ├── Action Chains (event logic)
      ├── Service Connections (REST backends)
      ├── Business Objects (built-in DB tables)
      └── Resources (CSS, images, translations)
```

### Variable Scopes
| Scope | Lifetime | Access |
|-------|----------|--------|
| Page | While page active | Page only |
| Flow | While flow active | All pages in flow |
| Application | App lifetime | Everywhere |

### Binding Syntax
```html
[[ expression ]]    -- One-way (read-only)
{{ expression }}     -- Two-way (read-write, for inputs)

[[ $variables.name ]]                         -- Simple
[[ $variables.a > 0 ? 'Yes' : 'No' ]]       -- Ternary
[[ $variables.items.length ]]                 -- Array length
[[ $current.data.fieldName ]]                 -- In iterator
{{ $variables.formData.email }}               -- Input binding
```

### Key System Variables
```
$page.variables.xxx          -- Page variable
$flow.variables.xxx          -- Flow variable
$application.variables.xxx   -- App variable
$chain.results.xxx           -- Action chain results
$application.user.userId     -- Current user ID
$application.user.roles      -- User roles array
$current.data                -- Current item in loop
$current.index               -- Current index in loop
```

### Action Chain Actions (Most Used)
```json
// Assign Variable
{ "module": "vb/action/builtin/assignVariablesAction",
  "parameters": { "$page.variables.x": { "source": "value" } } }

// Call REST
{ "module": "vb/action/builtin/restAction",
  "parameters": { "endpoint": "myBackend/getOrders" },
  "outcomes": { "success": "next", "failure": "error" } }

// Navigate
{ "module": "vb/action/builtin/navigateToPageAction",
  "parameters": { "page": "detail", "params": { "id": "{{ $variables.id }}" } } }

// Call JS Function
{ "module": "vb/action/builtin/callModuleFunctionAction",
  "parameters": { "module": "{{ $page.functions }}", "functionName": "myFunc" } }

// Show Toast
{ "module": "vb/action/builtin/fireNotificationEventAction",
  "parameters": { "summary": "Done", "type": "confirmation", "displayMode": "transient" } }

// Refresh Table
{ "module": "vb/action/builtin/fireDataProviderEventAction",
  "parameters": { "target": "{{ $page.variables.mySDP }}", "refresh": true } }

// Open Dialog
{ "module": "vb/action/builtin/callComponentMethodAction",
  "parameters": { "component": "{{ document.getElementById('dlg') }}", "method": "open" } }

// If/Else
{ "module": "vb/action/builtin/ifAction",
  "parameters": { "condition": "{{ $variables.amount > 1000 }}" },
  "outcomes": { "true": "approve", "false": "skip" } }
```

### Lifecycle Events
```
vbBeforeEnter  -- Before page renders (can redirect)
vbEnter        -- Page rendered (load data here)
vbBeforeExit   -- Before leaving (check unsaved changes)
vbExit         -- Page left (cleanup)
```

### Data Providers
```json
// Service Data Provider (SDP) - fetches from REST
{ "type": "vb/ServiceDataProvider",
  "defaultValue": {
    "endpoint": "businessObjects/getall_Order",
    "keyAttributes": "id",
    "itemsPath": "items",
    "uriParameters": { "limit": 25 },
    "filterCriterion": { "op": "$eq", "attribute": "status", "value": "ACTIVE" },
    "sortCriteria": [{ "attribute": "date", "direction": "descending" }]
  }
}

// Array Data Provider (ADP) - wraps local array
{ "type": "vb/ArrayDataProvider",
  "defaultValue": { "data": "{{ $variables.items }}", "keyAttributes": "id" }
}
```

### Common UI Components
```html
<!-- Table -->
<oj-table data="{{sdp}}" columns='[{"headerText":"Name","field":"name"}]'
  selection-mode='{"row":"single"}' scroll-policy="loadMoreOnScroll"/>

<!-- Form -->
<oj-validation-group id="form">
  <oj-form-layout max-columns="2">
    <oj-input-text label-hint="Name" value="{{$variables.name}}" required/>
    <oj-input-number label-hint="Amt" value="{{$variables.amt}}" min="0"/>
    <oj-input-date label-hint="Date" value="{{$variables.date}}"/>
    <oj-select-single label-hint="Status" value="{{$variables.status}}" data="{{statusSDP}}"/>
  </oj-form-layout>
</oj-validation-group>

<!-- Dialog -->
<oj-dialog id="dlg" dialog-title="Confirm">
  <div slot="body">Are you sure?</div>
  <div slot="footer">
    <oj-button on-oj-action="[[cancel]]">No</oj-button>
    <oj-button chroming="callToAction" on-oj-action="[[confirm]]">Yes</oj-button>
  </div>
</oj-dialog>

<!-- Conditional -->
<oj-bind-if test="[[ $variables.show ]]">...</oj-bind-if>

<!-- Loop -->
<oj-bind-for-each data="[[ $variables.items ]]">
  <template><oj-bind-text value="[[ $current.data.name ]]"/></template>
</oj-bind-for-each>

<!-- Charts -->
<oj-chart type="bar" data="{{chartDP}}"/>
```

### JavaScript Module Template
```javascript
define([], function() {
  'use strict';
  var PageModule = function PageModule() {};

  PageModule.prototype.myFunction = function(param1, param2) {
    // Logic here
    return result;
  };

  PageModule.prototype.formatCurrency = function(amount) {
    return new Intl.NumberFormat('en-US',
      { style: 'currency', currency: 'USD' }).format(amount);
  };

  return PageModule;
});
```

### Business Object Query Syntax
```
?q=status='ACTIVE'                           -- Equals
?q=status!='DRAFT'                           -- Not equals
?q=amount>1000                               -- Greater than
?q=name LIKE '%text%'                        -- Contains
?q=status IN ('A','B')                       -- In list
?q=field IS NULL                             -- Null check
?q=cond1 AND cond2                           -- AND
?orderBy=date:desc                           -- Sort
?limit=25&offset=0                           -- Pagination
?fields=id,name,status                       -- Select fields
?expand=ChildResource                        -- Include children
?totalResults=true                           -- Count
```

### Form Validation Pattern
```
1. Call Component Method: form.showMessages()
2. Call Component Method: form.valid() -> returns 'valid'/'invalidShown'
3. If valid -> call REST to save
4. If invalid -> show error toast
```

### Security
```html
<!-- Role check in binding -->
<oj-bind-if test="[[ $application.user.roles.indexOf('Admin') !== -1 ]]">
  <oj-button>Admin Only</oj-button>
</oj-bind-if>
```

### CSS Theming
```css
:root {
  --oj-button-calltoaction-bg-color: #0572CE;
  --oj-core-danger-color: #D43B2A;
  --oj-core-success-color: #0A7B3E;
}
```

### Deployment
```
Design -> Stage (test URL) -> Publish (live URL)
Export: .vba archive | Import to other instances
VB Studio: Git + CI/CD pipelines
```

### Troubleshooting
| Issue | Fix |
|-------|-----|
| Page blank | Check browser console for JS errors |
| SDP no data | Test service connection, check auth |
| Binding not updating | Use {{ }} for two-way, not [[ ]] |
| Table not refreshing | Fire DataProviderEvent with refresh:true |
| 401 on REST | Check backend credentials |

### Full Documentation
- See `docs/VBCS-Complete-Guide.md` for comprehensive guide
- See `docs/VBCS-Commands-Reference.md` for API reference
