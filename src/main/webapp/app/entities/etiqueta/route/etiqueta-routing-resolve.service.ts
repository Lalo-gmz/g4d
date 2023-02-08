import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEtiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';

@Injectable({ providedIn: 'root' })
export class EtiquetaRoutingResolveService implements Resolve<IEtiqueta | null> {
  constructor(protected service: EtiquetaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEtiqueta | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((etiqueta: HttpResponse<IEtiqueta>) => {
          if (etiqueta.body) {
            return of(etiqueta.body);
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
