import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAtributo, NewAtributo } from '../atributo.model';

export type PartialUpdateAtributo = Partial<IAtributo> & Pick<IAtributo, 'id'>;

export type EntityResponseType = HttpResponse<IAtributo>;
export type EntityArrayResponseType = HttpResponse<IAtributo[]>;

@Injectable({ providedIn: 'root' })
export class AtributoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/atributos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(atributo: NewAtributo): Observable<EntityResponseType> {
    return this.http.post<IAtributo>(this.resourceUrl, atributo, { observe: 'response' });
  }

  update(atributo: IAtributo): Observable<EntityResponseType> {
    return this.http.put<IAtributo>(`${this.resourceUrl}/${this.getAtributoIdentifier(atributo)}`, atributo, { observe: 'response' });
  }

  partialUpdate(atributo: PartialUpdateAtributo): Observable<EntityResponseType> {
    return this.http.patch<IAtributo>(`${this.resourceUrl}/${this.getAtributoIdentifier(atributo)}`, atributo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAtributo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAtributo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAtributoIdentifier(atributo: Pick<IAtributo, 'id'>): number {
    return atributo.id;
  }

  compareAtributo(o1: Pick<IAtributo, 'id'> | null, o2: Pick<IAtributo, 'id'> | null): boolean {
    return o1 && o2 ? this.getAtributoIdentifier(o1) === this.getAtributoIdentifier(o2) : o1 === o2;
  }

  addAtributoToCollectionIfMissing<Type extends Pick<IAtributo, 'id'>>(
    atributoCollection: Type[],
    ...atributosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const atributos: Type[] = atributosToCheck.filter(isPresent);
    if (atributos.length > 0) {
      const atributoCollectionIdentifiers = atributoCollection.map(atributoItem => this.getAtributoIdentifier(atributoItem)!);
      const atributosToAdd = atributos.filter(atributoItem => {
        const atributoIdentifier = this.getAtributoIdentifier(atributoItem);
        if (atributoCollectionIdentifiers.includes(atributoIdentifier)) {
          return false;
        }
        atributoCollectionIdentifiers.push(atributoIdentifier);
        return true;
      });
      return [...atributosToAdd, ...atributoCollection];
    }
    return atributoCollection;
  }
}
