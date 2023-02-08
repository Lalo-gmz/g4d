import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConfiguracionDetailComponent } from './configuracion-detail.component';

describe('Configuracion Management Detail Component', () => {
  let comp: ConfiguracionDetailComponent;
  let fixture: ComponentFixture<ConfiguracionDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfiguracionDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ configuracion: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ConfiguracionDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ConfiguracionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load configuracion on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.configuracion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
