import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from '@angular/common/http';
import {catchError, map, Observable, of, throwError} from "rxjs";
import {TokenDTO} from "../../dtos/TokensDTO";

interface LoginData {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private apiUrl = 'http://localhost:8081/auth/user/public/login';

  constructor(private http: HttpClient) {}

  login(loginData: LoginData): Observable<boolean> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<TokenDTO>(`${this.apiUrl}`, loginData, { headers:{skip:"true"} }).pipe(
      map((response: TokenDTO) => {

        const currentTime = new Date();
        const accessTokenExpiration = new Date(
          currentTime.getTime() + parseInt(response.expires_in) * 30); // Convert seconds to milliseconds
        const refreshTokenExpiration = new Date(
          currentTime.getTime() + parseInt(response.refresh_expires_in) * 30);

        localStorage.setItem('access_token', response.access_token);
        localStorage.setItem('expires_in', accessTokenExpiration.toString());
        localStorage.setItem('refresh_token', response.refresh_token);
        localStorage.setItem('refresh_expires_in', refreshTokenExpiration.toString());
        console.log(refreshTokenExpiration.toString());
        return true;
      }),
      catchError((error: any) => {
        console.error('Login error:', error);
        return of(false);
      })
    );
  }
}
