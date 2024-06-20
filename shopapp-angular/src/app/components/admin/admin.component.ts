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
    }

    logout() {
        this.userService.removeUserResponseFromLocalStorage();
        this.tokenService.removeToken();
        this.userResponse = this.userService.getUserResponseFromLocalStorage();
    }

    showAdminComponent(componentName: string) {
        this.adminComponent = componentName;
    }
}
