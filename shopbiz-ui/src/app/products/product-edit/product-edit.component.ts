import {Component, OnInit, Inject} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormControl} from "@angular/forms";
import {Router, ActivatedRoute} from "@angular/router";
import {ProductService} from "../../services/product.service";
import {Product} from "../../models/product";
import {ShoppingListService} from "../../services/shopping-list.service";

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.scss']
})
export class ProductEditComponent implements OnInit {

  product: Product;
  productForm: FormGroup;
  fileToUpload: File = null;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private productService: ProductService,
              private shoppingListService: ShoppingListService,
              private formBuilder: FormBuilder,
              @Inject('BaseURL') public baseURL) { }

  ngOnInit() {

    this.createForm();

    this.productService.getProduct(this.route.snapshot.params['id'])
      .subscribe(product => {
        this.product = product;
        this.productForm.patchValue({
          id: product.id,
          name: product.name,
          sku: product.sku,
          category: product.category,
          featured: product.featured,
          description: product.description,
          active: product.active,
          lastUpdated: product.lastUpdated,
          image: '',
          price: product.price
        });
      });
  }

  createForm(): void {
    this.productForm = this.formBuilder.group({
      id: [''],
      name: [, Validators.required],
      sku: new FormControl(null, [Validators.required]),
      category: new FormControl(),
      featured: new FormControl(),
      description: new FormControl(),
      active: new FormControl(),
      lastUpdated: new FormControl(),
      image: new FormControl(''),
      price: new FormControl(null, [Validators.required])
    });
  }

  onFileChange(event) {
    console.log("on file change");
    const dataTransfer = new ClipboardEvent('').clipboardData || new DataTransfer();
    dataTransfer.items.add(new File(['my-file'], 'new-file-name'));
    let inputElement: HTMLInputElement = document.getElementById('image') as HTMLInputElement;
    //inputElement.files = dataTransfer.files;
    //inputElement.value = dataTransfer.files[0].name;
    //this.productForm.get('image').setValue('');
    if (event.target.files.length > 0) {
      this.fileToUpload = event.target.files[0];
      console.log("file ", this.fileToUpload.name);
      //const file = event.target.files[0];
      //this.productForm.get('productImage').setValue(file);
    }
  }

  onSubmit(): void {
    this.product = this.productForm.value;
    const formData = new FormData();
    if (this.fileToUpload) {
      formData.append('multipartImage'
            , this.fileToUpload
            , this.fileToUpload.name);
    }
    formData.append('info', new Blob([JSON.stringify(this.product)],
      {
        type: "application/json"
      }));
    this.productService.updateProduct(formData, this.product)
      .subscribe((product) => {
          let id = product.id;
          this.router.navigate(['/products', id]);
        },
        error => {
          this.product = null;
        }
      );
  }

  onAddToShoppingList() {
    this.shoppingListService.addProductToShoppingList(this.product);
  }


}
