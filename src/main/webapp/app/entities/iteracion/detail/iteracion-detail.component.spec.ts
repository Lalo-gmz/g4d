import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IteracionDetailComponent } from './iteracion-detail.component';

describe('Iteracion Management Detail Component', () => {
  let comp: IteracionDetailComponent;
  let fixture: ComponentFixture<IteracionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IteracionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ iteracion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IteracionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IteracionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load iteracion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.iteracion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
