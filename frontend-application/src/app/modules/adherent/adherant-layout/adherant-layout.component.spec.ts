import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdherantLayoutComponent } from './adherant-layout.component';

describe('AdherantLayoutComponent', () => {
  let component: AdherantLayoutComponent;
  let fixture: ComponentFixture<AdherantLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdherantLayoutComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdherantLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
