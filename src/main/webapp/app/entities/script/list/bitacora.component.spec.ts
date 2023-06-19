import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BitacoraService } from '../service/script.service';

import { BitacoraComponent } from './script.component';

describe('Bitacora Management Component', () => {
  let comp: BitacoraComponent;
  let fixture: ComponentFixture<BitacoraComponent>;
  let service: BitacoraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'bitacora', component: BitacoraComponent }]), HttpClientTestingModule],
      declarations: [BitacoraComponent],
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
      .overrideTemplate(BitacoraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BitacoraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BitacoraService);

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
    expect(comp.bitacoras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to bitacoraService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBitacoraIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBitacoraIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
