import {Injectable, Inject} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';
import {BehaviorSubject, throwError, Observable} from 'rxjs';
import {catchError, tap} from 'rxjs/internal/operators';

import { User } from '../models/user';

export interface AuthResponseData {
  kind: string;
  token: string;
  email: string;
  refreshToken: string;
  expiresIn: string;
  id: string;
  roles: string[];
  registered?: boolean;
}
// service within another service
@Injectable({ providedIn: 'root' })
export class AuthService {
  user = new BehaviorSubject<User>(null);
  loggedInUser: User;
  private tokenExpirationTimer: any;

  constructor(
    private http: HttpClient,
    private router: Router,
    @Inject('BaseURL') private baseURL) {}

  signup(userName: string, email: string, password: string) {
    const signupUrl = `${this.baseURL}users/signup`;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json'
      })
    };
    return this.http
      .post<AuthResponseData>(signupUrl, {
        userName,
        email: email,
        password: password
      }, httpOptions)
      .pipe(
        catchError(this.handleError),
        tap(resData => {
          if (resData) {
            this.handleAuthentication(
              resData.email,
              resData.id,
              resData.token,
              resData.roles,
              +resData.expiresIn
            );
          }
        })
      );
  }

  login(email: string, password: string): Observable<AuthResponseData> {
    const loginUrl = `${this.baseURL}login`;
    return this.http
      .post<AuthResponseData>(loginUrl, {
        email,
        password
      })
      .pipe(
        catchError(this.handleError),
        tap(resData => {
          this.handleAuthentication(
            resData.email,
            resData.id,
            resData.token,
            resData.roles,
            +resData.expiresIn
          );
        })
      );
  }

  autoLogin() {
    const userData: {
      email: string;
      id: string;
      _token: string;
      roles: string[];
      _tokenExpirationDate: string;
    } = JSON.parse(localStorage.getItem('userData'));
    if (!userData) {
      return;
    }

    const loadedUser = new User(
      userData.email,
      userData.id,
      userData._token,
      userData.roles,
      new Date(userData._tokenExpirationDate)
    );

    if (loadedUser.token) {
      this.user.next(loadedUser);
      const expirationDuration =
        new Date(userData._tokenExpirationDate).getTime() -
        new Date().getTime();
      this.autoLogout(expirationDuration);
      this.loggedInUser = loadedUser;
    }
  }

  logout() {
    this.user.next(null);
    this.loggedInUser = null;
    this.router.navigate(['/login']);
    localStorage.removeItem('userData');
    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }

  autoLogout(expirationDuration: number) {
    console.log('expiration duration: ', expirationDuration);
    this.tokenExpirationTimer = setTimeout(() => {
      this.logout();
    }, expirationDuration);
  }

  private handleAuthentication(
    email: string,
    userId: string,
    token: string,
    roles: string[],
    expiresIn: number
  ) {
    const expirationDate = new Date(new Date().getTime() + expiresIn);
    this.loggedInUser = new User(email, userId, token, roles, expirationDate);
    this.user.next(this.loggedInUser);
    this.autoLogout(expiresIn);
    localStorage.setItem('userData', JSON.stringify(this.loggedInUser));
  }

  private handleError(errorRes: HttpErrorResponse) {
    let errorMessage = 'An unknown error occurred!';
    if (!errorRes.error || !errorRes.error.error) {
      return throwError(errorMessage);
    }
    switch (errorRes.error.error.message) {
      case 'EMAIL_EXISTS':
        errorMessage = 'This email exists already';
        break;
      case 'EMAIL_NOT_FOUND':
        errorMessage = 'This email does not exist.';
        break;
      case 'INVALID_PASSWORD':
        errorMessage = 'This password is not correct.';
        break;
    }
    return throwError(errorMessage);
  }

  isAdmin() {
    if (this.loggedInUser) {
      // console.log("roles: " + this.loggedInUser.roles);
      return this.loggedInUser.roles.includes('ROLE_MANAGE_PRODUCT');
    }
  }
}
