import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from "@angular/forms";
import {Product} from "../../models/product";
import {ProductService} from "../../services/product.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss']
})
export class AddProductComponent implements OnInit {

  @ViewChild ('pform', {static: false}) addProductForm: NgForm;
  product: Product;
  fileToUpload: File = null;

  constructor(private productService: ProductService,
              private router: Router) {
  }

  ngOnInit() {
  }

  onSubmit() {
    this.product = this.addProductForm.value;
    const formData = new FormData();
    formData.append('multipartImage'
      , this.fileToUpload
      , this.fileToUpload.name);
    formData.append('product', new Blob([JSON.stringify(this.product)],
      {
        type: "application/json"
      }));
    this.productService.addProduct(formData)
      .subscribe((product) => {
          this.product = product;
          this.addProductForm.reset();
        },
        error => {
          this.product = null;
        }
      );
    this.router.navigate(['/products']);
  }

  onFileChange(event) {
    console.log("on file change");
    if (event.target.files.length > 0) {
      this.fileToUpload = event.target.files[0];
      console.log("file ", this.fileToUpload.name);
      //const file = event.target.files[0];
      //this.productForm.get('productImage').setValue(file);
    }
  }

}
