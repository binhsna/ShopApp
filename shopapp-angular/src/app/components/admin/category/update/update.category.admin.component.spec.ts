import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UpdateCategoryAdminComponent} from './update.category.admin.component';

describe('OrdersComponent', () => {
    let component: UpdateCategoryAdminComponent;
    let fixture: ComponentFixture<UpdateCategoryAdminComponent>;

    beforeEach(() => {
        TestBed.configureTestingModule({
            declarations: [UpdateCategoryAdminComponent]
        });
        fixture = TestBed.createComponent(UpdateCategoryAdminComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
