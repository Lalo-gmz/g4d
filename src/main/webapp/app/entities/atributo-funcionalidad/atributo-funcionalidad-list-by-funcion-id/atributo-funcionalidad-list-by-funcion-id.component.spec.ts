import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtributoFuncionalidadListByFuncionIdComponent } from './atributo-funcionalidad-list-by-funcion-id.component';

describe('AtributoFuncionalidadListByFuncionIdComponent', () => {
  let component: AtributoFuncionalidadListByFuncionIdComponent;
  let fixture: ComponentFixture<AtributoFuncionalidadListByFuncionIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AtributoFuncionalidadListByFuncionIdComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AtributoFuncionalidadListByFuncionIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
