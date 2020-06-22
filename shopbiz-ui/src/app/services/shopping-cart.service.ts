import { Injectable, EventEmitter } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { tap, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';

import { MessageService } from './message.service';
import { CartItem } from '../models/cartItem';
import { ShoppingCart } from '../models/shopping-cart';
import { Observable, of, BehaviorSubject, throwError } from 'rxjs';
import { User } from '../models/user';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { baseURL } from 'src/environments/environment';
import { isNgTemplate } from '@angular/compiler';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root', // {providedIn: 'root'} - no need to declare in appModule
})
export class ShoppingCartService {

  private url = `${baseURL}shoppingCart`;
  private currentUser: User;
  
  shoppingCartInOrder = new EventEmitter<ShoppingCart>();
  localCart: ShoppingCart = new ShoppingCart(null, [], null);
  
  private totalSubject: BehaviorSubject<number>;
  // private total: Observable<number>;

  // NavbarCounts
  // navbarCartCount = 0;

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private messageService: MessageService,
    private authService: AuthService,
    private router: Router
  ) {
    this.totalSubject = new BehaviorSubject<number>(null);
    // this.total = this.totalSubject.asObservable();
    this.authService.user.subscribe((user) => (this.currentUser = user));
  }

  private getLocalShoppingCart(): ShoppingCart {
    if (this.cookieService.check('cart')) {
      this.localCart = JSON.parse(this.cookieService.get('cart'));
    }
    return this.localCart;
  }

  getCart(): Observable<ShoppingCart> {
    const localShoppingCart = this.getLocalShoppingCart();
    if (this.currentUser) {
      if (localShoppingCart.cartItems.length > 0) {
        return this.http.post<ShoppingCart>(this.url, this.localCart)
          .pipe(
            tap((cart) => {
              console.log('got items: ' + cart.cartItems.length);
              const totalQuantity = cart.cartItems.reduce(
                (accum, item) => accum + item.quantity,
                0
              );
              this.totalSubject.next(totalQuantity);
              this.clearLocalCart();
            }),
            catchError(this.handleError)
          );
      } else {
        return this.http.get<ShoppingCart>(this.url).pipe(
          tap((cart) => {
            const cartItemsCount = cart.cartItems.reduce(
              (accumalatedQuantity, cartItem) =>
                accumalatedQuantity + cartItem.quantity,
              0
            );
            this.totalSubject.next(cartItemsCount);
            this.log(
              'fetched shopping cart with items: ' + cart.cartItems.length
            );
          }),
          catchError(this.handleError)
        );
      }
    } else {
      const cartCount = localShoppingCart.cartItems.reduce(
        (accum, item) => accum + item.quantity,
        0
      );
      this.totalSubject.next(cartCount);
      return of(localShoppingCart);
    }
  }

  addItemToShoppingCart(cartItem: CartItem): Observable<ShoppingCart> {
    if (!this.currentUser) {
      if (this.cookieService.check('cart')) {
        this.localCart = JSON.parse(this.cookieService.get('cart'));
      }
      if (
        this.localCart &&
        this.localCart.cartItems.some(
          (item) => item.product.id === cartItem.product.id
        )
      ) {
        const existingItem = this.localCart.cartItems.find((item) => {
          return item.product.id === cartItem.product.id;
        });
        existingItem.quantity += cartItem.quantity;
      } else {
        // this.localCart = new ShoppingCart(null, [], null);
        // this.localCart.cartItems = [];
        this.localCart.cartItems.push(cartItem);
      }
      this.calculateLocalCartProdCounts();
      this.cookieService.set('cart', JSON.stringify(this.localCart));
      return of(this.localCart);
    } else {
      const url = `${this.url}`;
      return this.http.post<ShoppingCart>(url, cartItem, httpOptions).pipe(
        tap((shoppingCart: ShoppingCart) => {
          const cartCount = shoppingCart.cartItems.reduce(
            (accum, item) => accum + item.quantity,
            0
          );
          this.totalSubject.next(cartCount);
          console.log(
            `For loggedIn user, added item to shopping cart: ${shoppingCart.id}`
          );
          this.log(
            `For loggedIn user, added item to shopping cart ` + shoppingCart.id
          );
        })
      );
    }
  }

  update(cartItem) {
    if (this.currentUser) {
      const url = `${this.url}/${cartItem.id}`;
      return this.http.put<ShoppingCart>(url, cartItem.quantity).pipe(
        tap((cart) => {
          const itemCount = cart.cartItems.reduce(
            (accum, item) => accum + item.quantity,
            0
          );
          this.totalSubject.next(itemCount);
        })
      );
    } else {
      // this.localCart.cartItems = this.localCart.cartItems.map(item => {
      //   if (item.product.id === cartItem.product.id) {
      //     item.quantity = cartItem.quantity;
      //   }
      //   return item;
      // });
      // this.localCart.cartItems.find(item => item.product.id === cartItem.product.id).quantity = cartItem.quantity;
      this.calculateLocalCartProdCounts();
      this.cookieService.set('cart', JSON.stringify(this.localCart));
      return of(this.localCart);
    }
  }

  remove(cartItem: CartItem) {
    if (!this.currentUser) {
      const index = this.localCart.cartItems.findIndex(
        (item) => item.product.id === cartItem.product.id
      );
      if (index !== -1) {
        this.localCart.cartItems.splice(index, 1);
      }
      this.calculateLocalCartProdCounts();
      this.cookieService.set('cart', JSON.stringify(this.localCart));
      return of(this.localCart);
    } else {
      const url = `${this.url}/${cartItem.id}`;
      return this.http
        .delete<ShoppingCart>(url, httpOptions)
        .pipe
        // tap(cart =>
        // this.navbarCartCount = cart.cartItems.length)
        ();
    }
  }

  storeLocalCart() {
    this.cookieService.set('cart', JSON.stringify(this.localCart));
  }

  clearLocalCart() {
    console.log('clear local cart');
    this.cookieService.delete('cart');
    this.localCart.cartItems = [];
  }

  getTotalSubject() {
    return this.totalSubject;
  }

  getTotal() {
    return this.totalSubject.asObservable();
  }

  checkout() {
    this.shoppingCartInOrder.emit(this.localCart);
    // const url = `${this.url}/checkout`;
    // return this.http.post(url, null).pipe();
    this.router.navigate(['/checkout']);
  }

  getTotalItemsCount() {
    return this.totalSubject.asObservable();
  }

  populateShoppingCartNavBarCount() {
    this.getCart().subscribe((cart) => {
      const cartCount = cart.cartItems.reduce(
        (accum, item) => accum + item.quantity,
        0
      );
      this.totalSubject.next(cartCount);
    });
  }

  // returning products count in local cart
  calculateLocalCartProdCounts() {
    const cartCount = this.localCart.cartItems.reduce(
      (accum, item) => accum + item.quantity,
      0
    );
    this.totalSubject.next(cartCount);
  }

  // getShoppingCartNavBarCount() {
  //   //console.log("updating nav bar count after refresh");
  //   this.getCart();
  // }

  private log(message: string) {
    this.messageService.add(`ShoppingCartService: ${message}`);
  }

  private handleError(err: any) {
    let errorMessage: string;
    if (err.error instanceof ErrorEvent) {
      errorMessage = `An error occurred: ${err.error.message}`;
    } else {
      errorMessage = `Backend returned code ${err.status}: ${err.body.error}`;
    }
    console.error(err);
    return throwError(errorMessage);
  }
}
