import {Component} from '@angular/core';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
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
}

