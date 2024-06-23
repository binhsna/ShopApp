import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CategoryService} from "../../../../services/category.service";
import {CategoryDTO} from "../../../../dtos/category/category.dto";


@Component({
  selector: 'app-category-admin',
  templateUrl: './insert.category.admin.component.html',
  styleUrls: ['./insert.category.admin.component.scss']
})
export class InsertCategoryAdminComponent implements OnInit {
  newCategory!: CategoryDTO;

  constructor(
    private categoryService: CategoryService,
    private router: Router) {
  }

  ngOnInit() {

  }

  insertCategory(): void {
    this.categoryService.insertCategory(this.newCategory).subscribe({
      next: (response: any) => {
        this.router.navigate(['/admin/categories']);
      }, complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error create category: ', error);
      }
    });
  }
}
