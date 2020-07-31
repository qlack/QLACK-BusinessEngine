import {Injectable} from '@angular/core';
import {CrudService} from './crud.service';
import {HttpClient} from '@angular/common/http';
import {QFormsService} from '@qlack/forms';
import {RuleDto} from '../dto/rule-dto';

@Injectable({
  providedIn: 'root'
})
export class RuleService extends CrudService<RuleDto> {

  constructor(http: HttpClient, qForms: QFormsService) {
    super(http, 'rules', qForms);
  }

}
