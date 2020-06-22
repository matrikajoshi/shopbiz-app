import { Comment } from './comment';
import { Category } from './category';

export class Product {
  id: number;
  name: string;
  sku: string;
  category: Category;
  featured: boolean;
  active: boolean;
  description: string;
  imageUrl: string;
  priceNormal: number;
  price: number;
  reduction: string;
  availableQuantities: number;
  lastUpdated: string;
  comments: Comment[];
}
