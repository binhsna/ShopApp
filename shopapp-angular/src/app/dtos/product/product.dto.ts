import {Product} from "../../models/product";
import {ProductImage} from "../../models/product.image";

export class ProductDTO implements Product {
  id!: number;
  name!: string;
  price!: number;
  thumbnail!: string;
  description!: string;
  category_id!: number;
  url!: string;
  product_images!: ProductImage[];

  constructor() {
  }

  public init(data: any) {
    this.id = data.id;
    this.name = data.name;
    this.price = data.price;
    this.thumbnail = data.thumbnail;
    this.description = data.description;
    this.category_id = data.category_id;
    this.url = data.url;
  }

  public setProductImages(list: ProductImage[]) {
    this.product_images = list;
  }
}
