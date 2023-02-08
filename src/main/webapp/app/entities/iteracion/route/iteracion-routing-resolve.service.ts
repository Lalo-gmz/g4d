import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIteracion } from '../iteracion.model';
import { IteracionService } from '../service/iteracion.service';

@Injectable({ providedIn: 'root' })
export class IteracionRoutingResolveService implements Resolve<IIteracion | null> {
  constructor(protected service: IteracionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIteracion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((iteracion: HttpResponse<IIteracion>) => {
          if (iteracion.body) {
            return of(iteracion.body);
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
