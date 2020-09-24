import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {RuleDto} from '../../dto/rule-dto';
import {RuleService} from '../../services/rule.service';
import {RuleVersionService} from '../../services/rule-version.service';
import {RuleVersionDto} from '../../dto/rule-version-dto';

@Component({
  selector: 'app-add-rule-popup',
  templateUrl: './add-rule-popup.component.html',
  styleUrls: ['./add-rule-popup.component.css']
})
export class AddRulePopupComponent implements OnInit {

  createRuleForm = this.fb.group({
    rule: ['', Validators.required],
    version: ['', Validators.required]
  });

  allRules: RuleDto[];
  allVersions: RuleVersionDto[];

  rule: RuleDto;
  version: RuleVersionDto;

  cancelButton = 'CANCEL';
  okButton = 'ADD';
  title = 'New rule';
  subtitle = 'Add a new rule to the working set';

  constructor(
    @Inject(MAT_DIALOG_DATA) private data,
    private ruleService: RuleService,
    private ruleVersionService: RuleVersionService,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddRulePopupComponent>
  ) {
    if (data) {
      this.rule = data.rule;
      this.version = data.version;
      this.title = 'Edit rule';
      this.subtitle = 'Edit the working set rule';
      this.okButton = 'EDIT';
    }
  }

  private static equalObjects(rule1, rule2) {
    return rule1.id === rule2.id;
  }

  cancel() {
    this.dialogRef.close();
  }

  add() {
    if (this.createRuleForm.valid) {
      const data = {
        rule: this.createRuleForm.get('rule'),
        version: this.createRuleForm.get('version')
      };
      this.dialogRef.close(data);
    }
  }

  ruleSelect(ruleDto: RuleDto) {
    this.rule = ruleDto;

    this.ruleVersionService.getByRule(this.rule.id).subscribe(response =>
      this.allVersions = response
    );
  }

  ngOnInit(): void {
    this.ruleService.getAllSorted().subscribe(response => {
      this.allRules = response;
      this.allRules.forEach(rule => {
        if (AddRulePopupComponent.equalObjects(rule, this.rule)) {
          this.allVersions = rule.ruleVersions;
          this.allVersions.forEach(version => {
            if (AddRulePopupComponent.equalObjects(version, this.version)) {
              this.createRuleForm.patchValue({rule: rule, version: version});
            }
          })
        }
      });
    });
  }
}
