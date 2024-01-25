import { Component } from '@angular/core';
import {
  AbstractControl,
  AbstractControlOptions,
  FormBuilder,
  FormGroup,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {ChangePasswordService} from "../shared/services/change-password.service";
import {ApiErrorResponse} from "../entities/HttpResponse";
import {passwordValidator} from "../register/register.component";

export function passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
  const password = control.get('newPassword')?.value;
  const confirmPassword = control.get('confirmNewPassword')?.value;
  return password === confirmPassword ? null : { passwordMismatch: true };
}

@Component({
  selector: 'change-password',
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css'
})
export class ChangePasswordComponent {

  changePasswordForm: FormGroup;
  changePasswordError = false;
  errorMessage: string = "";
  errorMessages: string[];

  constructor(private changePasswordService: ChangePasswordService,
              private fb: FormBuilder,
              private router: Router,
              private toastr: ToastrService) {
    this.changePasswordForm = this.fb.group({
      oldPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
      newPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20), passwordValidator]],
      confirmNewPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]]
    }, { validator: passwordMatchValidator } as AbstractControlOptions);
  }

  onSubmit() {
    if (this.changePasswordForm.valid) {
      this.changePasswordService.changePassword(this.changePasswordForm.value).subscribe({
        next: (response) => {

          // Check if the response is an HttpResponse (success)
          if (response.status === 200) {
            this.changePasswordError = false;
            this.toastr.success('Password changed successfully');
            this.router.navigate(['/protected-home']);
          } else {
            // Handle unexpected response format
            console.error('Unexpected response format:', response);
          }
        },
        error: (err: ApiErrorResponse) => {
          this.changePasswordError = true;
          this.errorMessage = err.message;
          this.errorMessages = err.errors;
        }
      });
    }
  }

}
