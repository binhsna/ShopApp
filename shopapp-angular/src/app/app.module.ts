import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HomeComponent} from './components/home/home.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {OrderComponent} from './components/order/order.component';
import {OrderDetailComponent} from './components/order-detail/order-detail.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {DetailProductComponent} from './components/detail-product/detail-product.component';
import {FormsModule} from "@angular/forms";
import {ReactiveFormsModule} from "@angular/forms";
import {
    HttpClientModule,
    HTTP_INTERCEPTORS
} from '@angular/common/http';
import {TokenInterceptor} from "./interceptors/token.interceptor";
import {AppComponent} from './app/app.component';
import {AppRoutingModule} from "./app-routing.module";
import {NgbPopover} from "@ng-bootstrap/ng-bootstrap";
import {AdminComponent} from './components/admin/admin.component';
import {OrderAdminComponent} from './components/admin/order/order.admin.component';
import {ProductAdminComponent} from "./components/admin/product/product.admin.component";
import {CategoryAdminComponent} from "./components/admin/category/category.admin.component";
import {UserProfileComponent} from "./components/user-profile/user.profile.component";
import {UpdateCategoryAdminComponent} from "./components/admin/category/update/update.category.admin.component";
import {InsertCategoryAdminComponent} from "./components/admin/category/insert/insert.category.admin.component";
import {UpdateProductAdminComponent} from "./components/admin/product/update/update.product.admin.component";
import {InsertProductAdminComponent} from "./components/admin/product/insert/insert.product.admin.component";

@NgModule({
    declarations: [
        HomeComponent,
        HeaderComponent,
        FooterComponent,
        OrderComponent,
        OrderDetailComponent,
        LoginComponent,
        RegisterComponent,
        UserProfileComponent,
        DetailProductComponent,
        AppComponent,
        // Admin
        AdminComponent,
        // Category
        CategoryAdminComponent,
        InsertCategoryAdminComponent,
        UpdateCategoryAdminComponent,
        // Product
        ProductAdminComponent,
        InsertProductAdminComponent,
        UpdateProductAdminComponent,
        // Order
        OrderAdminComponent,
    ],
    imports: [
        ReactiveFormsModule,
        BrowserModule,
        FormsModule,
        HttpClientModule,
        AppRoutingModule,
        NgbPopover,
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true,
        },
    ],
    bootstrap: [
        AppComponent,
        //HomeComponent,
        //OrderComponent,
        //OrderDetailComponent,
        //LoginComponent,
        //DetailProductComponent,
        //UserProfileComponent,
    ]
})
export class AppModule {
}
