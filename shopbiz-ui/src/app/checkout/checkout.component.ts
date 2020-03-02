import { Component, OnInit, Inject } from '@angular/core';
import {Location} from '@angular/common';
import { ShoppingCart } from '../models/shopping-cart';
import { OrderItem } from '../models/OrderItem';
import { Subscription } from 'rxjs';
import { Order } from '../models/Order';
import { MessageService } from '../services/message.service';
import { OrderService } from '../services/order.service';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {

  shoppingCart: ShoppingCart;
  orderItem: OrderItem;
  orderItems: OrderItem[] = [];
  total: number;
  paid: boolean;
  sub: Subscription;
  order: Order;
  customer: any;

  constructor(
    private messageService: MessageService,
    private orderService: OrderService,
    private shoppingCartService: ShoppingCartService,
    private router: Router,
    private location: Location,
    @Inject('BaseURL') public baseURL) {
      this.customer = {
        firstName: "TestFirstName",
        lastName: "TestLastName"
      };
    }

    ngOnInit() {
      this.shoppingCartService.getCart()
        .subscribe((cart: ShoppingCart) => {
          this.log("Got cart items: " + cart.cartItems.length);
          cart.cartItems.forEach((cartItem) => {
            this.orderItems.push(new OrderItem(cartItem.product, cartItem.quantity));
            this.total += cartItem.totalPrice;
          });
      });
      this.paid = false;
      this.loadTotal();
    }

    loadTotal() {
      this.shoppingCartService.getTotal()
        .subscribe((orderTotal) => {
          this.log("got total from cart: " + orderTotal);
          this.total = orderTotal;
        });
    }

    onCompleteOrder() {
      this.orderService.saveOrder(this.orderItems)
          .subscribe(order => {
            this.order = order;
            this.paid = true;
            this.log("Order created with id: " + this.order.id);
            this.router.navigate(['/order']);
          });
    }

    onBack() {
      this.location.back();
    }

    private log(message: string) {
      this.messageService.add(`OrderComponent: ${message}`);
    }

}
