import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { Observable } from 'rxjs';
import { CartItem } from '../../models/product.model';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="min-h-screen py-16 bg-surface">
      <div class="max-w-7xl mx-auto px-4 sm:px-6">
        <h1 class="font-display text-3xl font-bold text-foreground mb-8">Shopping Cart</h1>
        
        <div *ngIf="(items$ | async) as items" class="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <div class="lg:col-span-2 space-y-4">
            <div *ngFor="let item of items" class="bg-card rounded-2xl border border-border p-6 flex gap-6">
              <img [src]="item.product.image" [alt]="item.product.name" class="w-24 h-24 object-cover rounded-xl" />
              <div class="flex-1">
                <h3 class="font-semibold text-foreground">{{item.product.name}}</h3>
                <p class="text-sm text-muted-foreground mt-1">{{item.product.category}}</p>
                <div class="flex items-center gap-4 mt-4">
                  <div class="flex items-center gap-2">
                    <button (click)="updateQuantity(item.product.id, item.quantity - 1)" class="w-8 h-8 rounded-lg bg-surface border border-border flex items-center justify-center hover:bg-accent">-</button>
                    <span class="w-12 text-center font-semibold">{{item.quantity}}</span>
                    <button (click)="updateQuantity(item.product.id, item.quantity + 1)" class="w-8 h-8 rounded-lg bg-surface border border-border flex items-center justify-center hover:bg-accent">+</button>
                  </div>
                  <span class="text-lg font-bold text-foreground ml-auto">\${{(item.product.price * item.quantity).toFixed(2)}}</span>
                </div>
              </div>
              <button (click)="removeItem(item.product.id)" class="text-muted-foreground hover:text-red-500">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <path d="M18 6 6 18"/><path d="m6 6 12 12"/>
                </svg>
              </button>
            </div>
            <div *ngIf="items.length === 0" class="text-center py-12">
              <p class="text-muted-foreground mb-4">Your cart is empty</p>
              <a routerLink="/products" class="inline-flex items-center gap-2 bg-primary text-primary-foreground font-semibold px-6 py-3 rounded-xl hover:bg-[#0284c7] transition-colors">
                Continue Shopping
              </a>
            </div>
          </div>
          
          <div class="bg-card rounded-2xl border border-border p-6 h-fit">
            <h2 class="font-semibold text-foreground text-lg mb-4">Order Summary</h2>
            <div class="space-y-3 mb-6">
              <div class="flex justify-between text-sm">
                <span class="text-muted-foreground">Subtotal</span>
                <span class="font-semibold">\${{(totalPrice$ | async)?.toFixed(2)}}</span>
              </div>
              <div class="flex justify-between text-sm">
                <span class="text-muted-foreground">Shipping</span>
                <span class="font-semibold">Free</span>
              </div>
              <div class="border-t border-border pt-3 flex justify-between">
                <span class="font-semibold">Total</span>
                <span class="text-xl font-bold text-primary">\${{(totalPrice$ | async)?.toFixed(2)}}</span>
              </div>
            </div>
            <button class="w-full bg-primary text-primary-foreground font-semibold py-3 rounded-xl hover:bg-[#0284c7] transition-colors">
              Proceed to Checkout
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class CartComponent {
  items$: Observable<CartItem[]> = this.cartService.items$;
  totalPrice$ = this.cartService.totalPrice$;

  constructor(private cartService: CartService) {}

  updateQuantity(productId: number, quantity: number): void {
    this.cartService.updateQuantity(productId, quantity);
  }

  removeItem(productId: number): void {
    this.cartService.removeFromCart(productId);
  }
}
