import {Component, OnInit} from '@angular/core';
import { RegisterService } from "../shared/services/register.service";
import {
  AbstractControl,
  AbstractControlOptions,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;

  constructor(private registerService: RegisterService, private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      name: ['', [Validators.required, Validators.maxLength(20)]],
      surname: ['', [Validators.required, Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.maxLength(20), Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
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
      this.registerService.register(this.registerForm.value)

    }
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }
}

