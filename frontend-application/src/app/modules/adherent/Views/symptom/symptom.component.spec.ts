import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SymptomComponent } from './symptom.component';

describe('SymptomComponent', () => {
  let component: SymptomComponent;
  let fixture: ComponentFixture<SymptomComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SymptomComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SymptomComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
