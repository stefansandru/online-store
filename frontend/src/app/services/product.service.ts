import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';


@Injectable({ providedIn: 'root' })
export class ProductService {
    private apiUrl = 'http://localhost:8080/api/seller/products';

    constructor(private http: HttpClient) {}

    getAllProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(this.apiUrl);
    }

    getSellerProducts(): Observable<Product[]> {
        return this.http.get<Product[]>(this.apiUrl);
    }

    addProduct(product: Product): Observable<Product> {
        console.log('ProductService.addProduct called with:', product);
        return this.http.post<Product>(this.apiUrl, product);
    }

    deleteProduct(productId: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}/${productId}`);
    }

    updateProduct(product: Product): Observable<Product> {
        return this.http.put<Product>(`${this.apiUrl}/${product.id}`, product);
    }
}
