import { CartItem } from './cartItem';


export class ShoppingCart {
  id: number;
  cartItems: CartItem[];
  userName: string;

  constructor($id: number, $cartItems: CartItem[], $userName: string) {
    this.id = $id;
    this.cartItems = $cartItems;
    this.userName = $userName;
  }
}
