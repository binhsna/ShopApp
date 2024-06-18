import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {OrderDTO} from "../dtos/order/order.dto";
import {Product} from "../models/product";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = `${environment.apiBaseUrl}/orders`;

  constructor(private http: HttpClient) {
  }

  placeOrder(orderData: OrderDTO): Observable<any> {
    // Gửi yêu cầu đặt hàng
    return this.http.post(this.apiUrl, orderData);
  }

  getOrderById(orderId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${orderId}`);
  }
}
