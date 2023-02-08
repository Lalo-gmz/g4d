import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EstatusFuncionalidadDetailComponent } from './estatus-funcionalidad-detail.component';

describe('EstatusFuncionalidad Management Detail Component', () => {
  let comp: EstatusFuncionalidadDetailComponent;
  let fixture: ComponentFixture<EstatusFuncionalidadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EstatusFuncionalidadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ estatusFuncionalidad: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EstatusFuncionalidadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EstatusFuncionalidadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load estatusFuncionalidad on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.estatusFuncionalidad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
