import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {Product} from "../models/product";
import {UpdateProductDTO} from "../dtos/product/product.update.dto";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiBaseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {
  }

  getProducts(keyword: string, categoryId: number, page: number, limit: number): Observable<Product[]> {
    const params = new HttpParams()
      .set('keyword', keyword.toString())
      .set('category_id', categoryId.toString())
      .set('page', page.toString())
      .set('limit', limit.toString());
    return this.http.get<Product[]>(`${this.apiBaseUrl}/products`, {params});
  }

  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiBaseUrl}/products/${productId}`);
  }

  getProductsByIds(productIds: number[]): Observable<Product[]> {
    // Chuyển danh sách ID thành một chuỗi và truyền vào params
    const params = new HttpParams().set('ids', productIds.join(","));
    return this.http.get<Product[]>(`${this.apiBaseUrl}/products/by-ids`, {params});
  }

  deleteProduct(productId: number): Observable<string> {
    return this.http.delete<string>(`${this.apiBaseUrl}`)
  }

  updateProduct(productId: number, updateProduct: UpdateProductDTO): Observable<UpdateProductDTO> {
    return this.http.put<UpdateProductDTO>(`${this.apiBaseUrl}/products/${productId}`, updateProduct);
  }

  insertProduct(insertProduct: Product): Observable<any> {
    return this.http.post<Product>(`${this.apiBaseUrl}/products/`, insertProduct);
  }

  uploadImages(productId: number, files: FileList): Observable<any> {
    debugger;
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files[]', files[i]);
    }
    const headers = new HttpHeaders();
    headers.set('Content-Type', 'multipart/form-data');
    const url: string = `${this.apiBaseUrl}/products/uploads/${productId}`;
    return this.http.post(url, formData, {headers});
  }
}
