import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { OrderItem } from '../models/OrderItem';
import { MessageService } from './message.service';
import { Order } from '../models/Order';
import { baseURL } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private url = `${baseURL}orders`;

  constructor(private http: HttpClient,
    private messageService: MessageService,
    private router: Router) { }

  saveOrder(orderItems: OrderItem[]): Observable<Order>  {
    return this.http.post<Order>(this.url, orderItems).pipe(
      tap(order => {
        this.log("Order created with id: " + order.id);
      }),
      catchError(_ => of(null))
    );
  }

  private log(message: string) {
    this.messageService.add(`OrderService: ${message}`);
  }
}
