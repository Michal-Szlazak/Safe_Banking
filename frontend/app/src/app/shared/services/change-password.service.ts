import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpResponse} from "@angular/common/http";
import {TokenService} from "./token.service";
import {catchError, map, Observable, of, throwError} from "rxjs";
import {ApiErrorResponse} from "../../entities/HttpResponse";
import {ToastrService} from "ngx-toastr";

interface ChangePasswordData {
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

@Injectable({
  providedIn: 'root',
})
export class ChangePasswordService {
  private apiUrl = 'https://localhost:8443/api/auth/user/private/changePassword';


  constructor(private http: HttpClient, private tokenService: TokenService,
              private toastr: ToastrService) {}

  changePassword(changePasswordData: ChangePasswordData): Observable<HttpResponse<any> | ApiErrorResponse> {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenService.getToken()}`
    });

    return this.http.post(this.apiUrl, changePasswordData, { headers: {Authorization:`Bearer ${this.tokenService.getToken()}`}
      , observe: 'response' })
      .pipe(
        map((response: HttpResponse<any>) => {
          const statusCode = response.status;

          // Map to different objects based on the status code
          if (statusCode === 200) {
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
        map((response: ApiErrorResponse) => {
          return response;
        }),
        catchError((error: ApiErrorResponse) => {
          const customResponse: ApiErrorResponse = {
            status: error.status.toString(),
            message: "error.error.message", // Customize the message as needed
            errors: [], // Customize error extraction as needed
          };
          return throwError(customResponse);
        })
      );
  }
}
