import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrioridad } from '../prioridad.model';
import { PrioridadService } from '../service/prioridad.service';

@Injectable({ providedIn: 'root' })
export class PrioridadRoutingResolveService implements Resolve<IPrioridad | null> {
  constructor(protected service: PrioridadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrioridad | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prioridad: HttpResponse<IPrioridad>) => {
          if (prioridad.body) {
            return of(prioridad.body);
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
