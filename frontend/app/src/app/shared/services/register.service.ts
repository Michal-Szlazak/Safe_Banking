import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import {catchError, map, Observable, throwError} from "rxjs";
import {ApiErrorResponse, SuccessResponse} from "../../entities/HttpResponse";

interface RegisterData {
  name: string;
  surname: string;
  email: string;
  // phoneNumber: string;
  password: string;
  confirmPassword: string;
}

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  private apiUrl = 'http://localhost:8081/auth/user/public/register';

  constructor(private http: HttpClient) {}

  register(registerData: RegisterData): Observable<HttpResponse<any> | ApiErrorResponse>{
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post(this.apiUrl, registerData, { headers, observe: 'response' })
      .pipe(
        map((response: HttpResponse<any>) => {
          // Check the response status code
          const statusCode = response.status;

          // Map to different objects based on the status code
          if (statusCode === 201) {
            // Success response (status code 201)
            return response;
          } else {
            // Handle a different status code (e.g., Conflict - 409)
            const customResponse: ApiErrorResponse = {
              status: response.status.toString(),
              message: 'Failed to create user.', // Customize the message as needed
              errors: response.body?.errors || [], // Customize error extraction as needed
            };
            return customResponse;
          }
        }),
        catchError((error: HttpErrorResponse) => {
          // Handle other HTTP errors here if needed
          const customResponse: ApiErrorResponse = {
            status: error.error.status,
            message: error.error.message, // Customize the message as needed
            errors: error.error.errors || [], // Customize error extraction as needed
          };
          return throwError(customResponse);
        })
      );
  }
}
