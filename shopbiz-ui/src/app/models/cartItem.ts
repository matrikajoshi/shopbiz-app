import { Product } from './product';

export class CartItem {
    id: number;
    product: Product;
    quantity: number;
    totalPrice: number;

    constructor(product: Product, quantity: number) {
      this.product = product;
      this.quantity = quantity;
    }
}
