import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParticipacionProyectoDetailComponent } from './participacion-proyecto-detail.component';

describe('ParticipacionProyecto Management Detail Component', () => {
  let comp: ParticipacionProyectoDetailComponent;
  let fixture: ComponentFixture<ParticipacionProyectoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ParticipacionProyectoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ participacionProyecto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ParticipacionProyectoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ParticipacionProyectoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load participacionProyecto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.participacionProyecto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
