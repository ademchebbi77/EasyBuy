import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-rating-stars',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rating-stars.component.html'
})
export class RatingStarsComponent implements OnInit {
  @Input() rating: number = 0;
  stars: { fill: string }[] = [];

  ngOnInit(): void {
    this.stars = Array.from({ length: 5 }, (_, i) => ({
      fill: i < Math.floor(this.rating) ? 'currentColor' : 'none'
    }));
  }
}
