import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/user.service";
import {UserResponse} from "../../responses/user/user.response";
import {NgbPopoverConfig} from "@ng-bootstrap/ng-bootstrap";
import {CartService} from "../../services/cart.service";
import {TokenService} from "../../services/token.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  userResponse?: UserResponse | null;
  isPopoverOpen = false;

  constructor(
    private userService: UserService,
    private popoverConfig: NgbPopoverConfig,
    private tokenService: TokenService
  ) {
  }

  ngOnInit() {
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
  }

  togglePopover(event: Event): void {
    event.preventDefault();
    this.isPopoverOpen = !this.isPopoverOpen;
  }

  handleItemClick(index: number): void {
    // Handle the click event for earch popover item
    // You can implement the logic you need here
    // alert(`Clicked on ${index}`);
    if (index == 2) {
      this.userService.removeUserResponseFromLocalStorage();
      this.tokenService.removeToken();
      this.userResponse = this.userService.getUserResponseFromLocalStorage();
    }
    this.isPopoverOpen = false; // Close the popover after clicking an item
  }
}
