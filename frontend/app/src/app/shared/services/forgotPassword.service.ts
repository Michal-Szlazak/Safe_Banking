import {HttpClient, HttpResponse} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {map, Observable} from "rxjs";
import {ApiErrorResponse} from "../../entities/HttpResponse";

@Injectable({
  providedIn: 'root',
})
export class ForgotPasswordService {

  private apiUri: string = 'http://localhost:8081/auth/user/public/forgotPassword'

  constructor(private http: HttpClient) {}

  sendEmail(email: string): Observable<boolean> {
    const requestBody = { email: email };
    return this.http.post(this.apiUri, requestBody, { headers:{skip:"true"},
      observe: 'response' }).pipe(
      map((response: HttpResponse<any>) => {
        // Check the response status code
        const statusCode = response.status;
        return statusCode === 200;
      })
    );
  }
}
