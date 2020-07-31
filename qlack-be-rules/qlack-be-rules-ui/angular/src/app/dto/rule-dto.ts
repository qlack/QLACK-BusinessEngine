import {BaseDto} from './base-dto';
import {CategoryDto} from './category-dto';
import {RuleVersionDto} from './rule-version-dto';

export interface RuleDto extends BaseDto {
  name?: string;
  description?: string;
  status?: boolean;
  ruleVersions?: RuleVersionDto[];
  categories?: CategoryDto[];
}
