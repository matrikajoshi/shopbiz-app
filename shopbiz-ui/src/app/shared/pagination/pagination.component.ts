import { Component, OnInit, Input, Output, EventEmitter, OnChanges } from "@angular/core";
import { PageableProducts } from "src/app/services/product.service";

@Component({
  selector: "app-pagination",
  templateUrl: "./pagination.component.html",
  styleUrls: ["./pagination.component.scss"]
})
export class PaginationComponent implements OnInit, OnChanges {

  @Input() pageableProducts: PageableProducts;
  @Output() getProductsByPage = new EventEmitter<number>();
  totalPages: any;
  pageable: {
    pageNumber: number;
    pageSize: number;
    offset: number;
    sort: any;
  };
  
  constructor() {  }

  ngOnInit() {}

  ngOnChanges(changes: any) {
    if (changes['pageableProducts'] && changes['pageableProducts'].currentValue !== changes['pageableProducts'].previousValue) {
      this.pageable = this.pageableProducts.pageable;
      this.totalPages = this.pageableProducts.totalPages;
    }
  }

  setPage(pageNum: number) {
    this.getProductsByPage.emit(pageNum);
  }

  counter(i = 1) {
    return new Array(i);
  }
}
