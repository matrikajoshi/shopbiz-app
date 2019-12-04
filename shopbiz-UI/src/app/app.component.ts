import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { ShoppingCartService } from './services/shopping-cart.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'shopNtop';

  constructor(private authService: AuthService,
              private shoppingCartService: ShoppingCartService) {}

  ngOnInit() {
    this.authService.autoLogin();
    this.shoppingCartService.getShoppingCartNavBarCount();
  }

}
