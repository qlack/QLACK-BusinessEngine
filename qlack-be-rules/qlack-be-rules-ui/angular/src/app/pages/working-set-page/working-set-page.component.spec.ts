import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {WorkingSetPageComponent} from './working-set-page.component';

describe('WorkingSetPageComponent', () => {
  let component: WorkingSetPageComponent;
  let fixture: ComponentFixture<WorkingSetPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkingSetPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkingSetPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
