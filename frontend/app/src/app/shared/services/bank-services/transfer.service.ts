import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, map, Observable, of} from "rxjs";

interface TransferData {
  receiverName: string,
  receiverAccount: string,
  senderAccount: string,
  title: string,
  amount: number
}

export interface TransferGet {
  title: string;
  receiverName: string;
  senderNumber: string;
  receiverNumber: string;
  amount: number;
  timestamp: Date;
}

@Injectable({
  providedIn: 'root',
})
export class TransferService {
  private apiUrl = 'https://localhost:8443/api/bank/transfer';

  constructor(private http: HttpClient) {}

  create(transferData: TransferData, jwtToken: string): Observable<boolean> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
    });

    return this.http.post<TransferData>(`${this.apiUrl}`, transferData,
      { headers, observe: 'response' }).pipe(
      map(response => {
        if (response.status === 200) {
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

  get(jwtToken: string): Observable<TransferGet[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${jwtToken}`
    });

    return this.http.get<TransferGet[]>(`${this.apiUrl}`,
      { headers, observe: 'response' }).pipe(
      map(response => {
        if (response.status === 200) {
          return response.body;
        } else {
          console.error('Unexpected response status:', response.status);
          return [];
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
        return of([]);
      })
    );
  }
}
