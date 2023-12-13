import { Component } from '@angular/core';
import { LoginService } from "../shared/services/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(private loginService: LoginService) {}

  loginData = {
    email: '',
    password: ''
  };

  onSubmit() {
    this.loginService.login(this.loginData.email, this.loginData.password).subscribe(
      response => console.log(response)
    );
  }
}
