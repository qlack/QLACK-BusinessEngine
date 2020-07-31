import {Component, OnInit} from '@angular/core';
import {ProjectComponent} from '../../project/project.component';
import {ConfirmPopupComponent} from '../../confirm-popup/confirm-popup.component';
import {MatDialog} from '@angular/material/dialog';
import {CategoryService} from '../../services/category.service';
import {CategoryDto} from '../../dto/category-dto';
import {RuleDto} from '../../dto/rule-dto';
import {RuleVersionDto} from '../../dto/rule-version-dto';
import {ActivatedRoute, Router} from '@angular/router';
import {RuleService} from '../../services/rule.service';
import {FormBuilder, Validators} from '@angular/forms';
import {RuleVersionService} from '../../services/rule-version.service';
import 'brace';
import 'brace/mode/xml';
import 'brace/theme/eclipse';

@Component({
  selector: 'app-rule-page',
  templateUrl: './rule-page.component.html',
  styleUrls: ['./rule-page.component.scss']
})

export class RulePageComponent implements OnInit {

  ruleForm = this.fb.group({
    id: [''],
    name: ['', Validators.required],
    description: [''],
    status: [false],
    ruleVersions: [],
    categories: [null],
    currentVersionDescription: [''],
    createdBy: [''],
    createdOn: [''],
    modifiedBy: [''],
    modifiedOn: ['']
  });

  id: string;
  name: string;
  versionName: string;
  edit = false;
  isModified = false;

  dmn = '';

  version: RuleVersionDto;
  rule: RuleDto;

  ruleCategories: CategoryDto[];
  allCategories: CategoryDto[];
  ruleVersions: RuleVersionDto[];

  constructor(
    private project: ProjectComponent,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private categoryService: CategoryService,
    private ruleService: RuleService,
    private ruleVersionService: RuleVersionService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.ruleForm.reset();
      this.id = params.id;

      this.ruleService.get(this.id).subscribe(response => {
        this.rule = response;
        this.name = response.name;

        this.version = response.ruleVersions[response.ruleVersions.length - 1];
        this.initiate();

        this.ruleCategories = response.categories;
        this.ruleVersions = response.ruleVersions;

        this.ruleForm.patchValue(response);
        this.ruleForm.disable();
        this.edit = false;
      });
    });

    this.categoryService.getAllSorted().subscribe(response =>
      this.allCategories = response);
  }

  initiate() {
    if (this.version) {
      this.versionName = this.version.name;
      this.ruleForm.get('currentVersionDescription').setValue(this.version.description);
      this.isModified = !!this.version.modifiedBy;
      if (this.version.dmnXml) {
        this.dmn = this.version.dmnXml;
      } else {
        this.dmn = '';
      }
    } else {
      this.versionName = 'Rule version';
    }
  }

  deleteRule() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        name: 'Delete rule',
        text: 'Are you sure, you want to delete this rule?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.ruleService.delete(this.id).subscribe(response => {
          this.project.updateRules();
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
        this.ruleVersionService.delete(this.version.id).subscribe(response =>
          window.location.reload()
        );
      }
    });
  }

  editRule() {
    this.edit = !this.edit;
    this.ruleForm.get('name').enable();
    this.ruleForm.get('description').enable();
    this.ruleForm.get('status').enable();
    this.ruleForm.get('categories').enable();
    this.ruleForm.get('currentVersionDescription').enable();
  }

  cancel() {
    this.ruleForm.patchValue(this.rule);
    if (this.version && this.version.dmnXml) {
      this.dmn = this.version.dmnXml;
    } else {
      this.dmn = '';
    }

    this.ruleForm.get('name').disable();
    this.ruleForm.get('description').disable();
    this.ruleForm.get('status').disable();
    this.ruleForm.get('categories').disable();
    this.ruleForm.get('currentVersionDescription').disable();
    this.edit = !this.edit;
  }

  editComplete() {
    if (this.ruleForm.valid) {
      if (this.version) {
        if (this.dmn || this.version.dmnXml) {
          this.version.dmnXml = this.dmn;
        }
        this.version.description = this.ruleForm.get('currentVersionDescription').value;
        this.ruleVersionService.save(this.version).subscribe();
      }

      this.ruleForm.enable();
      const ruleDto: RuleDto = this.ruleForm.value;
      this.ruleService.save(ruleDto).subscribe(response =>
        window.location.reload()
      );
    }
  }

  checkCategories(c1: any, c2: any) {
    return (c1 && c2) ? (c1.id === c2.id) : false;
  }

  changeVersion(versionDto: RuleVersionDto) {
    this.version = versionDto;
    this.initiate();
  }

  async inputFile(event) {
    const file = event.target.files[0];
    this.dmn = await this.readFileContent(file);
  }

  readFileContent(file: File): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      if (!file) {
        resolve('');
      }

      const reader = new FileReader();

      reader.onload = (e) => {
        const text = reader.result.toString();
        resolve(text);
      };

      reader.readAsText(file);
    });
  }
}
