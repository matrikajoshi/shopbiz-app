import { Injectable } from '@angular/core';
import {Product} from '../models/product';
import {HttpHeaders} from '@angular/common/http';

import {MessageService} from './message.service';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/internal/operators';
import {ShoppingList} from '../models/shoppingList';
import {Observable, Subject, of} from 'rxjs';
import { baseURL } from 'src/environments/environment';


const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class ShoppingListService {

  shoppingListChanged = new Subject<ShoppingList>();
  private shoppingList: ShoppingList;

  constructor(private http: HttpClient,
              private messageService: MessageService) { }

  getShoppingList(): Observable<ShoppingList> {
    const url = `${baseURL}shoppingList`;
    return this.http.get<ShoppingList>(url);
  }

  addProductToShoppingList(product: Product): Observable<ShoppingList> {
    const url = `${baseURL}shoppingList`;

    return this.http.post<ShoppingList>(url, product, httpOptions)
      .pipe(
        tap((shoppingList: ShoppingList) => {
          this.shoppingList = shoppingList;
          this.shoppingListChanged.next(this.shoppingList);
          this.log(`added product to shopping list w/ id=${shoppingList.id}`);
        })
      );
  }

  removeProductFromShoppingList(product: Product): Observable<ShoppingList> {
    const url = `${baseURL}shoppingList/removeProduct`;
    return this.http.put<ShoppingList>(url, product, httpOptions)
      .pipe(
        tap((shoppingList: ShoppingList) => {
          this.shoppingList = shoppingList;
          this.shoppingListChanged.next(this.shoppingList);
          this.log('removed product from shopping list w/ id=' + product.id);
        })
      );
    }

  private log(message: string) {
    this.messageService.add(`ProductService: ${message}`);
  }

}
