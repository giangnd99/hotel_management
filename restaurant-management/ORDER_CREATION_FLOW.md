# 🍽️ Restaurant Order Creation Flow

## 📋 Overview

This document describes the complete order creation flow for the Restaurant Management System, including request format, validation, processing, and response.

## 🚀 Order Creation Flow

### **1. Request Format**

```http
POST http://localhost:8082/api/restaurant/orders
Content-Type: application/json
```

**Request Body:**
```json
{
  "id": "O004",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "tableId": "T001",
  "items": [
    {
      "menuItemId": "M001",
      "quantity": 2,
      "price": 85000
    },
    {
      "menuItemId": "M002", 
      "quantity": 1,
      "price": 275000
    }
  ],
  "status": "NEW",
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": "No onions please, serve quickly",
  "orderNumber": "R-1004"
}
```

### **2. Flow Architecture**

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controller    │───▶│   Use Case       │───▶│   Handler       │───▶│   Repository    │
│                 │    │                  │    │                 │    │                 │
│ OrderController │    │ OrderUseCaseImpl │    │ OrderHandlerImpl│    │ OrderRepository │
└─────────────────┘    └──────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │                       │
         ▼                       ▼                       ▼                       ▼
   Request DTO              Domain Entity           Validation              Database
   OrderDTO                 Order                  & Business              Save
                                                      Logic
```

### **3. Processing Steps**

#### **Step 1: Controller Layer**
- **File**: `OrderController.java`
- **Method**: `createOrder(@RequestBody @Valid OrderDTO request)`
- **Action**: Receives HTTP request, validates DTO, calls use case

#### **Step 2: Use Case Layer**
- **File**: `OrderUseCaseImpl.java`
- **Method**: `createOrder(OrderDTO request)`
- **Action**: Maps DTO to domain entity, calls handler

#### **Step 3: Handler Layer**
- **File**: `OrderHandlerImpl.java`
- **Method**: `createOrder(Order order)`
- **Action**: Validates order, sets status to NEW, saves to repository

#### **Step 4: Repository Layer**
- **File**: `OrderRepositoryAdapter.java`
- **Method**: `save(Order order)`
- **Action**: Persists order to database

### **4. Validation Rules**

#### **Order Validation**
- ✅ Order ID cannot be null or empty
- ✅ Customer ID cannot be null or empty
- ✅ Table ID cannot be null or empty
- ✅ Order must have at least one item
- ✅ Order status will be set to "NEW" automatically

#### **Order Item Validation**
- ✅ Menu item ID cannot be null or empty
- ✅ Quantity must be greater than 0
- ✅ Price must be greater than 0

#### **Customer Validation**
- ✅ Customer ID must exist in mock data:
  - `11111111-1111-1111-1111-111111111111` (Nguyen Van A)
  - `22222222-2222-2222-2222-222222222222` (Tran Thi B)

### **5. Expected Success Response**

**HTTP Status**: `201 Created`

**Response Body:**
```json
{
  "id": "O004",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "tableId": "T001",
  "items": [
    {
      "menuItemId": "M001",
      "quantity": 2,
      "price": 85000
    },
    {
      "menuItemId": "M002",
      "quantity": 1,
      "price": 275000
    }
  ],
  "status": "NEW",
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": "No onions please, serve quickly",
  "orderNumber": "R-1004"
}
```

### **6. Error Responses**

#### **Validation Error (400 Bad Request)**
```json
{
  "timestamp": "2025-01-15T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Order must have at least one item",
  "path": "/api/restaurant/orders"
}
```

#### **Customer Not Found (404 Not Found)**
```json
{
  "timestamp": "2025-01-15T14:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found: invalid-customer-id",
  "path": "/api/restaurant/orders"
}
```

#### **Menu Item Not Found (500 Internal Server Error)**
If you get this error:
```
java.lang.RuntimeException: Menu item not found: 68306759
```

**Solution:**
Use the **correct menu item IDs** from the database. The system now uses fixed UUIDs.

**❌ WRONG IDs (will cause errors):**
- `68306759` - Invalid ID
- `991894631` - Invalid ID

**✅ CORRECT IDs (use these):**
- `M001` - Caesar Salad (85,000 VND)
- `M002` - Grilled Salmon (275,000 VND)
- `M003` - Chocolate Cake (95,000 VND)
- `M004` - Fresh Orange Juice (35,000 VND)

### **7. Available Data for Testing**

#### **Customers (Mock Data)**
```json
{
  "11111111-1111-1111-1111-111111111111": {
    "firstName": "Nguyen",
    "lastName": "Van A",
    "level": "BRONZE",
    "active": true
  },
  "22222222-2222-2222-2222-222222222222": {
    "firstName": "Tran",
    "lastName": "Thi B", 
    "level": "SILVER",
    "active": true
  }
}
```

#### **Menu Items (Database)**
**Available Menu Items:**
- Caesar Salad - 85,000 VND (ID: `M001`)
- Grilled Salmon - 275,000 VND (ID: `M002`)  
- Chocolate Cake - 95,000 VND (ID: `M003`)
- Fresh Orange Juice - 35,000 VND (ID: `M004`)

**Available Tables:**
- Table 1 - AVAILABLE (ID: `T001`)
- Table 2 - AVAILABLE (ID: `T002`)
- Table 3 - RESERVED (ID: `T003`)
- Table 4 - OCCUPIED (ID: `T004`)
- Table 5 - AVAILABLE (ID: `T005`)

### **8. Testing Commands**

#### **Create Order (PowerShell)**
```powershell
$body = @{
    id = "O004"
    customerId = "11111111-1111-1111-1111-111111111111"
    tableId = "T001"
    items = @(
        @{
            menuItemId = "M001"  # Caesar Salad
            quantity = 2
            price = 85000
        },
        @{
            menuItemId = "M002"  # Grilled Salmon
            quantity = 1
            price = 275000
        }
    )
    status = "NEW"
    createdAt = "2025-01-15T14:30:00"
    customerNote = "No onions please, serve quickly"
    orderNumber = "R-1004"
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8082/api/restaurant/orders" -Method POST -Body $body -ContentType "application/json"
```

#### **Create Order (cURL)**
```bash
curl -X POST http://localhost:8082/api/restaurant/orders \
  -H "Content-Type: application/json" \
  -d '{
    "id": "O004",
    "customerId": "11111111-1111-1111-1111-111111111111",
    "tableId": "T001",
    "items": [
      {
        "menuItemId": "M001",
        "quantity": 2,
        "price": 85000
      },
      {
        "menuItemId": "M002",
        "quantity": 1,
        "price": 275000
      }
    ],
    "status": "NEW",
    "createdAt": "2025-01-15T14:30:00",
    "customerNote": "No onions please, serve quickly",
    "orderNumber": "R-1004"
  }'
```

### **9. Order Status Flow**

```
NEW ──▶ IN_PROGRESS ──▶ COMPLETED
 │           │
 │           └──▶ CANCELLED
 └──▶ CANCELLED
```

- **NEW**: Order created, waiting to be processed
- **IN_PROGRESS**: Order is being prepared
- **COMPLETED**: Order is ready/delivered
- **CANCELLED**: Order has been cancelled

### **10. Logging**

The system logs all order creation activities:

```
INFO  - Creating new order: order_004
INFO  - Order created successfully: order_004 with status: NEW
INFO  - Order saved to database: order_004
```

## 🎯 Summary

The order creation flow is now **simplified and robust**:

1. **✅ Validation**: Comprehensive input validation
2. **✅ Status Management**: Automatic status setting
3. **✅ Error Handling**: Clear error messages
4. **✅ Logging**: Complete audit trail
5. **✅ Mock Data**: Ready-to-use test data
6. **✅ Clean Architecture**: Proper separation of concerns

**⚠️ IMPORTANT**: Use the **correct menu item IDs** from the database. The system now uses short string IDs (M001, M002, etc.) and VND prices instead of USD.

The system will successfully create orders with the provided request format and return the expected response with status "NEW"! 🎉
