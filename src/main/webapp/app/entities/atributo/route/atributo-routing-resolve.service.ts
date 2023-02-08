import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAtributo } from '../atributo.model';
import { AtributoService } from '../service/atributo.service';

@Injectable({ providedIn: 'root' })
export class AtributoRoutingResolveService implements Resolve<IAtributo | null> {
  constructor(protected service: AtributoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAtributo | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((atributo: HttpResponse<IAtributo>) => {
          if (atributo.body) {
            return of(atributo.body);
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
