import { Component, OnInit } from '@angular/core';
import {ProjectComponent} from '../../project/project.component';

@Component({
  selector: 'app-add-version',
  templateUrl: './add-version.component.html',
  styleUrls: ['./add-version.component.css']
})
export class AddVersionComponent implements OnInit {

  versions = ['Version 1', 'Version 2', 'Version 3'];

  constructor(
    private project: ProjectComponent
  ) { }

  ngOnInit(): void {
  }

  cancel() {
    if (this.project.caller === 'rule') {
      this.project.openRulePage();
    }
    else {
      this.project.openWorkingSetPage();
    }
  }

  createVersion() {
    if (this.project.caller === 'rule') {
      this.project.openRulePage();
    }
    else {
      this.project.openWorkingSetPage();
    }
  }
}
