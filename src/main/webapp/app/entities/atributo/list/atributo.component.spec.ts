import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AtributoService } from '../service/atributo.service';

import { AtributoComponent } from './atributo.component';

describe('Atributo Management Component', () => {
  let comp: AtributoComponent;
  let fixture: ComponentFixture<AtributoComponent>;
  let service: AtributoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'atributo', component: AtributoComponent }]), HttpClientTestingModule],
      declarations: [AtributoComponent],
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
      .overrideTemplate(AtributoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AtributoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AtributoService);

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
    expect(comp.atributos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to atributoService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAtributoIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAtributoIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
