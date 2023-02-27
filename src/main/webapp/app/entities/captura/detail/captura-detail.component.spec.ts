import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CapturaDetailComponent } from './captura-detail.component';

describe('Captura Management Detail Component', () => {
  let comp: CapturaDetailComponent;
  let fixture: ComponentFixture<CapturaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CapturaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ captura: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CapturaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CapturaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load captura on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.captura).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
