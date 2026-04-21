# Spring Boot Microservices Integration Guide

## Overview

This Angular frontend is configured to work with Spring Boot microservices architecture using Spring Cloud Gateway.

## Architecture

```
┌─────────────────┐
│  Angular App    │
│  (Port 4200)    │
└────────┬────────┘
         │
         ▼
┌─────────────────────────┐
│  Spring Cloud Gateway   │
│  (Port 8080)            │
└────────┬────────────────┘
         │
         ├──────────────────────────────┐
         │                              │
         ▼                              ▼
┌──────────────────┐         ┌──────────────────┐
│ Products Service │         │  Cart Service    │
│  (Port 8081)     │         │  (Port 8082)     │
└──────────────────┘         └──────────────────┘
         │                              │
         ▼                              ▼
┌──────────────────┐         ┌──────────────────┐
│ Orders Service   │         │  Auth Service    │
│  (Port 8083)     │         │  (Port 8084)     │
└──────────────────┘         └──────────────────┘
         │
         ▼
┌──────────────────┐
│  Users Service   │
│  (Port 8085)     │
└──────────────────┘
```

## Configuration

### 1. Environment Configuration

**Development** (`src/environments/environment.ts`):
```typescript
export const environment = {
  production: false,
  apiGateway: 'http://localhost:8080',
  services: {
    products: 'http://localhost:8080/products-service',
    cart: 'http://localhost:8080/cart-service',
    orders: 'http://localhost:8080/orders-service',
    auth: 'http://localhost:8080/auth-service',
    users: 'http://localhost:8080/users-service'
  }
};
```

**Production** (`src/environments/environment.prod.ts`):
```typescript
export const environment = {
  production: true,
  apiGateway: 'https://api.yourdomain.com',
  services: {
    products: 'https://api.yourdomain.com/products-service',
    cart: 'https://api.yourdomain.com/cart-service',
    orders: 'https://api.yourdomain.com/orders-service',
    auth: 'https://api.yourdomain.com/auth-service',
    users: 'https://api.yourdomain.com/users-service'
  }
};
```

### 2. Spring Cloud Gateway Configuration

Create `application.yml` in your Gateway service:

```yaml
spring:
  cloud:
    gateway:
      routes:
        # Products Service
        - id: products-service
          uri: lb://PRODUCTS-SERVICE
          predicates:
            - Path=/products-service/**
          filters:
            - StripPrefix=1

        # Cart Service
        - id: cart-service
          uri: lb://CART-SERVICE
          predicates:
            - Path=/cart-service/**
          filters:
            - StripPrefix=1

        # Orders Service
        - id: orders-service
          uri: lb://ORDERS-SERVICE
          predicates:
            - Path=/orders-service/**
          filters:
            - StripPrefix=1

        # Auth Service
        - id: auth-service
          uri: lb://AUTH-SERVICE
          predicates:
            - Path=/auth-service/**
          filters:
            - StripPrefix=1

        # Users Service
        - id: users-service
          uri: lb://USERS-SERVICE
          predicates:
            - Path=/users-service/**
          filters:
            - StripPrefix=1

      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "http://localhost:4200"
              - "https://yourdomain.com"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
              - OPTIONS
            allowedHeaders:
              - "*"
            allowCredentials: true
            maxAge: 3600

server:
  port: 8080
```

### 3. Spring Security Configuration

Add CORS configuration in your Spring Security:

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter(), 
                UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "https://yourdomain.com"
        ));
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

## API Endpoints

### Products Service

```
GET    /products-service/api/products              - Get all products (paginated)
GET    /products-service/api/products/{id}         - Get product by ID
GET    /products-service/api/products/category/{category} - Get products by category
GET    /products-service/api/products/search       - Search products
GET    /products-service/api/categories            - Get all categories
```

### Cart Service

```
GET    /cart-service/api/cart                      - Get user's cart
POST   /cart-service/api/cart/items                - Add item to cart
PUT    /cart-service/api/cart/items/{id}           - Update cart item
DELETE /cart-service/api/cart/items/{id}           - Remove item from cart
DELETE /cart-service/api/cart                      - Clear cart
```

### Orders Service

```
GET    /orders-service/api/orders                  - Get user's orders (paginated)
GET    /orders-service/api/orders/{id}             - Get order by ID
POST   /orders-service/api/orders                  - Create new order
PUT    /orders-service/api/orders/{id}/cancel      - Cancel order
```

### Auth Service

```
POST   /auth-service/api/auth/login                - User login
POST   /auth-service/api/auth/register             - User registration
POST   /auth-service/api/auth/logout               - User logout
POST   /auth-service/api/auth/refresh              - Refresh JWT token
```

### Users Service

```
GET    /users-service/api/users/profile            - Get user profile
PUT    /users-service/api/users/profile            - Update user profile
PUT    /users-service/api/users/password           - Change password
```

## JWT Authentication

### Request Flow

1. User logs in via `/auth-service/api/auth/login`
2. Backend returns JWT token
3. Angular stores token in localStorage
4. Auth interceptor adds token to all requests
5. Spring Security validates token

### Spring JWT Filter Example

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String userId = tokenProvider.getUserIdFromToken(jwt);
                
                UserDetails userDetails = userDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication", ex);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

## Running the Application

### Development Mode

1. **Start Spring Boot microservices:**
```bash
# Start each service
cd products-service && mvn spring-boot:run
cd cart-service && mvn spring-boot:run
cd orders-service && mvn spring-boot:run
cd auth-service && mvn spring-boot:run
cd users-service && mvn spring-boot:run
cd gateway-service && mvn spring-boot:run
```

2. **Start Angular frontend:**
```bash
npm start
```

The proxy configuration will forward API calls to Spring Cloud Gateway.

### Production Mode

1. **Build Angular:**
```bash
npm run build:prod
```

2. **Deploy to Spring Boot:**
   - Copy `dist/easytobuy/browser/*` to `src/main/resources/static/` in your Gateway service
   - Spring Boot will serve the Angular app

## Docker Deployment

### Build Images

```bash
# Build Angular image
docker build -t easytobuy-frontend .

# Build Spring services
cd products-service && mvn clean package
docker build -t products-service .
```

### Run with Docker Compose

```bash
docker-compose up -d
```

## Common Issues

### CORS Errors

Make sure:
1. Spring Cloud Gateway has CORS configured
2. Each microservice has CORS enabled
3. Angular proxy is configured correctly

### 401 Unauthorized

Check:
1. JWT token is being sent in Authorization header
2. Token is not expired
3. Spring Security is configured correctly

### Connection Refused

Verify:
1. All Spring Boot services are running
2. Ports are correct in environment files
3. Spring Cloud Gateway is routing correctly

## Testing

### Test API Connection

```typescript
// In Angular component
constructor(private apiService: ApiService) {
  this.apiService.getProducts().subscribe(
    data => console.log('Products:', data),
    error => console.error('Error:', error)
  );
}
```

### Test Authentication

```typescript
this.apiService.login('user@example.com', 'password').subscribe(
  response => {
    localStorage.setItem('auth_token', response.token);
    console.log('Login successful');
  },
  error => console.error('Login failed:', error)
);
```

## Next Steps

1. Copy this frontend to your microservices `frontend/` folder
2. Update environment files with your actual service URLs
3. Configure Spring Cloud Gateway routes
4. Enable CORS in Spring Security
5. Test the integration
6. Deploy!
