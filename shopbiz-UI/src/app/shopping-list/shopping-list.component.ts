import { Component, OnInit, OnDestroy, Inject } from '@angular/core';
import {ShoppingList} from "../models/shoppingList";
import {ShoppingListService} from "../services/shopping-list.service";
import { Subscription } from 'rxjs';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { CartItem } from '../models/cartItem';
import { Product } from '../models/product';
import { Router } from '@angular/router';

@Component({
  selector: "app-shopping-list",
  templateUrl: "./shopping-list.component.html",
  styleUrls: ["./shopping-list.component.css"]
})
export class ShoppingListComponent implements OnInit, OnDestroy {
  subscription: Subscription;
  shoppingList: ShoppingList;
  title: string;

  constructor(
    private shoppingListService: ShoppingListService,
    private shoppingCartService: ShoppingCartService,
    private router: Router,
    @Inject("BaseURL") private baseURL
  ) {}

  ngOnInit() {
    this.getShoppingList();
    //this.setTitle();
  }

  private getShoppingList() {
    this.shoppingListService
      .getShoppingList()
      .subscribe(shoppingList => (this.shoppingList = shoppingList));
    this.subscription = this.shoppingListService.shoppingListChanged.subscribe(
      shoppingList => {
        this.shoppingList = shoppingList;
        this.setTitle();
      }
    );
  }

  private setTitle() {
    if (this.shoppingList) {
      this.title = this.shoppingList.customerName + "'s Shopping List";
    } else {
      this.title = "You don't have any products in Shopping List";
    }
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  removeFromList(product: Product) {
    this.shoppingListService.removeProductFromShoppingList(product).subscribe(
      sl => {
        this.shoppingList = sl;
      },
      _ => console.log("Removing from shopping list failed")
    );
  }

  addToCart(product: Product) {
    const item = new CartItem(product, 1);
    this.shoppingCartService.addItemToShoppingCart(item).subscribe(
      cart => {
        console.log("Product added to cart");
        this.removeFromList(product);
        this.router.navigateByUrl("/cart");
      },
      _ => console.log("Adding to cart failed")
    );
  }
}
