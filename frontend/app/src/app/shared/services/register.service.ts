import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  constructor(private http: HttpClient) {}

  register(name: string, password: string) {
    const loginData = { name, password };
    return this.http.post('/api/login', loginData);
  }
}
