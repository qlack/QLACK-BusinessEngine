import { Component, OnInit } from '@angular/core';
import {ProjectComponent} from '../../project/project.component';

@Component({
  selector: 'app-add-working-set',
  templateUrl: './add-working-set.component.html',
  styleUrls: ['./add-working-set.component.css']
})
export class AddWorkingSetComponent implements OnInit {

  categories = ['Category 1', 'Category 2', 'Category 3'];

  constructor(private project: ProjectComponent) { }

  ngOnInit(): void {
  }

  cancel() {
    if (this.project.caller === 'rule') {
      this.project.openRulePage();
    }
    else if (this.project.caller === 'category') {
      this.project.openCategoryPage();
    }
    else if (this.project.caller === 'workingSet'){
      this.project.openWorkingSetPage();
    }
    else {
      this.project.addWorkingSet();
    }
  }

  addWorkingSet() {
    if (this.project.caller === 'rule') {
      this.project.openRulePage();
    }
    else if (this.project.caller === 'category') {
      this.project.openCategoryPage();
    }
    else if (this.project.caller === 'workingSet'){
      this.project.openWorkingSetPage();
    }
    else {
      this.project.addWorkingSet();
    }
  }
}
