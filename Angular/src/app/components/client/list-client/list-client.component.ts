import { Component, Input } from '@angular/core';
import { User } from 'src/app/Models/user';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';
import { AuthenticateService } from 'src/app/services/authenticate.service';

@Component({
  selector: 'app-list-client',
  templateUrl: './list-client.component.html',
  styleUrls: ['./list-client.component.css']
})
export class ListClientComponent {
  @Input() user!: User;
  users: User[] = [];
  userRoles: string[] = [];

  constructor ( 
    private userService : UserService,
    private router : Router,
    private authService: AuthenticateService) {}

  ngOnInit(): void {

    this.userService.getAllUsers().subscribe((data: User[]) => {
      this.users = data;
    });

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

  deleteUser(user: User): void {
    if (user && user.id) {
      this.userService.deleteUser(user.id).subscribe(
        () => {
          console.log(`User with ID ${user.id} deleted successfully.`);
          this.router.navigate(['/list-client']).then(() => {
            location.reload();
          });
          
        },
        (error) => {
          console.error('Error deleting user:', error);
        }
      );
    } else {
      console.error('Invalid user data for deletion.', user);
    }
  }
}
