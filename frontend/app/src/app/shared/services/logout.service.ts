import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {TokenDTO} from "../../dtos/TokensDTO";
import {TokenService} from "./token.service";

@Injectable({
  providedIn: 'root',
})
export class LogoutService {
  private apiUrl = 'https://localhost:8443/api/auth/user/private/logout';


  constructor(private http: HttpClient, private tokenService: TokenService) {}

  logout(): Observable<any> {

    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${this.tokenService.getToken()}`);

    return this.http.post<string>(`${this.apiUrl}`, {}, { headers }).pipe(
      map((response: string) => {
        localStorage.clear();
        return of(true);
      }),
      catchError((error: any) => {
        localStorage.clear();
        return of(true);
      })
    );
  }
}
