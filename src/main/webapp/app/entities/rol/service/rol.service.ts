import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRol, NewRol } from '../rol.model';

export type PartialUpdateRol = Partial<IRol> & Pick<IRol, 'id'>;

export type EntityResponseType = HttpResponse<IRol>;
export type EntityArrayResponseType = HttpResponse<IRol[]>;

@Injectable({ providedIn: 'root' })
export class RolService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rols');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rol: NewRol): Observable<EntityResponseType> {
    return this.http.post<IRol>(this.resourceUrl, rol, { observe: 'response' });
  }

  update(rol: IRol): Observable<EntityResponseType> {
    return this.http.put<IRol>(`${this.resourceUrl}/${this.getRolIdentifier(rol)}`, rol, { observe: 'response' });
  }

  partialUpdate(rol: PartialUpdateRol): Observable<EntityResponseType> {
    return this.http.patch<IRol>(`${this.resourceUrl}/${this.getRolIdentifier(rol)}`, rol, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRol>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRol[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRolIdentifier(rol: Pick<IRol, 'id'>): number {
    return rol.id;
  }

  compareRol(o1: Pick<IRol, 'id'> | null, o2: Pick<IRol, 'id'> | null): boolean {
    return o1 && o2 ? this.getRolIdentifier(o1) === this.getRolIdentifier(o2) : o1 === o2;
  }

  addRolToCollectionIfMissing<Type extends Pick<IRol, 'id'>>(rolCollection: Type[], ...rolsToCheck: (Type | null | undefined)[]): Type[] {
    const rols: Type[] = rolsToCheck.filter(isPresent);
    if (rols.length > 0) {
      const rolCollectionIdentifiers = rolCollection.map(rolItem => this.getRolIdentifier(rolItem)!);
      const rolsToAdd = rols.filter(rolItem => {
        const rolIdentifier = this.getRolIdentifier(rolItem);
        if (rolCollectionIdentifiers.includes(rolIdentifier)) {
          return false;
        }
        rolCollectionIdentifiers.push(rolIdentifier);
        return true;
      });
      return [...rolsToAdd, ...rolCollection];
    }
    return rolCollection;
  }
}
