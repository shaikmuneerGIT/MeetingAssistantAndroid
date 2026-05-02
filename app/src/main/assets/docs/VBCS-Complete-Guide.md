# Oracle Visual Builder Cloud Service (VBCS) - Complete Guide

## Table of Contents

1. [Overview & Architecture](#1-overview--architecture)
2. [Application Structure](#2-application-structure)
3. [Pages & Navigation](#3-pages--navigation)
4. [UI Components](#4-ui-components)
5. [Variables & Types](#5-variables--types)
6. [Action Chains](#6-action-chains)
7. [Service Connections](#7-service-connections)
8. [Business Objects](#8-business-objects)
9. [JavaScript in VBCS](#9-javascript-in-vbcs)
10. [Data Binding](#10-data-binding)
11. [Forms & Validation](#11-forms--validation)
12. [CSS & Theming](#12-css--theming)
13. [Security & Roles](#13-security--roles)
14. [Deployment & Lifecycle](#14-deployment--lifecycle)
15. [Integration with Oracle SaaS](#15-integration-with-oracle-saas)
16. [Advanced Topics](#16-advanced-topics)
17. [Best Practices](#17-best-practices)
18. [Troubleshooting](#18-troubleshooting)

---

## 1. Overview & Architecture

### What is VBCS?

Oracle Visual Builder Cloud Service (VBCS) is a low-code development platform for building web and mobile applications. It provides:

- **Visual Page Designer** - Drag-and-drop UI builder
- **Business Objects** - Built-in database tables with REST APIs
- **Service Connections** - Connect to any REST API or Oracle SaaS
- **Action Chains** - Visual event-driven logic
- **Mobile Support** - PWA, iOS, Android apps
- **Role-Based Security** - IDCS/IAM integration

### Standalone vs Embedded

| Mode | Description |
|------|-------------|
| **Standalone** | Full VBCS instance for building custom apps |
| **Embedded (in SaaS)** | Extend Oracle Fusion apps (ERP/HCM) using Application Composer + Visual Builder |

### Architecture

```
┌──────────────────────────────────────────────────┐
│               VBCS Instance                       │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐ │
│  │  Web Apps   │  │ Mobile Apps│  │   PWAs     │ │
│  └────────────┘  └────────────┘  └────────────┘ │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐ │
│  │  Business   │  │  Service   │  │  Action    │ │
│  │  Objects    │  │ Connections│  │  Chains    │ │
│  └────────────┘  └────────────┘  └────────────┘ │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐ │
│  │  Security   │  │  Git/VBS  │  │  Themes    │ │
│  └────────────┘  └────────────┘  └────────────┘ │
└──────────────────────────────────────────────────┘
         │                    │
   Oracle SaaS APIs      External REST APIs
   (ERP, HCM, SCM)      (any third-party)
```

### Visual Builder Studio (VBS)

- Git-based version control for VBCS apps
- CI/CD pipelines for automated builds and deployment
- Team collaboration with branches and merge requests
- Environment management (DEV, TEST, PROD)

---

## 2. Application Structure

### 2.1 Web Applications

```
Application
├── Web App 1 (SPA - Single Page Application)
│   ├── main (default flow)
│   │   ├── main-start (default page)
│   │   ├── main-orders
│   │   └── main-order-detail
│   ├── admin (flow)
│   │   ├── admin-users
│   │   └── admin-settings
│   ├── Resources
│   │   ├── CSS
│   │   ├── Images
│   │   └── Translations
│   └── App Settings
├── Service Connections
├── Business Objects
└── Security (Roles)
```

### 2.2 Application Settings

```json
{
  "id": "myapp",
  "description": "My Custom Application",
  "defaultPage": "main-start",
  "security": {
    "requiresAuthentication": true
  },
  "settings": {
    "header": {
      "title": "My Application",
      "logo": "resources/images/logo.png"
    },
    "theme": "redwood"
  }
}
```

### 2.3 Flows

Flows group related pages and share variables/action chains.

```
Flow: "orders"
├── Variables (shared across all pages in flow)
├── Action Chains (shared across all pages in flow)
├── Pages:
│   ├── orders-list
│   ├── orders-create
│   └── orders-detail
└── Flow Lifecycle Events:
    ├── vbEnter (flow entered)
    └── vbExit (flow exiting)
```

---

## 3. Pages & Navigation

### 3.1 Page Designer Modes

| Mode | Purpose |
|------|---------|
| **Design** | Visual drag-and-drop layout |
| **Code** | Edit page HTML directly |
| **Live** | Interactive preview with real data |
| **Structure** | Tree view of component hierarchy |

### 3.2 Page Parameters

```json
// Define input parameters for a page
{
  "inputParameters": {
    "orderId": {
      "type": "number",
      "required": true
    },
    "mode": {
      "type": "string",
      "required": false,
      "defaultValue": "view"
    }
  }
}
```

### 3.3 Navigation

**Declarative Navigation (Action Chain):**
```json
{
  "id": "navigateToDetail",
  "actions": {
    "navigateToPage": {
      "module": "vb/action/builtin/navigateToPageAction",
      "parameters": {
        "page": "main-order-detail",
        "params": {
          "orderId": "{{ $variables.selectedOrderId }}"
        }
      }
    }
  }
}
```

**Navigation Rules:**
```
main-start ──→ main-orders (button click)
main-orders ──→ main-order-detail (row select, pass orderId)
main-order-detail ──→ main-orders (back button, navigateBack)
```

### 3.4 Shell Page

The shell page wraps all pages and provides consistent navigation:

```html
<!-- Shell page structure -->
<div class="oj-web-applayout-max-width oj-web-applayout-content">
  <!-- Header / Navigation Bar -->
  <oj-navigation-list edge="top">
    <ul>
      <li><a href="#">Home</a></li>
      <li><a href="#">Orders</a></li>
      <li><a href="#">Reports</a></li>
      <li><a href="#">Settings</a></li>
    </ul>
  </oj-navigation-list>

  <!-- Page Content -->
  <oj-vb-content></oj-vb-content>

  <!-- Footer -->
  <footer>...</footer>
</div>
```

### 3.5 Page Lifecycle Events

```
vbBeforeEnter   → Before page renders (can redirect)
vbEnter         → Page has rendered
vbBeforeExit    → Before leaving page (can cancel)
vbExit          → Page is being left
vbAfterNavigate → After navigation completes
vbNotification  → When notification fires
```

### 3.6 Fragments

Reusable page fragments (like components):

```html
<!-- Define fragment -->
<oj-vb-fragment name="address-form">
  <oj-form-layout>
    <oj-input-text label-hint="Street" value="{{ $fragment.variables.street }}"/>
    <oj-input-text label-hint="City" value="{{ $fragment.variables.city }}"/>
    <oj-input-text label-hint="State" value="{{ $fragment.variables.state }}"/>
    <oj-input-text label-hint="ZIP" value="{{ $fragment.variables.zip }}"/>
  </oj-form-layout>
</oj-vb-fragment>

<!-- Use fragment in a page -->
<oj-vb-fragment-param name="street" value="{{ $variables.address.street }}"/>
```

---

## 4. UI Components

### 4.1 Layout Components

```html
<!-- Flex Layout -->
<oj-flex>
  <oj-flex-item flex="1"><p>Left</p></oj-flex-item>
  <oj-flex-item flex="2"><p>Center (wider)</p></oj-flex-item>
  <oj-flex-item flex="1"><p>Right</p></oj-flex-item>
</oj-flex>

<!-- Form Layout (auto-responsive) -->
<oj-form-layout max-columns="2" label-edge="start">
  <oj-input-text label-hint="First Name" value="{{ $variables.firstName }}"/>
  <oj-input-text label-hint="Last Name" value="{{ $variables.lastName }}"/>
  <oj-input-text label-hint="Email" value="{{ $variables.email }}"/>
  <oj-input-date label-hint="DOB" value="{{ $variables.dob }}"/>
</oj-form-layout>

<!-- Collapsible -->
<oj-collapsible>
  <h3 slot="header">Advanced Options</h3>
  <div>Content here...</div>
</oj-collapsible>

<!-- Panel -->
<div class="oj-panel oj-panel-shadow-sm">
  <h3>Panel Title</h3>
  <p>Panel content</p>
</div>
```

### 4.2 Input Components

```html
<!-- Text Input -->
<oj-input-text label-hint="Name" value="{{ $variables.name }}"
  placeholder="Enter name" required="true"/>

<!-- Number Input -->
<oj-input-number label-hint="Amount" value="{{ $variables.amount }}"
  min="0" max="999999" step="0.01"
  converter='{"type":"number","options":{"style":"currency","currency":"USD"}}'/>

<!-- Date Picker -->
<oj-input-date label-hint="Start Date" value="{{ $variables.startDate }}"
  min="2024-01-01" max="2025-12-31"/>

<!-- Date-Time Picker -->
<oj-input-date-time label-hint="Meeting Time" value="{{ $variables.meetingTime }}"/>

<!-- Select (Single) -->
<oj-select-single label-hint="Status" value="{{ $variables.status }}"
  data="{{ $variables.statusListSDP }}"
  item-text="label" value-attribute="value"/>

<!-- Combobox (with search) -->
<oj-combobox-one label-hint="Customer" value="{{ $variables.customerId }}"
  options="{{ $variables.customerList }}"
  options-keys='{"value":"id","label":"name"}'/>

<!-- Multi-select -->
<oj-select-many label-hint="Tags" value="{{ $variables.selectedTags }}"
  options="{{ $variables.tagOptions }}"/>

<!-- Checkbox -->
<oj-checkboxset label-hint="Preferences" value="{{ $variables.preferences }}">
  <oj-option value="email">Email Notifications</oj-option>
  <oj-option value="sms">SMS Alerts</oj-option>
</oj-checkboxset>

<!-- Switch (Toggle) -->
<oj-switch label-hint="Active" value="{{ $variables.isActive }}"/>

<!-- Text Area -->
<oj-text-area label-hint="Notes" value="{{ $variables.notes }}"
  rows="4" max-length="2000"/>

<!-- File Picker -->
<oj-file-picker accept="image/*,.pdf" selection-mode="multiple"
  on-oj-select="{{ $listeners.fileSelected }}">
  <oj-button slot="trigger">Upload Files</oj-button>
</oj-file-picker>

<!-- Radio Buttons -->
<oj-radioset label-hint="Priority" value="{{ $variables.priority }}">
  <oj-option value="high">High</oj-option>
  <oj-option value="medium">Medium</oj-option>
  <oj-option value="low">Low</oj-option>
</oj-radioset>
```

### 4.3 Display Components

```html
<!-- Formatted Text -->
<oj-bind-text value="[[ $variables.description ]]"/>

<!-- Avatar -->
<oj-avatar initials="JD" size="md" background="orange"/>

<!-- Badge -->
<oj-badge>NEW</oj-badge>

<!-- Progress Bar -->
<oj-progress-bar value="{{ $variables.progress }}" max="100"/>

<!-- Progress Circle -->
<oj-progress-circle value="{{ $variables.completion }}" max="100"/>

<!-- Status Meter Gauge -->
<oj-status-meter-gauge value="75" min="0" max="100"
  thresholds='[{"max":33,"color":"red"},{"max":66,"color":"yellow"},{"max":100,"color":"green"}]'/>

<!-- Rating Gauge -->
<oj-rating-gauge value="{{ $variables.rating }}" max="5" step="0.5"/>

<!-- Tag Cloud -->
<oj-tag-cloud data="{{ $variables.tagCloudData }}"/>
```

### 4.4 Collection Components

```html
<!-- Table -->
<oj-table id="ordersTable"
  data="{{ $variables.ordersListSDP }}"
  columns='[
    {"headerText":"Order #","field":"orderNumber","sortable":"enabled"},
    {"headerText":"Customer","field":"customerName","sortable":"enabled"},
    {"headerText":"Amount","field":"amount","sortable":"enabled",
     "template":"amountTemplate"},
    {"headerText":"Status","field":"status","sortable":"enabled"},
    {"headerText":"Date","field":"orderDate","sortable":"enabled"}
  ]'
  selection-mode='{"row":"single"}'
  on-first-selected-row-changed="[[ $listeners.tableRowSelected ]]"
  scroll-policy="loadMoreOnScroll"
  scroll-policy-options='{"fetchSize":25}'>

  <!-- Custom column template -->
  <template slot="amountTemplate" data-oj-as="cell">
    <oj-bind-text value="[[ '$' + cell.data.toFixed(2) ]]"/>
  </template>
</oj-table>

<!-- List View -->
<oj-list-view data="{{ $variables.itemsSDP }}"
  selection-mode="single"
  on-first-selected-item-changed="[[ $listeners.itemSelected ]]">
  <template slot="itemTemplate" data-oj-as="item">
    <oj-list-item-layout>
      <span slot="leading">
        <oj-avatar initials="[[ item.data.initials ]]" size="sm"/>
      </span>
      <span><oj-bind-text value="[[ item.data.name ]]"/></span>
      <span slot="secondary">
        <oj-bind-text value="[[ item.data.email ]]"/>
      </span>
      <span slot="metadata">
        <oj-bind-text value="[[ item.data.role ]]"/>
      </span>
    </oj-list-item-layout>
  </template>
</oj-list-view>

<!-- Tree View -->
<oj-tree-view data="{{ $variables.treeDataProvider }}">
  <template slot="itemTemplate" data-oj-as="item">
    <span><oj-bind-text value="[[ item.data.name ]]"/></span>
  </template>
</oj-tree-view>
```

### 4.5 Navigation Components

```html
<!-- Tab Bar -->
<oj-tab-bar edge="top" data="{{ $variables.tabsDP }}"
  selection="{{ $variables.selectedTab }}">
  <template slot="itemTemplate" data-oj-as="item">
    <span><oj-bind-text value="[[ item.data.label ]]"/></span>
  </template>
</oj-tab-bar>

<!-- Navigation List (sidebar) -->
<oj-navigation-list edge="start" data="{{ $variables.navItems }}"
  selection="{{ $variables.selectedNav }}">
  <template slot="itemTemplate" data-oj-as="item">
    <li>
      <a href="#">
        <span class="oj-navigationlist-item-icon" :class="[[ item.data.icon ]]"/>
        <oj-bind-text value="[[ item.data.label ]]"/>
      </a>
    </li>
  </template>
</oj-navigation-list>

<!-- Train (Step Progress) -->
<oj-train selected-step="{{ $variables.currentStep }}"
  steps='[
    {"id":"step1","label":"Basic Info"},
    {"id":"step2","label":"Details"},
    {"id":"step3","label":"Review"},
    {"id":"step4","label":"Submit"}
  ]'/>
```

### 4.6 Charts & Visualization

```html
<!-- Bar Chart -->
<oj-chart type="bar" data="{{ $variables.salesChartDP }}"
  orientation="vertical" stack="off"
  x-axis='{"title":"Quarter"}' y-axis='{"title":"Revenue ($)"}'>
  <template slot="itemTemplate" data-oj-as="item">
    <oj-chart-item value="[[ item.data.value ]]"
      group-id="[[ [item.data.quarter] ]]"
      series-id="[[ item.data.region ]]"/>
  </template>
</oj-chart>

<!-- Pie Chart -->
<oj-chart type="pie" data="{{ $variables.pieDataDP }}">
  <template slot="itemTemplate" data-oj-as="item">
    <oj-chart-item value="[[ item.data.value ]]"
      group-id="[[ [item.data.category] ]]"/>
  </template>
</oj-chart>

<!-- Line Chart -->
<oj-chart type="line" data="{{ $variables.trendDP }}"
  time-axis-type="enabled"/>

<!-- Gantt Chart -->
<oj-gantt start="2024-01-01" end="2024-12-31"
  major-axis='{"scale":"months"}' minor-axis='{"scale":"weeks"}'
  rows="{{ $variables.ganttRowsDP }}"/>
```

### 4.7 Dialogs & Popups

```html
<!-- Dialog -->
<oj-dialog id="confirmDialog" dialog-title="Confirm Action"
  cancel-behavior="icon"
  style="width:400px;min-height:200px;">
  <div slot="body">
    <p>Are you sure you want to delete this record?</p>
  </div>
  <div slot="footer">
    <oj-button on-oj-action="[[ $listeners.cancelDelete ]]">Cancel</oj-button>
    <oj-button chroming="callToAction"
      on-oj-action="[[ $listeners.confirmDelete ]]">Delete</oj-button>
  </div>
</oj-dialog>

<!-- Open dialog from action chain -->
<!-- Action: Call Component Method → document.getElementById('confirmDialog').open() -->

<!-- Message Toast -->
<!-- Fire from action chain: Fire Notification action -->
<!-- Params: summary="Success", detail="Record saved", type="confirmation" -->
```

---

## 5. Variables & Types

### 5.1 Variable Scopes

| Scope | Lifetime | Access | Use Case |
|-------|----------|--------|----------|
| **Page** | While page is active | Page only | Form inputs, local state |
| **Flow** | While flow is active | All pages in flow | Shared filter, selected item |
| **Application** | App lifetime | All flows/pages | User info, global config |

### 5.2 Variable Types

```json
// Built-in types
"string"       // Text
"number"       // Integer or decimal
"boolean"      // true/false
"object"       // Key-value pairs
"array"        // List of items
"any"          // Dynamic type

// Custom type definition
{
  "types": {
    "Order": {
      "type": "object",
      "properties": {
        "id": { "type": "number" },
        "orderNumber": { "type": "string" },
        "customerName": { "type": "string" },
        "amount": { "type": "number" },
        "status": { "type": "string" },
        "lineItems": {
          "type": "array",
          "items": { "$ref": "#/types/LineItem" }
        }
      }
    },
    "LineItem": {
      "type": "object",
      "properties": {
        "productId": { "type": "number" },
        "quantity": { "type": "number" },
        "unitPrice": { "type": "number" }
      }
    }
  }
}
```

### 5.3 System Variables

```
$page.variables.xxx         // Page variable
$flow.variables.xxx         // Flow variable
$application.variables.xxx  // App variable
$page.constants.xxx         // Page constant

$chain.variables.xxx        // Action chain variable
$chain.results.xxx          // Action chain results

$variables                  // Shorthand for current scope variables
$constants                  // Shorthand for current scope constants

$application.user           // Current user info
$application.user.userId    // User ID
$application.user.email     // User email
$application.user.roles     // User roles array

$application.path.app       // App base URL
$responsive.smUp            // Responsive breakpoint (boolean)
$responsive.mdUp
$responsive.lgUp
$responsive.xlUp
```

### 5.4 Expression Syntax

```
// Read-only expression (one-way binding)
[[ $variables.name ]]
[[ $variables.amount > 0 ? 'Credit' : 'Debit' ]]
[[ $variables.items.length ]]
[[ $variables.firstName + ' ' + $variables.lastName ]]

// Two-way binding (writeable)
{{ $variables.name }}
{{ $variables.formData.email }}

// Iterator current item
[[ $current.data.fieldName ]]
[[ $current.index ]]
```

---

## 6. Action Chains

### 6.1 Overview

Action chains are visual sequences of actions triggered by events (button click, page load, value change, etc.).

### 6.2 Built-in Actions

#### Assign Variables
```json
{
  "assignVariables": {
    "module": "vb/action/builtin/assignVariablesAction",
    "parameters": {
      "$page.variables.status": { "source": "ACTIVE" },
      "$page.variables.count": { "source": "{{ $page.variables.count + 1 }}" },
      "$page.variables.order": { "source": { "id": 1, "name": "Test" } }
    }
  }
}
```

#### Call REST Endpoint
```json
{
  "callRest": {
    "module": "vb/action/builtin/restAction",
    "parameters": {
      "endpoint": "businessObjects/getall_Order",
      "uriParams": { "Order_Id": "{{ $page.variables.orderId }}" }
    },
    "outcomes": {
      "success": "handleSuccess",
      "failure": "handleError"
    }
  }
}
```

#### Navigate
```json
{
  "navigate": {
    "module": "vb/action/builtin/navigateToPageAction",
    "parameters": {
      "page": "main-order-detail",
      "params": {
        "orderId": "{{ $variables.selectedId }}",
        "mode": "edit"
      }
    }
  }
}
```

#### Navigate Back
```json
{
  "goBack": {
    "module": "vb/action/builtin/navigateBackAction",
    "parameters": {}
  }
}
```

#### Call Module Function
```json
{
  "callFunction": {
    "module": "vb/action/builtin/callModuleFunctionAction",
    "parameters": {
      "module": "{{ $page.functions }}",
      "functionName": "calculateTotal",
      "params": ["{{ $variables.lineItems }}"]
    },
    "outcomes": {
      "success": "assignResult"
    }
  }
}
```

#### Fire Event
```json
{
  "fireEvent": {
    "module": "vb/action/builtin/fireCustomEventAction",
    "parameters": {
      "name": "orderCreated",
      "payload": { "orderId": "{{ $variables.newOrderId }}" }
    }
  }
}
```

#### Fire Notification (Toast)
```json
{
  "showNotification": {
    "module": "vb/action/builtin/fireNotificationEventAction",
    "parameters": {
      "summary": "Success",
      "message": "Order saved successfully",
      "displayMode": "transient",
      "type": "confirmation"
    }
  }
}
// Types: confirmation, info, warning, error
```

#### If / Else
```json
{
  "ifAction": {
    "module": "vb/action/builtin/ifAction",
    "parameters": {
      "condition": "{{ $variables.amount > 10000 }}"
    },
    "outcomes": {
      "true": "requireApproval",
      "false": "autoApprove"
    }
  }
}
```

#### For Each
```json
{
  "forEach": {
    "module": "vb/action/builtin/forEachAction",
    "parameters": {
      "items": "{{ $variables.lineItems }}"
    },
    "actions": {
      "processItem": {
        "module": "vb/action/builtin/callModuleFunctionAction",
        "parameters": {
          "module": "{{ $page.functions }}",
          "functionName": "processLine",
          "params": ["{{ $current.data }}"]
        }
      }
    }
  }
}
```

#### Try / Catch
```json
{
  "tryCatch": {
    "try": "callRestEndpoint",
    "catch": [
      {
        "error": "application:error",
        "actions": "handleError"
      }
    ],
    "finally": "cleanup"
  }
}
```

#### Call Component Method
```json
{
  "openDialog": {
    "module": "vb/action/builtin/callComponentMethodAction",
    "parameters": {
      "component": "{{ document.getElementById('confirmDialog') }}",
      "method": "open"
    }
  }
}
```

### 6.3 Event Listeners

```json
{
  "eventListeners": {
    "vbEnter": { "chains": [{ "chainId": "loadPageData" }] },
    "vbBeforeExit": { "chains": [{ "chainId": "checkUnsavedChanges" }] },
    "buttonClicked": { "chains": [{ "chainId": "submitForm" }] },
    "valueChanged": { "chains": [{ "chainId": "onStatusChange" }] },
    "tableRowSelected": { "chains": [{ "chainId": "loadOrderDetail" }] }
  }
}
```

---

## 7. Service Connections

### 7.1 Creating a Service Connection

**From OpenAPI/Swagger:**
```
1. Services → + Service Connection → Define by Specification
2. Provide URL: https://api.example.com/swagger.json
3. Select endpoints to include
4. Configure authentication
```

**From Endpoint:**
```
1. Services → + Service Connection → Define by Endpoint
2. Base URL: https://api.example.com/v1
3. Add endpoints manually:
   - GET /orders
   - POST /orders
   - GET /orders/{id}
   - PUT /orders/{id}
   - DELETE /orders/{id}
4. Define request/response schemas
```

### 7.2 Oracle SaaS Service Connections

```
Backend: Oracle Fusion Applications
  - Select from catalog of pre-built SaaS endpoints
  - Catalog URL: https://erp-instance.fa.oraclecloud.com

Endpoints automatically available:
  - /fscmRestApi/resources/11.13.18.05/invoices
  - /fscmRestApi/resources/11.13.18.05/purchaseOrders
  - /fscmRestApi/resources/11.13.18.05/suppliers
  - /hcmRestApi/resources/11.13.18.05/workers
  etc.
```

### 7.3 Authentication Types

```
1. None (public APIs)
2. Basic Authentication (username/password)
3. OAuth 2.0:
   - Client Credentials
   - Authorization Code
   - Resource Owner Password
4. Oracle Cloud Account (for SaaS)
5. OCI Signature (for OCI services)
6. Custom Headers (API Key, tokens)
```

### 7.4 Headers & Parameters

```json
// Custom headers per backend
{
  "headers": {
    "X-API-Key": "your-api-key",
    "Accept": "application/json",
    "REST-Framework-Version": "4"
  },
  "queryParameters": {
    "onlyData": "true",
    "totalResults": "true"
  }
}
```

---

## 8. Business Objects

### 8.1 Creating Business Objects

```
Data → + Business Object

Name: Order
Fields:
  - id (Number, auto-increment, PK)
  - orderNumber (String, required, unique)
  - customerName (String, required)
  - orderDate (Date, default=today)
  - amount (Number, min=0)
  - status (String, default="DRAFT")
  - createdBy (String)
  - lastUpdated (DateTime)
```

### 8.2 Relationships

```
One-to-Many:
  Order (1) ──── OrderLine (Many)
  OrderLine has FK: orderId → Order.id

Many-to-Many:
  Order (Many) ──── Product (Many)
  via junction: OrderProduct (orderId, productId)
```

### 8.3 Business Object REST APIs

VBCS auto-generates REST endpoints for each Business Object:

```
GET    /Order                  → List all orders
GET    /Order/{id}             → Get single order
POST   /Order                  → Create order
PATCH  /Order/{id}             → Update order
DELETE /Order/{id}             → Delete order
GET    /Order/{id}/child/OrderLine  → Get child records

Query parameters:
  ?q=status='ACTIVE'                     → Filter
  ?orderBy=orderDate:desc                → Sort
  ?limit=25&offset=0                     → Pagination
  ?fields=id,orderNumber,status          → Field selection
  ?totalResults=true                     → Include count
  ?expand=OrderLine                      → Include children
```

### 8.4 Query Syntax (q parameter)

```
// Equals
?q=status='ACTIVE'

// Not equals
?q=status!='DRAFT'

// Greater than / Less than
?q=amount>1000
?q=orderDate<'2024-06-01'

// LIKE (contains)
?q=customerName LIKE '%Oracle%'

// AND / OR
?q=status='ACTIVE' AND amount>500
?q=status='ACTIVE' OR status='PENDING'

// IN
?q=status IN ('ACTIVE','PENDING','APPROVED')

// IS NULL / IS NOT NULL
?q=completedDate IS NULL

// Combined complex query
?q=status='ACTIVE' AND amount>1000 AND customerName LIKE '%Corp%'
```

### 8.5 Business Rules

```
Field-level rules:
  - Required fields
  - Min/Max values
  - Pattern validation (regex)
  - Unique constraints

Object Functions (server-side JS):
  - beforeInsert trigger
  - afterInsert trigger
  - beforeUpdate trigger
  - afterUpdate trigger
  - beforeDelete trigger
  - Custom validation function
```

### 8.6 Object Function Example

```javascript
// Object function: validate before insert
function beforeInsert(record) {
  // Auto-generate order number
  if (!record.orderNumber) {
    record.orderNumber = 'ORD-' + Date.now();
  }

  // Set audit fields
  record.createdBy = context.user.userId;
  record.lastUpdated = new Date().toISOString();

  // Validate amount
  if (record.amount < 0) {
    throw new Error('Amount cannot be negative');
  }

  return record;
}
```

---

## 9. JavaScript in VBCS

### 9.1 Page Module Functions

```javascript
// app/flows/main/pages/main-orders-page.js
define([], function() {
  'use strict';

  var PageModule = function PageModule() {};

  /**
   * Calculate total for line items
   */
  PageModule.prototype.calculateTotal = function(lineItems) {
    if (!lineItems || !Array.isArray(lineItems)) return 0;
    return lineItems.reduce(function(sum, item) {
      return sum + (item.quantity * item.unitPrice);
    }, 0);
  };

  /**
   * Format currency
   */
  PageModule.prototype.formatCurrency = function(amount, currency) {
    currency = currency || 'USD';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency
    }).format(amount);
  };

  /**
   * Format date for display
   */
  PageModule.prototype.formatDate = function(dateStr) {
    if (!dateStr) return '';
    var d = new Date(dateStr);
    return d.toLocaleDateString('en-US', {
      year: 'numeric', month: 'short', day: 'numeric'
    });
  };

  /**
   * Generate unique ID
   */
  PageModule.prototype.generateId = function(prefix) {
    prefix = prefix || 'ID';
    return prefix + '-' + Date.now() + '-' + Math.random().toString(36).substr(2, 9);
  };

  /**
   * Validate email format
   */
  PageModule.prototype.isValidEmail = function(email) {
    var pattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return pattern.test(email);
  };

  /**
   * Download data as CSV
   */
  PageModule.prototype.downloadCSV = function(data, fileName) {
    if (!data || data.length === 0) return;

    var headers = Object.keys(data[0]);
    var csvRows = [headers.join(',')];

    data.forEach(function(row) {
      var values = headers.map(function(h) {
        var val = (row[h] !== null && row[h] !== undefined) ? String(row[h]) : '';
        return '"' + val.replace(/"/g, '""') + '"';
      });
      csvRows.push(values.join(','));
    });

    var csvContent = csvRows.join('\n');
    var blob = new Blob([csvContent], { type: 'text/csv' });
    var url = URL.createObjectURL(blob);
    var a = document.createElement('a');
    a.href = url;
    a.download = fileName || 'export.csv';
    a.click();
    URL.revokeObjectURL(url);
  };

  return PageModule;
});
```

### 9.2 Flow Module Functions

```javascript
// Shared across all pages in the flow
define([], function() {
  'use strict';

  var FlowModule = function FlowModule() {};

  FlowModule.prototype.sharedFormatter = function(value) {
    // Available to all pages in this flow
    return value ? value.toUpperCase() : '';
  };

  return FlowModule;
});
```

### 9.3 Application Module Functions

```javascript
// Global functions available everywhere
define([], function() {
  'use strict';

  var AppModule = function AppModule() {};

  AppModule.prototype.getEnvironment = function() {
    var host = window.location.hostname;
    if (host.includes('-test')) return 'TEST';
    if (host.includes('-dev')) return 'DEV';
    return 'PROD';
  };

  AppModule.prototype.getCurrentUser = function() {
    // Access user info
    return this.application.getCurrentUser();
  };

  return AppModule;
});
```

### 9.4 Using Third-Party Libraries

```javascript
// In requirejs config or page module
define(['https://cdn.jsdelivr.net/npm/lodash@4/lodash.min.js'], function(_) {
  'use strict';

  var PageModule = function PageModule() {};

  PageModule.prototype.processData = function(data) {
    return _.groupBy(data, 'category');
  };

  return PageModule;
});
```

---

## 10. Data Binding

### 10.1 Service Data Provider (SDP)

SDP fetches data from REST endpoints:

```json
// Variable definition
{
  "ordersListSDP": {
    "type": "vb/ServiceDataProvider",
    "defaultValue": {
      "endpoint": "businessObjects/getall_Order",
      "keyAttributes": "id",
      "itemsPath": "items",
      "responsePath": "",
      "uriParameters": {
        "limit": 25
      },
      "filterCriterion": {
        "op": "$and",
        "criteria": [
          { "op": "$eq", "attribute": "status", "value": "{{ $variables.filterStatus }}" }
        ]
      },
      "sortCriteria": [
        { "attribute": "orderDate", "direction": "descending" }
      ]
    }
  }
}
```

### 10.2 Array Data Provider (ADP)

ADP wraps an array variable for table/list binding:

```json
{
  "localItemsADP": {
    "type": "vb/ArrayDataProvider",
    "defaultValue": {
      "data": "{{ $variables.localItems }}",
      "keyAttributes": "id"
    }
  }
}
```

### 10.3 Binding Syntax

```html
<!-- One-way (read-only) -->
<oj-bind-text value="[[ $variables.name ]]"/>
<div class="[[ $variables.isActive ? 'active' : 'inactive' ]]">

<!-- Two-way (read/write) -->
<oj-input-text value="{{ $variables.name }}"/>

<!-- Conditional rendering -->
<oj-bind-if test="[[ $variables.showDetails ]]">
  <div>Details here...</div>
</oj-bind-if>

<!-- Loop rendering -->
<oj-bind-for-each data="[[ $variables.items ]]">
  <template>
    <div>
      <oj-bind-text value="[[ $current.data.name ]]"/>
      <oj-bind-text value="[[ $current.index ]]"/>
    </div>
  </template>
</oj-bind-for-each>
```

### 10.4 Master-Detail Pattern

```html
<!-- Master: Table -->
<oj-table data="{{ $variables.ordersSDP }}"
  on-first-selected-row-changed="[[ $listeners.orderSelected ]]"
  selection-mode='{"row":"single"}'/>

<!-- Detail: Form (bound to selectedOrder variable) -->
<oj-form-layout>
  <oj-input-text label-hint="Order #"
    value="[[ $variables.selectedOrder.orderNumber ]]" readonly="true"/>
  <oj-input-text label-hint="Customer"
    value="[[ $variables.selectedOrder.customerName ]]" readonly="true"/>
  <oj-input-number label-hint="Amount"
    value="[[ $variables.selectedOrder.amount ]]" readonly="true"/>
</oj-form-layout>
```

**Action chain for master-detail:**
```json
// orderSelected action chain
{
  "assignSelectedOrder": {
    "module": "vb/action/builtin/assignVariablesAction",
    "parameters": {
      "$page.variables.selectedOrder": {
        "source": "{{ $chain.variables.rowData }}"
      }
    }
  }
}
```

---

## 11. Forms & Validation

### 11.1 Form Creation Pattern

```html
<oj-validation-group id="orderForm">
  <oj-form-layout max-columns="2">
    <oj-input-text label-hint="Order Number" required="true"
      value="{{ $variables.formData.orderNumber }}"
      validators='[{"type":"regExp","options":{"pattern":"^ORD-[0-9]+$",
        "messageDetail":"Must match ORD-XXXX format"}}]'/>

    <oj-input-text label-hint="Customer Name" required="true"
      value="{{ $variables.formData.customerName }}"/>

    <oj-input-number label-hint="Amount" required="true"
      value="{{ $variables.formData.amount }}"
      min="0.01" max="9999999.99"
      validators='[{"type":"numberRange","options":{
        "min":0.01,"max":9999999.99,
        "messageDetail":{"rangeOverflow":"Max $9,999,999.99"}
      }}]'/>

    <oj-input-date label-hint="Order Date" required="true"
      value="{{ $variables.formData.orderDate }}"
      min="{{ $variables.minDate }}"/>

    <oj-select-single label-hint="Status" required="true"
      value="{{ $variables.formData.status }}"
      data="{{ $variables.statusListSDP }}"/>

    <oj-text-area label-hint="Notes"
      value="{{ $variables.formData.notes }}"
      max-length="500"/>
  </oj-form-layout>
</oj-validation-group>

<oj-button chroming="callToAction"
  on-oj-action="[[ $listeners.submitForm ]]">Save Order</oj-button>
```

### 11.2 Form Validation in Action Chain

```javascript
// submitForm action chain
// Step 1: Validate form
{
  "callValidate": {
    "module": "vb/action/builtin/callComponentMethodAction",
    "parameters": {
      "component": "{{ document.getElementById('orderForm') }}",
      "method": "showMessages",
      "params": []
    }
  },
  "callIsValid": {
    "module": "vb/action/builtin/callComponentMethodAction",
    "parameters": {
      "component": "{{ document.getElementById('orderForm') }}",
      "method": "valid",
      "params": []
    },
    "outcomes": {
      "success": "checkValid"
    }
  },
  "checkValid": {
    "module": "vb/action/builtin/ifAction",
    "parameters": {
      "condition": "{{ $chain.results.callIsValid === 'valid' }}"
    },
    "outcomes": {
      "true": "saveOrder",
      "false": "showError"
    }
  }
}
```

---

## 12. CSS & Theming

### 12.1 Redwood Theme

VBCS uses Oracle's Redwood theme (based on Oracle JET).

```css
/* Override Redwood variables */
:root {
  --oj-core-text-color-primary: #1a1a2e;
  --oj-core-bg-color-primary: #ffffff;
  --oj-button-calltoaction-bg-color: #0572CE;
  --oj-button-calltoaction-bg-color-hover: #045DAD;
  --oj-core-danger-color: #D43B2A;
  --oj-core-success-color: #0A7B3E;
}
```

### 12.2 Application CSS

```css
/* app.css - Global styles */

/* Card layout */
.custom-card {
  background: var(--oj-core-bg-color-primary);
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  padding: 16px;
  margin: 8px;
}

/* Status badges */
.status-active {
  color: #0A7B3E;
  background: #E6F4EA;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.status-inactive {
  color: #D43B2A;
  background: #FCE8E6;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

/* Responsive layout */
@media (max-width: 768px) {
  .desktop-only { display: none; }
  .oj-flex { flex-direction: column; }
}

/* Table row hover */
.oj-table-body-row:hover {
  background-color: #f0f5ff;
}

/* Dashboard KPI card */
.kpi-card {
  text-align: center;
  padding: 24px;
}
.kpi-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--oj-button-calltoaction-bg-color);
}
.kpi-label {
  font-size: 14px;
  color: #666;
  margin-top: 8px;
}
```

---

## 13. Security & Roles

### 13.1 Authentication

```
Settings → Security:
  - Require Authentication: Yes/No
  - Identity Provider: IDCS / OCI IAM
  - Default role for authenticated users: Authenticated
```

### 13.2 Roles

```
Built-in Roles:
  - Anonymous: No authentication required
  - Authenticated: Any logged-in user

Custom Roles:
  - Admin: Full CRUD access
  - Manager: Approve, view all
  - User: Create, view own
  - ReadOnly: View only
```

### 13.3 Page-Level Security

```json
// page-settings.json
{
  "security": {
    "access": {
      "requiresAuthentication": true,
      "roles": ["Admin", "Manager"]
    }
  }
}
```

### 13.4 Component-Level Visibility

```html
<!-- Show only for Admin role -->
<oj-bind-if test="[[ $application.user.roles.indexOf('Admin') !== -1 ]]">
  <oj-button on-oj-action="[[ $listeners.deleteRecord ]]">Delete</oj-button>
</oj-bind-if>

<!-- Hide for ReadOnly users -->
<oj-button disabled="[[ $application.user.roles.indexOf('ReadOnly') !== -1 ]]"
  on-oj-action="[[ $listeners.editRecord ]]">Edit</oj-button>
```

### 13.5 Check Roles in JavaScript

```javascript
PageModule.prototype.isAdmin = function() {
  var user = this.application.getCurrentUser();
  return user.roles && user.roles.indexOf('Admin') >= 0;
};

PageModule.prototype.canEdit = function() {
  var user = this.application.getCurrentUser();
  var editRoles = ['Admin', 'Manager', 'Editor'];
  return user.roles && user.roles.some(function(r) {
    return editRoles.indexOf(r) >= 0;
  });
};
```

---

## 14. Deployment & Lifecycle

### 14.1 Development Workflow

```
Design → Preview → Stage → Publish (Live)

Stages:
  1. Development: Design mode, frequent saves
  2. Stage: Deploy for testing (staging URL)
  3. Live: Publish for production (live URL)
```

### 14.2 Versioning

```
Version format: major.minor.patch
  1.0.0 → 1.0.1 (patch: bug fix)
  1.0.1 → 1.1.0 (minor: new feature)
  1.1.0 → 2.0.0 (major: breaking change)

Actions:
  - Create new version (clone current)
  - Switch between versions
  - Delete old versions
```

### 14.3 Import / Export

```
Export: Application → Options → Export
  Creates a .vba file (Visual Builder Archive)

Import: Applications → Import → Upload .vba file
  Can import into same or different instance
```

### 14.4 Visual Builder Studio Integration

```
Git Repository:
  - Branches for features (feature/order-search)
  - Merge requests for code review
  - CI/CD pipeline for automated deployment

Pipeline Steps:
  1. Build (npm install, grunt build)
  2. Test (unit tests)
  3. Package (.vba archive)
  4. Deploy to target instance
```

---

## 15. Integration with Oracle SaaS

### 15.1 ERP Cloud Integration

```
Service Connection:
  - Backend: Oracle Fusion Cloud Applications
  - Instance URL: https://fa-xxxx.oraclecloud.com
  - Auth: Oracle Cloud Account

Common patterns:
  1. CRUD on invoices, POs, suppliers via REST
  2. Display ERP data in custom VBCS dashboard
  3. Custom approval screens using ERP data
  4. File upload → FBDI import via OIC
```

### 15.2 OIC Integration

```
Service Connection:
  - Backend: Custom REST service from OIC
  - Base URL: https://oic-instance/ic/api/integration/v1/flows/rest/...
  - Auth: Basic or OAuth

Use cases:
  - VBCS form → OIC integration → ERP/HCM/SCM
  - Complex orchestrations via OIC, UI via VBCS
  - File upload in VBCS → process in OIC
```

### 15.3 Example: Invoice Dashboard

```
VBCS App Flow:
  1. Page loads → Call REST: GET /invoices?status=PENDING
  2. Display in table with filters
  3. User clicks row → Detail page
  4. User approves → Call REST: POST /invoices/{id}/approve
  5. Show toast notification
  6. Refresh table data
```

---

## 16. Advanced Topics

### 16.1 Dynamic Components

```html
<!-- Dynamic form based on metadata -->
<oj-bind-for-each data="[[ $variables.formFields ]]">
  <template>
    <oj-bind-if test="[[ $current.data.type === 'text' ]]">
      <oj-input-text label-hint="[[ $current.data.label ]]"
        value="{{ $variables.formData[$current.data.fieldName] }}"/>
    </oj-bind-if>
    <oj-bind-if test="[[ $current.data.type === 'number' ]]">
      <oj-input-number label-hint="[[ $current.data.label ]]"
        value="{{ $variables.formData[$current.data.fieldName] }}"/>
    </oj-bind-if>
    <oj-bind-if test="[[ $current.data.type === 'date' ]]">
      <oj-input-date label-hint="[[ $current.data.label ]]"
        value="{{ $variables.formData[$current.data.fieldName] }}"/>
    </oj-bind-if>
  </template>
</oj-bind-for-each>
```

### 16.2 Offline Support (Mobile)

```json
// persistence configuration
{
  "offline": {
    "enabled": true,
    "syncPolicy": "fetchFirst",
    "conflictResolution": "clientWins",
    "tables": ["Order", "Customer"]
  }
}
```

### 16.3 Internationalization (Translations)

```json
// resources/strings/app/nls/root/app-strings.json
{
  "appTitle": "Meeting Assistant",
  "orders": "Orders",
  "save": "Save",
  "cancel": "Cancel",
  "deleteConfirm": "Are you sure you want to delete?"
}

// resources/strings/app/nls/fr/app-strings.json
{
  "appTitle": "Assistant de Reunion",
  "orders": "Commandes",
  "save": "Sauvegarder",
  "cancel": "Annuler",
  "deleteConfirm": "Etes-vous sur de vouloir supprimer?"
}
```

```html
<!-- Usage in page -->
<oj-bind-text value="[[ $application.translations.app.appTitle ]]"/>
```

### 16.4 PWA Configuration

```json
// manifest.json
{
  "name": "Meeting Assistant",
  "short_name": "MeetAssist",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#ffffff",
  "theme_color": "#0572CE",
  "icons": [
    { "src": "icon-192.png", "sizes": "192x192", "type": "image/png" },
    { "src": "icon-512.png", "sizes": "512x512", "type": "image/png" }
  ]
}
```

---

## 17. Best Practices

### 17.1 Naming Conventions

```
Pages:       flow-purpose (e.g., main-order-list, admin-user-detail)
Variables:   camelCase (e.g., selectedOrderId, filterStatus)
Types:       PascalCase (e.g., OrderDetail, CustomerAddress)
Actions:     verbNoun (e.g., loadOrders, saveCustomer, deleteItem)
Functions:   camelCase (e.g., calculateTotal, formatDate)
SDP/ADP:     entityActionSDP (e.g., ordersListSDP, customerSearchADP)
```

### 17.2 Architecture Patterns

- Use flows to group related pages (orders flow, admin flow)
- Share common data at flow level, page-specific at page level
- Use app-level variables for user session data
- Create reusable fragments for repeated UI patterns
- Centralize REST calls in service connections (avoid hardcoded URLs)

### 17.3 Performance Tips

- Use pagination (`scroll-policy="loadMoreOnScroll"`) for large tables
- Limit fields returned from REST (`$fields` parameter)
- Minimize the number of SDP variables per page
- Use `$expand` judiciously (only when child data is needed)
- Lazy-load tab content (load data on tab select, not page load)
- Debounce search inputs before firing REST calls

### 17.4 Security Guidelines

- Always require authentication for apps with data
- Use role-based page access, not just component visibility
- Validate inputs server-side (Business Object rules)
- Never store sensitive data in client-side variables
- Use OAuth for external service connections
- Audit user actions for critical operations

---

## 18. Troubleshooting

### 18.1 Common Issues

| Issue | Cause | Solution |
|-------|-------|----------|
| Page blank after publish | JS error on load | Check browser console, fix JS syntax |
| SDP not loading data | Auth or endpoint issue | Test service connection, check credentials |
| Two-way binding not working | Used `[[ ]]` instead of `{{ }}` | Use `{{ }}` for writeable bindings |
| Table not refreshing | SDP cache | Fire `vbDataProviderNotification` event |
| Action chain not firing | Event listener not wired | Check event listener in page JSON |
| 401 errors on REST calls | Token expired or wrong auth | Verify backend credentials |
| Mobile app won't install | PWA manifest error | Validate manifest.json, check HTTPS |
| Custom CSS not applying | Specificity issue | Use more specific selectors, check `!important` |
| Variable undefined | Wrong scope | Check scope (page vs flow vs app) |
| Performance slow | Too many REST calls | Batch calls, add pagination, lazy-load |

### 18.2 Debugging Techniques

1. **Browser DevTools Console** - Check for JavaScript errors
2. **Network Tab** - Inspect REST requests/responses
3. **Page JSON Editor** - View/edit page definition directly
4. **Preview Mode** - Test with real data before publishing
5. **Audit Log** - Review user actions and data changes
6. **VB Studio Git** - Compare changes between versions

### 18.3 Refreshing Data

```javascript
// Force refresh SDP in action chain
// Fire Data Provider Event action
{
  "refreshSDP": {
    "module": "vb/action/builtin/fireDataProviderEventAction",
    "parameters": {
      "target": "{{ $page.variables.ordersListSDP }}",
      "refresh": true
    }
  }
}
```

---

*This guide covers Oracle Visual Builder Cloud Service comprehensively. For the latest updates, refer to [Oracle VBCS Documentation](https://docs.oracle.com/en/cloud/paas/app-builder-cloud/).*
