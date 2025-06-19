import { Component, OnInit } from '@angular/core';
import { BuyerService } from '../buyer.service';
import { Product } from '../models/product';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../category.service';
import { Category } from '../models/category';
import { FormsModule } from '@angular/forms';
import { CartItem } from '../models/cart-item';
import { AuthService } from '../auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-buyer-dashboard',
  standalone: true,
  templateUrl: './buyer-dashboard.component.html',
  styleUrls: ['./buyer-dashboard.component.css'],
  imports: [CommonModule, FormsModule],
})
export class BuyerDashboardComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  loading = false;
  error: string | null = null;
  searchName: string = '';
  selectedCategory: string = '';
  cartItems: CartItem[] = [];
  selectedQuantities: { [productId: number]: number } = {};

  constructor(
    private buyerService: BuyerService,
    private categoryService: CategoryService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.loadProducts();
    this.loadCategories();
    this.loadCartItems();
  }

  loadProducts() {
    this.buyerService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products;
      },
      error: (err) => {
        console.error('Failed to load products:', err);
      },
      complete: () => {
        console.log('Product loading completed');
      }
    });
  }

  loadCategories() {
    this.categoryService.getCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: () => {
        this.error = 'Failed to load categories.';
      },
    });
  }

  loadCartItems() {
    this.buyerService.getCartItems().subscribe({
      next: (items) => {
        this.cartItems = items;
      },
      error: () => {
        this.cartItems = [];
        this.error = 'Failed to load cart items.';
      }
    });
  }

  searchProducts() {
    this.loading = true;
    this.buyerService.filterProducts(this.searchName, this.selectedCategory).subscribe({
      next: (products) => {
        this.products = products;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to search products.';
        this.loading = false;
      }
    });
  }

  addToCart(product: Product, quantity: number) {
    if (!quantity || quantity < 1) quantity = 1;
    this.buyerService.addProductToCart(product.id, quantity).subscribe({
      next: () => {
        this.loadCartItems();
        this.error = null;
      },
      error: (err) => {
        setTimeout(() => {
          if (!this.cartItems.some(item => item.productId === product.id)) {
            this.error = typeof err === 'string' ? err : 'Failed to add product to cart.';
          } else {
            this.error = null;
          }
        }, 500);
      }
    });
  }

  removeFromCart(item: CartItem) {
    this.buyerService.removeProductFromCart(item.productId).subscribe({
      next: () => {
        this.loadCartItems();
      },
      error: () => {
        this.error = 'Failed to remove item from cart.';
      }
    });
  }

  placeOrder(): void {
    this.buyerService.placeOrder().subscribe({
      next: () => {
        this.loadCartItems();
        this.error = null;
      },
      error: (err) => {
        this.error = typeof err === 'string' ? err : 'Failed to place order.';
      }
    });
  }

  logout() {
    this.authService.logout();
  }

  get cartTotal(): number {
    return this.buyerService.calculateCartTotal(this.cartItems);
  }
}
