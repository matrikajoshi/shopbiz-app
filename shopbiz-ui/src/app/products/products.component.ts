import { Component, OnInit, Inject } from '@angular/core';

import { ProductService, PageableProducts } from '../services/product.service';
import { Product } from '../models/product';
import { AuthService } from '../services/auth.service';
import { Observable, BehaviorSubject, EMPTY, Subject } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { CartItem } from '../models/cartItem';
import { ShoppingCartService } from '../services/shopping-cart.service';
import { CartService } from '../services/cart.service';
import { CategoryService } from '../services/category.service';
import { catchError, switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  private categorySelectedSubject = new BehaviorSubject<number>(0);
  categorySelectedAction$ = this.categorySelectedSubject.asObservable();

  private errorMessageSubject = new Subject<string>();
  errorMessage$ = this.errorMessageSubject.asObservable();

  products: Product[];
  pageableProducts: PageableProducts;

  // for pagination
  thePageNumber: number = 1;
  thePageSize: number = 5;
  theTotalElements: number = 0;
  searchMode: boolean = false;
  previousKeyword: string = null;


  categories$ = this.categoryService.categories$.pipe(
    catchError((err) => {
      this.errorMessageSubject.next(err);
      return EMPTY;
    })
  );

  constructor(
    private productService: ProductService,
    private cartService: ShoppingCartService,
    private categoryService: CategoryService,
    private authService: AuthService,
    private route: ActivatedRoute,
    @Inject('BaseURL') public baseURL
  ) {  }

  ngOnInit() {
    this.getProducts();
  }

  getProductsByPage(pageNum: number) {
    this.getProducts(pageNum);
  }

  getProducts(page: number = 1): void {
    this.searchMode = this.route.snapshot.paramMap.has('keyword');
    if (this.searchMode) {
      this.handleSearchProducts();
    } else {
      this.categorySelectedAction$
        .pipe(
          switchMap(catId => this.productService.getProducts(page, catId))
        )
        .subscribe((resp: PageableProducts) => {
          this.pageableProducts = resp;
          this.products = resp.content;
        });
    }
  }

  handleSearchProducts() {
    const theKeyword: string = this.route.snapshot.paramMap.get('keyword');

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

  onCategorySelected(categoryId: string): void {
    this.categorySelectedSubject.next(+categoryId);
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
