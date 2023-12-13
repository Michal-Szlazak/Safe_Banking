import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    const loginData = { username, password };
    return this.http.post('/api/login', loginData);
  }
}
