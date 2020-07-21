import { Component, OnInit } from '@angular/core';
import {ConfirmPopupComponent} from '../../confirm-popup/confirm-popup.component';
import {ProjectComponent} from '../../project/project.component';
import {MatDialog} from '@angular/material/dialog';
import {AddRulePopupComponent} from '../../add-pages/add-rule-popup/add-rule-popup.component';
import {FormBuilder, Validators} from '@angular/forms';
import {RuleDto} from '../../dto/rule-dto';

@Component({
  selector: 'app-working-set-page',
  templateUrl: './working-set-page.component.html',
  styleUrls: ['./working-set-page.component.css']
})
export class WorkingSetPageComponent implements OnInit {

  flag = false;
  indexes: number[] = [];
  ruleDto: RuleDto[] = [];
  versions = ['Version 1', 'Version 2', 'Version 3'];
  categories = ['Category 1', 'Category 2', 'Category 3'];

  constructor(
    private fb: FormBuilder,
    private project: ProjectComponent,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  addRow(): void {
    const dialogRef = this.dialog.open(AddRulePopupComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.ruleDto.push(data);
        this.indexes.push(this.indexes.length);
      }
    });
  }

  deleteRow(position: number): void {
    const index = this.indexes.indexOf(position, 0);
    if (index > -1) {
      this.indexes.splice(index, 1);
      this.ruleDto.splice(index, 1);
    }
  }

  editRow(index: number) {
    const popUpData = {
      name: this.ruleDto[index].name,
      version: this.ruleDto[index].version
    };

    const dialogRef = this.dialog.open(AddRulePopupComponent, {
      width: '350px',
      data: popUpData
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.ruleDto[index] = data;
      }
    });
  }

  editWorkingSet() {
    this.flag = !this.flag;
  }

  deleteWorkingSet() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        title: 'Delete working set',
        text: 'Are you sure, you want to delete this working set?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.project.openWorkingSetPage();
      }
    });
  }

  deleteVersion() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        title: 'Delete version',
        text: 'Are you sure, you want to delete this version?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.project.openWorkingSetPage();
      }
    });
  }

  createVersion() {
    this.project.addVersion('workingSet');
  }

}
