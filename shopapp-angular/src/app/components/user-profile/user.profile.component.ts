import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, NgForm, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "../../services/user.service";
import {UserResponse} from "../../responses/user/user.response";

@Component({
    selector: 'app-user-profile',
    templateUrl: './user.profile.component.html',
    styleUrls: ['./user.profile.component.scss']
})

export class UserProfileComponent implements OnInit {
    userProfile: FormGroup;
    userResponse?: UserResponse | null;
    a!: {
        fullname: string,
        address: string,
        date_of_birth: Date,
        password: string,
        retype_password: string
    };

    constructor(
        private userService: UserService,
        private fb: FormBuilder,
        private router: Router,
    ) {
        // Tạo FormGroup và các FormControl tương ứng
        this.userProfile = this.fb.group({
            fullname: ['Nguyễn Công Bình', Validators.required],
            email: ['binhsna@gmail.com', [Validators.email]],
            phone_number: ['0971912776', [Validators.required, Validators.minLength(6)]],
            address: ['Nhà a ngõ b', [Validators.required, Validators.minLength(5)]],
            note: ['Cẩn thận'],
            shipping_method: ['express'],
            payment_method: ['cod']
        });
    }

    ngOnInit() {
        this.userResponse = this.userService.getUserResponseFromLocalStorage();
    }

    checkPasswordMatch() {
        if (this.a.password !== this.a.retype_password) {
            this.userProfile.controls["retype_password"]
                .setErrors({'passwordMismatch': true});
        } else {
            this.userProfile.controls["retype_password"]
                .setErrors(null);
        }
    }

    save() {

    }
}


