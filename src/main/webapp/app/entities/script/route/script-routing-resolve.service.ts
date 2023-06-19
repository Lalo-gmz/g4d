import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScript } from '../script.model';
import { ScriptService } from '../service/script.service';

@Injectable({ providedIn: 'root' })
export class ScriptRoutingResolveService implements Resolve<IScript | null> {
  constructor(protected service: ScriptService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IScript | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((configuracion: HttpResponse<IScript>) => {
          if (configuracion.body) {
            return of(configuracion.body);
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
