import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { tap, catchError } from 'rxjs/operators';
import { Subject, Observable, of, throwError } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { ShoppingCart } from '../models/shopping-cart';
import { CartItem } from '../models/cartItem';
import { AuthService } from './auth.service';
import { User } from '../models/user';
import { baseURL } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private url = `${baseURL}shoppingCart`;
  currentUser: User;
  cart: ShoppingCart = new ShoppingCart(null, [], null);
  localCart: ShoppingCart = new ShoppingCart(null, [], null);
  totalPrice: Subject<number> = new Subject<number>();
  totalItemsInCart: Subject<number> = new Subject<number>();

  constructor(
    private cookieService: CookieService,
    private httpClient: HttpClient,
    private authService: AuthService
  ) {
    this.authService.user.subscribe(user =>
      this.currentUser = user);
  }

  private getLocalShoppingCart(): ShoppingCart {
    if (this.cookieService.check('cart')) {
      this.localCart = JSON.parse(this.cookieService.get('cart'));
    }
    return this.localCart;
  }

  getCart(): Observable<ShoppingCart> {
    const localCart = this.getLocalShoppingCart();
    // this.calculateLocalCartTotals();
    if (!this.currentUser) {
      return of(this.localCart);
    } else {
      if (this.localCart.cartItems.length === 0) {
        return this.httpClient.get<ShoppingCart>(this.url)
          .pipe(
            tap(),
            catchError(this.handleError)
          );
      }
    }
  }


  addToCart(cartItem: CartItem): Observable<ShoppingCart>  {
    if (!this.currentUser) {
      if (this.cookieService.check('cart')) {
        this.localCart = JSON.parse(this.cookieService.get('cart'));
      }
      // check if item already exist in cart
      const existingItem = this.localCart.cartItems.find(item => item.product.id === cartItem.product.id);
      if (existingItem) {
        existingItem.quantity += cartItem.quantity;
      } else {
        this.localCart.cartItems.push(cartItem);
      }
      // update cart in cookie
      this.cookieService.set('cart', JSON.stringify(this.localCart));
    } else {
      const url = `${this.url}`;
      return this.httpClient.post<ShoppingCart>(url, cartItem, httpOptions).pipe(
        tap((shoppingCart: ShoppingCart) => {
          this.localCart = shoppingCart;
        })
      );
    }
    this.calculateLocalCartTotals();
  }

  calculateLocalCartTotals() {
    let totalPriceValue = 0;
    let totalQntyValue = 0;

    for ( const cartItem of this.localCart.cartItems) {
      totalPriceValue += cartItem.totalPrice;
      totalQntyValue += cartItem.quantity;
    }

    // publish the new values
    this.totalPrice.next(totalPriceValue);
    console.log('from cart service: ' + totalQntyValue);
    this.totalItemsInCart.next(totalQntyValue);
  }

  private handleError(err: any) {
    let errorMessage: string;
    if (err.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      errorMessage = `An error occurred: ${err.error.message}`;
    } else {
      // The backend returned an unsuccessful response code.
      errorMessage = `Backend returned code ${err.status}: ${err.body.error}`;
    }
    console.error(err);
    return throwError(errorMessage);
  }
}
