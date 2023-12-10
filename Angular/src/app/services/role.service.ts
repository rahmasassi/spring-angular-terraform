import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticateService } from './authenticate.service';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  userRoles: string[] = [];

  constructor(private authService: AuthenticateService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean> {
    const userId = this.authService.getCurrentUserId();

    return this.authService.getUserRolesById(userId).pipe(
      map(roles => {
        console.log('Roles:', roles);

        if (roles.includes('ROLE_ADMIN')) {
          return true;
        } else {
          this.router.navigate(['/home']);
          return false;
        }
      })
    );
  }
}