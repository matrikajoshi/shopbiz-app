import { OrderItem } from './OrderItem';

export class Order {
  id: number;
  orderDate: string;
  orderItems: OrderItem[];
  orderStatus: string;
  totalAmount: string;
  delivered: string;
  updateDate: string;

}
