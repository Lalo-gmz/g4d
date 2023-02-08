import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProyecto } from '../proyecto.model';
import { ProyectoService } from '../service/proyecto.service';

@Injectable({ providedIn: 'root' })
export class ProyectoRoutingResolveService implements Resolve<IProyecto | null> {
  constructor(protected service: ProyectoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProyecto | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((proyecto: HttpResponse<IProyecto>) => {
          if (proyecto.body) {
            return of(proyecto.body);
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
