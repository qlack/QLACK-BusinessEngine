import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {CategoryService} from '../../services/category.service';
import {CategoryDto} from '../../dto/category-dto';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {ProjectComponent} from '../../project/project.component';

@Component({
  selector: 'app-add-category',
  templateUrl: './add-category.component.html',
  styleUrls: ['./add-category.component.css']
})
export class AddCategoryComponent implements OnInit {

  addCategoryForm = this.fb.group({
    name: ['', Validators.required],
    description: ['']
  });

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private location: Location,
    private categoryService: CategoryService,
    private project: ProjectComponent
  ) {
  }

  ngOnInit(): void {
  }

  cancel() {
    this.location.back();
  }

  addCategory() {
    if (this.addCategoryForm.valid) {
      const category: CategoryDto = this.addCategoryForm.value;
      this.categoryService.save(category).subscribe(response => {
        this.project.updateCategories();
        this.router.navigate(['project']);
      });
    }
  }

}
