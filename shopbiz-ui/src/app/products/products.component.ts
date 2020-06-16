import { Component, OnInit, Inject } from '@angular/core';

import { ProductService, PageableProducts } from '../services/product.service';
import { Product } from '../models/product';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { CartItem } from '../models/cartItem';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { CartService } from '../services/cart.service';

@Component({
  selector: "app-products",
  templateUrl: "./products.component.html",
  styleUrls: ["./products.component.scss"],
})
export class ProductsComponent implements OnInit {
  products: Product[];
  addProductAllowed: boolean;
  authService: AuthService;
  pageableProducts: PageableProducts;
  searchMode: boolean = false;

  // for pagination
  thePageNumber: number = 1;
  thePageSize: number = 5;
  theTotalElements: number = 0;

  previousKeyword: string = null;

  constructor(
    private productService: ProductService,
    private cartService: ShoppingCartService,
    authService: AuthService,
    private route: ActivatedRoute,
    @Inject("BaseURL") public baseURL
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
    this.searchMode = this.route.snapshot.paramMap.has("keyword");
    if (this.searchMode) {
      this.handleSearchProducts();
    } else {
      this.productService
        .getProducts(page)
        .subscribe((resp: PageableProducts) => {
          this.pageableProducts = resp;
          this.products = resp.content;
        });
    }
  }

  handleSearchProducts() {
    const theKeyword: string = this.route.snapshot.paramMap.get("keyword");

    // if we have a different keyword than previous
    // then set thePageNumber to 1

    if (this.previousKeyword != theKeyword) {
      this.thePageNumber = 1;
    }

    this.previousKeyword = theKeyword;

    console.log(`keyword=${theKeyword}, thePageNumber=${this.thePageNumber}`);

    // now search for the products using keyword
    this.productService
      .searchProductsPaginate(
        this.thePageNumber - 1,
        this.thePageSize,
        theKeyword
      )
      .subscribe((resp: PageableProducts) => {
        this.pageableProducts = resp;
        this.products = resp.content;
      });
  }

  add(name: string): void {
    name = name.trim();
    if (!name) {
      return;
    }
    this.productService.addProduct({ name } as Product).subscribe((product) => {
      this.products.push(product);
    });
  }

  onAddToShoppingCart(theProduct: Product) {
    console.log(`adding to cart: ${theProduct.id}`);
    const cartItem: CartItem = new CartItem(theProduct, 1);
    this.cartService
      .addItemToShoppingCart(cartItem)
      .subscribe((cart) => console.log(`added item to cart: ${cart.id}`));
  }
}
