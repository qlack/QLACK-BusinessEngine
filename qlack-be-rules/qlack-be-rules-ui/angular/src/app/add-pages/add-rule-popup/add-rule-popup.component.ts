import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
import {RuleDto} from '../../dto/rule-dto';

@Component({
  selector: 'app-add-rule-popup',
  templateUrl: './add-rule-popup.component.html',
  styleUrls: ['./add-rule-popup.component.css']
})
export class AddRulePopupComponent implements OnInit {

  createRuleForm = this.fb.group({
    name: ['', Validators.required],
    version: ['', Validators.required]
  });

  name: string;
  version: string;
  cancelButton = 'CANCEL';
  okButton = 'ADD';
  title = 'New rule';
  subtitle = 'Add a new rule to the working set';

  versions = ['Version 1', 'Version 2', 'Version 3'];
  rules = ['Rule 1', 'Rule 2', 'Rule 3'];

  constructor(
    @Inject(MAT_DIALOG_DATA) private data,
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<AddRulePopupComponent>
  ) {
    if (data) {
      this.name = data.name;
      this.version = data.version;
      this.title = 'Edit rule';
      this.subtitle = 'Edit the working set rule';
      this.okButton = 'EDIT';
    }
  }

  ngOnInit(): void {
  }

  cancel() {
    this.dialogRef.close();
  }

  add() {
    if (this.createRuleForm.valid) {
      const ruleDto: RuleDto = this.createRuleForm.value;
      this.dialogRef.close(ruleDto);
    }
  }
}
