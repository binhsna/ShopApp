import {ComponentFixture, TestBed} from '@angular/core/testing';

import {InsertProductAdminComponent} from './insert.product.admin.component';

describe('OrdersComponent', () => {
  let component: InsertProductAdminComponent;
  let fixture: ComponentFixture<InsertProductAdminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InsertProductAdminComponent]
    });
    fixture = TestBed.createComponent(InsertProductAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
