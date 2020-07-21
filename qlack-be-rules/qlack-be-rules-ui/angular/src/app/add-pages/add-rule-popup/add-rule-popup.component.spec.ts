import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRulePopupComponent } from './add-rule-popup.component';

describe('AddRulePopupComponent', () => {
  let component: AddRulePopupComponent;
  let fixture: ComponentFixture<AddRulePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddRulePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddRulePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
