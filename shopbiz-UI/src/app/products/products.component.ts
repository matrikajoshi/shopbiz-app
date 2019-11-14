import { Component, OnInit, Inject } from '@angular/core';

import { ProductService } from '../services/product.service';
import { Product } from '../models/product';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})

export class ProductsComponent implements OnInit {

  products: Product[];
  addProductAllowed: boolean;
  authService: AuthService;

  constructor(
    private productService: ProductService,
    authService: AuthService,
    @Inject('BaseURL') private baseURL
  ) {
    this.authService = authService;
  }

  ngOnInit() {
    this.getProducts();
    this.addProductAllowed = true;
  }

  getProducts(): void {
    this.productService.getProducts()
      .subscribe(products => this.products = products);
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
