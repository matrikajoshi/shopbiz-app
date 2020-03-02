import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from '../app-routing.module';
import { FormsModule } from '@angular/forms';

import { PriceComponent } from './price/price.component';
import { PaginationComponent } from './pagination/pagination.component';


@NgModule({
    declarations: [
        PriceComponent,
        PaginationComponent
     
    ],
    imports: [
        CommonModule,
        AppRoutingModule,
        FormsModule
    ],
    exports: [
        PriceComponent,
        
        CommonModule,
        AppRoutingModule,
        FormsModule
    ]
})
export class SharedModule {}
