import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {TokenDTO} from "../../../dtos/TokensDTO";
import {Injectable} from "@angular/core";

interface AccountData {
  accountName: string
}

@Injectable({
  providedIn: 'root',
})
export class CreateAccountService {
  private apiUrl = 'https://localhost:8443/api/bank/account';

  constructor(private http: HttpClient) {}

  create(accountData: AccountData, jwtToken: string): Observable<boolean> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
    });

    return this.http.post<AccountData>(`${this.apiUrl}`, accountData,
      { headers, observe: 'response' }).pipe(
      map(response => {
        if (response.status === 201) {
          return true;
        } else {
          console.error('Unexpected response status:', response.status);
          return false;
        }
      }),
      catchError(error => {
        if (error.status === 404) {
          // Handle 404 Not Found
          console.error('Resource not found:', error);
        } else {
          // Handle other errors
          console.error('Error:', error);
        }
        return of(false);
      })
    );
  }
}
