import {Injectable} from '@angular/core';
import {CrudService} from './crud.service';
import {HttpClient} from '@angular/common/http';
import {QFormsService} from '@qlack/forms';
import {WorkingSetDto} from '../dto/working-set-dto';

@Injectable({
  providedIn: 'root'
})
export class WorkingSetService extends CrudService<WorkingSetDto> {

  constructor(http: HttpClient, qForms: QFormsService) {
    super(http, 'workingSets', qForms);
  }

}
