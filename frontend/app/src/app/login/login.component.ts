import { Component } from '@angular/core';
import { LoginService } from "../shared/services/login.service";
import {AbstractControlOptions, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;
  loginError = false;

  constructor(private loginService: LoginService, private fb: FormBuilder, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.maxLength(20), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]]
    });
  }

  onSubmit() {
    console.log("submit login");
    if (this.loginForm.valid) {
      this.loginService.login(this.loginForm.value).subscribe({
        next: (success) => {
          if (success) {
            console.log('Login successful');
            this.loginError = false;
            this.router.navigate(['/']);
            // Redirect or perform other actions after successful login
          } else {
            console.error('Login failed');
            this.loginError = true;
            // Handle login failure (show error message, etc.)
          }
        },
        error: (err) => {
          alert('There was an error in retrieving data from the server');
          this.loginError = true;
        }
      });
    }
  }
}
