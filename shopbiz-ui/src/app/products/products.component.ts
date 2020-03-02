import { Component, OnInit, Inject } from '@angular/core';

import { ProductService, PageableProducts } from '../services/product.service';
import { Product } from '../models/product';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})

export class ProductsComponent implements OnInit {

  products: Product[];
  addProductAllowed: boolean;
  authService: AuthService;
  pageableProducts: PageableProducts;

  constructor(
    private productService: ProductService,
    authService: AuthService,
    @Inject('BaseURL') public baseURL
  ) {
    this.authService = authService;
  }

  ngOnInit() {
    this.getProducts();
    this.addProductAllowed = true;
  }

  getProductsByPage(pageNum: number) {
    this.getProducts(pageNum);
  }

  getProducts(page: number = 1): void {
    this.productService.getProducts(page)
      .subscribe((resp: PageableProducts) => {
        this.pageableProducts = resp;
        this.products = resp.content;
      });
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.productService.addProduct({ name } as Product)
      .subscribe(product => {
      this.products.push(product);
    });
  }


}
