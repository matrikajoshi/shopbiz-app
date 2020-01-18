import { Injectable, EventEmitter } from '@angular/core';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { tap, catchError } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';

import { MessageService } from './message.service';
import { CartItem } from '../models/cartItem';
import { ShoppingCart } from '../models/shopping-cart';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { User } from '../models/user';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { baseURL } from 'src/environments/environment';

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root' // {providedIn: 'root'} - no need to declare in appModule
})
export class ShoppingCartService {
  shoppingCartInOrder = new EventEmitter<ShoppingCart>();
  localCart: ShoppingCart = new ShoppingCart(null, [], null);
  private url = `${baseURL}shoppingCart`;
  private currentUser: User;
  private totalSubject: BehaviorSubject<number>;
  private total: Observable<number>;

  // NavbarCounts
  navbarCartCount = 0;

  constructor(
    private http: HttpClient,
    private cookieService: CookieService,
    private messageService: MessageService,
    private authService: AuthService,
    private router: Router
  ) {
    this.totalSubject = new BehaviorSubject<number>(null);
    this.total = this.totalSubject.asObservable();
    this.authService.user.subscribe(user => (this.currentUser = user));
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
        return this.http.post<ShoppingCart>(this.url, this.localCart).pipe(
          tap(cart => {
            this.navbarCartCount = cart.cartItems.length;
            this.clearLocalCart();
          }),
          catchError(_ => of(localShoppingCart))
        );
      } else {
        return this.http.get<ShoppingCart>(this.url)
        .pipe(
          tap(cart => {
            // this.navbarCartCount = cart.cartItems.length;

            this.navbarCartCount = cart.cartItems.reduce(
              (accumalatedQuantity, cartItem) =>
                accumalatedQuantity + cartItem.quantity,
              0
            );
            this.log('fetched shopping cart with items: ' + cart.cartItems.length);
          }),
          catchError(_ => new Observable<ShoppingCart>())
        );
      }
    } else {
      // can use reduce method of javascript
      this.navbarCartCount = localShoppingCart.cartItems.length;
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
          item => item.product.id === cartItem.product.id
        )
      ) {
        let existingItem = this.localCart.cartItems.find(item => {
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
          this.navbarCartCount = shoppingCart.cartItems.length;
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
      return this.http.put<CartItem>(url, cartItem.quantity)
      .pipe(
        tap(item => console.log('Updated cart item quantity to: ' + item.quantity))
      );
    }
  }

  remove(cartItem: CartItem) {
    if (!this.currentUser) {
      let index = this.localCart.cartItems.findIndex(
        item => item.product.id === cartItem.product.id
      );
      if (index !== -1) {
        this.localCart.cartItems.splice(index, 1);
      }
      this.calculateLocalCartProdCounts();
      this.cookieService.set('cart', JSON.stringify(this.localCart));
      return of(this.localCart);
    } else {
      const url = `${this.url}/${cartItem.id}`;
      return this.http.delete<ShoppingCart>(url, httpOptions).pipe(
        tap(cart => this.navbarCartCount = cart.cartItems.length)
      );
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

  checkout() {
    this.shoppingCartInOrder.emit(this.localCart);
    // const url = `${this.url}/checkout`;
    // return this.http.post(url, null).pipe();
    this.router.navigate(['/order']);
  }

  getTotal() {
    return this.total;
  }

  // returning products count in local cart
  calculateLocalCartProdCounts() {
    this.navbarCartCount = this.localCart.cartItems.length;
  }

  getShoppingCartNavBarCount() {
    //console.log("updating nav bar count after refresh");
    this.getCart();
  }

  private log(message: string) {
    this.messageService.add(`ShoppingCartService: ${message}`);
  }
}
