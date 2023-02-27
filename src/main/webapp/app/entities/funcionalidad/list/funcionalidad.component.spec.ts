import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FuncionalidadService } from '../service/funcionalidad.service';

import { FuncionalidadComponent } from './funcionalidad.component';

describe('Funcionalidad Management Component', () => {
  let comp: FuncionalidadComponent;
  let fixture: ComponentFixture<FuncionalidadComponent>;
  let service: FuncionalidadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'funcionalidad', component: FuncionalidadComponent }]), HttpClientTestingModule],
      declarations: [FuncionalidadComponent],
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
      .overrideTemplate(FuncionalidadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FuncionalidadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FuncionalidadService);

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
    expect(comp.funcionalidads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to funcionalidadService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFuncionalidadIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFuncionalidadIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
