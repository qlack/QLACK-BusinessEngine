import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-confirm-popup',
  templateUrl: './confirm-popup.component.html',
  styleUrls: ['./confirm-popup.component.scss']
})
export class ConfirmPopupComponent implements OnInit {

  title: string;
  text: string;
  buttonRight: string;
  buttonLeft: string;

  constructor(@Inject(MAT_DIALOG_DATA) private data,
              private dialogRef: MatDialogRef<ConfirmPopupComponent>) {
    this.title = data.title;
    this.text = data.text;
    this.buttonLeft = data.buttonLeft;
    this.buttonRight = data.buttonRight;
  }

  ngOnInit() {
  }

  close() {
    this.dialogRef.close('NO');
  }

  submit() {
    this.dialogRef.close('OK');
  }


}
