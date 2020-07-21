import { Component, OnInit } from '@angular/core';
import {ProjectComponent} from '../../project/project.component';
import {ConfirmPopupComponent} from '../../confirm-popup/confirm-popup.component';
import {MatDialog} from '@angular/material/dialog';
import dmn from '../../dmn';

@Component({
  selector: 'app-rule-page',
  templateUrl: './rule-page.component.html',
  styleUrls: ['./rule-page.component.scss']
})

export class RulePageComponent implements OnInit {
  dmn = dmn;
  dmnXml = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns:camunda="http://camunda.org/schema/1.0/dmn" xmlns="https://www.omg.org/spec/DMN/20191111/MODEL/"
             id="definitions_1hemumb" name="definitions" namespace="http://camunda.org/schema/1.0/dmn">
    <decision id="dish" name="Dish">
        <decisionTable id="decisionTable">
            <input id="input1" label="Season" camunda:inputVariable="">
                <inputExpression id="inputExpression1" typeRef="string">
                    <text>season</text>
                </inputExpression>
            </input>
            <input id="InputClause_0rwlbk7" label="How many guests" camunda:inputVariable="">
                <inputExpression id="LiteralExpression_0h5951a" typeRef="integer">
                    <text>guestCount</text>
                </inputExpression>
            </input>
            <output id="output1" label="Dish" name="desiredDish" typeRef="string"/>
            <rule id="row-129502239-1">
                <description></description>
                <inputEntry id="UnaryTests_0e47zyl">
                    <text>"Fall"</text>
                </inputEntry>
                <inputEntry id="UnaryTests_1132erh">
                    <text>&lt;= 8</text>
                </inputEntry>
                <outputEntry id="LiteralExpression_0wv4x30">
                    <text>"Spareribs"</text>
                </outputEntry>
            </rule>
            <rule id="row-129502239-2">
                <inputEntry id="UnaryTests_0iwabd6">
                    <text>"Winter"</text>
                </inputEntry>
                <inputEntry id="UnaryTests_10qbns7">
                    <text>&lt;= 8</text>
                </inputEntry>
                <outputEntry id="LiteralExpression_0ysj59c">
                    <text>"Roastbeef"</text>
                </outputEntry>
            </rule>
            <rule id="row-129502239-3">
                <inputEntry id="UnaryTests_1nu2fxo">
                    <text>"Spring"</text>
                </inputEntry>
                <inputEntry id="UnaryTests_1b4mcpz">
                    <text>&lt;= 4</text>
                </inputEntry>
                <outputEntry id="LiteralExpression_1osqv9r">
                    <text>"Dry Aged Gourmet Steak"</text>
                </outputEntry>
            </rule>
            <rule id="row-129502239-4">
                <description>Save money</description>
                <inputEntry id="UnaryTests_0vux2zw">
                    <text>"Spring"</text>
                </inputEntry>
                <inputEntry id="UnaryTests_07v2n3t">
                    <text>[5..8]</text>
                </inputEntry>
                <outputEntry id="LiteralExpression_11v4xrs">
                    <text>"Steak"</text>
                </outputEntry>
            </rule>
            <rule id="row-129502239-5">
                <description>Less effort</description>
                <inputEntry id="UnaryTests_0p7gr7e">
                    <text>"Fall","Winter","Spring"</text>
                </inputEntry>
                <inputEntry id="UnaryTests_0zebpmx">
                    <text>&gt; 8</text>
                </inputEntry>
                <outputEntry id="LiteralExpression_0maai06">
                    <text>"Stew"</text>
                </outputEntry>
            </rule>
            <rule id="row-129502239-6">
                <description>Hey, why not?</description>
                <inputEntry id="UnaryTests_1e1us67">
                    <text>"Summer"</text>
                </inputEntry>
                <inputEntry id="UnaryTests_0q3acue">
                    <text></text>
                </inputEntry>
                <outputEntry id="LiteralExpression_0ewtv5x">
                    <text>"Light Salad and nice Steak"</text>
                </outputEntry>
            </rule>
        </decisionTable>
    </decision>
</definitions>
`;
  flag = false;
  categories = ['Category 1', 'Category 2', 'Category 3'];
  versions = ['Version 1', 'Version 2', 'Version 3'];

  constructor(
    private project: ProjectComponent,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
  }

  editRule() {
    this.flag = !this.flag;
  }

  deleteRule() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        title: 'Delete rule',
        text: 'Are you sure, you want to delete this rule?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.project.openRulePage();
      }
    });
  }

  deleteVersion() {
    const dialogRef = this.dialog.open(ConfirmPopupComponent, {
      width: '350px',
      data: {
        title: 'Delete version',
        text: 'Are you sure, you want to delete this version?',
        buttonRight: 'YES',
        buttonLeft: 'NO'
      }
    });

    dialogRef.afterClosed().subscribe(data => {
      if (data === 'OK') {
        this.project.openRulePage();
      }
    });
  }

  createVersion() {
    this.project.addVersion('rule');
  }

}
