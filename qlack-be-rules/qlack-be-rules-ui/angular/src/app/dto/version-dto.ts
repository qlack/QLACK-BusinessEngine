import {BaseDto} from './base-dto';

export interface VersionDto extends BaseDto {
  name?: string;
  description?: string;
}
