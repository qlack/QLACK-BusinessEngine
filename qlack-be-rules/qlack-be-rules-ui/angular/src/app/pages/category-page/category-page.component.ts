import { Component, OnInit } from '@angular/core';
import {ConfirmPopupComponent} from '../../confirm-popup/confirm-popup.component';
import {ProjectComponent} from '../../project/project.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-category-page',
  templateUrl: './category-page.component.html',
  styleUrls: ['./category-page.component.scss']
})
export class CategoryPageComponent implements OnInit {

  flag = false;
  constructor(
    private project: ProjectComponent,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  editCategory() {
    this.flag = !this.flag;
  }

  deleteCategory() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        title: 'Delete category',
        text: 'Are you sure, you want to delete this category?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.project.openCategoryPage();
      }
    });
  }

}
