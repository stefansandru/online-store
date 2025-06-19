import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from './models/category';
import { Seller } from './models/seller';

@Injectable({ providedIn: 'root' })
export class ModeratorService {
    private apiUrl = 'http://localhost:8080/api/moderator';

    constructor(private http: HttpClient) {}

    getAllSellers(): Observable<Seller[]> {
        return this.http.get<Seller[]>(`${this.apiUrl}/users`);
    }

    blockSeller(sellerId: number): Observable<string> {
        return this.http.post(`${this.apiUrl}/block-seller`, null, {
            params: { sellerId },
            responseType: 'text',
        });
    }

    unblockSeller(sellerId: number): Observable<string> {
        return this.http.post(`${this.apiUrl}/unblock-seller`, null, {
            params: { sellerId },
            responseType: 'text',
        });
    }

    getAllCategories(): Observable<Category[]> {
        return this.http.get<Category[]>(`${this.apiUrl}/categories`);
    }

    createCategory(categoryName: string): Observable<string> {
        return this.http.post(`${this.apiUrl}/create-category`, null, {
            params: { categoryName },
            responseType: 'text',
        });
    }

    editCategory(id: number, newName: string): Observable<string> {
        return this.http.put(`${this.apiUrl}/edit-category/${id}`, null, {
            params: { newName },
            responseType: 'text',
        });
    }
}
