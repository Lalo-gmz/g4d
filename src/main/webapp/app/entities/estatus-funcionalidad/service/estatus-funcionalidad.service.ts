import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEstatusFuncionalidad, NewEstatusFuncionalidad } from '../estatus-funcionalidad.model';

export type PartialUpdateEstatusFuncionalidad = Partial<IEstatusFuncionalidad> & Pick<IEstatusFuncionalidad, 'id'>;

export type EntityResponseType = HttpResponse<IEstatusFuncionalidad>;
export type EntityArrayResponseType = HttpResponse<IEstatusFuncionalidad[]>;

@Injectable({ providedIn: 'root' })
export class EstatusFuncionalidadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/estatus-funcionalidads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(estatusFuncionalidad: NewEstatusFuncionalidad): Observable<EntityResponseType> {
    return this.http.post<IEstatusFuncionalidad>(this.resourceUrl, estatusFuncionalidad, { observe: 'response' });
  }

  update(estatusFuncionalidad: IEstatusFuncionalidad): Observable<EntityResponseType> {
    return this.http.put<IEstatusFuncionalidad>(
      `${this.resourceUrl}/${this.getEstatusFuncionalidadIdentifier(estatusFuncionalidad)}`,
      estatusFuncionalidad,
      { observe: 'response' }
    );
  }

  partialUpdate(estatusFuncionalidad: PartialUpdateEstatusFuncionalidad): Observable<EntityResponseType> {
    return this.http.patch<IEstatusFuncionalidad>(
      `${this.resourceUrl}/${this.getEstatusFuncionalidadIdentifier(estatusFuncionalidad)}`,
      estatusFuncionalidad,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEstatusFuncionalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEstatusFuncionalidad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEstatusFuncionalidadIdentifier(estatusFuncionalidad: Pick<IEstatusFuncionalidad, 'id'>): number {
    return estatusFuncionalidad.id;
  }

  compareEstatusFuncionalidad(o1: Pick<IEstatusFuncionalidad, 'id'> | null, o2: Pick<IEstatusFuncionalidad, 'id'> | null): boolean {
    return o1 && o2 ? this.getEstatusFuncionalidadIdentifier(o1) === this.getEstatusFuncionalidadIdentifier(o2) : o1 === o2;
  }

  addEstatusFuncionalidadToCollectionIfMissing<Type extends Pick<IEstatusFuncionalidad, 'id'>>(
    estatusFuncionalidadCollection: Type[],
    ...estatusFuncionalidadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const estatusFuncionalidads: Type[] = estatusFuncionalidadsToCheck.filter(isPresent);
    if (estatusFuncionalidads.length > 0) {
      const estatusFuncionalidadCollectionIdentifiers = estatusFuncionalidadCollection.map(
        estatusFuncionalidadItem => this.getEstatusFuncionalidadIdentifier(estatusFuncionalidadItem)!
      );
      const estatusFuncionalidadsToAdd = estatusFuncionalidads.filter(estatusFuncionalidadItem => {
        const estatusFuncionalidadIdentifier = this.getEstatusFuncionalidadIdentifier(estatusFuncionalidadItem);
        if (estatusFuncionalidadCollectionIdentifiers.includes(estatusFuncionalidadIdentifier)) {
          return false;
        }
        estatusFuncionalidadCollectionIdentifiers.push(estatusFuncionalidadIdentifier);
        return true;
      });
      return [...estatusFuncionalidadsToAdd, ...estatusFuncionalidadCollection];
    }
    return estatusFuncionalidadCollection;
  }
}
