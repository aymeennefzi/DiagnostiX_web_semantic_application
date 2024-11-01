import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateDiseasesComponent } from './update-diseases.component';

describe('UpdateDiseasesComponent', () => {
  let component: UpdateDiseasesComponent;
  let fixture: ComponentFixture<UpdateDiseasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateDiseasesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateDiseasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
