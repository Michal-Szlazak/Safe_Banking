import { Component } from '@angular/core';
import {AuthService} from "../shared/services/auth.service";
import {LogoutService} from "../shared/services/logout.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html'
})
export class AppHeaderComponent {
  constructor(private authService: AuthService,
              private logoutService: LogoutService,
              private router: Router,
              private toastr: ToastrService
  ) {}

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(): void {

    this.logoutService.logout().subscribe({
      next: (success) => {
        if (success) {
          this.toastr.success("Logout successful");
          this.router.navigate(['/']);
          // Redirect or perform other actions after successful login
        } else {
          this.toastr.error("Logout failed");
        }
      },
      error: (err) => {
        alert('There was an error in retrieving data from the server');
      }
    });
  }
}
