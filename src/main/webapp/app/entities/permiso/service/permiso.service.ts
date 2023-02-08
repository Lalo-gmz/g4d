import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPermiso, NewPermiso } from '../permiso.model';

export type PartialUpdatePermiso = Partial<IPermiso> & Pick<IPermiso, 'id'>;

export type EntityResponseType = HttpResponse<IPermiso>;
export type EntityArrayResponseType = HttpResponse<IPermiso[]>;

@Injectable({ providedIn: 'root' })
export class PermisoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/permisos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(permiso: NewPermiso): Observable<EntityResponseType> {
    return this.http.post<IPermiso>(this.resourceUrl, permiso, { observe: 'response' });
  }

  update(permiso: IPermiso): Observable<EntityResponseType> {
    return this.http.put<IPermiso>(`${this.resourceUrl}/${this.getPermisoIdentifier(permiso)}`, permiso, { observe: 'response' });
  }

  partialUpdate(permiso: PartialUpdatePermiso): Observable<EntityResponseType> {
    return this.http.patch<IPermiso>(`${this.resourceUrl}/${this.getPermisoIdentifier(permiso)}`, permiso, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPermiso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPermiso[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPermisoIdentifier(permiso: Pick<IPermiso, 'id'>): number {
    return permiso.id;
  }

  comparePermiso(o1: Pick<IPermiso, 'id'> | null, o2: Pick<IPermiso, 'id'> | null): boolean {
    return o1 && o2 ? this.getPermisoIdentifier(o1) === this.getPermisoIdentifier(o2) : o1 === o2;
  }

  addPermisoToCollectionIfMissing<Type extends Pick<IPermiso, 'id'>>(
    permisoCollection: Type[],
    ...permisosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const permisos: Type[] = permisosToCheck.filter(isPresent);
    if (permisos.length > 0) {
      const permisoCollectionIdentifiers = permisoCollection.map(permisoItem => this.getPermisoIdentifier(permisoItem)!);
      const permisosToAdd = permisos.filter(permisoItem => {
        const permisoIdentifier = this.getPermisoIdentifier(permisoItem);
        if (permisoCollectionIdentifiers.includes(permisoIdentifier)) {
          return false;
        }
        permisoCollectionIdentifiers.push(permisoIdentifier);
        return true;
      });
      return [...permisosToAdd, ...permisoCollection];
    }
    return permisoCollection;
  }
}
