import {Component, OnInit} from '@angular/core';
import {CategoryService} from '../../services/category.service';
import {CategoryDto} from '../../dto/category-dto';
import {FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RuleDto} from '../../dto/rule-dto';
import {RuleService} from '../../services/rule.service';
import {Location} from '@angular/common';
import {ProjectComponent} from '../../project/project.component';
import {RuleVersionService} from '../../services/rule-version.service';

@Component({
  selector: 'app-add-rule',
  templateUrl: './add-rule.component.html',
  styleUrls: ['./add-rule.component.css']
})
export class AddRuleComponent implements OnInit {

  categories: CategoryDto[];
  ruleDto: RuleDto;
  rules: RuleDto[];

  addRuleForm = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    status: [true],
    categories: []
  });

  constructor(
    private project: ProjectComponent,
    private categoryService: CategoryService,
    private ruleService: RuleService,
    private ruleVersionService: RuleVersionService,
    private router: Router,
    private location: Location,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.ruleService.getAllSorted().subscribe(response =>
      this.rules = response);

    this.categoryService.getAllSorted().subscribe(response =>
      this.categories = response);
  }

  cancel() {
    this.location.back();
  }

  addRule() {
    if (this.addRuleForm.valid) {
      const ruleDto: RuleDto = this.addRuleForm.value;

      this.ruleService.save(ruleDto).subscribe(response => {
        if (this.ruleDto && this.ruleDto.ruleVersions) {
          const ruleVersions = this.ruleDto.ruleVersions;
          ruleVersions.forEach(ruleVersion => {
            ruleVersion.id = null;
            ruleVersion.createdBy = null;
            ruleVersion.createdOn = null;
            ruleVersion.modifiedBy = null;
            ruleVersion.modifiedOn = null;
            ruleVersion.rule = response.id;

            this.ruleVersionService.save(ruleVersion).subscribe();
          });
        }

        this.project.updateRules();
        this.router.navigate(['project']);
      });
    }
  }

  changeRule(ruleDto: any) {
    this.ruleDto = ruleDto;
  }

}
