import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CategoryService} from "../../../../services/category.service";
import {ProductService} from "../../../../services/product.service";
import {Category} from "../../../../models/category";
import {Product} from "../../../../models/product";
import {ProductDTO} from "../../../../dtos/product/product.dto";

@Component({
    selector: 'app-insert-product-admin',
    templateUrl: './insert.product.admin.component.html',
    styleUrls: ['./insert.product.admin.component.scss']
})
export class InsertProductAdminComponent implements OnInit {
    insertProductDTO: Product = new ProductDTO();
    categories: Category[] = []; // Dữ liệu động từ CategoryService

    constructor(
        private productService: ProductService,
        private categoryService: CategoryService,
        private router: Router) {

    }

    ngOnInit(): void {
        this.getCategories(1, 100);
    }

    private getCategories(page: number, limit: number): void {
        this.categoryService.getCategories(page, limit).subscribe({
            next: (response: Category[]) => {
                this.categories = response;
            }, complete: () => {
                debugger;
            }, error: (error: any) => {
                console.error('Error fetching categories', error);
            }
        });
    }

    insertProduct(): void {
        this.productService.insertProduct(this.insertProductDTO!).subscribe({
            next: (response: any) => {
                this.router.navigate(['/admin/products']);
            }, complete: () => {
                debugger;
            },
            error: (error: any) => {
                debugger;
                console.error('Error create product: ', error);
            }
        });
    }

    onFileChange(event: Event) {
        const element = event.currentTarget as HTMLInputElement;
        let fileList: FileList | null = element.files;
        if (fileList) {
            console.log("FileUpload -> files", fileList);
        }
    }
}
