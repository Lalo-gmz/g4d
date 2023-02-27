import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICaptura } from '../captura.model';
import { CapturaService } from '../service/captura.service';

@Injectable({ providedIn: 'root' })
export class CapturaRoutingResolveService implements Resolve<ICaptura | null> {
  constructor(protected service: CapturaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICaptura | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((captura: HttpResponse<ICaptura>) => {
          if (captura.body) {
            return of(captura.body);
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
