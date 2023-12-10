
import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { CarDTO } from 'src/app/Models/CarDTO';
import { CarsService } from 'src/app/services/cars.service';
import { ListVoitureUserComponent } from '../cars/list-voiture-user/list-voiture-user.component';
// import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticateService } from 'src/app/services/authenticate.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})

export class HeaderComponent implements OnInit {

  isLoggedIn: boolean = false;
  userRoles: string[] = [];

  constructor(private authService: AuthenticateService, private router: Router) {}

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthenticated();
    const userId=this.authService.getCurrentUserId();
    console.log("userid", userId);

    this.authService.getUserRolesById(userId).subscribe(
      (roles) => {
        this.userRoles = roles;
        console.log('Roles:', roles);
      },
      (error) => {
        console.error('Error fetching user roles:', error);
      }
    );
  }
  logout() {
    this.authService.logout();
    this.isLoggedIn = false;
  }
}
