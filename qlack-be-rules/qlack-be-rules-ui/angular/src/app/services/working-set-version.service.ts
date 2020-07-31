import {Injectable} from '@angular/core';
import {CrudService} from './crud.service';
import {HttpClient} from '@angular/common/http';
import {QFormsService} from '@qlack/forms';
import {WorkingSetVersionDto} from '../dto/working-set-version-dto';

@Injectable({
  providedIn: 'root'
})
export class WorkingSetVersionService extends CrudService<WorkingSetVersionDto> {

  constructor(http: HttpClient, qForms: QFormsService) {
    super(http, 'workingSetVersions', qForms);
  }

}
