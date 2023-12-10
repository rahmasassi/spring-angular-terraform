import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticateService } from './authenticate.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authService: AuthenticateService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    const isAuthenticated = this.authService.isAuthenticated();

    if (isAuthenticated) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}