import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/product";
import {environment} from "../../environments/environment";
import {CartService} from "../../services/cart.service";
import {ProductService} from "../../services/product.service";
import {OrderService} from "../../services/order.service";
import {OrderDTO} from "../../dtos/order/order.dto";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  cartItems: { product: Product, quantity: number }[] = [];
  couponCode: string = ''; // Mã giảm giá
  totalAmount: number = 0; // Tổng tiền
  orderData: OrderDTO = {
    user_id: 1, // Thay bằng user_id thích hợp
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod',
    shipping_method: 'express',
    coupon_code: '',
    cart_items: []
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService
  ) {
  }

  ngOnInit() {
    // Lấy ra danh sách sản phẩm từ giỏ hàng
    debugger;
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());// Chuyển danh sách ID từ Map giỏ hàng
    // Gọi service để lấy thông tin sản phẩm dựa trên danh sách ID
    debugger;
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products: Product[]) => {
        debugger;
        // Lấy thông tin sản phẩm và số lượng từ danh sách sản phẩm và giỏ hàng
        this.cartItems = productIds.map((productId) => {
          debugger;
          const product = products.find((p) => p.id === productId);
          if (product) {
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }
          return {
            product: product!,
            quantity: cart.get(productId)!
          };
        });
      },
      complete: () => {
        debugger;
        this.calculateTotal();
      }, error: (error: any) => {
        debugger;
        console.error('Error fetching details', error);
      }
    });
  }

  // Hàm tính tổng tiền
  calculateTotal() {
    this.totalAmount = this.cartItems
      .reduce((total, item) =>
        total + item.product.price * item.quantity, 0
      );
  }

  // Hàm xử lý việc áp dụng mã giảm giá
  applyCoupon() {
    // Viết mã xử lý áp dụng mã giảm giá
    // Cập nhật giá trị totalAmount dựa trên mã giảm giá (Nếu áp dụng)

  }

  placeOrder() {

  }
}
