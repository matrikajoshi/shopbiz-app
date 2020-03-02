import {Component, Output, EventEmitter, OnInit, OnDestroy} from "@angular/core";
import { Subscription } from 'rxjs';

import { ShoppingCartService } from '../services/shopping-cart.service';
import { AuthService } from '../services/auth.service';
// import { ShoppingIcon } from '../../assets/shopping-bag.svg';
// import  * as ShoppingIcon   from 'raw-loader!../../assets/shopping-bag.svg';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  isAuthenticated = false;
  private userSub: Subscription;
  title = 'Shop-n-Top';
  collapsed = true;

  constructor(
    private authService: AuthService,
    public shoppingCartService: ShoppingCartService
  ) {}

  ngOnInit(): void {
    this.userSub = this.authService.user.subscribe(user => {
      this.isAuthenticated = !!user;
      // console.log("user1", !user);
      // console.log("user2", !!user);
    });
  }


  onLogout() {
    this.authService.logout();
  }

  ngOnDestroy() {
    this.userSub.unsubscribe();
  }
}
