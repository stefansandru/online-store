import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Product } from './models/product';
import { Seller } from './models/seller';

@Injectable({ providedIn: 'root' })
export class BuyerService {
  private apiUrl = 'http://localhost:8080/api/buyer'; 

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<Product[]> {
    return this.http.get<any[]>(`${this.apiUrl}/products`).pipe(
      map(products => products.map(p => ({
        id: p.id,
        name: p.name,
        description: p.description,
        price: p.price,
        stock: p.stock,
        categoryName: p.categoryName
      })))
    );
  }

  filterProducts(name?: string, category?: string): Observable<Product[]> {
    let params: any = {};
    if (name) params.name = name;
    if (category) params.category = category;
    return this.http.get<Product[]>(`${this.apiUrl}/products/filter`, { params });
  }

  addProductToCart(productId: number, quantity: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/cart/add`,
      {},
      {
        params: { productId, quantity },
        withCredentials: true
      }
    ).pipe(
      catchError((error) => {
        // Pass backend error message to component
        return throwError(() => error.error || 'An error occurred');
      })
    );
  }

  getCartItems(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/cart`, { withCredentials: true });
  }

  removeProductFromCart(productId: number): Observable<any> {
    return this.http.delete<any>(
      `${this.apiUrl}/cart/remove`,
      {
        params: { productId },
        withCredentials: true
      }
    );
  }

  placeOrder(): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/order`,
      {},
      { withCredentials: true }
    ).pipe(
      catchError((error) => {
        return throwError(() => error.error || 'An error occurred');
      })
    );
  }

  getAllSellers(): Observable<Seller[]> {
    return this.http.get<Seller[]>(`${this.apiUrl}/users`);
  }

  blockSeller(sellerId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/block-seller?sellerId=${sellerId}`, {}, { responseType: 'text' });
  }

  unblockSeller(sellerId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/unblock-seller?sellerId=${sellerId}`, {}, { responseType: 'text' });
  }
}