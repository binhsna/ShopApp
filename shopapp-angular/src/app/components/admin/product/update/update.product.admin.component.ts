import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CategoryService} from "../../../../services/category.service";
import {ProductService} from "../../../../services/product.service";
import {Product} from "../../../../models/product";
import {Category} from "../../../../models/category";
import {ProductImage} from "../../../../models/product.image";
import {environment} from "../../../../environments/environment";
import {UpdateProductDTO} from "../../../../dtos/product/product.update.dto";

@Component({
  selector: 'app-update-product-admin',
  templateUrl: './update.product.admin.component.html',
  styleUrls: ['./update.product.admin.component.scss']
})
export class UpdateProductAdminComponent implements OnInit {
  updatedProduct!: UpdateProductDTO;
  categories!: Category[];
  productId: number = 0;
  currentImageIndex: number = 0;
  selectedFiles!: FileList;
  message = '';

  constructor(
    private categoryService: CategoryService,
    private productService: ProductService,
    private activatedRoute: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit() {
    this.getCategories(1, 100);
    const idParam = this.activatedRoute.snapshot.paramMap.get('id')!;
    if (idParam !== null) {
      this.productId = +idParam;
      debugger;
      this.productService.getProductById(this.productId).subscribe({
        next: (response: any) => {
          debugger;
          if (response.product_images && response.product_images.length > 0) {
            response.product_images.forEach((product_image: ProductImage) => {
              product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
            });
          }
          this.updatedProduct = response;
          // Bắt đầu với ảnh đầu tiên
          this.showImage(0);
        }, complete: () => {
          debugger;
        }, error: (error: any) => {
          console.error('Error get product: ', error);
        }
      });
    }
  }

  showImage(index: number) {
    debugger;
    if (this.updatedProduct && this.updatedProduct.product_images && this.updatedProduct.product_images.length > 0) {
      // Đảm bảo index nằm trong khoảng hợp lệ
      if (index < 0) {
        index = 0;
      } else if (index >= this.updatedProduct.product_images.length) {
        index = this.updatedProduct.product_images.length - 1;
      }
      // Gán index hiện tại và cập nhật ảnh hiển thị
      this.currentImageIndex = index;
    }
  }

  private getCategories(page: number, limit: number) {
    this.categoryService.getCategories(page, limit).subscribe({
      next: (categories: Category[]) => {
        debugger;
        this.categories = categories;
      }, complete: () => {
        debugger;
      }, error: (error: any) => {
        console.error('Error fetching categories', error);
      }
    });
  }

  updateProduct(): void {
    this.productService.updateProduct(this.productId, this.updatedProduct).subscribe({
      next: (response: any) => {
        debugger;
        this.productService.uploadImages(this.productId, this.selectedFiles).subscribe({
          next: (response: any) => {
            debugger;
            this.router.navigate(['/admin/products']);
          }, complete: () => {
            debugger;
          }, error: (error: any) => {
            alert('Error upload files: ' + error);
          }
        });
      },
      complete: () => {
        debugger;
      }, error: (error: any) => {
        alert('Error update product: ' + error);
      }
    });
  }

  thumbnailClick(index: number) {
    this.currentImageIndex = index;
  }

  deleteImage(productImage: ProductImage) {

  }

  onFileChange(event: any) {
    this.selectedFiles = event.target.files;
  }
}
