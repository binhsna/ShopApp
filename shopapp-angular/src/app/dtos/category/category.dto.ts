import {IsString} from "class-validator";

export class CategoryDTO {
  @IsString()
  name: string;

  constructor(data: any) {
    this.name = data.name;
  }
}
