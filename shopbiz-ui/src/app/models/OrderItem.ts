import { CartItem } from './cartItem';
import { Product } from './product';


export class OrderItem {
  id: number;
  product: Product;
  orderedQuantities: number;

  constructor(product: Product, orderedQuantities: number) {
    this.product = product;
    this.orderedQuantities = orderedQuantities;
  }
}
