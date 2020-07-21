import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {
  searchText = '';

  flagWorkingSet = false;
  flagRule = false;
  flagCategory = false;
  flagAddWorkingSet = false;
  flagAddRule = false;
  flagAddCategory = false;
  flagAddVersion = false;
  caller = '';
  workingSets = ['WorkingSet001', 'WorkingSet002', 'WorkingSet003', 'WorkingSet004'];
  rules = ['Rule001', 'Rule002', 'Rule003', 'Rule004', 'Rule005'];
  categories = ['Category001', 'Category002', 'Category003'];

  constructor(
    private router: Router,
  ) { }

  ngOnInit(): void {
  }

  openWorkingSetPage() {
    this.flagWorkingSet = !this.flagWorkingSet;
    this.flagRule = false;
    this.flagCategory = false;
    this.flagAddWorkingSet = false;
    this.flagAddRule = false;
    this.flagAddCategory = false;
    this.flagAddVersion = false;
    if (this.caller === 'workingSet') {
      this.caller = '';
    }
    else {this.caller = 'workingSet'; }
  }

  openRulePage() {
    this.flagWorkingSet = false;
    this.flagRule = !this.flagRule;
    this.flagCategory = false;
    this.flagAddWorkingSet = false;
    this.flagAddRule = false;
    this.flagAddCategory = false;
    this.flagAddVersion = false;
    if (this.caller === 'rule') {
      this.caller = '';
    }
    else {this.caller = 'rule'; }
  }

  openCategoryPage() {
    this.flagWorkingSet = false;
    this.flagRule = false;
    this.flagCategory = !this.flagCategory;
    this.flagAddWorkingSet = false;
    this.flagAddRule = false;
    this.flagAddCategory = false;
    this.flagAddVersion = false;
    if (this.caller === 'category') {
      this.caller = '';
    }
    else {this.caller = 'category'; }
  }

  addWorkingSet() {
    this.flagWorkingSet = false;
    this.flagRule = false;
    this.flagCategory = false;
    this.flagAddWorkingSet = !this.flagAddWorkingSet;
    this.flagAddRule = false;
    this.flagAddCategory = false;
    this.flagAddVersion = false;
  }

  addRule() {
    this.flagWorkingSet = false;
    this.flagRule = false;
    this.flagCategory = false;
    this.flagAddWorkingSet = false;
    this.flagAddRule = !this.flagAddRule;
    this.flagAddCategory = false;
    this.flagAddVersion = false;
  }

  addCategory() {
    this.flagWorkingSet = false;
    this.flagRule = false;
    this.flagCategory = false;
    this.flagAddWorkingSet = false;
    this.flagAddRule = false;
    this.flagAddCategory = !this.flagAddCategory;
    this.flagAddVersion = false;
  }

  addVersion(callerPage: string) {
    this.flagWorkingSet = false;
    this.flagRule = false;
    this.flagCategory = false;
    this.flagAddWorkingSet = false;
    this.flagAddRule = false;
    this.flagAddCategory = false;
    this.flagAddVersion = !this.flagAddVersion;
    this.caller = callerPage;
  }

  backToMenu() {
    this.router.navigate(['home']);
  }

  searchClose() {
    this.searchText = '';
  }
}
