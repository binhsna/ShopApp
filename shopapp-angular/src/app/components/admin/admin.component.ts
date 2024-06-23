import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {TokenService} from "../../services/token.service";
import {UserResponse} from "../../responses/user/user.response";
import {Router} from "@angular/router";

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
    adminComponent: string = 'orders';
    userResponse?: UserResponse | null;

    constructor(
        private userService: UserService,
        private tokenService: TokenService,
        private router: Router
    ) {
    }

    ngOnInit() {
        this.userResponse = this.userService.getUserResponseFromLocalStorage();
        if (this.router.url === '/admin') {
            this.router.navigate(['/admin/orders']);
        }
    }

    logout() {
        this.userService.removeUserResponseFromLocalStorage();
        this.tokenService.removeToken();
        this.userResponse = this.userService.getUserResponseFromLocalStorage();
        this.router.navigate(['/']);
    }

    showAdminComponent(componentName: string) {
        //this.adminComponent = componentName;
        if (componentName === "orders") {
            this.router.navigate(['/admin/orders']);
        } else if (componentName === "categories") {
            this.router.navigate(['/admin/categories']);
        } else if (componentName === "products") {
            this.router.navigate(['/admin/products']);
        }
    }
}
