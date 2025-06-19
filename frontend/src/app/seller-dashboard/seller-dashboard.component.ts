import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { Product } from '../models/product';
import { CategoryService } from '../category.service';
import { Category } from '../models/category';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth.service';

export interface ProductCreateDTO {
    name: string;
    description: string;
    price: number;
    stock: number;
    category: { id: number };
}

@Component({
    selector: 'app-seller-dashboard',
    templateUrl: './seller-dashboard.component.html',
    styleUrls: ['./seller-dashboard.component.css'],
    standalone: true,
    imports: [CommonModule],
})
export class SellerDashboardComponent implements OnInit {
    products: Product[] = [];
    categories: Category[] = [];
    loading = false;
    error: string | null = null;
    showAddForm = false;
    showEditForm = false;
    selectedProduct: Product | null = null;

    constructor(
        private productService: ProductService,
        private categoryService: CategoryService,
        private authService: AuthService,
    ) {}

    ngOnInit() {
        this.loadProducts();
        this.categoryService.getCategories().subscribe({
            next: (categories) => (this.categories = categories),
            error: () => (this.error = 'Failed to load categories.'),
        });
    }

    loadProducts() {
        this.loading = true;
        this.productService.getSellerProducts().subscribe({
            next: (products) => {
                this.products = products;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load products.';
                this.loading = false;
            },
        });
    }

    openAddForm() {
        this.showAddForm = true;
    }

    closeAddForm() {
        this.showAddForm = false;
    }

    submitAdd(
        name: string,
        description: string,
        price: string,
        stock: string,
        categoryId: string,
    ) {
        console.log('submitAdd called with:', {
            name,
            description,
            price,
            stock,
            categoryId,
        });
        if (!name || !description || !price || !stock || !categoryId) return;
        const product: ProductCreateDTO = {
            name,
            description,
            price: Number(price),
            stock: Number(stock),
            category: { id: Number(categoryId) },
        };
        this.productService.addProduct(product).subscribe({
            next: (product) => {
                this.products.push(product);
                this.showAddForm = false;
            },
            error: () => (this.error = 'Failed to add product.'),
        });
    }

    openEditForm(product: Product) {
        this.selectedProduct = { ...product };
        this.showEditForm = true;
    }

    closeEditForm() {
        this.showEditForm = false;
        this.selectedProduct = null;
    }

    submitEdit(
        name: string,
        description: string,
        price: string,
        stock: string,
    ) {
        if (!this.selectedProduct || !name || !description || !price || !stock)
            return;
        const updated = {
            ...this.selectedProduct,
            name,
            description,
            price: Number(price),
            stock: Number(stock),
        };
        this.productService.updateProduct(updated).subscribe({
            next: (prod) => {
                const idx = this.products.findIndex((p) => p.id === prod.id);
                if (idx > -1) this.products[idx] = prod;
                this.showEditForm = false;
                this.selectedProduct = null;
            },
            error: () => (this.error = 'Failed to update product.'),
        });
    }

    deleteProduct(product: Product) {
        if (!confirm('Delete this product?')) return;
        this.productService.deleteProduct(product.id).subscribe({
            next: () => {
                this.products = this.products.filter(
                    (p) => p.id !== product.id,
                );
            },
            error: () => (this.error = 'Failed to delete product.'),
        });
    }

    logout() {
        this.authService.logout();
    }
}
