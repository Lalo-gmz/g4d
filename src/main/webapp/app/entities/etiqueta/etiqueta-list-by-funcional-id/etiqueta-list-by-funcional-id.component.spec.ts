import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtiquetaListByFuncionalIdComponent } from './etiqueta-list-by-funcional-id.component';

describe('EtiquetaListByFuncionalIdComponent', () => {
  let component: EtiquetaListByFuncionalIdComponent;
  let fixture: ComponentFixture<EtiquetaListByFuncionalIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EtiquetaListByFuncionalIdComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(EtiquetaListByFuncionalIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
