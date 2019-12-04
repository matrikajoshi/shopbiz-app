import { OrderItem } from './OrderItem';

export class Order {
  id: number;
  orderItems: OrderItem[];
  totalAmount: string;
  delivered: string;
  orderDate: string;
  updateDate: string;

}
