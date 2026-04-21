# 🧪 Microservices Direct Port Testing Guide

## 📋 Service Ports Overview

| Service | Port | Database |
|---------|------|----------|
| User Service | 8081 | MySQL (easybuy_db) |
| Order Service | 8082 | MySQL (commande_db) |
| Product Service | 8083 | MySQL (product_db) |
| Category Service | 8084 | H2 (file-based) |
| Review Service | 8085 | H2 (in-memory) |
| Payment Service | 8086 | MongoDB Atlas |
| API Gateway | 8087 | N/A |
| Eureka Server | 8761 | N/A |
| Config Server | 8888 | N/A |
| Keycloak | 8080 | H2 (embedded) |

---

## 🔐 OAuth 2.0 Token Setup

### Get Access Token from Keycloak:
```bash
curl -X POST http://localhost:8080/realms/EcommerceRealm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=gateway" \
  -d "client_secret=xLrWX0ulXElEl4UR9J5RMt5B21G4a2uz"
```

**Response:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "expires_in": 300,
  "token_type": "Bearer"
}
```

**Copy the `access_token` value for testing!**

---

## 🧪 Testing Each Service

---

## 1️⃣ USER SERVICE (Port 8081)

### ❌ Without Auth (Should return 401)
```bash
curl -X GET http://localhost:8081/api/users
```

**Expected:** `401 Unauthorized`

### ✅ With Auth (Should return 200)
```bash
curl -X GET http://localhost:8081/api/users \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Expected:** `200 OK` with user list

### Additional Endpoints to Test:
```bash
# Get user by ID
curl -X GET http://localhost:8081/api/users/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Create user
curl -X POST http://localhost:8081/api/users \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

---

## 2️⃣ ORDER SERVICE (Port 8082)

### ❌ Without Auth (Should return 401)
```bash
curl -X GET http://localhost:8082/api/orders
```

**Expected:** `401 Unauthorized`

### ✅ With Auth (Should return 200)
```bash
curl -X GET http://localhost:8082/api/orders \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Expected:** `200 OK` with order list

### Additional Endpoints to Test:
```bash
# Get order by ID
curl -X GET http://localhost:8082/api/orders/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Create order
curl -X POST http://localhost:8082/api/orders \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 2,
    "items": [
      {"productId": 1, "quantity": 2}
    ]
  }'
```

---

## 3️⃣ PRODUCT SERVICE (Port 8083)

### ❌ Without Auth (Should return 401)
```bash
curl -X GET http://localhost:8083/api/products
```

**Expected:** `401 Unauthorized`

### ✅ With Auth (Should return 200)
```bash
curl -X GET http://localhost:8083/api/products \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Expected:** `200 OK` with product list

### Additional Endpoints to Test:
```bash
# Get product by ID
curl -X GET http://localhost:8083/api/products/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Create product
curl -X POST http://localhost:8083/api/products \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "price": 99.99,
    "stock": 100
  }'
```

---

## 4️⃣ CATEGORY SERVICE (Port 8084)

### ❌ Without Auth (Should return 401)
```bash
curl -X GET http://localhost:8084/api/categories
```

**Expected:** `401 Unauthorized`

### ✅ With Auth (Should return 200)
```bash
curl -X GET http://localhost:8084/api/categories \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Expected:** `200 OK` with category list

### Additional Endpoints to Test:
```bash
# Get category by ID
curl -X GET http://localhost:8084/api/categories/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Create category
curl -X POST http://localhost:8084/api/categories \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic products"
  }'
```

---

## 5️⃣ REVIEW SERVICE (Port 8085)

### ❌ Without Auth (Should return 401)
```bash
curl -X GET http://localhost:8085/api/reviews
```

**Expected:** `401 Unauthorized`

### ✅ With Auth (Should return 200)
```bash
curl -X GET http://localhost:8085/api/reviews \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Expected:** `200 OK` with review list

### Additional Endpoints to Test:
```bash
# Get reviews by product ID
curl -X GET http://localhost:8085/api/reviews/product/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Create review
curl -X POST http://localhost:8085/api/reviews \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "userId": 2,
    "rating": 5,
    "comment": "Great product!"
  }'
```

---

## 6️⃣ PAYMENT SERVICE (Port 8086)

### ❌ Without Auth (Should return 401)
```bash
curl -X GET http://localhost:8086/api/payments
```

**Expected:** `401 Unauthorized`

### ✅ With Auth (Should return 200)
```bash
curl -X GET http://localhost:8086/api/payments \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

**Expected:** `200 OK` with payment list

### Additional Endpoints to Test:
```bash
# Get payment by order ID (auto-creates if not exists)
curl -X GET http://localhost:8086/api/payments/order/22 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Process payment
curl -X POST http://localhost:8086/api/payments/69e789b843b41c44c14ea68a/process \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Retry failed payment
curl -X POST http://localhost:8086/api/payments/69e789b843b41c44c14ea68a/retry \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"

# Refund payment
curl -X POST http://localhost:8086/api/payments/69e789b843b41c44c14ea68a/refund \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

---

## 📝 Testing Checklist

### For Each Service:
- [ ] **User Service (8081)**
  - [ ] ❌ GET without auth → 401
  - [ ] ✅ GET with auth → 200
  
- [ ] **Order Service (8082)**
  - [ ] ❌ GET without auth → 401
  - [ ] ✅ GET with auth → 200
  
- [ ] **Product Service (8083)**
  - [ ] ❌ GET without auth → 401
  - [ ] ✅ GET with auth → 200
  
- [ ] **Category Service (8084)**
  - [ ] ❌ GET without auth → 401
  - [ ] ✅ GET with auth → 200
  
- [ ] **Review Service (8085)**
  - [ ] ❌ GET without auth → 401
  - [ ] ✅ GET with auth → 200
  
- [ ] **Payment Service (8086)**
  - [ ] ❌ GET without auth → 401
  - [ ] ✅ GET with auth → 200

---

## 🚀 Quick Test Script

Save this as `test-all-services.sh`:

```bash
#!/bin/bash

# Get OAuth token
echo "🔐 Getting OAuth 2.0 token..."
TOKEN_RESPONSE=$(curl -s -X POST http://localhost:8080/realms/EcommerceRealm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=gateway" \
  -d "client_secret=xLrWX0ulXElEl4UR9J5RMt5B21G4a2uz")

TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"access_token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
  echo "❌ Failed to get token!"
  exit 1
fi

echo "✅ Token obtained!"
echo ""

# Test each service
services=("8081:User" "8082:Order" "8083:Product" "8084:Category" "8085:Review" "8086:Payment")

for service in "${services[@]}"; do
  PORT="${service%%:*}"
  NAME="${service##*:}"
  
  echo "🧪 Testing $NAME Service (Port $PORT)..."
  
  # Without auth
  echo "  ❌ Without auth:"
  curl -s -o /dev/null -w "    Status: %{http_code}\n" http://localhost:$PORT/api/${NAME,,}s
  
  # With auth
  echo "  ✅ With auth:"
  curl -s -o /dev/null -w "    Status: %{http_code}\n" \
    -H "Authorization: Bearer $TOKEN" \
    http://localhost:$PORT/api/${NAME,,}s
  
  echo ""
done

echo "✅ All tests completed!"
```

Run with: `bash test-all-services.sh`

---

## 🎯 Expected Results Summary

| Service | Without Auth | With Auth |
|---------|--------------|-----------|
| User (8081) | 401 | 200 |
| Order (8082) | 401 | 200 |
| Product (8083) | 401 | 200 |
| Category (8084) | 401 | 200 |
| Review (8085) | 401 | 200 |
| Payment (8086) | 401 | 200 |

---

## 🔧 Troubleshooting

### If you get 401 with valid token:
1. Check Keycloak is running: `http://localhost:8080`
2. Verify token hasn't expired (5 min lifetime)
3. Check service logs for JWT validation errors
4. Ensure issuer-uri is correct in config-server

### If you get connection refused:
1. Check service is running
2. Verify port is correct
3. Check Eureka dashboard: `http://localhost:8761`

### If you get 500 errors:
1. Check service logs
2. Verify database connections
3. Check RabbitMQ is running (for order/payment)

---

## ✅ SUCCESS CRITERIA

All 6 microservices should:
- ✅ Return **401 Unauthorized** without OAuth 2.0 token
- ✅ Return **200 OK** with valid OAuth 2.0 token
- ✅ Be registered in Eureka
- ✅ Load config from Config Server
- ✅ Validate JWT from Keycloak

---

**🔥 YOU'RE COOKING WITH FIRE! LET'S TEST EVERYTHING! 🔥**
