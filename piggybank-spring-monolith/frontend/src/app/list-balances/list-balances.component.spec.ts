import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListBalancesComponent } from './list-balances.component';

describe('ListBalancesComponent', () => {
  let component: ListBalancesComponent;
  let fixture: ComponentFixture<ListBalancesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListBalancesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListBalancesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
