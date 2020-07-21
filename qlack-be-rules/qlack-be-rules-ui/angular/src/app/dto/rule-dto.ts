import {BaseDto} from './base-dto';
import {CategoryDto} from './category-dto';

export interface RuleDto extends BaseDto {
  name?: string;
  description?: string;
  status?: boolean;
  version?: string;
  dmnXml?: string;
  categories?: CategoryDto[];
}
