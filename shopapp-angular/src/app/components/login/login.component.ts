import {Component, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {LoginDTO} from "../../dtos/user/login.dto";
import {LoginResponse} from "../../responses/user/login.response";
import {TokenService} from "../../services/token.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  @ViewChild("loginForm") loginForm!: NgForm
  phoneNumber: string;
  password: string;

  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService
  ) {
    this.phoneNumber = "";
    this.password = "";
  }

  onPhoneNumberChange() {
    console.log(`Phone typed: ${this.phoneNumber}`);
  }

  login() {
    debugger
    const loginDTO: LoginDTO = {
      "phone_number": this.phoneNumber,
      "password": this.password
    }
    // Call api from userService
    this.userService.login(loginDTO).subscribe(
      {
        next: (response: LoginResponse) => {
          debugger
          const {token} = response; // const token = response.token;
          this.tokenService.setToken(token);
          // npm install @ngrx/store
          // this.router.navigate(['/login']);
        },
        complete: () => {
          debugger
        }, error: (error: any) => {
          // Xử lý lỗi nếu có
          alert(`Cannot login, error: ${error.error}`)
        }
      }
    );
  }
}
