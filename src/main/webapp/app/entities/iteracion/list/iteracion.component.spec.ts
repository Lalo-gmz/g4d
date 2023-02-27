import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IteracionService } from '../service/iteracion.service';

import { IteracionComponent } from './iteracion.component';

describe('Iteracion Management Component', () => {
  let comp: IteracionComponent;
  let fixture: ComponentFixture<IteracionComponent>;
  let service: IteracionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'iteracion', component: IteracionComponent }]), HttpClientTestingModule],
      declarations: [IteracionComponent],
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
      .overrideTemplate(IteracionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IteracionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IteracionService);

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
    expect(comp.iteracions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to iteracionService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getIteracionIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getIteracionIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
