import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TransferService} from "../shared/services/bank-services/transfer.service";
import {TokenService} from "../shared/services/token.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {MatDialog} from "@angular/material/dialog";
import {PartialPasswordComponent} from "../partial-password/partial-password.component";

export function lettersAndSpaceValidator(control: { value: string; }) {

  const isValid = /^[a-zA-Z\s]+$/.test(control.value);
  return isValid ? null : { 'lettersAndSpaceValidator': true };
}

export function accountNumberValidator(control: { value: string; }) {

  const isValid = /^[A-Z]{2}\d{26}$/.test(control.value);
  return isValid ? null : { 'accountNumberValidator': true };
}

export function amountValidation(control: { value: number; }) {

  const value = control.value;

  if (value <= 0) {
    return { 'amountValidation': true };
  }

  // Check if it has exactly two decimal places
  const regex = /^[0-9]+(\.[0-9]{1,2})?$/;
  if (!regex.test(value.toString())) {
    return { 'amountValidation': true };
  }

  return null;
}

@Component({
  selector: 'create-transfer',
  templateUrl: './create-transfer.component.html',
  styleUrl: './create-transfer.component.css'
})
export class CreateTransferComponent {
  bankTransferForm: FormGroup;

  constructor(private fb: FormBuilder,
              private transferService: TransferService,
              private tokenService: TokenService,
              private toastr: ToastrService,
              private router: Router,
              private dialog: MatDialog) { }

  ngOnInit(): void {
    this.bankTransferForm = this.fb.group({
      receiverName: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), lettersAndSpaceValidator]],
      receiverAccount: ['', [Validators.required, Validators.minLength(28), Validators.maxLength(28), accountNumberValidator]],
      senderAccount: ['', [Validators.required, Validators.minLength(28), Validators.maxLength(28), accountNumberValidator]],
      title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(20), lettersAndSpaceValidator]],
      amount: [0, [Validators.required, amountValidation]]
    });
  }

  submitTransferForm() {

    if (this.bankTransferForm.valid) {

      const dialogRef = this.dialog.open(PartialPasswordComponent, {
        width: '500px',
        disableClose: true,
      });

      dialogRef.afterClosed().subscribe((result) => {

        if (result === 200) {

          const formValues = this.bankTransferForm.value;
          const jwtToken = this.tokenService.getToken();
          this.transferService.create(this.bankTransferForm.value, jwtToken).subscribe(
            success => {
              if (success) {
                this.toastr.success('Transfer sent successfully', 'Success');
                this.router.navigate(['/protected-home']);
              } else {
                this.toastr.error('Failed to make the transfer.', 'Failed');
              }
            },
            error => {
              this.toastr.error('Failed to make the transfer', 'Failed');
            }
          );

        } else {
          this.toastr.error('Failed to authenticate.', 'Unauthorized');
        }
      });

    } else {
      // Handle invalid form
      console.log('Form is invalid. Please check the fields.');
    }
  }
}
