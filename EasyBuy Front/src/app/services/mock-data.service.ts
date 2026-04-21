import { Injectable } from '@angular/core';
import { Product, Category, Review, Order } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class MockDataService {
  readonly PRODUCTS: Product[] = [
    {
      id: 1,
      name: 'Sony WH-1000XM5 Wireless Headphones',
      price: 279.99,
      originalPrice: 349.99,
      rating: 4.8,
      reviews: 2847,
      category: 'Electronics',
      image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&q=80',
      badge: 'Best Seller',
      description: 'Industry-leading noise cancellation with two processors and eight microphones. Up to 30-hour battery life with quick charge. Exceptional call quality with auto noise cancellation.',
      inStock: true,
    },
    {
      id: 2,
      name: 'Apple MacBook Air M3 13"',
      price: 1099.00,
      originalPrice: 1299.00,
      rating: 4.9,
      reviews: 5123,
      category: 'Electronics',
      image: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400&q=80',
      badge: 'New',
      description: 'Supercharged by the M3 chip. Up to 18 hours of battery life. Fanless design stays silent. Razor-thin and lightweight.',
      inStock: true,
    },
    {
      id: 3,
      name: 'Nike Air Max 270 React',
      price: 89.99,
      originalPrice: 130.00,
      rating: 4.6,
      reviews: 1923,
      category: 'Fashion',
      image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&q=80',
      badge: 'Sale',
      description: 'Delivers amazing cushioning with a large Air unit and React foam. Street-ready style with a high-fashion look.',
      inStock: true,
    },
    {
      id: 4,
      name: 'Samsung 65" QLED 4K Smart TV',
      price: 799.99,
      originalPrice: 999.99,
      rating: 4.7,
      reviews: 3241,
      category: 'Electronics',
      image: 'https://images.unsplash.com/photo-1593359677879-a4bb92f829e1?w=400&q=80',
      badge: 'Deal',
      description: 'Quantum Dot technology delivers 100% color volume. Neural Quantum Processor 4K with AI upscaling. Built-in smart home hub.',
      inStock: true,
    },
    {
      id: 5,
      name: 'Instant Pot Duo 7-in-1',
      price: 59.99,
      originalPrice: 99.99,
      rating: 4.8,
      reviews: 8712,
      category: 'Home & Kitchen',
      image: 'https://images.unsplash.com/photo-1585515320310-259814833e62?w=400&q=80',
      badge: 'Best Seller',
      description: 'Pressure cooker, slow cooker, rice cooker, steamer, sauté, yogurt maker, and warmer all in one. Cook up to 70% faster.',
      inStock: true,
    },
    {
      id: 6,
      name: "Levi's 501 Original Jeans",
      price: 49.99,
      originalPrice: 79.99,
      rating: 4.5,
      reviews: 4521,
      category: 'Fashion',
      image: 'https://images.unsplash.com/photo-1542272454315-4c01d7abdf4a?w=400&q=80',
      description: "The original straight fit that started it all. Authentic Levi's denim with button fly closure. Classic 5-pocket styling.",
      inStock: true,
    },
    {
      id: 7,
      name: 'Dyson V15 Detect Vacuum',
      price: 649.99,
      originalPrice: 749.99,
      rating: 4.7,
      reviews: 1876,
      category: 'Home & Kitchen',
      image: 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&q=80',
      badge: 'New',
      description: 'Laser reveals microscopic dust. Acoustic piezo sensor counts and measures particles. Up to 60 minutes runtime.',
      inStock: false,
    },
    {
      id: 8,
      name: 'Kindle Paperwhite 11th Gen',
      price: 129.99,
      originalPrice: 159.99,
      rating: 4.9,
      reviews: 6234,
      category: 'Books & Media',
      image: 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400&q=80',
      description: 'The thinnest, lightest Kindle Paperwhite yet. 6.8" display with adjustable warm light. 10 weeks of battery life.',
      inStock: true,
    },
  ];

  readonly CATEGORIES: Category[] = [
    { id: 1, name: 'Electronics', icon: 'Cpu', count: 1240, image: 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=300&q=80' },
    { id: 2, name: 'Fashion', icon: 'Shirt', count: 3200, image: 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=300&q=80' },
    { id: 3, name: 'Home & Kitchen', icon: 'Home', count: 870, image: 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=300&q=80' },
    { id: 4, name: 'Books & Media', icon: 'BookOpen', count: 5400, image: 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=300&q=80' },
    { id: 5, name: 'Sports', icon: 'Dumbbell', count: 620, image: 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=300&q=80' },
    { id: 6, name: 'Beauty', icon: 'Sparkles', count: 940, image: 'https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=300&q=80' },
  ];

  readonly REVIEWS: Review[] = [
    { id: 1, author: 'Sarah M.', rating: 5, date: 'Jan 15, 2025', comment: 'Absolutely love this product! Exceeded all my expectations. The quality is top-notch.', avatar: 'https://i.pravatar.cc/40?img=1' },
    { id: 2, author: 'James K.', rating: 4, date: 'Jan 8, 2025', comment: 'Great value for money. Shipping was fast and packaging was secure. Would recommend!', avatar: 'https://i.pravatar.cc/40?img=3' },
    { id: 3, author: 'Emily R.', rating: 5, date: 'Dec 28, 2024', comment: 'Perfect! Exactly as described. Very happy with my purchase.', avatar: 'https://i.pravatar.cc/40?img=5' },
    { id: 4, author: 'Carlos T.', rating: 3, date: 'Dec 20, 2024', comment: 'Good product overall. Took a bit longer to arrive than expected but quality is fine.', avatar: 'https://i.pravatar.cc/40?img=7' },
  ];

  readonly ORDERS: Order[] = [
    { id: '#ETB-4521', date: 'Feb 10, 2025', status: 'delivered', total: 279.99, items: 2, image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=60&q=80' },
    { id: '#ETB-4399', date: 'Jan 28, 2025', status: 'shipped', total: 1099.00, items: 1, image: 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=60&q=80' },
    { id: '#ETB-4102', date: 'Jan 5, 2025', status: 'delivered', total: 149.98, items: 3, image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=60&q=80' },
    { id: '#ETB-3871', date: 'Dec 18, 2024', status: 'delivered', total: 799.99, items: 1, image: 'https://images.unsplash.com/photo-1593359677879-a4bb92f829e1?w=60&q=80' },
  ];
}
