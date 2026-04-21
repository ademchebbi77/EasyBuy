import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="min-h-screen flex items-center justify-center py-16 bg-surface">
      <div class="w-full max-w-md px-4">
        <div class="bg-card rounded-2xl border border-border p-8">
          <h1 class="font-display text-2xl font-bold text-foreground mb-6 text-center">Sign In</h1>
          <form (submit)="onSubmit($event)" class="space-y-4">
            <div>
              <label class="block text-sm font-medium text-foreground mb-2">Email</label>
              <input type="email" [(ngModel)]="email" name="email" class="w-full px-4 py-2.5 bg-surface border border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/40 focus:border-primary" />
            </div>
            <div>
              <label class="block text-sm font-medium text-foreground mb-2">Password</label>
              <input type="password" [(ngModel)]="password" name="password" class="w-full px-4 py-2.5 bg-surface border border-border rounded-xl focus:outline-none focus:ring-2 focus:ring-primary/40 focus:border-primary" />
            </div>
            <button type="submit" class="w-full bg-primary text-primary-foreground font-semibold py-3 rounded-xl hover:bg-[#0284c7] transition-colors">
              Sign In
            </button>
          </form>
          <p class="text-center text-sm text-muted-foreground mt-6">
            Don't have an account? <a routerLink="/auth/register" class="text-primary font-semibold hover:underline">Sign up</a>
          </p>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  email = '';
  password = '';

  onSubmit(event: Event): void {
    event.preventDefault();
  }
}
