import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DmnEditorPageComponent} from './dmn-editor-page.component';

describe('DmnEditorPageComponent', () => {
  let component: DmnEditorPageComponent;
  let fixture: ComponentFixture<DmnEditorPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DmnEditorPageComponent]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DmnEditorPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
