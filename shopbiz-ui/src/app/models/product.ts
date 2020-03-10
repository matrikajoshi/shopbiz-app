import { Comment } from './comment';

export class Product {
  id: number;
  name: string;
  sku: string;
  category: string;
  featured: boolean;
  description: string;
  imageUrl: string;
  priceNormal: number;
  price: number;
  reduction: string;
  active: boolean;
  lastUpdated: string;
  comments: Comment[];
}
