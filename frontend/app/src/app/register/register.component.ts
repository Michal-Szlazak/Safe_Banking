import {Component, OnInit} from '@angular/core';
import { RegisterService } from "../shared/services/register.service";
import {ApiErrorResponse, SuccessResponse} from "../entities/HttpResponse";
import {
  AbstractControl,
  AbstractControlOptions,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {Router} from "@angular/router";

export function onlyLettersValidator(control: { value: string; }) {

  const isValid = /^[a-zA-Z]+$/.test(control.value);
  return isValid ? null : { 'onlyLettersValidator': true };
}

export function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get('password')?.value;
  const confirmPassword = control.get('confirmPassword')?.value;
  return password === confirmPassword ? null : { passwordMismatch: true };
}

export function passwordValidator(control: {value: string; }) {
  const value: string = control.value;

  // Check for at least one uppercase letter, one lowercase letter, one digit, and one special character
  const hasUppercase = /[A-Z]/.test(value);
  const hasLowercase = /[a-z]/.test(value);
  const hasDigit = /\d/.test(value);
  const hasSpecialChar = /[!@#$%^&*()-_=+{}[\]:;'",.<>/?\\|]/.test(value);

  // Return null if all conditions are met, indicating a valid password
  if (hasUppercase && hasLowercase && hasDigit && hasSpecialChar) {
    return null;
  } else {
    // Return an object with an error key if any condition is not met
    return { 'passwordValidator': true };
  }
}

export function noWhiteSpaceValidator(control: {value: string; }) {
  return !/\\s/.test(control.value);
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  registerForm: FormGroup;
  registerError: boolean = false;
  errorMessage: string = "";
  errorMessages: string[];

  constructor(private registerService: RegisterService, private fb: FormBuilder, private router: Router) {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), onlyLettersValidator]],
      lastName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), onlyLettersValidator]],
      email: ['', [Validators.required, Validators.maxLength(20), Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), passwordValidator
      ,noWhiteSpaceValidator]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
    }, { validator: passwordMatchValidator } as AbstractControlOptions);
  }

  onSubmit() {
    console.log("submit");
    if (this.registerForm.valid) {
      // Call your registration service and handle the submission
      this.registerService.register(this.registerForm.value).subscribe({
        next: (response) => {
          // Handling success
          this.registerError = false;
          this.router.navigate(['/successful-register']);
          // Redirect or perform other actions after successful registration
        },
        error: (err: ApiErrorResponse) => {
          this.registerError = true;
          this.errorMessage = err.message;
          this.errorMessages = err.errors;
          // Checking for a specific error structure (assuming ApiError)
        }
      });
    }
  }


}

