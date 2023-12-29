import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable} from "rxjs";

interface RegisterData {
  name: string;
  surname: string;
  email: string;
  phoneNumber: string;
  password: string;
  confirmPassword: string;
}

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  constructor(private http: HttpClient) {}

  register(registerData: RegisterData) : Observable<any> {
    return this.http.post('/api/login', registerData);
  }
}
