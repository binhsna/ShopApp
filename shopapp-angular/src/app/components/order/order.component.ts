import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/product";
import {environment} from "../../environments/environment";
import {CartService} from "../../services/cart.service";
import {ProductService} from "../../services/product.service";
import {OrderService} from "../../services/order.service";
import {OrderDTO} from "../../dtos/order/order.dto";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TokenService} from "../../services/token.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss']
})
export class OrderComponent implements OnInit {
  orderForm: FormGroup;// Đối tượng FormGroup để quản lý dữ liệu của form (validation)
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
    private orderService: OrderService,
    private tokenService: TokenService,
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
  ) {
    // Tạo FormGroup và các FormControl tương ứng
    this.orderForm = this.fb.group({
      fullname: ['Nguyễn Công Bình', Validators.required],
      email: ['binhsna@gmail.com', [Validators.email]],
      phone_number: ['0971912776', [Validators.required, Validators.minLength(6)]],
      address: ['Nhà a ngõ b', [Validators.required, Validators.minLength(5)]],
      note: ['Cẩn thận'],
      shipping_method: ['express'],
      payment_method: ['cod']
    });
  }

  ngOnInit() {
    debugger;
    this.orderData.user_id = this.tokenService.getUserId();
    // Lấy ra danh sách sản phẩm từ giỏ hàng
    debugger;
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys());// Chuyển danh sách ID từ Map giỏ hàng
    // Gọi service để lấy thông tin sản phẩm dựa trên danh sách ID
    debugger;
    if (productIds.length === 0) {
      return;
    }
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
    debugger;
    if (this.orderForm.valid) {
      // Gán giá trị của form vào đối tượng orderData
      /*
       this.orderData.fullname = this.orderForm.get('fullname')!.value;
       this.orderData.email = this.orderForm.get('email')!.value;
       this.orderData.phone_number = this.orderForm.get('phone_number')!.value;
       this.orderData.address = this.orderForm.get('address')!.value;
       this.orderData.note = this.orderForm.get('note')!.value;
       this.orderData.shipping_method = this.orderForm.get('shipping_method')!.value;
       this.orderData.payment_method = this.orderForm.get('payment_method')!.value;
       */
      // Sử dụng toán tử spread (...) để sao chép các giá trị form vào orderData
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value
      };
      this.orderData.total_money = this.totalAmount;
      // Dữ liệu hợp lệ
      this.orderData.cart_items = this.cartItems.map(cartItem => ({
        product_id: cartItem.product.id,
        quantity: cartItem.quantity
      }));
      // Dữ liêệu hợp lệ, bạn có thể gửi đơn hàng đi
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response) => {
          debugger;
          alert("Đặt hàng thành công");
          this.cartService.clearCart();
          this.router.navigate(['/']);
        },
        complete: () => {
          debugger;
          this.calculateTotal();
        },
        error: (error: any) => {
          // Hiển thị thông báo lỗi hoặc xử lý khác
          alert(`Lỗi khi đặt hàng: ${error}`);
        }
      });
    } else {
      // Hiển thị thông báo lỗi hoặc khác
      alert("Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
    }
  }
}
