import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { ShoppingCartService } from './services/shopping-cart.service';
import { CartService } from './services/cart.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'shopNtop';

  constructor(private authService: AuthService,
              private cartService: CartService,
              private shoppingCartService: ShoppingCartService) {}

  ngOnInit() {
    this.authService.autoLogin();
    // this.cartService.getCart();
    this.shoppingCartService.populateShoppingCartNavBarCount();
  }

}
