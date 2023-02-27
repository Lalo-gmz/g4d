import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EstatusFuncionalidadService } from '../service/estatus-funcionalidad.service';

import { EstatusFuncionalidadComponent } from './estatus-funcionalidad.component';

describe('EstatusFuncionalidad Management Component', () => {
  let comp: EstatusFuncionalidadComponent;
  let fixture: ComponentFixture<EstatusFuncionalidadComponent>;
  let service: EstatusFuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'estatus-funcionalidad', component: EstatusFuncionalidadComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [EstatusFuncionalidadComponent],
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
      .overrideTemplate(EstatusFuncionalidadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EstatusFuncionalidadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EstatusFuncionalidadService);

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
    expect(comp.estatusFuncionalidads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to estatusFuncionalidadService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getEstatusFuncionalidadIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEstatusFuncionalidadIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
