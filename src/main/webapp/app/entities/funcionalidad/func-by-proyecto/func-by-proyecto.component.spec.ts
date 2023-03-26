import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FuncByProyectoComponent } from './func-by-proyecto.component';

describe('FuncByProyectoComponent', () => {
  let component: FuncByProyectoComponent;
  let fixture: ComponentFixture<FuncByProyectoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FuncByProyectoComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FuncByProyectoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
