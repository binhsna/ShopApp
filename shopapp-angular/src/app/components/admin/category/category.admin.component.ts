import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Category} from "../../../models/category";
import {CategoryService} from "../../../services/category.service";

@Component({
    selector: 'app-category-admin',
    templateUrl: './category.admin.component.html',
    styleUrls: ['./category.admin.component.scss']
})
export class CategoryAdminComponent implements OnInit {
    categories: Category[] = [];
    currentPage: number = 1;
    itemsPerPage: number = 12;
    totalPages: number = 0;
    visiblePages: number[] = [];
    keyword: string = "";

    constructor(
        private categoryService: CategoryService,
        private router: Router) {
    }

    ngOnInit() {
        this.getCategories(this.currentPage, this.itemsPerPage);
    }

    getCategories(page: number, limit: number) {
        this.categoryService.getCategories(page, limit).subscribe({
            next: (categories: Category[]) => {
                debugger;
                this.categories = categories;
                this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
            }, complete: () => {
                debugger;
            }, error: (error: any) => {
                console.error('Error fetching categories', error);
            }
        });
    }

    searchCategories() {
        this.currentPage = 1;
        this.itemsPerPage = 12;
        debugger;
        this.getCategories(this.currentPage, this.itemsPerPage);
    }

    onPageChange(page: number) {
        debugger;
        this.currentPage = page;
        this.getCategories(this.currentPage, this.itemsPerPage);
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

    onCategoryClick(categoryId: number) {
        this.router.navigate(['/categories', categoryId]);
    }

    deleteCategory(productId: number) {

    }
}
