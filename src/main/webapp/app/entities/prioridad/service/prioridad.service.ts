import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrioridad, NewPrioridad } from '../prioridad.model';

export type PartialUpdatePrioridad = Partial<IPrioridad> & Pick<IPrioridad, 'id'>;

export type EntityResponseType = HttpResponse<IPrioridad>;
export type EntityArrayResponseType = HttpResponse<IPrioridad[]>;

@Injectable({ providedIn: 'root' })
export class PrioridadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prioridads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prioridad: NewPrioridad): Observable<EntityResponseType> {
    return this.http.post<IPrioridad>(this.resourceUrl, prioridad, { observe: 'response' });
  }

  update(prioridad: IPrioridad): Observable<EntityResponseType> {
    return this.http.put<IPrioridad>(`${this.resourceUrl}/${this.getPrioridadIdentifier(prioridad)}`, prioridad, { observe: 'response' });
  }

  partialUpdate(prioridad: PartialUpdatePrioridad): Observable<EntityResponseType> {
    return this.http.patch<IPrioridad>(`${this.resourceUrl}/${this.getPrioridadIdentifier(prioridad)}`, prioridad, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrioridad>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrioridad[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPrioridadIdentifier(prioridad: Pick<IPrioridad, 'id'>): number {
    return prioridad.id;
  }

  comparePrioridad(o1: Pick<IPrioridad, 'id'> | null, o2: Pick<IPrioridad, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrioridadIdentifier(o1) === this.getPrioridadIdentifier(o2) : o1 === o2;
  }

  addPrioridadToCollectionIfMissing<Type extends Pick<IPrioridad, 'id'>>(
    prioridadCollection: Type[],
    ...prioridadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prioridads: Type[] = prioridadsToCheck.filter(isPresent);
    if (prioridads.length > 0) {
      const prioridadCollectionIdentifiers = prioridadCollection.map(prioridadItem => this.getPrioridadIdentifier(prioridadItem)!);
      const prioridadsToAdd = prioridads.filter(prioridadItem => {
        const prioridadIdentifier = this.getPrioridadIdentifier(prioridadItem);
        if (prioridadCollectionIdentifiers.includes(prioridadIdentifier)) {
          return false;
        }
        prioridadCollectionIdentifiers.push(prioridadIdentifier);
        return true;
      });
      return [...prioridadsToAdd, ...prioridadCollection];
    }
    return prioridadCollection;
  }
}
