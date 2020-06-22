import { Injectable, Inject } from '@angular/core';
import { tap, shareReplay, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { ProcessHttpmsgService } from './process-httpmsg.service';

import { Category } from '../models/category';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  categoriesUrl = `${this.baseURL}categories`;

  categories$ = this.httpClient.get<Category[]>(this.categoriesUrl).pipe(
    tap(/* (data) => console.log('categories', JSON.stringify(data)) */),
    shareReplay(1),
    catchError((err) => this.processHTTPMsgService.handleError(err))
  );

  constructor(
    private httpClient: HttpClient,
    private processHTTPMsgService: ProcessHttpmsgService,
    @Inject('BaseURL') private baseURL
  ) {}
}
