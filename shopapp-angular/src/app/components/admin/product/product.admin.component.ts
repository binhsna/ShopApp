import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Product} from "../../../models/product";
import {Category} from "../../../models/category";
import {ProductService} from "../../../services/product.service";
import {CategoryService} from "../../../services/category.service";
import {environment} from "../../../environments/environment";

@Component({
    selector: 'app-product-admin',
    templateUrl: './product.admin.component.html',
    styleUrls: ['./product.admin.component.scss']
})
export class ProductAdminComponent implements OnInit {
    products: Product[] = [];
    categories: Category[] = []; // Dữ liệu đọng từ CategoryService
    selectedCategoryId: number = 0; // Giá trị category được chọn
    currentPage: number = 1;
    itemsPerPage: number = 12;
    totalPages: number = 0;
    visiblePages: number[] = [];
    keyword: string = "";

    constructor(
        private productService: ProductService,
        private categoryService: CategoryService,
        private router: Router) {
    }

    ngOnInit() {
        this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
        this.getCategories(1, 100);
    }

    getCategories(page: number, limit: number) {
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

    searchProducts() {
        this.currentPage = 1;
        this.itemsPerPage = 12;
        debugger;
        this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    }

    private getProducts(keyword: string, selectedCategoryId: number, page: number, limit: number) {
        this.productService.getProducts(keyword, selectedCategoryId, page, limit).subscribe({
            next: (response: any) => {
                debugger;
                response.products.forEach((product: Product) => {
                    debugger
                    product.url = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
                });
                this.products = response.products;
                this.totalPages = response.totalPages;
                this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
            },
            complete: () => {
                debugger;
            },
            error: (error: any) => {
                debugger;
                console.error('Error fetching products:', error);
            }
        });
    }

    onPageChange(page: number) {
        debugger;
        this.currentPage = page;
        this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
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

    // Hàm xử lý sự kiện khi sản phẩm được bấm vào
    onProductClick(productId: number) {
        debugger;
        // Điều hướng đến trang detail-product với productId là tham số
        this.router.navigate(['/products', productId]);
    }

    deleteProduct(productId: number) {

    }
}
