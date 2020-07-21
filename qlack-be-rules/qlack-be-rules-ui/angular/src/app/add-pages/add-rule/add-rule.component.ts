import { Component, OnInit } from '@angular/core';
import {ProjectComponent} from '../../project/project.component';

@Component({
  selector: 'app-add-rule',
  templateUrl: './add-rule.component.html',
  styleUrls: ['./add-rule.component.css']
})
export class AddRuleComponent implements OnInit {

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
      this.project.addRule();
    }
  }

  addRule() {
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
      this.project.addRule();
    }
  }

  inputFile(fileInputEvent: any) {
    console.log(fileInputEvent.target.files[0]);
  }
}
