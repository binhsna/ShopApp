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

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'admin', component: AdminComponent, canActivate: [AdminGuardFn]},
  {path: 'register', component: RegisterComponent},
  {path: 'products/:id', component: DetailProductComponent},
  {path: 'orders', component: OrderComponent, canActivate: [AuthGuardFn]},
  {path: 'user-profile', component: OrderComponent, canActivate: [AuthGuardFn]},
  {path: 'orders/:id', component: OrderDetailComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
