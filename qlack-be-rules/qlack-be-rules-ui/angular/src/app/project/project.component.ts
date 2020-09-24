import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {CategoryDto} from '../dto/category-dto';
import {CategoryService} from '../services/category.service';
import {WorkingSetDto} from '../dto/working-set-dto';
import {RuleDto} from '../dto/rule-dto';
import {WorkingSetService} from '../services/working-set.service';
import {RuleService} from '../services/rule.service';
import {MatSelectionList} from '@angular/material/list';
import {Observable} from 'rxjs';
import {FormControl} from '@angular/forms';
import {map, startWith} from 'rxjs/operators';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {
  searchText = '';

  workingSets: WorkingSetDto[];
  rules: RuleDto[];
  categories: CategoryDto[];

  formControl = new FormControl();
  options = new Map();
  searchOptions: string[] = [];
  filteredSearchOptions: Observable<string[]>;

  @ViewChild('workingSetList') workingSetList: MatSelectionList;
  @ViewChild('ruleList') ruleList: MatSelectionList;
  @ViewChild('categoryList') categoryList: MatSelectionList;

  constructor(
    private router: Router,
    private categoryService: CategoryService,
    private workingSetService: WorkingSetService,
    private ruleService: RuleService
  ) {
  }

  ngOnInit(): void {
    this.workingSetService.getAllSorted().subscribe(response => {
      this.workingSets = response;
      response.forEach(resp => {
        this.searchOptions.push(resp.name);
        this.options.set(resp.name, 'w' + resp.id);
      });
    });
    this.ruleService.getAllSorted().subscribe(response => {
      this.rules = response;
      response.forEach(resp => {
        this.searchOptions.push(resp.name);
        this.options.set(resp.name, 'r' + resp.id);
      });
    });
    this.categoryService.getAllSorted().subscribe(response => {
      this.categories = response;
      response.forEach(resp => {
        this.searchOptions.push(resp.name);
        this.options.set(resp.name, 'c' + resp.id);
      });
    });

    this.filteredSearchOptions = this.formControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );
  }

  optionSelect(name: string) {
    if (this.options.has(name)) {
      const fullId: string = this.options.get(name);
      const id: string = fullId.slice(1, fullId.length);

      if (fullId.charAt(0) == 'w') {
        this.router.navigate(['project/working-set', id]);
      } else if (fullId.charAt(0) == 'r') {
        this.router.navigate(['project/rule', id]);
      } else {
        this.router.navigate(['project/category', id]);
      }
    }
  }

  backToMenu() {
    this.router.navigate(['home']);
  }

  searchClose() {
    this.searchText = '';
  }

  disableOthers(list1: MatSelectionList, list2: MatSelectionList) {
    list1.deselectAll();
    list2.deselectAll();
  }

  updateWorkingSets() {
    this.workingSetService.getAllSorted().subscribe(response =>
      this.workingSets = response
    );
    this.deselectAll();
  }

  updateRules() {
    this.ruleService.getAllSorted().subscribe(response =>
      this.rules = response
    );
    this.deselectAll();
  }

  updateCategories() {
    this.categoryService.getAllSorted().subscribe(response =>
      this.categories = response
    );
    this.deselectAll();
  }

  deselectAll() {
    this.workingSetList.deselectAll();
    this.ruleList.deselectAll();
    this.categoryList.deselectAll();
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.searchOptions.filter(option => option.toLowerCase().indexOf(filterValue) === 0);
  }
}
