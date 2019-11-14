import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from "@angular/router";
import { Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";
import { baseURL } from "../shared/baseurl";
import { Product } from "../models/product";
import { MessageService } from "./message.service";


@Injectable({
  providedIn: "root"
})
export class ProductService implements Resolve<Product> {
  httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" })
  };

  constructor(
    private http: HttpClient,
    private messageService: MessageService
  ) { }

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<Product> | Promise<Product> | Product {
    this.log("From ProductService resolve");
    return this.getProduct(+route.params["id"]);
  }

  /** GET products from the server */
  getProducts(): Observable<Product[]> {
    const productsUrl = `${baseURL}products`;
    return this.http.get<Product[]>(productsUrl).pipe(
      /*res => res.json(),*/
      tap(_ => this.log("fetched products**")),
      catchError(this.handleError("getProducts", []))
    );
  }

  getProduct(id: number): Observable<Product> {
    const url = `${baseURL}products/${id}`;
    return this.http.get<Product>(url).pipe(
      tap(_ => this.log(`fetched product id=${id}`)),
      catchError(this.handleError<Product>(`getProduct id=${id}`))
    );
  }

  deleteProduct(id: number): Observable<Product> {
    const url = `${baseURL}products/${id}`;
    return this.http.delete<Product>(url, this.httpOptions).pipe(
      tap(_ => this.log(`deleted product id=$id`)),
      catchError(this.handleError<Product>("deleteProduct"))
    );
  }

  /** PUT: update the product on the server */
  // 'Content-Type': 'multipart/form-data'
  updateProduct(data, product: Product): Observable<Product> {
    const url = `${baseURL}products/${product.id}`;
    console.log("url " + url);
    const httpOptions = {
      headers: new HttpHeaders({})
    };
    return this.http.put<Product>(url, data, httpOptions).pipe(
      tap(_ => this.log(`updated product id=${product.id}`)),
      catchError(this.handleError<any>("updateProduct"))
    );
  }

  /** POST: add a new hero to the server */
  addProduct(data): Observable<Product> {
    const url = `${baseURL}products`;
    const httpOptions = {
      headers: new HttpHeaders({})
    };
    return this.http.post<Product>(url, data, httpOptions).pipe(
      tap((newProduct: Product) =>
        this.log(`added product w/ id=${newProduct.id}`)
      ),
      catchError(this.handleError<Product>("addProduct"))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = "operation", result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  private log(message: string) {
    this.messageService.add(`ProductService: ${message}`);
  }
}
