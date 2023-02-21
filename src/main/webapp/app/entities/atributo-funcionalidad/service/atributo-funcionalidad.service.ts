import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAtributoFuncionalidad, NewAtributoFuncionalidad } from '../atributo-funcionalidad.model';

export type PartialUpdateAtributoFuncionalidad = Partial<IAtributoFuncionalidad> & Pick<IAtributoFuncionalidad, 'id'>;

export type EntityResponseType = HttpResponse<IAtributoFuncionalidad>;
export type EntityArrayResponseType = HttpResponse<IAtributoFuncionalidad[]>;

@Injectable({ providedIn: 'root' })
export class AtributoFuncionalidadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/atributo-funcionalidads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(atributoFuncionalidad: NewAtributoFuncionalidad): Observable<EntityResponseType> {
    return this.http.post<IAtributoFuncionalidad>(this.resourceUrl, atributoFuncionalidad, { observe: 'response' });
  }

  update(atributoFuncionalidad: IAtributoFuncionalidad): Observable<EntityResponseType> {
    return this.http.put<IAtributoFuncionalidad>(
      `${this.resourceUrl}/${this.getAtributoFuncionalidadIdentifier(atributoFuncionalidad)}`,
      atributoFuncionalidad,
      { observe: 'response' }
    );
  }

  partialUpdate(atributoFuncionalidad: PartialUpdateAtributoFuncionalidad): Observable<EntityResponseType> {
    return this.http.patch<IAtributoFuncionalidad>(
      `${this.resourceUrl}/${this.getAtributoFuncionalidadIdentifier(atributoFuncionalidad)}`,
      atributoFuncionalidad,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAtributoFuncionalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAtributoFuncionalidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAtributoFuncionalidadIdentifier(atributoFuncionalidad: Pick<IAtributoFuncionalidad, 'id'>): number {
    return atributoFuncionalidad.id;
  }

  findByFuncionalidadId(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IAtributoFuncionalidad[]>(`${this.resourceUrl}/funcionalidad/${id}`, { observe: 'response' });
  }

  compareAtributoFuncionalidad(o1: Pick<IAtributoFuncionalidad, 'id'> | null, o2: Pick<IAtributoFuncionalidad, 'id'> | null): boolean {
    return o1 && o2 ? this.getAtributoFuncionalidadIdentifier(o1) === this.getAtributoFuncionalidadIdentifier(o2) : o1 === o2;
  }

  addAtributoFuncionalidadToCollectionIfMissing<Type extends Pick<IAtributoFuncionalidad, 'id'>>(
    atributoFuncionalidadCollection: Type[],
    ...atributoFuncionalidadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const atributoFuncionalidads: Type[] = atributoFuncionalidadsToCheck.filter(isPresent);
    if (atributoFuncionalidads.length > 0) {
      const atributoFuncionalidadCollectionIdentifiers = atributoFuncionalidadCollection.map(
        atributoFuncionalidadItem => this.getAtributoFuncionalidadIdentifier(atributoFuncionalidadItem)!
      );
      const atributoFuncionalidadsToAdd = atributoFuncionalidads.filter(atributoFuncionalidadItem => {
        const atributoFuncionalidadIdentifier = this.getAtributoFuncionalidadIdentifier(atributoFuncionalidadItem);
        if (atributoFuncionalidadCollectionIdentifiers.includes(atributoFuncionalidadIdentifier)) {
          return false;
        }
        atributoFuncionalidadCollectionIdentifiers.push(atributoFuncionalidadIdentifier);
        return true;
      });
      return [...atributoFuncionalidadsToAdd, ...atributoFuncionalidadCollection];
    }
    return atributoFuncionalidadCollection;
  }
}
