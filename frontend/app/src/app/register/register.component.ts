import { Component } from '@angular/core';
import { RegisterService } from "../shared/services/register.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {

  registerForm: FormGroup;
  constructor(private registerService: RegisterService, private fb: FormBuilder) {

    this.registerForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirmPassword: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  registerData = {
    name: '',
    surname: '',
    email: '',
    phoneNumber: '',
    password: '',
    confirmPassword: ''
  };

  onSubmit() {

  }

  private passwordMatchValidator(formGroup: FormGroup) {
    const password = formGroup.get('password').value;
    const confirmPassword = formGroup.get('confirmPassword').value;

    if (password !== confirmPassword) {
      formGroup.get('confirmPassword').setErrors({ mismatch: true });
    } else {
      formGroup.get('confirmPassword').setErrors(null);
    }
  }
}
