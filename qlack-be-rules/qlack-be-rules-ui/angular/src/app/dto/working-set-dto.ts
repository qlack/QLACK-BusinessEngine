import {BaseDto} from './base-dto';
import {RuleDto} from './rule-dto';
import {CategoryDto} from './category-dto';

export interface WorkingSetDto extends BaseDto {
  name?: string;
  description?: string;
  status?: boolean;
  version?: string;
  rules?: RuleDto[];
  categories?: CategoryDto[];
}
