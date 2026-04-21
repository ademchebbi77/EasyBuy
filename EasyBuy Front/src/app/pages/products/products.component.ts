import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MockDataService } from '../../services/mock-data.service';
import { ProductCardComponent } from '../../shared/product-card/product-card.component';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, ProductCardComponent],
  template: `
    <div class="min-h-screen py-16 bg-surface">
      <div class="max-w-7xl mx-auto px-4 sm:px-6">
        <h1 class="font-display text-3xl sm:text-4xl font-bold text-foreground mb-8">Products</h1>
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <app-product-card *ngFor="let product of filteredProducts" [product]="product"></app-product-card>
        </div>
      </div>
    </div>
  `
})
export class ProductsComponent implements OnInit {
  products = this.mockData.PRODUCTS;
  filteredProducts: Product[] = [];

  constructor(
    private mockData: MockDataService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const category = params['category'];
      const badge = params['badge'];
      
      this.filteredProducts = this.products.filter(p => {
        if (category && p.category !== category) return false;
        if (badge && p.badge !== badge) return false;
        return true;
      });

      if (!category && !badge) {
        this.filteredProducts = this.products;
      }
    });
  }
}
