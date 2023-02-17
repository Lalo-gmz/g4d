import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IParticipacionProyecto } from '../participacion-proyecto.model';
import { ParticipacionProyectoService } from '../service/participacion-proyecto.service';

import { ParticipacionProyectoRoutingResolveService } from './participacion-proyecto-routing-resolve.service';

describe('ParticipacionProyecto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ParticipacionProyectoRoutingResolveService;
  let service: ParticipacionProyectoService;
  let resultParticipacionProyecto: IParticipacionProyecto | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ParticipacionProyectoRoutingResolveService);
    service = TestBed.inject(ParticipacionProyectoService);
    resultParticipacionProyecto = undefined;
  });

  describe('resolve', () => {
    it('should return IParticipacionProyecto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticipacionProyecto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParticipacionProyecto).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticipacionProyecto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultParticipacionProyecto).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IParticipacionProyecto>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultParticipacionProyecto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultParticipacionProyecto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
