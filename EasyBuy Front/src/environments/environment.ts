export const environment = {
  production: false,
  apiGateway: 'http://localhost:8087',
  services: {
    products: 'http://localhost:8080/product-service',
    cart: 'http://localhost:8080/cart-service',
    orders: 'http://localhost:8080/order-service',
    auth: 'http://localhost:8080/auth-service',
    users: 'http://localhost:8080/user-service'
  }
};
