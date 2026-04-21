# EasyToBuy - E-Commerce Application

A modern e-commerce application built with Angular 18, designed to integrate with Spring Boot microservices architecture.

## Features

- 🛍️ Product catalog with filtering
- 🛒 Shopping cart functionality
- 📦 Product detail pages
- 👤 User authentication (JWT)
- 📊 User dashboard with order history
- 📱 Fully responsive design
- 🎨 Modern UI with Tailwind CSS
- 🔐 Spring Security integration
- 🌐 Spring Cloud Gateway ready

## Tech Stack

### Frontend
- **Angular 18** - Standalone components
- **TypeScript** - Type-safe development
- **Tailwind CSS** - Utility-first styling
- **RxJS** - Reactive state management
- **Angular Router** - Client-side routing

### Backend Integration
- **Spring Boot** - Microservices
- **Spring Cloud Gateway** - API Gateway
- **Spring Security** - JWT Authentication
- **RESTful APIs** - HTTP communication

## Getting Started

### Prerequisites

- Node.js 18 or higher
- npm or yarn
- Spring Boot microservices running (optional for development)

### Installation

1. Install dependencies:
```bash
npm install
```

2. Start the development server:
```bash
npm start
```

3. Open your browser to `http://localhost:4200`

## Integration with Spring Boot

This frontend is configured to work with Spring Boot microservices. See detailed guides:

- **[SPRING-INTEGRATION.md](SPRING-INTEGRATION.md)** - Complete Spring Boot integration guide
- **[COPY-TO-MICROSERVICES.md](COPY-TO-MICROSERVICES.md)** - How to add to your microservices project

### Quick Integration

1. Copy this project to your microservices `frontend/` folder
2. Update `src/environments/environment.ts` with your Gateway URL
3. Configure CORS in Spring Cloud Gateway
4. Run `npm start` to connect to your backend

## Available Scripts

- `npm start` - Start development server with proxy to Spring Boot
- `npm run build` - Build for production
- `npm run build:prod` - Build optimized for production
- `npm run watch` - Build and watch for changes
- `npm test` - Run unit tests

## Project Structure

```
src/
├── app/
│   ├── models/              # TypeScript interfaces
│   ├── services/            # Business logic & API calls
│   │   ├── api.service.ts   # Spring Boot API integration
│   │   ├── cart.service.ts  # Cart state management
│   │   └── mock-data.service.ts
│   ├── interceptors/        # HTTP interceptors
│   │   ├── auth.interceptor.ts    # JWT token injection
│   │   └── error.interceptor.ts   # Error handling
│   ├── shared/              # Reusable components
│   │   ├── navbar/
│   │   ├── footer/
│   │   ├── product-card/
│   │   └── category-card/
│   ├── pages/               # Route components
│   │   ├── home/
│   │   ├── products/
│   │   ├── product-detail/
│   │   ├── cart/
│   │   ├── dashboard/
│   │   └── auth/
│   ├── app.component.ts     # Root component
│   └── app.routes.ts        # Routing configuration
├── environments/            # Environment configs
│   ├── environment.ts       # Development (Spring Boot URLs)
│   └── environment.prod.ts  # Production
├── assets/                  # Static assets
└── styles.css               # Global styles
```

## API Integration

### Environment Configuration

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

### Using API Service

```typescript
import { ApiService } from './services/api.service';

constructor(private apiService: ApiService) {}

// Get products from Spring Boot
this.apiService.getProducts().subscribe(
  data => console.log('Products:', data),
  error => console.error('Error:', error)
);

// Login with JWT
this.apiService.login(email, password).subscribe(
  response => {
    localStorage.setItem('auth_token', response.token);
  }
);
```

## Key Features

### Shopping Cart
- Add/remove products
- Update quantities
- Real-time total calculation
- Persistent cart state
- Syncs with Spring Boot cart service

### Product Catalog
- Browse all products
- Filter by category
- Filter by badges (New, Sale, Best Seller)
- Product search
- Detailed product pages
- Pagination support

### Authentication
- JWT token-based authentication
- Auto token injection via interceptor
- Token refresh support
- Secure route guards
- Spring Security integration

### User Dashboard
- Order history
- Order status tracking
- Account information
- Profile management

## Docker Deployment

### Build Image

```bash
docker build -t easytobuy-frontend .
```

### Run Container

```bash
docker run -p 80:80 easytobuy-frontend
```

### Docker Compose

```bash
docker-compose up -d
```

## Design System

- **Primary Color**: #0ea5e9 (Sky Blue)
- **Fonts**: Inter (body), Plus Jakarta Sans (headings)
- **Responsive Breakpoints**: sm, md, lg, xl
- **Dark Mode**: Supported via CSS variables

## Spring Boot Requirements

Your Spring Boot microservices should expose these endpoints:

- Products: `/products-service/api/products`
- Cart: `/cart-service/api/cart`
- Orders: `/orders-service/api/orders`
- Auth: `/auth-service/api/auth`
- Users: `/users-service/api/users`

See [SPRING-INTEGRATION.md](SPRING-INTEGRATION.md) for complete API documentation.

## License

MIT
