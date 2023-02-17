import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParticipacionProyecto } from '../participacion-proyecto.model';
import { ParticipacionProyectoService } from '../service/participacion-proyecto.service';

@Injectable({ providedIn: 'root' })
export class ParticipacionProyectoRoutingResolveService implements Resolve<IParticipacionProyecto | null> {
  constructor(protected service: ParticipacionProyectoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParticipacionProyecto | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((participacionProyecto: HttpResponse<IParticipacionProyecto>) => {
          if (participacionProyecto.body) {
            return of(participacionProyecto.body);
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
