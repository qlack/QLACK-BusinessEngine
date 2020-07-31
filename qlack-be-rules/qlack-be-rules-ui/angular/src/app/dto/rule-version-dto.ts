import {BaseDto} from './base-dto';

export interface RuleVersionDto extends BaseDto {
  name?: string;
  description?: string;
  rule?: string;
  dmnXml?: string;
}
