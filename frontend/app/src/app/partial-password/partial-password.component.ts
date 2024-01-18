import {Component, Inject, OnInit} from '@angular/core';
import {PartialPasswordService} from "../shared/services/partial-password.service";
import {of} from "rxjs";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'partial-password',
  templateUrl: './partial-password.component.html',
  styleUrl: './partial-password.component.css'
})
export class PartialPasswordComponent implements OnInit{
  password: string[] = Array(4).fill('');
  partialPasswordGet: PartialPasswordGet;
  passwordForm: FormGroup;
  passwordArray: { part: number, value: string }[] = [];
  constructor(private partialPasswordService: PartialPasswordService,
              private fb: FormBuilder,
              public dialogRef: MatDialogRef<PartialPasswordComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.passwordForm = this.fb.group({
      passwordInput0: ['', [Validators.required]],
      passwordInput1: ['', [Validators.required]],
      passwordInput2: ['', [Validators.required]],
      passwordInput3: ['', [Validators.required]],
    });
  }

  ngOnInit() {
    this.partialPasswordService.getPartialPassword().subscribe(
      (partialPassword: PartialPasswordGet) => {
        this.partialPasswordGet = partialPassword;
        this.passwordArray = partialPassword.parts.map(part => ({ part, value: '' }));

        const formControls = {};
        this.passwordArray.forEach(entry => {
          formControls[`passwordInput${entry.part}`] = [entry.value, Validators.required];
        });

        // Create the form group
        this.passwordForm = new FormGroup(formControls);
      },
      (error) => {
        // Handle the error here
        console.error('Error fetching partial password:', error);
      }
    );
  }

  submitPassword(): void {
    // Handle the submitted password
    const partsObject = {};
    partsObject[this.passwordArray.at(0).part] = this.passwordForm.value.passwordInput0;
    partsObject[this.passwordArray.at(1).part] = this.passwordForm.value.passwordInput1;
    partsObject[this.passwordArray.at(2).part] = this.passwordForm.value.passwordInput2;
    partsObject[this.passwordArray.at(3).part] = this.passwordForm.value.passwordInput3;


    const requestBody = {
      signature: this.partialPasswordGet.signature,
      parts: partsObject
    };

    this.partialPasswordService.checkPartialPassword(requestBody).subscribe(
      (response) => {
        this.dialogRef.close(response);
      },
      () => {
        // Handle the error here
        this.dialogRef.close(400);
      }
    );
  }

  protected readonly of = of;
}

interface PartialPasswordGet {
  parts: number[],
  signature: string
}
