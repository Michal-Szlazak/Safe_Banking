// token.interceptor.ts

import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { TokenService } from './token.service';
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(private tokenService: TokenService, private toastr: ToastrService,
              private router: Router) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const skipIntercept = request.headers.has('skip');
    if (skipIntercept) {
      request = request.clone({
        headers: request.headers.delete('skip')
      });
      return next.handle(request);
    }

    const accessToken = this.tokenService.getAccessToken();

    if (accessToken) {
      request = this.addToken(request, accessToken);
    }

    return next.handle(request).pipe(
      catchError((error) => {
        if(error.status === 401) {
          return this.handle401Error(request, next);
        }
      })
    );
  }

  private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const refreshToken = this.tokenService.getRefreshToken();
    if (!refreshToken) {
      this.tokenService.clearTokens();
      this.router.navigate(['/login']);
    }

    return this.tokenService.getNewToken(refreshToken).pipe(
      switchMap((newAccessToken) => {
        this.tokenService.setAccessToken(newAccessToken);
        request = this.addToken(request, newAccessToken);
        return next.handle(request);
      }),
      catchError((error) => {
        // Unable to refresh token, redirect to login or handle as needed
        this.tokenService.clearTokens();
        this.router.navigate(['/login']);
        return throwError('Unable to refresh token');
      })
    );
  }
}
