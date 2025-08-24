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
  "id": "order_004",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "tableId": "40a6c08f-80be-11f0-9513-0242ac150001",
  "items": [
    {
      "menuItemId": "550e8400-e29b-41d4-a716-446655440000",
      "quantity": 2,
      "price": 12.99
    },
    {
      "menuItemId": "550e8400-e29b-41d4-a716-446655440001", 
      "quantity": 1,
      "price": 28.99
    }
  ],
  "status": "NEW",
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": "No onions please, serve quickly",
  "orderNumber": "ORD-1705312200000"
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
  "id": "order_004",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "tableId": "40a6c08f-80be-11f0-9513-0242ac150001",
  "items": [
    {
      "menuItemId": "550e8400-e29b-41d4-a716-446655440000",
      "quantity": 2,
      "price": 12.99
    },
    {
      "menuItemId": "550e8400-e29b-41d4-a716-446655440001",
      "quantity": 1,
      "price": 28.99
    }
  ],
  "status": "NEW",
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": "No onions please, serve quickly",
  "orderNumber": "ORD-1705312200000"
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
- `550e8400-e29b-41d4-a716-446655440000` - Caesar Salad ($12.99)
- `550e8400-e29b-41d4-a716-446655440001` - Grilled Salmon ($28.99)
- `550e8400-e29b-41d4-a716-446655440002` - Chocolate Cake ($8.99)
- `550e8400-e29b-41d4-a716-446655440003` - Fresh Orange Juice ($4.99)

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
- Caesar Salad - $12.99 (ID: `550e8400-e29b-41d4-a716-446655440000`)
- Grilled Salmon - $28.99 (ID: `550e8400-e29b-41d4-a716-446655440001`)  
- Chocolate Cake - $8.99 (ID: `550e8400-e29b-41d4-a716-446655440002`)
- Fresh Orange Juice - $4.99 (ID: `550e8400-e29b-41d4-a716-446655440003`)

**Available Tables:**
- Table 1 - AVAILABLE (ID: `40a6c08f-80be-11f0-9513-0242ac150001`)
- Table 2 - AVAILABLE (ID: `40a6c08f-80be-11f0-9513-0242ac150002`)
- Table 3 - RESERVED (ID: `40a6c08f-80be-11f0-9513-0242ac150003`)
- Table 4 - OCCUPIED (ID: `40a6c08f-80be-11f0-9513-0242ac150004`)
- Table 5 - AVAILABLE (ID: `40a6c08f-80be-11f0-9513-0242ac150005`)

### **8. Testing Commands**

#### **Create Order (PowerShell)**
```powershell
$body = @{
    id = "order_004"
    customerId = "11111111-1111-1111-1111-111111111111"
    tableId = "40a6c08f-80be-11f0-9513-0242ac150001"
    items = @(
        @{
            menuItemId = "550e8400-e29b-41d4-a716-446655440000"  # Caesar Salad
            quantity = 2
            price = 12.99
        },
        @{
            menuItemId = "550e8400-e29b-41d4-a716-446655440001"  # Grilled Salmon
            quantity = 1
            price = 28.99
        }
    )
    status = "NEW"
    createdAt = "2025-01-15T14:30:00"
    customerNote = "No onions please, serve quickly"
    orderNumber = "ORD-1705312200000"
} | ConvertTo-Json -Depth 3

Invoke-RestMethod -Uri "http://localhost:8082/api/restaurant/orders" -Method POST -Body $body -ContentType "application/json"
```

#### **Create Order (cURL)**
```bash
curl -X POST http://localhost:8082/api/restaurant/orders \
  -H "Content-Type: application/json" \
  -d '{
    "id": "order_004",
    "customerId": "11111111-1111-1111-1111-111111111111",
    "tableId": "40a6c08f-80be-11f0-9513-0242ac150001",
    "items": [
      {
        "menuItemId": "550e8400-e29b-41d4-a716-446655440000",
        "quantity": 2,
        "price": 12.99
      },
      {
        "menuItemId": "550e8400-e29b-41d4-a716-446655440001",
        "quantity": 1,
        "price": 28.99
      }
    ],
    "status": "NEW",
    "createdAt": "2025-01-15T14:30:00",
    "customerNote": "No onions please, serve quickly",
    "orderNumber": "ORD-1705312200000"
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

**⚠️ IMPORTANT**: Use the **correct menu item IDs** from the database. The system now uses fixed UUIDs instead of random IDs.

The system will successfully create orders with the provided request format and return the expected response with status "NEW"! 🎉
