import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParticipacionProyectoService } from '../service/participacion-proyecto.service';

import { ParticipacionProyectoComponent } from './participacion-proyecto.component';

describe('ParticipacionProyecto Management Component', () => {
  let comp: ParticipacionProyectoComponent;
  let fixture: ComponentFixture<ParticipacionProyectoComponent>;
  let service: ParticipacionProyectoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'participacion-proyecto', component: ParticipacionProyectoComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [ParticipacionProyectoComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(ParticipacionProyectoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParticipacionProyectoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ParticipacionProyectoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.participacionProyectos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to participacionProyectoService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getParticipacionProyectoIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getParticipacionProyectoIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
