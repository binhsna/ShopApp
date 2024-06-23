import {Component, OnInit} from '@angular/core';
import {Product} from "../../models/product";
import {ProductService} from "../../services/product.service";
import {environment} from "../../environments/environment";
import {ProductImage} from "../../models/product.image";
import {CartService} from "../../services/cart.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductDTO} from "../../dtos/product/product.dto";

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss']
})
export class DetailProductComponent implements OnInit {
  product!: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;
  isPressedAddToCart: boolean = false;

  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
  ) {
  }

  ngOnInit() {
    // Lấy ra productId từ URL
    debugger;
    // this.cartService.clearCart();
    const idParam = this.activatedRoute.snapshot.paramMap.get('id')!;
    if (idParam !== null) {
      this.productId = +idParam;
    }
    if (!isNaN(this.productId)) {
      this.productService.getProductById(this.productId).subscribe({
        next: (response: any) => {
          // Lấy danh sách ảnh sản phẩm và thay đổi URL
          debugger;
          let listImage: { id: number, image_url: string }[] = [];
          let productDTO: ProductDTO = new ProductDTO();
          productDTO.init(response);
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_image: ProductImage) => {
              product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
              productDTO.setProductImages(response.product_images);
            });
          } else {
            debugger;
            for (let i = 0; i < 5; i++) {
              debugger;
              let image_url = `${environment.apiBaseUrl}/products/images/not-found.png`;
              listImage.push({id: i, image_url: image_url});
              productDTO.setProductImages(listImage);
            }
          }
          this.product = productDTO;
          // Bắt đầu với ảnh đầu tiên
          this.showImage(0);
        }, complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          console.error('Error fetching detail', error);
        }
      });
    } else {
      console.error('Invalid productId', idParam);
    }
  }

  showImage(index: number) {
    debugger;
    if (this.product && this.product.product_images && this.product.product_images.length > 0) {
      // Đảm bảo index nằm trong khoảng hợp lệ
      if (index < 0) {
        index = 0;
      } else if (index >= this.product.product_images.length) {
        index = this.product.product_images.length - 1;
      }
      // Gán index hiện tại và cập nhật ảnh hiển thị
      this.currentImageIndex = index;
    }
  }

  thumbnailClick(index: number) {
    debugger;
    // Gọi khi một thumbnail được bấm
    this.currentImageIndex = index; // Cập nhật currentImageIndex
  }

  nextImage(): void {
    debugger;
    this.showImage(this.currentImageIndex + 1);
  }

  previousImage(): void {
    debugger;
    this.showImage(this.currentImageIndex - 1);
  }

  addToCart(): void {
    debugger;
    this.isPressedAddToCart = true;
    if (this.product) {
      this.cartService.addToCart(this.productId, this.quantity);
    } else {
      // Xử lý khi product là null
      console.error("Không thể thêm sản phẩm vào giỏ hàng vì product là null.");
    }
  }

  increaseQuantity(): void {
    this.quantity++;
  }

  decreaseQuantity(): void {
    if (this.quantity > 1)
      this.quantity--;
  }

  buyNow(): void {
    // Thực hiện xử lý khi người dùng muốn mua ngay
    if (this.isPressedAddToCart == false) {
      this.addToCart();
    }
    this.router.navigate(['/orders']);
  }

  getTotalPrice(): number {
    if (this.product) {
      return this.quantity * this.product.price;
    }
    return 0;
  }
}
