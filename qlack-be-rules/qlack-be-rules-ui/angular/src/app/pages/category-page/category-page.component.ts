import {Component, OnInit} from '@angular/core';
import {ConfirmPopupComponent} from '../../confirm-popup/confirm-popup.component';
import {ProjectComponent} from '../../project/project.component';
import {MatDialog} from '@angular/material/dialog';
import {ActivatedRoute, Router} from '@angular/router';
import {CategoryService} from '../../services/category.service';
import {CategoryDto} from '../../dto/category-dto';
import {FormBuilder, Validators} from '@angular/forms';

@Component({
  selector: 'app-category-page',
  templateUrl: './category-page.component.html',
  styleUrls: ['./category-page.component.scss']
})
export class CategoryPageComponent implements OnInit {

  categoryForm = this.fb.group({
    id: [''],
    name: ['', Validators.required],
    description: [''],
    createdBy: [],
    createdOn: [''],
    modifiedBy: [],
    modifiedOn: []
  });

  name: string;
  id: string;
  edit = false;

  category: CategoryDto;

  constructor(
    private project: ProjectComponent,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.categoryForm.reset();
      this.id = params.id;

      this.categoryService.get(this.id).subscribe(response => {
        this.category = response;
        this.name = response.name;

        this.categoryForm.patchValue(response);
        this.categoryForm.disable();
        this.edit = false;
      });
    });
  }

  editCategory() {
    this.edit = !this.edit;
    this.categoryForm.get('name').enable();
    this.categoryForm.get('description').enable();
  }

  cancel() {
    this.categoryForm.patchValue(this.category);

    this.categoryForm.get('name').disable();
    this.categoryForm.get('description').disable();
    this.edit = !this.edit;
  }

  editComplete() {
    if (this.categoryForm.valid) {
      this.categoryForm.enable();
      const categoryDto: CategoryDto = this.categoryForm.value;
      this.categoryService.save(categoryDto).subscribe(response =>
        window.location.reload()
      );
    }
  }

  deleteCategory() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        name: 'Delete category',
        text: 'Are you sure, you want to delete this category?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.categoryService.delete(this.id).subscribe(response => {
          this.project.updateCategories();
          this.router.navigate(['project']);
        });
      }
    });
  }

}
