import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {TokenDTO} from "../../dtos/TokensDTO";
import {Injectable} from "@angular/core";
import {TokenService} from "./token.service";


interface PartialPasswordGet {
  parts: number[],
  signature: string
}

interface PartialPasswordPost {
  signature: string,
  parts: {}
}

@Injectable({
  providedIn: 'root',
})
export class PartialPasswordService {

  private apiUrl = 'https://localhost:8443/api/auth/private/partial-password';

  constructor(private http: HttpClient,
              private tokenService: TokenService) {}

  getPartialPassword(): Observable<PartialPasswordGet> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    return this.http.get<HttpResponse<PartialPasswordGet>>(`${this.apiUrl}`, { headers }).pipe(
      map((response: HttpResponse<PartialPasswordGet>) => {
        return response;
      }),
      catchError((error: any) => {
        return of(error);
      })
    );
  }

  checkPartialPassword(partialPasswordPost: PartialPasswordPost): Observable<number> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.tokenService.getToken()}`
    });

    return this.http.post<any>(`${this.apiUrl}/check`, partialPasswordPost, { headers, observe: 'response' }).pipe(
      map((response: any) => {
        console.log(response.status);
        return response.status;
      }),
      catchError((error: any) => {
        return of(error);
      })
    );
  }

}
