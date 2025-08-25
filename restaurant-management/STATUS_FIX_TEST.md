# Status Fix Test Guide

## Issue Fixed
- **Problem**: Order O002 was showing as "NEW" instead of "IN_PROGRESS" from database
- **Root Cause**: OrderEntityMapper.toDomain() was not setting the status from JPA entity
- **Solution**: Added status mapping in OrderEntityMapper.toDomain()

## Test Steps

### 1. Verify O002 Status is Correct
```bash
curl -X GET http://localhost:8082/api/restaurant/orders/O002
```

**Expected Response:**
```json
{
  "id": "O002",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "tableId": "T002",
  "items": [
    {
      "menuItemId": "M002",
      "quantity": 1,
      "price": 275000
    }
  ],
  "status": "IN_PROGRESS",  // Should now show IN_PROGRESS instead of NEW
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": "Extra napkins",
  "orderNumber": "R-1002"
}
```

### 2. Test Complete Order (IN_PROGRESS → COMPLETED)
```bash
curl -X PUT http://localhost:8082/api/restaurant/orders/O002/complete
```

**Expected Response:**
```json
{
  "id": "O002",
  "customerId": "11111111-1111-1111-1111-111111111111",
  "tableId": "T002",
  "items": [
    {
      "menuItemId": "M002",
      "quantity": 1,
      "price": 275000
    }
  ],
  "status": "COMPLETED",  // Should now be COMPLETED
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": "Extra napkins",
  "orderNumber": "R-1002"
}
```

### 3. Test Generic Status Update
```bash
curl -X PUT "http://localhost:8082/api/restaurant/orders/O003/status?status=IN_PROGRESS"
```

**Expected Response:**
```json
{
  "id": "O003",
  "customerId": "22222222-2222-2222-2222-222222222222",
  "tableId": "T003",
  "items": [
    {
      "menuItemId": "M003",
      "quantity": 1,
      "price": 95000
    }
  ],
  "status": "IN_PROGRESS",  // Should be updated to IN_PROGRESS
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": null,
  "orderNumber": "R-1003"
}
```

### 4. Test Cancel Order
```bash
curl -X PUT http://localhost:8082/api/restaurant/orders/O003/cancel
```

**Expected Response:**
```json
{
  "id": "O003",
  "customerId": "22222222-2222-2222-2222-222222222222",
  "tableId": "T003",
  "items": [
    {
      "menuItemId": "M003",
      "quantity": 1,
      "price": 95000
    }
  ],
  "status": "CANCELLED",  // Should be CANCELLED
  "createdAt": "2025-01-15T14:30:00",
  "customerNote": null,
  "orderNumber": "R-1003"
}
```

## PowerShell Commands

### Get Order Status:
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/restaurant/orders/O002" -Method GET
```

### Complete Order:
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/restaurant/orders/O002/complete" -Method PUT
```

### Generic Status Update:
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/restaurant/orders/O003/status?status=IN_PROGRESS" -Method PUT
```

### Cancel Order:
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/restaurant/orders/O003/cancel" -Method PUT
```

## Changes Made

### 1. Fixed OrderEntityMapper.toDomain()
- Added status mapping from JPA entity to domain entity
- Now correctly preserves the database status

### 2. Improved Status Transition Validation
- Allow setting the same status (no-op)
- Allow COMPLETED → CANCELLED for refund scenarios
- Allow CANCELLED → NEW for reactivation scenarios

## Expected Results

After the fix:
- ✅ O002 should show as "IN_PROGRESS" instead of "NEW"
- ✅ Complete order should work without 500 error
- ✅ All status transitions should work correctly
- ✅ Generic status updates should work
- ✅ Cancel operations should work

## Database Status Reference

| Order ID | Expected Status | Description |
|----------|----------------|-------------|
| O001 | COMPLETED | Order completed successfully |
| O002 | IN_PROGRESS | Order being processed |
| O003 | NEW | New order waiting to be processed |
| O004 | CANCELLED | Order cancelled by customer |
