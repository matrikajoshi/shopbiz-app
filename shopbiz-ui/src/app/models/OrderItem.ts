import { CartItem } from './cartItem';


export class OrderItem {
  id: number;
  cartItem: CartItem;
  price: number;

  constructor(cartItem: CartItem) {
    this.cartItem = cartItem;
  }
}
