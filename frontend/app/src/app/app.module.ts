import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppHeaderComponent } from './header/app-header.component';
import {RouterModule, Routes} from "@angular/router";
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { HttpClientModule } from '@angular/common/http';
import { RegisterComponent } from './register/register.component';
import { SuccessfulRegisterComponent } from './successful-register/successful-register.component';
import { MenuBarComponent } from './menu-bar/menu-bar.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatCardModule} from "@angular/material/card";
import { CreateBankAccountComponent } from './create-bank-account/create-bank-account.component';
import { ProtectedHomeComponent } from './protected-home/protected-home.component';
import {canActivate, canActivateChild} from "./shared/AuthGuard";
import {ToastrModule} from "ngx-toastr";
import { AccountListComponent } from './account-list/account-list.component';
import { CreateTransferComponent } from './create-transfer/create-transfer.component';
import { TransferListComponent } from './transfer-list/transfer-list.component';


const appRoutes: Routes = [
  { path: '', component:  HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'successful-register', component: SuccessfulRegisterComponent },
  { path: 'protected-home',
    canActivate: [canActivate],
    canActivateChild: [canActivateChild],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: 'transfer-list',
      },
      {
        path: 'create-account',
        component: CreateBankAccountComponent,
      },
      {
        path: 'account-list',
        component: AccountListComponent,
      },
      {
        path: 'create-transfer',
        component: CreateTransferComponent,
      },
      {
        path: 'transfer-list',
        component: TransferListComponent,
      }
    ],
    component: ProtectedHomeComponent
  }
];
@NgModule({
  declarations: [
    AppComponent,
    AppHeaderComponent,
    LoginComponent,
    HomeComponent,
    RegisterComponent,
    SuccessfulRegisterComponent,
    MenuBarComponent,
    CreateBankAccountComponent,
    ProtectedHomeComponent,
    AccountListComponent,
    CreateTransferComponent,
    TransferListComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatCardModule,
    ToastrModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
