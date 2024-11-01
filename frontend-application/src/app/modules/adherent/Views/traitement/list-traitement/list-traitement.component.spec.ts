import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListTraitementComponent } from './list-traitement.component';

describe('ListTraitementComponent', () => {
  let component: ListTraitementComponent;
  let fixture: ComponentFixture<ListTraitementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListTraitementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListTraitementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
