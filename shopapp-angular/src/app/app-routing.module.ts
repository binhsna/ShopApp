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
import {OrderAdminComponent} from "./components/admin/order/order.admin.component";
import {ProductAdminComponent} from "./components/admin/product/product.admin.component";
import {CategoryAdminComponent} from "./components/admin/category/category.admin.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'products/:id', component: DetailProductComponent},
  {path: 'orders', component: OrderComponent, canActivate: [AuthGuardFn]},
  {path: 'user-profile', component: OrderComponent, canActivate: [AuthGuardFn]},
  {path: 'orders/:id', component: OrderDetailComponent},
  // Admin
  {
    path: 'admin',
    component: AdminComponent,
    canActivate: [AdminGuardFn]
  },
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

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
