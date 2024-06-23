import {ProductImage} from "../../models/product.image";

export class UpdateProductDTO {
  id: number;
  name: string;
  price: number;
  thumbnail: string;
  description: string;
  category_id: number;
  url: string;
  product_images: ProductImage[];

  constructor(data: any) {
    this.id = data.id;
    this.name = data.name;
    this.price = data.price;
    this.thumbnail = data.thumbnail;
    this.description = data.description;
    this.category_id = data.category_id;
    this.url = data.url;
    this.product_images = data.product_images;
  }

  public setThumbnail(url: string) {
    this.thumbnail = url;
  }
}
