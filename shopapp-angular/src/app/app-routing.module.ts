import {NgModule} from '@angular/core';
import {HomeComponent} from './components/home/home.component';
import {OrderComponent} from './components/order/order.component';
import {OrderDetailComponent} from './components/order-detail/order-detail.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {DetailProductComponent} from './components/detail-product/detail-product.component';
import {RouterModule, Routes} from "@angular/router";
import {AdminComponent} from "./components/admin/admin.component";
import {AuthGuardFn} from "./guards/auth.guard";
import {AdminGuardFn} from "./guards/admin.guard";
import {UserProfileComponent} from "./components/user-profile/user.profile.component";
import {CategoryAdminComponent} from "./components/admin/category/category.admin.component";
import {ProductAdminComponent} from "./components/admin/product/product.admin.component";
import {OrderAdminComponent} from "./components/admin/order/order.admin.component";
import {UpdateCategoryAdminComponent} from "./components/admin/category/update/update.category.admin.component";
import {InsertCategoryAdminComponent} from "./components/admin/category/insert/insert.category.admin.component";
import {UpdateProductAdminComponent} from "./components/admin/product/update/update.product.admin.component";
import {InsertProductAdminComponent} from "./components/admin/product/insert/insert.product.admin.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'products/:id', component: DetailProductComponent},
  {path: 'orders', component: OrderComponent, canActivate: [AuthGuardFn]},
  {path: 'user-profile', component: UserProfileComponent, canActivate: [AuthGuardFn]},
  {path: 'orders/:id', component: OrderDetailComponent},
  // Admin
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuardFn],
    children: [
      {
        // Category
        path: 'categories',
        component: CategoryAdminComponent
      }, {
        path: 'categories/insert',
        component: InsertCategoryAdminComponent
      }, {
        path: 'categories/:id',
        component: UpdateCategoryAdminComponent
      },
      {
        path: 'products',
        component: ProductAdminComponent
      }, {
        path: 'products/insert',
        component: InsertProductAdminComponent
      }, {
        path: 'products/:id',
        component: UpdateProductAdminComponent
      }, {
        path: 'orders',
        component: OrderAdminComponent
      },
    ]
  },
  /*
   {
       path: 'admin/category',
       component: CategoryAdminComponent,
       canActivate: [AdminGuardFn]
   }, {
       path: 'admin/product',
       component: ProductAdminComponent,
       canActivate: [AdminGuardFn]
   }, {
       path: 'admin/order',
       component: OrderAdminComponent,
       canActivate: [AdminGuardFn]
   },
   */

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
