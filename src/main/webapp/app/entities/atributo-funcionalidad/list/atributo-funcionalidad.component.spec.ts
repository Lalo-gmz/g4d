import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';

import { AtributoFuncionalidadComponent } from './atributo-funcionalidad.component';

describe('AtributoFuncionalidad Management Component', () => {
  let comp: AtributoFuncionalidadComponent;
  let fixture: ComponentFixture<AtributoFuncionalidadComponent>;
  let service: AtributoFuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'atributo-funcionalidad', component: AtributoFuncionalidadComponent }]),
        HttpClientTestingModule,
      ],
      declarations: [AtributoFuncionalidadComponent],
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
      .overrideTemplate(AtributoFuncionalidadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtributoFuncionalidadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AtributoFuncionalidadService);

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
    expect(comp.atributoFuncionalidads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to atributoFuncionalidadService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAtributoFuncionalidadIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAtributoFuncionalidadIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
