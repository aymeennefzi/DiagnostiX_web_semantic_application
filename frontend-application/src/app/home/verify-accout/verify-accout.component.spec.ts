import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyAccoutComponent } from './verify-accout.component';

describe('VerifyAccoutComponent', () => {
  let component: VerifyAccoutComponent;
  let fixture: ComponentFixture<VerifyAccoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerifyAccoutComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyAccoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
