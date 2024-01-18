import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {TokenDTO} from "../../dtos/TokensDTO";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root',
})
export class TokenService {

  constructor(private http: HttpClient, private router: Router) {}

  isUserLoggedIn(): boolean {
    return !this.isRefreshTokenExpired();
  }

  getToken(): string {
    return this.getAccessToken();
  }

  private isTokenExpired(): boolean {
    const expirationDate = new Date(localStorage.getItem("expires_in"));
    return expirationDate < new Date();
  }

  private isRefreshTokenExpired(): boolean {
    const expirationDate = new Date(localStorage.getItem("refresh_expires_in"));
    return expirationDate < new Date();
  }

  public getNewToken(refresh_token: string) : Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'refresh_token': refresh_token
    });
    const apiUrl: string = 'https://localhost:8443/api/auth/user/public/refreshToken'

    return this.http.post<TokenDTO>(`${apiUrl}`, {}, { headers }).pipe(
      map((response: TokenDTO) => {
        localStorage.clear();

        const currentTime = new Date();
        const accessTokenExpiration = new Date(
          currentTime.getTime() + parseInt(response.expires_in) * 1000); // Convert seconds to milliseconds
        const refreshTokenExpiration = new Date(
          currentTime.getTime() + parseInt(response.refresh_expires_in) * 1000);

        localStorage.setItem('access_token', response.access_token);
        localStorage.setItem('expires_in', accessTokenExpiration.toString());
        localStorage.setItem('refresh_token', response.refresh_token);
        localStorage.setItem('expires_in', refreshTokenExpiration.toString());

        return response.access_token;
      }),
      catchError((error: any) => {
        console.error('Refresh error:', error);
        return of(false);
      })
    );


  }


  getAccessToken(): string | null {
    if(this.isTokenExpired()) {
      return null;
    }
    return localStorage.getItem('access_token');
  }

  setAccessToken(token: string): void {
    localStorage.setItem('access_token', token);
  }

  getRefreshToken(): string | null {
    if(this.isRefreshTokenExpired()) {
      return null;
    }
    return localStorage.getItem('refresh_token');
  }

  setRefreshToken(token: string): void {
    localStorage.setItem('refresh_token', token);
  }

  clearTokens(): void {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }

}
