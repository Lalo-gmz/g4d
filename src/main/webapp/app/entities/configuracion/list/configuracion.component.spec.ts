import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ConfiguracionService } from '../service/configuracion.service';

import { ConfiguracionComponent } from './configuracion.component';

describe('Configuracion Management Component', () => {
  let comp: ConfiguracionComponent;
  let fixture: ComponentFixture<ConfiguracionComponent>;
  let service: ConfiguracionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'configuracion', component: ConfiguracionComponent }]), HttpClientTestingModule],
      declarations: [ConfiguracionComponent],
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
      .overrideTemplate(ConfiguracionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConfiguracionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ConfiguracionService);

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
    expect(comp.configuracions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to configuracionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getConfiguracionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getConfiguracionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
