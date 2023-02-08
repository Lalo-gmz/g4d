import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProyectoDetailComponent } from './proyecto-detail.component';

describe('Proyecto Management Detail Component', () => {
  let comp: ProyectoDetailComponent;
  let fixture: ComponentFixture<ProyectoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProyectoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ proyecto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProyectoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProyectoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load proyecto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.proyecto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
