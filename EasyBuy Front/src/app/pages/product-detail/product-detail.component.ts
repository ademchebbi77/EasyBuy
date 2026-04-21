import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MockDataService } from '../../services/mock-data.service';
import { CartService } from '../../services/cart.service';
import { RatingStarsComponent } from '../../shared/rating-stars/rating-stars.component';
import { Product } from '../../models/product.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, RatingStarsComponent],
  template: `
    <div *ngIf="product" class="min-h-screen py-16 bg-surface">
      <div class="max-w-7xl mx-auto px-4 sm:px-6">
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-12">
          <div class="bg-white rounded-2xl overflow-hidden">
            <img [src]="product.image" [alt]="product.name" class="w-full h-96 object-cover" />
          </div>
          <div class="flex flex-col gap-6">
            <div>
              <span class="text-sm text-muted-foreground uppercase tracking-wide">{{product.category}}</span>
              <h1 class="font-display text-3xl font-bold text-foreground mt-2">{{product.name}}</h1>
            </div>
            <div class="flex items-center gap-3">
              <app-rating-stars [rating]="product.rating"></app-rating-stars>
              <span class="text-sm text-muted-foreground">({{product.reviews}} reviews)</span>
            </div>
            <div class="flex items-center gap-3">
              <span class="text-3xl font-bold text-foreground">\${{product.price.toFixed(2)}}</span>
              <span *ngIf="product.originalPrice" class="text-xl text-muted-foreground line-through">\${{product.originalPrice.toFixed(2)}}</span>
            </div>
            <p class="text-foreground leading-relaxed">{{product.description}}</p>
            <button (click)="addToCart()" [disabled]="!product.inStock" class="bg-primary text-primary-foreground font-semibold px-8 py-3 rounded-xl hover:bg-[#0284c7] transition-colors disabled:bg-muted disabled:text-muted-foreground disabled:cursor-not-allowed">
              {{product.inStock ? 'Add to Cart' : 'Out of Stock'}}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ProductDetailComponent implements OnInit {
  product: Product | undefined;

  constructor(
    private route: ActivatedRoute,
    private mockData: MockDataService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.product = this.mockData.PRODUCTS.find(p => p.id === id);
  }

  addToCart(): void {
    if (this.product) {
      this.cartService.addToCart(this.product);
    }
  }
}
