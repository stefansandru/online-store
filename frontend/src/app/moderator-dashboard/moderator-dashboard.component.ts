import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { ModeratorService } from '../services/moderator.service';
import { Seller } from '../models/seller';
import { Category } from '../models/category';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-moderator-dashboard',
    standalone: true,
    templateUrl: './moderator-dashboard.component.html',
    styleUrls: ['./moderator-dashboard.component.css'],
    imports: [CommonModule, FormsModule],
})
export class ModeratorDashboardComponent implements OnInit {
    sellers: Seller[] = [];
    loading = false;
    error: string | null = null;

    categories: Category[] = [];
    categoryLoading = false;
    categoryError: string | null = null;
    newCategoryName = '';
    editCategoryId: number | null = null;
    editCategoryName = '';

    constructor(
        private moderatorService: ModeratorService,
        private authService: AuthService,
    ) {}

    ngOnInit() {
        this.loadSellers();
        this.loadCategories();
    }

    loadSellers() {
        this.loading = true;
        this.moderatorService.getAllSellers().subscribe({
            next: (sellers) => {
                this.sellers = sellers.map((s) => ({
                    ...s,
                    blocked: s.accountStatus === 'BLOCKED',
                }));
                if (this.sellers.length > 0) {
                    console.log('First seller:', this.sellers[0]);
                } else {
                    console.log('No sellers received');
                }
                this.loading = false;
            },
            error: () => {
                this.error = 'Failed to load sellers.';
                this.loading = false;
            },
        });
    }

    blockOrUnblockSeller(seller: Seller) {
        const obs = seller.blocked
            ? this.moderatorService.unblockSeller(seller.id)
            : this.moderatorService.blockSeller(seller.id);
        obs.subscribe({
            next: () => {
                seller.blocked = !seller.blocked;
                this.error = null;
            },
            error: (err) => {
                this.error = seller.blocked
                    ? 'Failed to unblock seller.'
                    : 'Failed to block seller.';
                console.error('Block/Unblock error:', err);
            },
        });
    }

    loadCategories() {
        this.categoryLoading = true;
        this.moderatorService.getAllCategories().subscribe({
            next: (categories) => {
                this.categories = categories;
                this.categoryLoading = false;
            },
            error: () => {
                this.categoryError = 'Failed to load categories.';
                this.categoryLoading = false;
            },
        });
    }

    createCategory() {
        this.categoryLoading = true;
        this.moderatorService.createCategory(this.newCategoryName).subscribe({
            next: () => {
                this.loadCategories();
                this.newCategoryName = '';
                this.categoryError = null;
            },
            error: () => {
                this.categoryError = 'Failed to create category.';
                this.categoryLoading = false;
            },
        });
    }

    startEditCategory(category: Category) {
        this.editCategoryId = category.id;
        this.editCategoryName = category.name;
    }

    saveEditCategory() {
        if (this.editCategoryId !== null) {
            this.categoryLoading = true;
            this.moderatorService
                .editCategory(this.editCategoryId, this.editCategoryName)
                .subscribe({
                    next: () => {
                        this.loadCategories();
                        this.editCategoryId = null;
                        this.editCategoryName = '';
                        this.categoryError = null;
                    },
                    error: () => {
                        this.categoryError = 'Failed to update category.';
                        this.categoryLoading = false;
                    },
                });
        }
    }

    cancelEditCategory() {
        this.editCategoryId = null;
        this.editCategoryName = '';
    }

    logout() {
        this.authService.logout();
    }
}
