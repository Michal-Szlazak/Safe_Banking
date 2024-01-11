import { Component } from '@angular/core';
import { LoginService } from "../shared/services/login.service";
import {AbstractControlOptions, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {interval, Subscription} from "rxjs";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent {

  loginForm: FormGroup;
  loginError = false;

  loginAttempts: number = 0;
  maxLoginAttempts: number = 5;
  isUserBlocked: boolean = false;
  remainingTimeSubscription: Subscription | undefined;
  remainingTime: number = 60;
  blockDuration = 60 * 1000;

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
            this.router.navigate(['/protected-home']);
            // Redirect or perform other actions after successful login
          } else {
            console.error('Login failed');
            this.loginAttempts++;
            this.loginError = true;

            console.log(this.loginAttempts);
            if(this.loginAttempts >= this.maxLoginAttempts) {
              this.blockUser();
            }
          }
        },
        error: (err) => {
          alert('There was an error in retrieving data from the server');
          this.loginError = true;
        }
      });
    }
  }

  private calculateRemainingTime(): void {
    this.remainingTime--;
  }

  private blockUser(): void {
    this.isUserBlocked = true;
    this.remainingTime = 60;
    this.calculateRemainingTime(); // Trigger initial calculation
    this.startRemainingTimeUpdate();
    this.toggleControls();

    setTimeout(() => {
      this.unlockUser();
      this.stopRemainingTimeUpdate();
    }, this.blockDuration);
  }

  private unlockUser(): void {
    this.isUserBlocked = false;
    this.loginAttempts = 0;
    this.toggleControls();
  }

  private startRemainingTimeUpdate(): void {
    // Update remaining time every second
    this.remainingTimeSubscription = interval(1000).subscribe(() => {
      this.calculateRemainingTime();
    });
  }

  private stopRemainingTimeUpdate(): void {
    // Stop the interval when it's no longer needed
    if (this.remainingTimeSubscription) {
      this.remainingTimeSubscription.unsubscribe();
    }
  }

  toggleControls(): void {
    if (this.isUserBlocked) {
      // this.loginForm.get('email')?.disable();
      // this.loginForm.get('password')?.disable();
      // this.loginForm.get('button')?.disable();
      this.loginForm.disable();
    } else {
      // this.loginForm.get('email')?.enable();
      // this.loginForm.get('password')?.enable();
      // this.loginForm.get('button')?.enable();
      this.loginForm.enable();
    }
  }

  ngOnDestroy(): void {
    // Ensure to unsubscribe when the component is destroyed
    this.stopRemainingTimeUpdate();
  }
}
