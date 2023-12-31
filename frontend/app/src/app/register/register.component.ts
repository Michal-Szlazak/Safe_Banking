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
import {entropyValidator} from "./entropy.validator";

export function onlyLettersValidator(control: { value: string; }) {

  const isValid = /^[a-zA-Z]+$/.test(control.value);
  return isValid ? null : { 'onlyLettersValidator': true };
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit {
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
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), entropyValidator]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
    }, { validator: this.passwordMatchValidator } as AbstractControlOptions);
  }

  ngOnInit(): void {
    // If you have any initialization logic, you can place it here
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
          console.error('Registration failed', err);
          this.registerError = true;
          this.errorMessage = err.message;
          this.errorMessages = err.errors;
          // Checking for a specific error structure (assuming ApiError)
        }
      });
    }
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }
}

