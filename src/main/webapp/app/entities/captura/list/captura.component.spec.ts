import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CapturaService } from '../service/captura.service';

import { CapturaComponent } from './captura.component';

describe('Captura Management Component', () => {
  let comp: CapturaComponent;
  let fixture: ComponentFixture<CapturaComponent>;
  let service: CapturaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'captura', component: CapturaComponent }]), HttpClientTestingModule],
      declarations: [CapturaComponent],
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
      .overrideTemplate(CapturaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CapturaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CapturaService);

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
    expect(comp.capturas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to capturaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getCapturaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getCapturaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
