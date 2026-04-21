import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MockDataService } from '../../services/mock-data.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="min-h-screen py-16 bg-surface">
      <div class="max-w-7xl mx-auto px-4 sm:px-6">
        <h1 class="font-display text-3xl font-bold text-foreground mb-8">My Dashboard</h1>
        
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div class="bg-card rounded-2xl border border-border p-6">
            <p class="text-sm text-muted-foreground mb-2">Total Orders</p>
            <p class="text-3xl font-bold text-foreground">{{orders.length}}</p>
          </div>
          <div class="bg-card rounded-2xl border border-border p-6">
            <p class="text-sm text-muted-foreground mb-2">Total Spent</p>
            <p class="text-3xl font-bold text-foreground">\${{totalSpent.toFixed(2)}}</p>
          </div>
          <div class="bg-card rounded-2xl border border-border p-6">
            <p class="text-sm text-muted-foreground mb-2">Active Orders</p>
            <p class="text-3xl font-bold text-foreground">{{activeOrders}}</p>
          </div>
        </div>

        <div class="bg-card rounded-2xl border border-border p-6">
          <h2 class="font-semibold text-foreground text-xl mb-6">Recent Orders</h2>
          <div class="space-y-4">
            <div *ngFor="let order of orders" class="flex items-center gap-4 p-4 bg-surface rounded-xl">
              <img [src]="order.image" [alt]="order.id" class="w-16 h-16 object-cover rounded-lg" />
              <div class="flex-1">
                <p class="font-semibold text-foreground">{{order.id}}</p>
                <p class="text-sm text-muted-foreground">{{order.date}} • {{order.items}} items</p>
              </div>
              <div class="text-right">
                <p class="font-bold text-foreground">\${{order.total.toFixed(2)}}</p>
                <span [class]="'text-xs px-2 py-1 rounded-full ' + getStatusClass(order.status)">
                  {{order.status}}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent {
  orders = this.mockData.ORDERS;
  totalSpent = this.orders.reduce((sum, order) => sum + order.total, 0);
  activeOrders = this.orders.filter(o => o.status === 'shipped' || o.status === 'processing').length;

  constructor(private mockData: MockDataService) {}

  getStatusClass(status: string): string {
    const classes: Record<string, string> = {
      delivered: 'bg-green-100 text-green-700',
      shipped: 'bg-blue-100 text-blue-700',
      processing: 'bg-yellow-100 text-yellow-700',
      cancelled: 'bg-red-100 text-red-700'
    };
    return classes[status] || '';
  }
}
