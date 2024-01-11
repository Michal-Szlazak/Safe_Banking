import {Component, OnInit} from '@angular/core';
import {GetAccountsService} from "../shared/services/bank-services/get-accounts.service";
import {ToastrService} from "ngx-toastr";
import {TokenService} from "../shared/services/token.service";

interface BankAccount {
  accountName: string,
  accountNumber: string,
  cvv: string,
  expiresAt: Date,
  balance: number
}

@Component({
  selector: 'account-list',
  templateUrl: './account-list.component.html',
  styleUrl: './account-list.component.css'
})
export class AccountListComponent implements OnInit{
  accounts: BankAccount[] = [];

  constructor(private getAccountsService: GetAccountsService,
              private toastr: ToastrService,
              private tokenService: TokenService) {
  }

  ngOnInit() {
    const jwtToken = this.tokenService.getToken();

    this.getAccountsService.get(jwtToken).subscribe(
      result => {
        this.accounts = result;
      },
      error => {
        console.error('Error loading accounts:', error);
        this.toastr.error('Failed to load the accounts.', 'Failed');
      }
    );
  }
}
