import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticateService } from 'src/app/services/authenticate.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  isLoggedIn: boolean = false;

  constructor(private authService: AuthenticateService, private router: Router) {}

  ngOnInit() {
    this.isLoggedIn = this.authService.isAuthenticated();
  }
}
