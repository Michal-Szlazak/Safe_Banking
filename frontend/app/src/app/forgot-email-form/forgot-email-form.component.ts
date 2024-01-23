import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ForgotPasswordService} from "../shared/services/forgotPassword.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-forgot-email-form',
  templateUrl: './forgot-email-form.component.html',
  styleUrl: './forgot-email-form.component.css'
})
export class ForgotEmailFormComponent {
  passwordResetForm: FormGroup;

  constructor(private formBuilder: FormBuilder, private forgotPasswordService: ForgotPasswordService,
              private router: Router) {
    this.passwordResetForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.passwordResetForm.valid) {
      const email = this.passwordResetForm.get('email').value;
      // Call a service or perform an action to send the reset link using the email value
      this.forgotPasswordService.sendEmail(email).subscribe({
        next: (success) => {
          if (success) {
            console.log('ForgotEmail sent successfully');
            this.router.navigate(['/forgot-password-email-sent']);
          } else {
            console.error('ForgotEmail error');
          }
        }
      });
      console.log('Sending password reset link to:', email);
    }
  }
}
