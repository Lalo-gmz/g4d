import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConfiguracion } from '../configuracion.model';
import { ConfiguracionService } from '../service/configuracion.service';

@Injectable({ providedIn: 'root' })
export class ConfiguracionRoutingResolveService implements Resolve<IConfiguracion | null> {
  constructor(protected service: ConfiguracionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConfiguracion | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((configuracion: HttpResponse<IConfiguracion>) => {
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
