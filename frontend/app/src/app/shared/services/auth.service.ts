import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  getToken(): string | null {
    return localStorage.getItem('access_token');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    return token ? !this.isTokenExpired(token) : false;
  }

  private isTokenExpired(token: string): boolean {
    // Extract the expiration date from the token and convert it to a Date object
    const expirationDate = new Date(localStorage.getItem("expires_in"));
    return expirationDate < new Date();
  }
}
