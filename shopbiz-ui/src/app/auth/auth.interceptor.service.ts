import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpParams,
  HttpHeaders
} from '@angular/common/http';
import { take, exhaustMap } from 'rxjs/operators';

import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.authService.user.pipe(
      take(1), // take only once and unsubscribe
      exhaustMap(user => {
        if (!user) {
          //console.log("user not defined in auth interceptor");
          return next.handle(req);
        }
        const modifiedReq = req.clone({
          headers: new HttpHeaders().append("Authorization", "Bearer " + user.token),
          //params: new HttpParams().set('auth', user.token)
        });
        return next.handle(modifiedReq);
      })
    );
  }
}
