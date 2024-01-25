import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { AppHeaderComponent } from './header/app-header.component';
import {RouterModule, Routes} from "@angular/router";
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
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
import { PartialPasswordComponent } from './partial-password/partial-password.component';
import {TokenInterceptor} from "./shared/services/token-interceptor";
import {ForgotEmailFormComponent} from "./forgot-email-form/forgot-email-form.component";
import {ForgotEmailSentComponent} from "./forgot-email-sent/forgot-email-sent.component";
import { ChangePasswordComponent } from './change-password/change-password.component';


const appRoutes: Routes = [
  { path: '', component:  HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'successful-register', component: SuccessfulRegisterComponent },
  { path: 'forgot-email-form', component: ForgotEmailFormComponent },
  { path: 'forgot-password-email-sent', component: ForgotEmailSentComponent },
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
      },
      {
        path: 'partial-password',
        component: PartialPasswordComponent,
      },
      {
        path: 'change-password',
        component: ChangePasswordComponent,
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
    TransferListComponent,
    PartialPasswordComponent,
    ForgotEmailFormComponent,
    ChangePasswordComponent
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
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
