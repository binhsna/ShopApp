import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {OrderService} from "../../../services/order.service";
import {OrderResponse} from "../../../responses/order/order.response";

@Component({
  selector: 'app-order-admin',
  templateUrl: './order.admin.component.html',
  styleUrls: ['./order.admin.component.scss']
})
export class OrderAdminComponent implements OnInit {
  orders: OrderResponse[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 12;
  visiblePages: number[] = [];
  totalPages: number = 0;
  keyword: string = "";

  constructor(
    private orderService: OrderService,
    private router: Router) {
  }

  ngOnInit() {
    this.currentPage = Number(localStorage.getItem("currentOrderAdminPage")) || 1;
    this.getAllOrders(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }

  getAllOrders(keyword: string, page: number, limit: number) {
    this.orderService.getAllOrders(keyword, page, limit).subscribe({
      next: (response: any) => {
        debugger;
        this.orders = response.orders;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching order:', error);
      }
    });
  }

  searchOrders() {
    this.currentPage = 1;
    this.itemsPerPage = 12;
    debugger;
    this.getAllOrders(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }

  onPageChange(page: number) {
    debugger;
    this.currentPage = page < 0 ? 0 : page;
    localStorage.setItem("currentOrderAdminPage", String(this.currentPage));
    this.getAllOrders(this.keyword.trim(), this.currentPage, this.itemsPerPage);
  }

  private generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }
    return new Array(endPage - startPage + 1)
      .fill(0).map((_, index) => startPage + index);
  }

  viewDetails(orderId: number) {
    this.router.navigate(['/admin/orders', orderId]);
  }

  deleteOrder(orderId: number) {
    if (confirm('Bạn có chắc chắn muốn xóa đơn hàng này?')) {

    }
  }
}
