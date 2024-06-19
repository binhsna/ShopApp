import {inject, Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateFn, Router} from "@angular/router";
import {TokenService} from "../services/token.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {
  constructor(
    private tokenService: TokenService,
    private router: Router) {
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isTokenExpired = this.tokenService.isTokenExpired();
    const isUserIdValid = this.tokenService.getUserId() > 0;
    debugger;
    if (!isTokenExpired && isUserIdValid) {
      return true;
    } else {
      // Nếu không authenticated, bạn có thể redirect hoặc trả về một UrlTree khác.
      this.router.navigate(['/login']);
      return false;
    }
  }
}

// Sử dụng function guard như sau
export const AuthGuardFn: CanActivateFn = (
  next: ActivatedRouteSnapshot,
  state: RouterStateSnapshot): boolean => {
  debugger;
  return inject(AuthGuard).canActivate(next, state);
}
