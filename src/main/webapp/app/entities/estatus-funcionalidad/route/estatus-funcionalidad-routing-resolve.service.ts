import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEstatusFuncionalidad } from '../estatus-funcionalidad.model';
import { EstatusFuncionalidadService } from '../service/estatus-funcionalidad.service';

@Injectable({ providedIn: 'root' })
export class EstatusFuncionalidadRoutingResolveService implements Resolve<IEstatusFuncionalidad | null> {
  constructor(protected service: EstatusFuncionalidadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEstatusFuncionalidad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((estatusFuncionalidad: HttpResponse<IEstatusFuncionalidad>) => {
          if (estatusFuncionalidad.body) {
            return of(estatusFuncionalidad.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
