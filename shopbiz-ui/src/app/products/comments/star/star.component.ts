import { Component, OnInit, OnChanges, Input } from '@angular/core';

@Component({
  selector: 'app-star',
  templateUrl: './star.component.html',
  styleUrls: ['./star.component.scss']
})
export class StarComponent implements OnInit, OnChanges {

  @Input() rating: number;
  widths;
  constructor() { }

  ngOnInit() {
  }

  ngOnChanges() {
    this.widths = this.rating * 90 / 5;
  }

}
