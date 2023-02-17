import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrioridadDetailComponent } from './prioridad-detail.component';

describe('Prioridad Management Detail Component', () => {
  let comp: PrioridadDetailComponent;
  let fixture: ComponentFixture<PrioridadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrioridadDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ prioridad: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PrioridadDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PrioridadDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load prioridad on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.prioridad).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
