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
    categoryName: string;
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
        event: Event,
        name: string,
        description: string,
        price: string,
        stock: string,
        categoryId: string,
    ) {
        event.preventDefault();
        if (!name || !description || !price || !stock || !categoryId) return;
        const selectedCategory = this.categories.find(
            (cat) => cat.id === Number(categoryId),
        );
        if (!selectedCategory) return;
        const product: Product = {
            id: 0,
            name,
            description,
            price: Number(price),
            stock: Number(stock),
            categoryName: selectedCategory.name,
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
