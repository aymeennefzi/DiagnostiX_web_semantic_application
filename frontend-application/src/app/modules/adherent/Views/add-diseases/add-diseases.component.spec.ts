import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddDiseasesComponent } from './add-diseases.component';

describe('AddDiseasesComponent', () => {
  let component: AddDiseasesComponent;
  let fixture: ComponentFixture<AddDiseasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddDiseasesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddDiseasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
