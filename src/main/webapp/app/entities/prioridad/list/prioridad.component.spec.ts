import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PrioridadService } from '../service/prioridad.service';

import { PrioridadComponent } from './prioridad.component';

describe('Prioridad Management Component', () => {
  let comp: PrioridadComponent;
  let fixture: ComponentFixture<PrioridadComponent>;
  let service: PrioridadService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'prioridad', component: PrioridadComponent }]), HttpClientTestingModule],
      declarations: [PrioridadComponent],
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
      .overrideTemplate(PrioridadComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PrioridadComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PrioridadService);

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
    expect(comp.prioridads?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to prioridadService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPrioridadIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPrioridadIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
