import { Component, OnInit, OnDestroy, AfterContentChecked, Inject, Output, EventEmitter } from '@angular/core';
import { MessageService } from '../services/message.service';
import { User } from '../models/user';
import { Router } from '@angular/router';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { CartItem } from '../models/cartItem';
import { Subscription, Subject } from 'rxjs';
import { ShoppingCart } from '../models/shopping-cart';
import { debounceTime, switchMap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent
  implements OnInit, OnDestroy, AfterContentChecked {

  userSubscription: Subscription;
  private currentUser: User;
  shoppingCart: ShoppingCart;
  total = 0;

  private updatedCartItem = new Subject<CartItem>();
  sub: Subscription;

  constructor(private messageService: MessageService,
              private router: Router,
              private shoppingCartService: ShoppingCartService,
              private authService: AuthService,
              @Inject('BaseURL') public baseURL) {
      this.userSubscription = this.authService.user.subscribe(user => (this.currentUser = user));
  }

  ngOnInit() {
    this.shoppingCartService.getCart().subscribe(shoppingCart => {
      this.shoppingCart = shoppingCart;
      console.log('shoppig cart items: ' + this.shoppingCart.cartItems.length);
    });

    this.sub = this.updatedCartItem
      .pipe(
        debounceTime(300),
        // switch to new observable
        switchMap((cartItem: CartItem) =>
          // console.log(`updating quantity: ${cartItem.quantity}`);
          this.shoppingCartService.update(cartItem)
        )
      )
      .subscribe(
        (cart) => {
          if (cart) {
            console.log(`updated: ${cart.id}`);
            // throw new Error();
          }
        },
        (error) => console.log('Updating Cart Item failed: ' + error)
      );
  }

  ngOnDestroy(): void {
    if (!this.currentUser) {
      this.shoppingCartService.storeLocalCart();
    }
    this.userSubscription.unsubscribe();
  }

  ngAfterContentChecked() {
    if (this.shoppingCart) {
      this.total = this.shoppingCart.cartItems.reduce(
      (prev, cur) => prev + cur.quantity * cur.product.price, 0);
    }
  }

  addOne(cartItem) {
    cartItem.quantity++;
    this.updatedCartItem.next(cartItem);
  }

  minusOne(cartItem) {
    cartItem.quantity--;
    this.updatedCartItem.next(cartItem);
  }

  onChange(cartItem) {
    if (this.currentUser) {
      this.updatedCartItem.next(cartItem);
    }
  }

  remove(cartItem: CartItem) {
    this.shoppingCartService.remove(cartItem).subscribe(
      success => {
        this.shoppingCart.cartItems = this.shoppingCart.cartItems
          .filter(e => e.product.id !== cartItem.product.id);
      },
      _ => this.messageService.add('Removing Product failed'));
  }

  checkout() {
    if (!this.currentUser) {
      this.router.navigate(['/login'], {queryParams: {returnUrl: 'order'}});
    } else {
      this.shoppingCartService.checkout();
    }
  }

}
