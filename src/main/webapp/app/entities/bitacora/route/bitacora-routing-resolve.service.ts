import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBitacora } from '../bitacora.model';
import { BitacoraService } from '../service/bitacora.service';

@Injectable({ providedIn: 'root' })
export class BitacoraRoutingResolveService implements Resolve<IBitacora | null> {
  constructor(protected service: BitacoraService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBitacora | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bitacora: HttpResponse<IBitacora>) => {
          if (bitacora.body) {
            return of(bitacora.body);
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
