import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {LoginDTO} from "../../dtos/user/login.dto";
import {LoginResponse} from "../../responses/user/login.response";
import {TokenService} from "../../services/token.service";
import {RoleService} from "../../services/role.service";
import {Role} from "../../models/role";
import {UserResponse} from "../../responses/user/user.response";
import {CartService} from "../../services/cart.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  @ViewChild("loginForm") loginForm!: NgForm
  /*
  // Login user
  phoneNumber: string = "0971912772";
  password: string = "123";
  */

  // Login admin
  phoneNumber: string = "0971912776";
  password: string = "123";
  showPassword: boolean = false;
  roles: Role[] = [];// Mảng roles
  rememberMe: boolean = true;
  selectedRole: Role | undefined; // Biến để lưu giá trị được chọn từ dropdown
  userResponse?: UserResponse;

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService,
    private cartService: CartService
  ) {
  }

  // Khi trang login được load
  ngOnInit() {
    // Gọi API lấy ra danh sách roles và lưu vào biến roles
    debugger
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => { // Sử dụng kiểu Role[]
        debugger
        this.roles = roles;
        this.selectedRole = roles.length > 0 ? roles[0] : undefined;
      }, error: (error: any) => {
        debugger
        console.error('Error getting roles: ', error);
      }
    });
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`);
  }

  login() {
    debugger
    const loginDTO: LoginDTO = {
      phone_number: this.phoneNumber,
      password: this.password,
      role_id: this.selectedRole?.id ?? 1
    }
    // Call api from userService
    this.userService.login(loginDTO).subscribe(
      {
        next: (response: LoginResponse) => {
          debugger
          const {token} = response; // const token = response.token;
          if (this.rememberMe) {
            this.tokenService.setToken(token);
            this.userService.getUserDetail(token).subscribe({
              next: (response: any) => {
                debugger;
                /*
                    this.userResponse = {
                    id: response.id,
                    fullname: response.fullname,
                    phone_number: response.phone_number,
                    address: response.address,
                    is_active: response.is_active,
                    date_of_birth: new Date(response.date_of_birth),
                    facebook_account_id: response.facebook_account_id,
                    google_account_id: response.google_account_id,
                    role: response.role
                  }
                 */
                this.userResponse = {
                  ...response,
                  date_of_birth: new Date(response.date_of_birth)
                };
                this.userService.saveUserResponseToLocalStorage(this.userResponse);
                if (this.userResponse?.role.name.toUpperCase().trim() == 'ADMIN') {
                  this.router.navigate(['/admin']);
                } else if (this.userResponse?.role.name.toUpperCase().trim() == 'USER') {
                  this.router.navigate(['/']);
                }

              },
              complete: () => {
                debugger;
                this.cartService.refreshCart();
              }, error: (error: any) => {
                alert(error?.error?.message)
              }
            });
          }
          // npm install @ngrx/store

        },
        complete: () => {
          debugger
        }, error: (error: any) => {
          // Xử lý lỗi nếu có
          alert(error?.error?.message);
        }
      }
    );
  }
}
