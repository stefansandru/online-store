import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from './models/category';

@Injectable({ providedIn: 'root' })
export class CategoryService {
  private apiUrl = 'http://localhost:8080/api/categories';

  constructor(private http: HttpClient) {}

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  // createCategory(categoryName: string) {
  //   return this.http.get(`${this.apiUrl}/create-category`, { params: { categoryName }, responseType: 'text' });
  // }

  // editCategory(id: number, newName: string) {
  //   return this.http.put(`${this.apiUrl}/edit-category/${id}`, null, { params: { newName }, responseType: 'text' });
  // }
}
