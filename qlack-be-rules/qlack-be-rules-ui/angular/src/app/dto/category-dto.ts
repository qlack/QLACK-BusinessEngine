import {BaseDto} from './base-dto';

export interface CategoryDto extends BaseDto {
  name?: string;
  description?: string;
}
