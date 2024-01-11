import { Component } from '@angular/core';
import {AbstractControlOptions, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {RegisterService} from "../shared/services/register.service";
import {Router} from "@angular/router";
import {entropyValidator} from "../register/entropy.validator";
import {onlyLettersValidator} from "../register/register.component";
import {CreateAccountService} from "../shared/services/bank-services/create-account.service";
import {AuthService} from "../shared/services/auth.service";
import {TokenService} from "../shared/services/token.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'create-account',
  templateUrl: './create-bank-account.component.html',
  styleUrl: './create-bank-account.component.css'
})
export class CreateBankAccountComponent {

  form: FormGroup;
  registerError: boolean = false;
  errorMessage: string = "";
  errorMessages: string[];
  private jwtToken: string;

  constructor(private fb: FormBuilder, private router: Router,
              private createAccountService: CreateAccountService,
              private tokenService: TokenService,
              private toastr: ToastrService) {
    this.form = this.fb.group({
      accountName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), onlyLettersValidator]]
    });
  }
  onSubmit() {
    this.jwtToken = this.tokenService.getToken();
    console.log(this.jwtToken);
    this.createAccountService.create(this.form.value, this.jwtToken).subscribe(
      success => {
        if (success) {
          this.toastr.success('Account created successfully', 'Success');
          this.router.navigate(['/protected-home']);
          console.log('Account creation successful');
        } else {
          this.errorMessage = "Failed to create the account.";
          this.toastr.error('Failed to create the account.', 'Failed');
          console.error('Account creation failed');
        }
      },
      error => {
        this.errorMessage = "Failed to create the account.";
        this.toastr.error('Failed to create the account.', 'Failed');
        console.error('Unexpected error during account creation:', error);
      }
    );
  }
}
