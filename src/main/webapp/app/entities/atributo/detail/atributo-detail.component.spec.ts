import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtributoDetailComponent } from './atributo-detail.component';

describe('Atributo Management Detail Component', () => {
  let comp: AtributoDetailComponent;
  let fixture: ComponentFixture<AtributoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AtributoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ atributo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AtributoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AtributoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load atributo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.atributo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
