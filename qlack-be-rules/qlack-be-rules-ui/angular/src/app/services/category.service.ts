import {Injectable} from '@angular/core';
import {CrudService} from './crud.service';
import {CategoryDto} from '../dto/category-dto';
import {HttpClient} from '@angular/common/http';
import {QFormsService} from '@qlack/forms';

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends CrudService<CategoryDto> {

  constructor(http: HttpClient, qForms: QFormsService) {
    super(http, 'categories', qForms);
  }

}
