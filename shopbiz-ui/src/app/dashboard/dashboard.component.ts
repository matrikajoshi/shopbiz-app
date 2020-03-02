import { Component, OnInit, Inject } from '@angular/core';

import { Product } from '../models/product';
import { ProductService, PageableProducts } from '../services/product.service';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  products: Product[] = [];

  constructor(
    private productService: ProductService,
    public authService: AuthService,
    @Inject('BaseURL') public baseURL) {}

  ngOnInit() {
    this.getProducts();
  }

  getProducts(): void {
    let pageProdObs: Observable<PageableProducts>;
    pageProdObs = this.productService.getProducts();

    pageProdObs.subscribe(
        (resData: PageableProducts) => this.products = resData.content.slice(0, 5));
  }

}
