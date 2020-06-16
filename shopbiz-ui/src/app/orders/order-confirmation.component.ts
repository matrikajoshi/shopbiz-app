import { Component, OnInit } from '@angular/core';
import { OrderItem } from '../models/OrderItem';
import { Subscription } from 'rxjs';
import { ShoppingCart } from '../models/shopping-cart';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { MessageService } from '../services/message.service';
import { OrderService } from '../services/order.service';
import { Order } from '../models/Order';

@Component({
  selector: 'app-orders',
  templateUrl: './order-confirmation.component.html',
  styleUrls: ['./order-confirmation.component.scss']
})
export class OrderConfirmationComponent implements OnInit {

  shoppingCart: ShoppingCart;
  orderItem: OrderItem;
  orderItems: OrderItem[] = [];
  total: number;
  paid: boolean;
  sub: Subscription;
  order: Order;

  constructor(
    private messageService: MessageService,
    private orderService: OrderService,
    private shoppingCartService: ShoppingCartService) { }

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

  pay() {
    this.orderService.saveOrder(this.orderItems)
        .subscribe(order => {
          this.order = order;
          this.paid = true;
          this.log("Order created with id: " + this.order.id);
        });
  }

  private log(message: string) {
    this.messageService.add(`OrderComponent: ${message}`);
  }

}
