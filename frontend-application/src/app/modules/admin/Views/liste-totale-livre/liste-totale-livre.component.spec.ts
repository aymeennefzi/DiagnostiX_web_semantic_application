import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeTotaleLivreComponent } from './liste-totale-livre.component';

describe('ListeTotaleLivreComponent', () => {
  let component: ListeTotaleLivreComponent;
  let fixture: ComponentFixture<ListeTotaleLivreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListeTotaleLivreComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListeTotaleLivreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
