import {Component, OnInit} from '@angular/core';
import {CategoryService} from '../../services/category.service';
import {CategoryDto} from '../../dto/category-dto';
import {FormBuilder, Validators} from '@angular/forms';
import {WorkingSetDto} from '../../dto/working-set-dto';
import {Router} from '@angular/router';
import {WorkingSetService} from '../../services/working-set.service';
import {Location} from '@angular/common';
import {ProjectComponent} from '../../project/project.component';
import {WorkingSetVersionService} from '../../services/working-set-version.service';

@Component({
  selector: 'app-add-working-set',
  templateUrl: './add-working-set.component.html',
  styleUrls: ['./add-working-set.component.css']
})
export class AddWorkingSetComponent implements OnInit {

  categories: CategoryDto[];
  workingSetDto: WorkingSetDto;
  workingSets: WorkingSetDto[];

  addWorkingSetForm = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    status: [true],
    categories: []
  });

  constructor(
    private project: ProjectComponent,
    private categoryService: CategoryService,
    private workingSetService: WorkingSetService,
    private workingSetVersionService: WorkingSetVersionService,
    private router: Router,
    private location: Location,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.workingSetService.getAllSorted().subscribe(response =>
      this.workingSets = response
    );

    this.categoryService.getAllSorted().subscribe(response =>
      this.categories = response);
  }

  cancel() {
    this.location.back();
  }

  addWorkingSet() {
    if (this.addWorkingSetForm.valid) {
      const workingSetDto: WorkingSetDto = this.addWorkingSetForm.value;

      this.workingSetService.save(workingSetDto).subscribe(response => {
        if (this.workingSetDto && this.workingSetDto.workingSetVersions) {
          const workingSetVersions = this.workingSetDto.workingSetVersions;
          workingSetVersions.forEach(workingSetVersion => {
            workingSetVersion.id = null;
            workingSetVersion.createdBy = null;
            workingSetVersion.createdOn = null;
            workingSetVersion.modifiedBy = null;
            workingSetVersion.modifiedOn = null;
            workingSetVersion.workingSet = response.id;

            this.workingSetVersionService.save(workingSetVersion).subscribe();
          });
        }

        this.project.updateWorkingSets();
        this.router.navigate(['project']);
      });
    }
  }

  changeWorkingSet(workingSetDto: any) {
    this.workingSetDto = workingSetDto;
  }
}
