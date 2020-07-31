import {Component, OnInit} from '@angular/core';
import {ConfirmPopupComponent} from '../../confirm-popup/confirm-popup.component';
import {MatDialog} from '@angular/material/dialog';
import {AddRulePopupComponent} from '../../add-pages/add-rule-popup/add-rule-popup.component';
import {FormBuilder, Validators} from '@angular/forms';
import {CategoryService} from '../../services/category.service';
import {CategoryDto} from '../../dto/category-dto';
import {ActivatedRoute, Router} from '@angular/router';
import {WorkingSetService} from '../../services/working-set.service';
import {WorkingSetDto} from '../../dto/working-set-dto';
import {WorkingSetVersionDto} from '../../dto/working-set-version-dto';
import {RuleVersionDto} from '../../dto/rule-version-dto';
import {RuleService} from '../../services/rule.service';
import {RuleDto} from '../../dto/rule-dto';
import {WorkingSetVersionService} from '../../services/working-set-version.service';
import {ProjectComponent} from '../../project/project.component';

@Component({
  selector: 'app-working-set-page',
  templateUrl: './working-set-page.component.html',
  styleUrls: ['./working-set-page.component.css']
})
export class WorkingSetPageComponent implements OnInit {

  workingSetForm = this.fb.group({
    id: [''],
    name: ['', Validators.required],
    description: [''],
    status: [false],
    workingSetVersions: [],
    categories: [],
    createdBy: [''],
    createdOn: [],
    modifiedBy: [''],
    modifiedOn: [],
    currentVersionDescription: [],
    currentRuleVersions: [],
    currentRules: [],
    currentIndexes: []
  });

  id: string;
  name: string;
  versionName: string;
  description: string;
  edit = false;
  isModified = false;
  indexes: number[] = [];

  workingSetVersion: WorkingSetVersionDto;
  workingSet: WorkingSetDto;

  workingSetCategories: CategoryDto[];
  allCategories: CategoryDto[];
  workingSetVersions: WorkingSetVersionDto[];

  rules: RuleDto[] = [];
  ruleVersions: RuleVersionDto[] = [];

  currentRuleVersions: RuleDto[] = [];
  currentRules: RuleVersionDto[] = [];
  currentIndexes: number[] = [];

  constructor(
    private fb: FormBuilder,
    private dialog: MatDialog,
    private project: ProjectComponent,
    private workingSetService: WorkingSetService,
    private workingSetVersionService: WorkingSetVersionService,
    private ruleService: RuleService,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.workingSetForm.reset();
      this.id = params.id;

      this.workingSetService.get(this.id).subscribe(response => {
        this.workingSet = response;
        this.name = response.name;

        this.workingSetVersion = response.workingSetVersions[response.workingSetVersions.length - 1];

        this.initiate();

        this.workingSetCategories = response.categories;
        this.workingSetVersions = response.workingSetVersions;

        this.workingSetForm.patchValue(response);
        this.workingSetForm.disable();
        this.edit = false;
      });
    });

    this.categoryService.getAllSorted().subscribe(response =>
      this.allCategories = response);
  }

  initiate() {
    if (this.workingSetVersion) {
      this.versionName = this.workingSetVersion.name;
      this.description = this.workingSetVersion.description;
      this.workingSetForm.get('currentVersionDescription').setValue(
        this.workingSetVersion.description);
      this.isModified = !!this.workingSetVersion.modifiedBy;

      this.ruleVersions = this.workingSetVersion.ruleVersions;
      this.currentRuleVersions = JSON.parse(JSON.stringify(this.workingSetVersion.ruleVersions));

      if (this.ruleVersions && this.ruleVersions.length > 0) {
        this.rules = [];
        this.currentRules = [];
        this.indexes = [];
        this.currentIndexes = [];

        this.ruleVersions.forEach(ver => {
          this.ruleService.get(ver.rule).subscribe(param => {
            this.rules.push(param);
            this.currentRules.push(param);
            this.indexes.push(this.indexes.length);
            this.currentIndexes.push(this.currentIndexes.length);
          });
        });
      } else {
        this.ruleVersions = [];
        this.currentRuleVersions = [];
        this.rules = [];
        this.currentRules = [];
        this.indexes = [];
        this.currentIndexes = [];
      }
    } else {
      this.versionName = 'Working set rule';
      this.ruleVersions = [];
      this.currentRuleVersions = [];
      this.rules = [];
      this.currentRules = [];
      this.indexes = [];
      this.currentIndexes = [];
    }
  }

  addRow(): void {
    const dialogRef = this.dialog.open(AddRulePopupComponent, {
      width: '350px'
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.rules.push(data.rule.value);
        this.ruleVersions.push(data.version.value);
        this.indexes.push(this.indexes.length);
      }
    });
  }

  deleteRow(position: number): void {
    const index = this.indexes.indexOf(position, 0);
    if (index > -1) {
      this.indexes.splice(index, 1);
      this.rules.splice(index, 1);
      this.ruleVersions.splice(index, 1);
    }
  }

  editRow(index: number) {
    const popUpData = {
      rule: this.rules[index],
      version: this.ruleVersions[index]
    };

    const dialogRef = this.dialog.open(AddRulePopupComponent, {
      width: '350px',
      data: popUpData
    });
    dialogRef.afterClosed().subscribe(data => {
      if (data) {
        this.rules[index] = data.rule.value;
        this.ruleVersions[index] = data.version.value;
      }
    });
  }

  deleteWorkingSet() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        name: 'Delete working set',
        text: 'Are you sure, you want to delete this working set?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.workingSetService.delete(this.id).subscribe(response => {
          this.project.updateWorkingSets();
          this.router.navigate(['project']);
        });
      }
    });
  }

  deleteVersion() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        name: 'Delete version',
        text: 'Are you sure, you want to delete this version?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.workingSetVersionService.delete(this.workingSetVersion.id).subscribe(response =>
          window.location.reload()
        );
      }
    });
  }

  editWorkingSet() {
    this.edit = !this.edit;
    this.workingSetForm.get('name').enable();
    this.workingSetForm.get('description').enable();
    this.workingSetForm.get('status').enable();
    this.workingSetForm.get('categories').enable();
    this.workingSetForm.get('currentVersionDescription').enable();
  }

  cancel() {
    this.workingSetForm.patchValue(this.workingSet);
    this.rules = JSON.parse(JSON.stringify(this.currentRules));
    this.ruleVersions = JSON.parse(JSON.stringify(this.currentRuleVersions));
    this.indexes = JSON.parse(JSON.stringify(this.currentIndexes));

    this.workingSetForm.get('name').disable();
    this.workingSetForm.get('description').disable();
    this.workingSetForm.get('status').disable();
    this.workingSetForm.get('categories').disable();
    this.workingSetForm.get('currentVersionDescription').disable();
    this.edit = false;
  }

  editComplete() {
    if (this.workingSetForm.valid) {
      this.currentRuleVersions = JSON.parse(JSON.stringify(this.ruleVersions));
      this.currentRules = JSON.parse(JSON.stringify(this.rules));
      this.currentIndexes = JSON.parse(JSON.stringify(this.indexes));

      if (this.workingSetVersion) {
        this.workingSetVersion.description = this.workingSetForm.get(
          'currentVersionDescription').value;
        this.workingSetVersion.ruleVersions = this.ruleVersions;
        this.workingSetVersionService.save(this.workingSetVersion).subscribe();
      }

      this.workingSetForm.enable();
      const workingSetDto: WorkingSetDto = this.workingSetForm.value;
      this.workingSetService.save(workingSetDto).subscribe(response =>
        window.location.reload()
      );
    }
  }

  checkCategories(c1: any, c2: any) {
    return (c1 && c2) ? (c1.id === c2.id) : false;
  }

  changeVersion(versionDto: WorkingSetVersionDto) {
    this.workingSetVersion = versionDto;
    this.initiate();
  }
}
