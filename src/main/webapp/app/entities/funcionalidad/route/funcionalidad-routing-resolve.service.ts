import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadService } from '../service/funcionalidad.service';

@Injectable({ providedIn: 'root' })
export class FuncionalidadRoutingResolveService implements Resolve<IFuncionalidad | null> {
  constructor(protected service: FuncionalidadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFuncionalidad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((funcionalidad: HttpResponse<IFuncionalidad>) => {
          if (funcionalidad.body) {
            return of(funcionalidad.body);
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
