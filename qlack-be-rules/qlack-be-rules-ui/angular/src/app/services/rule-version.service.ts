import {Injectable} from '@angular/core';
import {CrudService} from './crud.service';
import {HttpClient} from '@angular/common/http';
import {QFormsService} from '@qlack/forms';
import {RuleVersionDto} from '../dto/rule-version-dto';
import {Observable} from 'rxjs';
import {AppConstants} from '../app.constants';

@Injectable({
  providedIn: 'root'
})
export class RuleVersionService extends CrudService<RuleVersionDto> {

  constructor(http: HttpClient, qForms: QFormsService) {
    super(http, 'ruleVersions', qForms);
  }

  getByRule(id: any): Observable<RuleVersionDto[]> {
    return this.http.get<RuleVersionDto[]>(`${AppConstants.API_ROOT}/ruleVersions/rule/${id}`);
  }

  saveXml(id: string, xml: string): Observable<RuleVersionDto[]> {
    return this.http.post<RuleVersionDto[]>(`${AppConstants.API_ROOT}/ruleVersions/rule/${id}`,
      xml);
  }

}
