# Copy to Microservices Project

## Quick Steps

### 1. Copy This Folder

Copy this entire Angular project to your microservices `frontend/` folder:

```bash
# From your microservices root directory
cp -r /path/to/EasyBuy-Front ./frontend
```

Or on Windows:
```powershell
xcopy /E /I "C:\path\to\EasyBuy Front" "C:\path\to\your-microservices\frontend"
```

### 2. Update Configuration

Edit `frontend/src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiGateway: 'http://localhost:8080', // Your Spring Cloud Gateway port
  services: {
    products: 'http://localhost:8080/products-service',
    cart: 'http://localhost:8080/cart-service',
    orders: 'http://localhost:8080/orders-service',
    auth: 'http://localhost:8080/auth-service',
    users: 'http://localhost:8080/users-service'
  }
};
```

### 3. Update Proxy Configuration

Edit `frontend/proxy.conf.json` to match your Spring service names:

```json
{
  "/products-service": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  },
  "/cart-service": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
```

### 4. Project Structure

Your microservices project should look like:

```
your-microservices/
├── products-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── cart-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── orders-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── auth-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── users-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── gateway-service/
│   ├── src/
│   ├── pom.xml
│   └── Dockerfile
├── frontend/                    ← Copy Angular project here
│   ├── src/
│   ├── package.json
│   ├── angular.json
│   ├── Dockerfile
│   └── nginx.conf
└── docker-compose.yml
```

### 5. Update Root docker-compose.yml

Add frontend service to your root `docker-compose.yml`:

```yaml
version: '3.8'

services:
  # Your existing Spring services...
  
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: easytobuy-frontend
    ports:
      - "80:80"
    depends_on:
      - gateway-service
    networks:
      - microservices-network

networks:
  microservices-network:
    driver: bridge
```

### 6. Spring Cloud Gateway Configuration

Add to your `gateway-service/src/main/resources/application.yml`:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: products-service
          uri: lb://PRODUCTS-SERVICE
          predicates:
            - Path=/products-service/**
          filters:
            - StripPrefix=1
        
        - id: cart-service
          uri: lb://CART-SERVICE
          predicates:
            - Path=/cart-service/**
          filters:
            - StripPrefix=1
        
        # Add other services...

      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "http://localhost:4200"
              - "http://localhost:80"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
            allowedHeaders: "*"
            allowCredentials: true
```

### 7. Run Everything

**Development:**
```bash
# Terminal 1: Start Spring services
cd products-service && mvn spring-boot:run

# Terminal 2: Start Gateway
cd gateway-service && mvn spring-boot:run

# Terminal 3: Start Angular
cd frontend && npm install && npm start
```

**Production (Docker):**
```bash
# From root directory
docker-compose up --build
```

## What's Already Configured

✅ **HTTP Interceptors** - Automatically adds JWT token to requests
✅ **Error Handling** - Handles 401, 403, 404, 500 errors
✅ **API Service** - Ready-to-use methods for all endpoints
✅ **Environment Config** - Separate dev/prod configurations
✅ **Proxy Config** - Routes API calls through Spring Gateway
✅ **Docker Setup** - Dockerfile and nginx config included
✅ **CORS Ready** - Configured for Spring Boot

## API Endpoints Expected

The Angular app expects these Spring Boot endpoints:

### Products Service
- `GET /products-service/api/products`
- `GET /products-service/api/products/{id}`
- `GET /products-service/api/products/category/{category}`
- `GET /products-service/api/categories`

### Cart Service
- `GET /cart-service/api/cart`
- `POST /cart-service/api/cart/items`
- `PUT /cart-service/api/cart/items/{id}`
- `DELETE /cart-service/api/cart/items/{id}`

### Orders Service
- `GET /orders-service/api/orders`
- `GET /orders-service/api/orders/{id}`
- `POST /orders-service/api/orders`

### Auth Service
- `POST /auth-service/api/auth/login`
- `POST /auth-service/api/auth/register`
- `POST /auth-service/api/auth/logout`

### Users Service
- `GET /users-service/api/users/profile`
- `PUT /users-service/api/users/profile`

## Testing Integration

1. **Start your Spring services**
2. **Start Angular:** `cd frontend && npm start`
3. **Open browser:** `http://localhost:4200`
4. **Check console** for API calls
5. **Verify** data is loading from Spring backend

## Troubleshooting

**Problem:** CORS errors
**Solution:** Enable CORS in Spring Cloud Gateway and each microservice

**Problem:** 401 Unauthorized
**Solution:** Check JWT token configuration in Spring Security

**Problem:** Connection refused
**Solution:** Verify all Spring services are running on correct ports

**Problem:** Routes not found
**Solution:** Check Spring Cloud Gateway route configuration

## Need Help?

See `SPRING-INTEGRATION.md` for detailed Spring Boot configuration examples.
