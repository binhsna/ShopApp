import {Component, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  // Tham chiếu đến form trên view
  @ViewChild("registerForm") registerForm!: NgForm
  // Khai báo các biến tương ứng với các trường dữ liệu trong java
  phone: string;
  password: string;
  retypePassword: string;
  fullName: string;
  address: string;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor() {
    this.phone = "";
    this.password = "";
    this.retypePassword = "";
    this.fullName = "";
    this.address = "";
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
  }

  onPhoneChange() {
    console.log(`Phone typed: ${this.phone}`);
  }

  register() {
    const message = `phone: ${this.phone}\n` +
      `password: ${this.password}\n` +
      `retypePassword: ${this.retypePassword}\n` +
      `fullName: ${this.fullName}\n` +
      `address: ${this.address}\n` +
      `isAccepted: ${this.isAccepted}\n` +
      `dateOfBirth: ${this.dateOfBirth}\n`
    ;
    alert(message);
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
}

