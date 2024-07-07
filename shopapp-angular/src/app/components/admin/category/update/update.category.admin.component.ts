import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CategoryService} from "../../../../services/category.service";
import {CategoryDTO} from "../../../../dtos/category/category.dto";
import {Category} from "../../../../models/category";


@Component({
  selector: 'app-category-admin',
  templateUrl: './update.category.admin.component.html',
  styleUrls: ['./update.category.admin.component.scss']
})
export class UpdateCategoryAdminComponent implements OnInit {
  updatedCategory!: CategoryDTO;
  categoryId: number = 0;

  constructor(
    private categoryService: CategoryService,
    private activatedRoute: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit() {
    const idParam = this.activatedRoute.snapshot.paramMap.get('id')!;
    if (idParam !== null) {
      this.categoryId = +idParam;
      debugger;
      this.categoryService.getCategoryById(this.categoryId).subscribe({
        next: (response: any) => {
          debugger;
          this.updatedCategory = new CategoryDTO(response.data);
        }, complete: () => {
          debugger;
        }, error: (error: any) => {
          console.error('Error get category: ', error);
        }
      });
    }
  }

  updateCategory(): void {
    if (!isNaN(this.categoryId)) {
      this.categoryService.updateCategory(this.categoryId, this.updatedCategory).subscribe({
        next: (response: any) => {
          this.router.navigate(['/admin/categories']);
        }, complete: () => {
          debugger;
        },
        error: (error: any) => {
          debugger;
          console.error('Error update category: ', error);
        }
      });
    } else {
      console.error('Invalid categoryId', this.categoryId);
    }
  }
}
