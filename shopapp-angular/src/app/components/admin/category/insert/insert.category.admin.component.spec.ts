import {ComponentFixture, TestBed} from '@angular/core/testing';

import {InsertCategoryAdminComponent} from './insert.category.admin.component';

describe('OrdersComponent', () => {
  let component: InsertCategoryAdminComponent;
  let fixture: ComponentFixture<InsertCategoryAdminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InsertCategoryAdminComponent]
    });
    fixture = TestBed.createComponent(InsertCategoryAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
