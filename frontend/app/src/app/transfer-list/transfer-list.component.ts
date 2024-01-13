import {Component, OnInit} from '@angular/core';
import {GetAccountsService} from "../shared/services/bank-services/get-accounts.service";
import {ToastrService} from "ngx-toastr";
import {TokenService} from "../shared/services/token.service";
import {TransferService} from "../shared/services/bank-services/transfer.service";

@Component({
  selector: 'transfer-list',
  templateUrl: './transfer-list.component.html',
  styleUrls: ['./transfer-list.component.css']
})
export class TransferListComponent implements OnInit {
  transfers: Transfer[] = [];

  constructor(private transferService: TransferService,
              private toastr: ToastrService,
              private tokenService: TokenService) {
  }

  ngOnInit() {
    const jwtToken = this.tokenService.getToken();

    this.transferService.get(jwtToken).subscribe(
      result => {
        this.transfers = result;
      },
      error => {
        this.toastr.error('Failed to load the accounts.', 'Failed');
      }
    );
  }
}

export interface Transfer {
  title: string;
  receiverName: string;
  senderNumber: string;
  receiverNumber: string;
  amount: number;
  timestamp: Date;
}
