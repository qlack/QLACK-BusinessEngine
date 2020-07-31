import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {CategoryDto} from '../dto/category-dto';
import {CategoryService} from '../services/category.service';
import {WorkingSetDto} from '../dto/working-set-dto';
import {RuleDto} from '../dto/rule-dto';
import {WorkingSetService} from '../services/working-set.service';
import {RuleService} from '../services/rule.service';
import {MatSelectionList} from '@angular/material/list';

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
    this.workingSetService.getAllSorted().subscribe(response =>
      this.workingSets = response
    );
    this.ruleService.getAllSorted().subscribe(response =>
      this.rules = response
    );
    this.categoryService.getAllSorted().subscribe(response =>
      this.categories = response
    );
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
}
