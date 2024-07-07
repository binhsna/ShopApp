import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../environments/environment";
import {OrderDTO} from "../dtos/order/order.dto";
import {Product} from "../models/product";
import {OrderResponse} from "../responses/order/order.response";

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = `${environment.apiBaseUrl}/orders`;
  private apiGetAllOrders = `${environment.apiBaseUrl}/orders/get-orders-by-keyword`;

  constructor(private http: HttpClient) {
  }

  placeOrder(orderData: OrderDTO): Observable<any> {
    // Gửi yêu cầu đặt hàng
    return this.http.post(this.apiUrl, orderData);
  }

  getOrderById(orderId: number): Observable<any> {
    const url = `${this.apiUrl}/${orderId}`;
    return this.http.get(url);
  }

  getAllOrders(keyword: string, page: number, limit: number, token: string)
    : Observable<OrderResponse[]> {
    debugger;
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`
    });
    const options = {params: params, headers: headers};

    return this.http.get<any>(this.apiGetAllOrders, options);
  }
}
