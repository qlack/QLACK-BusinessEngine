import { Component, OnInit } from '@angular/core';
import {ProjectComponent} from '../../project/project.component';

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent implements OnInit {

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
      this.project.addCategory();
    }
  }

  addCategory() {
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
      this.project.addCategory();
    }
  }

}
