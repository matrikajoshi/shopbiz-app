import { BrowserModule } from '@angular/platform-browser';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FileUploadModule } from 'ng2-file-upload';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProductsComponent } from './products/products.component';
import { ShoppingListComponent } from './shopping-list/shopping-list.component';
import { AuthComponent } from './auth/auth.component';
import { LoadingSpinnerComponent } from './shared/loading-spinner/loading-spinner.component';
import { ProductDetailComponent } from './products/product-detail/product-detail.component';
import { AddProductComponent } from './products/add-product/add-product.component';
import { ProductEditComponent } from './products/product-edit/product-edit.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { OrdersComponent } from './orders/orders.component';
import { CartModalComponent } from './shopping-cart/cart-modal/cart-modal.component';
import { ShoppingListService } from './services/shopping-list.service';
import { AuthInterceptorService } from './auth/auth.interceptor.service';
import { MessagesComponent } from './messages/messages.component';
import { baseURL } from 'src/environments/environment';
import { DropdownDirective } from './shared/dropdown.directive';

import {
  MatToolbarModule,
  MatListModule,
  MatGridListModule,
  MatCardModule,
  MatButtonModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatCheckboxModule,
  MatSelectModule,
  MatSlideToggleModule,
  MatProgressSpinnerModule,
  MatIconModule
} from '@angular/material';
import { CookieService } from 'ngx-cookie-service';

import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';



@NgModule({
  declarations: [
    AppComponent,
    ProductsComponent,
    MessagesComponent,
    ProductDetailComponent,
    DashboardComponent,
    HeaderComponent,
    AddProductComponent,
    ProductEditComponent,
    ShoppingListComponent,
    AuthComponent,
    LoadingSpinnerComponent,
    ShoppingCartComponent,
    OrdersComponent,
    CartModalComponent,
    DropdownDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    FileUploadModule,
    ReactiveFormsModule,
    AppRoutingModule,
    MatToolbarModule,
    MatListModule,
    MatGridListModule,
    MatCardModule,
    MatButtonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MDBBootstrapModule.forRoot(),
    BrowserAnimationsModule,
    FlexLayoutModule,
    HttpClientModule
  ],
  providers: [
    // service within another service
    CookieService,
    {provide: 'BaseURL', useValue: baseURL},
    ShoppingListService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [CartModalComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
