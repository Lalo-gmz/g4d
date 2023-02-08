import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPermiso } from '../permiso.model';
import { PermisoService } from '../service/permiso.service';

@Injectable({ providedIn: 'root' })
export class PermisoRoutingResolveService implements Resolve<IPermiso | null> {
  constructor(protected service: PermisoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPermiso | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((permiso: HttpResponse<IPermiso>) => {
          if (permiso.body) {
            return of(permiso.body);
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
