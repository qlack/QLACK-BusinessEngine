import {Component, OnInit} from '@angular/core';
import {FormBuilder, Validators} from '@angular/forms';
import {Location} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {RuleService} from '../../services/rule.service';
import {RuleVersionDto} from '../../dto/rule-version-dto';
import {RuleDto} from '../../dto/rule-dto';
import {RuleVersionService} from '../../services/rule-version.service';
import {WorkingSetService} from '../../services/working-set.service';
import {WorkingSetVersionService} from '../../services/working-set-version.service';
import {WorkingSetDto} from '../../dto/working-set-dto';
import {WorkingSetVersionDto} from '../../dto/working-set-version-dto';

@Component({
  selector: 'app-add-version',
  templateUrl: './add-version.component.html',
  styleUrls: ['./add-version.component.css']
})
export class AddVersionComponent implements OnInit {

  addVersionForm = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    basedOn: ['']
  });

  id: string;
  isRule: boolean;

  rule: RuleDto;
  ruleVersionDto: RuleVersionDto;
  ruleVersions: RuleVersionDto[];

  workingSet: WorkingSetDto;
  workingSetVersionDto: WorkingSetVersionDto;
  workingSetVersions: WorkingSetVersionDto[];


  constructor(
    private location: Location,
    private ruleService: RuleService,
    private ruleVersionService: RuleVersionService,
    private workingSetService: WorkingSetService,
    private workingSetVersionService: WorkingSetVersionService,
    private router: Router,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = params.id;
      this.isRule = (params.isRule === '1');

      if (this.isRule) {
        this.ruleService.get(this.id).subscribe(response => {
          this.rule = response;
          this.ruleVersions = response.ruleVersions;
          this.ruleVersionDto = response.ruleVersions[response.ruleVersions.length - 1];
        });
      } else {
        this.workingSetService.get(this.id).subscribe(response => {
          this.workingSet = response;
          this.workingSetVersions = response.workingSetVersions;
          this.workingSetVersionDto = response.workingSetVersions[response.workingSetVersions.length - 1];
        });
      }
    });
  }

  changeVersion(versionDto: any) {
    if (this.isRule) {
      this.ruleVersionDto = versionDto;
    } else {
      this.workingSetVersionDto = versionDto;
    }
  }

  cancel() {
    this.location.back();
  }

  createVersion() {
    if (this.addVersionForm.valid) {
      if (this.isRule) {
        const versionDto: RuleVersionDto = this.addVersionForm.value;
        versionDto.rule = this.id;
        if (this.ruleVersionDto) {
          versionDto.dmnXml = this.ruleVersionDto.dmnXml;
        }

        this.ruleVersionService.save(versionDto).subscribe(response =>
          this.router.navigate(['project/rule', this.id])
        );
      } else {
        const versionDto: WorkingSetVersionDto = this.addVersionForm.value;
        versionDto.workingSet = this.id;
        if (this.workingSetVersionDto) {
          versionDto.ruleVersions = this.workingSetVersionDto.ruleVersions;
        }

        this.workingSetVersionService.save(versionDto).subscribe(response =>
          this.router.navigate(['project/working-set', this.id])
        );
      }
    }
  }

}
