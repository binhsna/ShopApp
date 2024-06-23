import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UpdateProductAdminComponent} from './update.product.admin.component';

describe('OrdersComponent', () => {
  let component: UpdateProductAdminComponent;
  let fixture: ComponentFixture<UpdateProductAdminComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateProductAdminComponent]
    });
    fixture = TestBed.createComponent(UpdateProductAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
