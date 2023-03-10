import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProyectoService } from '../service/proyecto.service';

import { ProyectoComponent } from './proyecto-list-by-user.component';

describe('Proyecto Management Component', () => {
  let comp: ProyectoComponent;
  let fixture: ComponentFixture<ProyectoComponent>;
  let service: ProyectoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'proyecto', component: ProyectoComponent }]), HttpClientTestingModule],
      declarations: [ProyectoComponent],
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
      .overrideTemplate(ProyectoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProyectoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProyectoService);

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
    expect(comp.proyectos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to proyectoService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getProyectoIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getProyectoIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
