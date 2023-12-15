import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {FormGroup} from "@angular/forms";
import {Observable} from "rxjs";

interface LoginData {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private apiUrl = '/api/login';

  constructor(private http: HttpClient) {}

  login(loginData: LoginData): Observable<any> {
    return this.http.post(this.apiUrl, loginData);
  }
}
