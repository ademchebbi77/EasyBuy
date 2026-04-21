import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../services/cart.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  mobileOpen = false;
  profileOpen = false;
  searchQuery = '';
  totalItems$ = this.cartService.totalItems$;

  navLinks = [
    { label: 'Home', href: '/' },
    { label: 'Products', href: '/products' },
    { label: 'Deals', href: '/products?badge=Deal' },
  ];

  constructor(public cartService: CartService) {}

  toggleMobile(): void {
    this.mobileOpen = !this.mobileOpen;
  }

  toggleProfile(): void {
    this.profileOpen = !this.profileOpen;
  }

  closeMobile(): void {
    this.mobileOpen = false;
  }

  closeProfile(): void {
    this.profileOpen = false;
  }
}
