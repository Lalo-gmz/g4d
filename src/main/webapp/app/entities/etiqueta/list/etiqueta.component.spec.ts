import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EtiquetaService } from '../service/etiqueta.service';

import { EtiquetaComponent } from './etiqueta.component';

describe('Etiqueta Management Component', () => {
  let comp: EtiquetaComponent;
  let fixture: ComponentFixture<EtiquetaComponent>;
  let service: EtiquetaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'etiqueta', component: EtiquetaComponent }]), HttpClientTestingModule],
      declarations: [EtiquetaComponent],
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
      .overrideTemplate(EtiquetaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtiquetaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EtiquetaService);

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
    expect(comp.etiquetas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to etiquetaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getEtiquetaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getEtiquetaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
