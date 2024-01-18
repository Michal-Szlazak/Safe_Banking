import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";
import {List} from "postcss/lib/list";


interface BankAccount {
  accountName: string,
  accountNumber: string,
  cvv: string,
  expiresAt: Date,
  balance: number
}

@Injectable({
  providedIn: 'root',
})
export class GetAccountsService {
  private apiUrl = 'https://localhost:8443/api/bank/account';

  constructor(private http: HttpClient) {}

  get(jwtToken: string): Observable<BankAccount[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
    });

    return this.http.get<BankAccount[]>(`${this.apiUrl}`, { headers, observe: 'response' }).pipe(
      map((response: HttpResponse<BankAccount[]>) => {
        // Check if the response status is 200 (OK)
        if (response.status === 200) {
          return response.body;  // Successful response
        } else {
          console.error('Unexpected response status:', response.status);
          return response.body;  // Unsuccessful response
        }
      }),
      catchError(error => {
        // Handle errors
        if (error.status === 404) {
          console.error('Resource not found:', error);
        } else {
          console.error('Error:', error);
        }
        return of([]);  // Return an observable of false in case of error
      })
    );
  }
}
