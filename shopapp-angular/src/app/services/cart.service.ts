import {Injectable} from "@angular/core";
import {ProductService} from "./product.service";

@Injectable({
  providedIn: 'root'
})
export class CartService {
  // Dùng Map để lưu trử giỏ hàng, key là id sản phẩm, value là số lượt
  private cart: Map<number, number> = new Map();
  private myMap = new Map([
    ['key1', 'value1'],
    ['key2', 'value2'],
    ['key3', 'value3']
  ]);

  constructor(private productService: ProductService) {
    // Lấy dữ liệu giỏ hàng từ localStorage khi khởi tạo service
    const storedCart = localStorage.getItem("cart");
    if (storedCart) {
      this.cart = new Map(JSON.parse(storedCart));
      /*
       {
        "2":5,
        "3":10
       }
      */
    }
  }

  addToCart(productId: number, quantity: number = 1): void {
    debugger;
    if (this.cart.has(productId)) {
      // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng lên `quantity`
      let currentQuantity: number = Number(this.cart.get(productId)!);
      currentQuantity += Number(quantity);
      this.cart.set(productId, currentQuantity);
    } else {
      // Nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm vào với số lượng là `quantity`
      this.cart.set(productId, quantity);
    }
    // Sau khi thay đổi giỏ hàng, lưu trữ nó vào localStorage
    this.saveCartToLocalStorage();
  }

  getCart(): Map<number, number> {
    return this.cart;
  }

  // Lưu trữ giỏ hàng vào localstorage
  private saveCartToLocalStorage(): void {
    localStorage.setItem("cart", JSON.stringify(Array.from(this.cart.entries())));
  }

  // Hàm xóa dữ liệu giỏ hàng và cập nhật local Storage
  clearCart(): void {
    this.cart.clear(); // Xóa toàn bộ dữ liệu trong giỏ hàng
    this.saveCartToLocalStorage(); // Lưu trữ giỏ hàng với vào local Storage (trống)
  }
}
