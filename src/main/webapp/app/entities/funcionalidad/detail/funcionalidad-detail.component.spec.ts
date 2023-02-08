import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FuncionalidadDetailComponent } from './funcionalidad-detail.component';

describe('Funcionalidad Management Detail Component', () => {
  let comp: FuncionalidadDetailComponent;
  let fixture: ComponentFixture<FuncionalidadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FuncionalidadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ funcionalidad: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FuncionalidadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FuncionalidadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load funcionalidad on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.funcionalidad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
