import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtributoFuncionalidadDetailComponent } from './atributo-funcionalidad-detail.component';

describe('AtributoFuncionalidad Management Detail Component', () => {
  let comp: AtributoFuncionalidadDetailComponent;
  let fixture: ComponentFixture<AtributoFuncionalidadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AtributoFuncionalidadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ atributoFuncionalidad: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AtributoFuncionalidadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AtributoFuncionalidadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load atributoFuncionalidad on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.atributoFuncionalidad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
