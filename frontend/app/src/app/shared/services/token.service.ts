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

  getToken(): string {
    const token = localStorage.getItem('access_token');

    if(!this.isTokenExpired(token)) {
      return localStorage.getItem('access_token');
    } else {

      const refresh_token = localStorage.getItem('refresh_token');

      if(this.isRefreshTokenExpired(refresh_token)) {
        this.router.navigate(['/login']);
      }

      this.getNewToken(refresh_token).subscribe({
        next: (success) => {
          if (success) {
            console.log('Refresh successful');
            return localStorage.getItem('access_token');
          } else {
            console.error('Refresh failed');
            this.router.navigate(['/login']);
          }
        },
        error: (err) => {
          console.error("There was an error while trying to get the token.");
          this.router.navigate(['/login']);
          return "expired";
        }
      });
    }
  }

  private isTokenExpired(token: string): boolean {
    // Extract the expiration date from the token and convert it to a Date object
    const expirationDate = new Date(localStorage.getItem("expires_in"));
    return expirationDate < new Date();
  }

  private isRefreshTokenExpired(token: string): boolean {
    // Extract the expiration date from the token and convert it to a Date object
    const expirationDate = new Date(localStorage.getItem("refresh_expires_in"));
    return expirationDate < new Date();
  }

  private getNewToken(refresh_token: string) : Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'refresh_token': refresh_token
    });
    const apiUrl: string = 'http://localhost:8081/auth/user/public/refreshToken'

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

        return true;
      }),
      catchError((error: any) => {
        console.error('Refresh error:', error);
        return of(false);
      })
    );


  }

}
