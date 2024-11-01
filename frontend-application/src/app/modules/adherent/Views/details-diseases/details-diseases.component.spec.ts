import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailsDiseasesComponent } from './details-diseases.component';

describe('DetailsDiseasesComponent', () => {
  let component: DetailsDiseasesComponent;
  let fixture: ComponentFixture<DetailsDiseasesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailsDiseasesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailsDiseasesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
