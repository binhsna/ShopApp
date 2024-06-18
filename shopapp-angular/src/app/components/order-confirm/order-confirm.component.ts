import {Component, OnInit} from '@angular/core';
import {CartService} from "../../services/cart.service";
import {ProductService} from "../../services/product.service";
import {Product} from "../../models/product";
import {environment} from "../../environments/environment";

@Component({
  selector: 'app-order-confirm',
  templateUrl: './order-confirm.component.html',
  styleUrls: ['./order-confirm.component.scss']
})
export class OrderConfirmComponent implements OnInit {
  cartItems: { product: Product, quantity: number }[] = [];
  couponCode: string = ''; // Mã giảm giá
  totalAmount: number = 0; // Tổng tiền

  constructor(
    private cartService: CartService,
    private productService: ProductService) {
  }

  ngOnInit(): void {
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
}
