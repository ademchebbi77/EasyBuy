import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private http: HttpClient) {}

  // Products Service
  getProducts(page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${environment.services.products}/api/products`, { params });
  }

  getProductById(id: number): Observable<any> {
    return this.http.get(`${environment.services.products}/api/products/${id}`);
  }

  getProductsByCategory(category: string, page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${environment.services.products}/api/products/category/${category}`, { params });
  }

  searchProducts(query: string, page: number = 0, size: number = 20): Observable<any> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${environment.services.products}/api/products/search`, { params });
  }

  // Cart Service
  getCart(): Observable<any> {
    return this.http.get(`${environment.services.cart}/api/cart`);
  }

  addToCart(productId: number, quantity: number): Observable<any> {
    return this.http.post(`${environment.services.cart}/api/cart/items`, { productId, quantity });
  }

  updateCartItem(itemId: number, quantity: number): Observable<any> {
    return this.http.put(`${environment.services.cart}/api/cart/items/${itemId}`, { quantity });
  }

  removeFromCart(itemId: number): Observable<any> {
    return this.http.delete(`${environment.services.cart}/api/cart/items/${itemId}`);
  }

  clearCart(): Observable<any> {
    return this.http.delete(`${environment.services.cart}/api/cart`);
  }

  // Orders Service
  getOrders(page: number = 0, size: number = 10): Observable<any> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get(`${environment.services.orders}/api/orders`, { params });
  }

  getOrderById(id: string): Observable<any> {
    return this.http.get(`${environment.services.orders}/api/orders/${id}`);
  }

  createOrder(orderData: any): Observable<any> {
    return this.http.post(`${environment.services.orders}/api/orders`, orderData);
  }

  cancelOrder(id: string): Observable<any> {
    return this.http.put(`${environment.services.orders}/api/orders/${id}/cancel`, {});
  }

  // Auth Service (Spring Security)
  login(email: string, password: string): Observable<any> {
    return this.http.post(`${environment.services.auth}/api/auth/login`, { email, password });
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${environment.services.auth}/api/auth/register`, userData);
  }

  logout(): Observable<any> {
    return this.http.post(`${environment.services.auth}/api/auth/logout`, {});
  }

  refreshToken(): Observable<any> {
    return this.http.post(`${environment.services.auth}/api/auth/refresh`, {});
  }

  // Users Service
  getUserProfile(): Observable<any> {
    return this.http.get(`${environment.services.users}/api/users/profile`);
  }

  updateUserProfile(userData: any): Observable<any> {
    return this.http.put(`${environment.services.users}/api/users/profile`, userData);
  }

  changePassword(oldPassword: string, newPassword: string): Observable<any> {
    return this.http.put(`${environment.services.users}/api/users/password`, { oldPassword, newPassword });
  }

  // Categories
  getCategories(): Observable<any> {
    return this.http.get(`${environment.services.products}/api/categories`);
  }
}
