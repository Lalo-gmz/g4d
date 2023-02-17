import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';

@Injectable({ providedIn: 'root' })
export class AtributoFuncionalidadRoutingResolveService implements Resolve<IAtributoFuncionalidad | null> {
  constructor(protected service: AtributoFuncionalidadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAtributoFuncionalidad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((atributoFuncionalidad: HttpResponse<IAtributoFuncionalidad>) => {
          if (atributoFuncionalidad.body) {
            return of(atributoFuncionalidad.body);
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
