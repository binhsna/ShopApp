import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {Category} from "../models/category";
import {UpdateProductDTO} from "../dtos/product/product.update.dto";
import {CategoryDTO} from "../dtos/category/category.dto";
import {Product} from "../models/product";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiBaseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {
  }

  getCategories(page: number, limit: number): Observable<Category[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<Category[]>(`${this.apiBaseUrl}/categories`, {params});
  }

  getCategoryById(categoryId: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiBaseUrl}/categories/${categoryId}`);
  }

  insertCategory(newCategory: CategoryDTO): Observable<CategoryDTO> {
    return this.http.post<CategoryDTO>(`${this.apiBaseUrl}/categories`, newCategory);
  }

  updateCategory(categoryId: number, updateCategory: CategoryDTO): Observable<CategoryDTO> {
    return this.http.put<CategoryDTO>(`${this.apiBaseUrl}/categories/${categoryId}`, updateCategory);
  }

  deleteCategory(categoryId: number): Observable<string> {
    return this.http.delete<string>(`${this.apiBaseUrl}`)
  }
}
