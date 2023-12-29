import { Component } from '@angular/core';
import {AuthService} from "../shared/services/auth.service";
import {LogoutService} from "../shared/services/logout.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html'
})
export class AppHeaderComponent {
  constructor(private authService: AuthService,
              private logoutService: LogoutService,
              private router: Router
  ) {}

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(): void {
  console.log("logout");
    this.logoutService.logout().subscribe({
      next: (success) => {
        if (success) {
          console.log('Logout successful');
          this.router.navigate(['/']);
          // Redirect or perform other actions after successful login
        } else {
          console.error('Login failed');
        }
      },
      error: (err) => {
        alert('There was an error in retrieving data from the server');
      }
    });
  }
}
