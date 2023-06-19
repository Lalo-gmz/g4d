import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BitacoraDetailComponent } from './script-detail.component';

describe('Bitacora Management Detail Component', () => {
  let comp: BitacoraDetailComponent;
  let fixture: ComponentFixture<BitacoraDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BitacoraDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bitacora: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BitacoraDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BitacoraDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bitacora on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bitacora).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
