import {inject, Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivateFn, Router} from "@angular/router";
import {TokenService} from "../services/token.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(
    private tokenService: TokenService,
    private router: Router) {
  }

  canActive(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const isTokenExpired = this.tokenService.isTokenExpired();
    const isUserIdValid = this.tokenService.getUserId() > 0;
    debugger;
    if (!isTokenExpired && isUserIdValid) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}

// Sử dụng function guard như sau
export const AuthGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  debugger;
  return inject(AuthGuard).canActive(next, state);
}
