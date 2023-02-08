import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PermisoDetailComponent } from './permiso-detail.component';

describe('Permiso Management Detail Component', () => {
  let comp: PermisoDetailComponent;
  let fixture: ComponentFixture<PermisoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermisoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ permiso: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PermisoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PermisoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load permiso on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.permiso).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
