import {Component, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {RegisterDTO} from "../../dtos/user/register.dto";

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
    // Tham chiếu đến form trên view
    @ViewChild("registerForm") registerForm!: NgForm
    // Khai báo các biến tương ứng với các trường dữ liệu trong java
    phoneNumber: string;
    password: string;
    showPassword: boolean = false;
    retypePassword: string;
    showRetypePassword: boolean = false;
    fullName: string;
    address: string;
    isAccepted: boolean;
    dateOfBirth: Date;

    constructor(private router: Router, private userService: UserService) {
        this.phoneNumber = "";
        this.password = "";
        this.retypePassword = "";
        this.fullName = "";
        this.address = "";
        this.isAccepted = true;
        this.dateOfBirth = new Date();
        this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
        // Inject http: Gửi API, router: Chuyển màn hình
    }

    onPhoneNumberChange() {
        console.log(`Phone typed: ${this.phoneNumber}`);
    }

    register() {
        /*
        const message = `phone: ${this.phone}\n` +
          `password: ${this.password}\n` +
          `retypePassword: ${this.retypePassword}\n` +
          `fullName: ${this.fullName}\n` +
          `address: ${this.address}\n` +
          `isAccepted: ${this.isAccepted}\n` +
          `dateOfBirth: ${this.dateOfBirth}\n`;
        */
        //alert(message);
        debugger

        const registerDTO: RegisterDTO = {
            "fullname": this.fullName,
            "phone_number": this.phoneNumber,
            "address": this.address,
            "password": this.password,
            "retype_password": this.retypePassword,
            "date_of_birth": this.dateOfBirth,
            "facebook_account_id": 0,
            "google_account_id": 0,
            "role_id": 1
        }
        // Call api from userService
        this.userService.register(registerDTO).subscribe(
            {
                next: (response: any) => {
                    debugger
                    this.router.navigate(['/login']);
                },
                complete: () => {
                    debugger
                }, error: (error: any) => {
                    // Xử lý lỗi nếu có
                    alert(`Cannot register, error: ${error.error}`)
                }
            }
        );
    }

    // How to check password match?
    checkPasswordMatch() {
        if (this.password !== this.retypePassword) {
            this.registerForm.form.controls["retypePassword"]
                .setErrors({'passwordMismatch': true});
        } else {
            this.registerForm.form.controls["retypePassword"]
                .setErrors(null);
        }
    }

    // Check Age
    checkAge() {
        if (this.dateOfBirth) {
            const today = new Date();
            const birthDate = new Date(this.dateOfBirth);
            let age = today.getFullYear() - birthDate.getFullYear();
            const monthDiff = today.getMonth() - birthDate.getMonth();
            if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
                age--;
            }
            if (age < 18) {
                this.registerForm.form.controls["dateOfBirth"].setErrors({'invalidAge': true});
            } else {
                this.registerForm.form.controls["dateOfBirth"].setErrors(null);
            }
        }
    }

    togglePassword() {
        this.showPassword = !this.showPassword;
    }

    toggleRetypePassword() {
        this.showRetypePassword = !this.showRetypePassword;
    }
}

