import {BaseDto} from './base-dto';
import {CategoryDto} from './category-dto';
import {WorkingSetVersionDto} from './working-set-version-dto';

export interface WorkingSetDto extends BaseDto {
  name?: string;
  description?: string;
  status?: boolean;
  workingSetVersions?: WorkingSetVersionDto[];
  categories?: CategoryDto[];
}
