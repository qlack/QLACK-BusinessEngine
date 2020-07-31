import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AddWorkingSetComponent} from './add-working-set.component';

describe('AddWorkingSetComponent', () => {
  let component: AddWorkingSetComponent;
  let fixture: ComponentFixture<AddWorkingSetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddWorkingSetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddWorkingSetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
