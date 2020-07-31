import {BaseDto} from './base-dto';
import {RuleVersionDto} from './rule-version-dto';

export interface WorkingSetVersionDto extends BaseDto {
  name?: string;
  description?: string;
  workingSet?: string;
  ruleVersions?: RuleVersionDto[];
}
