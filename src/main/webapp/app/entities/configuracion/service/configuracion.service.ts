import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConfiguracion, NewConfiguracion } from '../configuracion.model';

export type PartialUpdateConfiguracion = Partial<IConfiguracion> & Pick<IConfiguracion, 'id'>;

export type EntityResponseType = HttpResponse<IConfiguracion>;
export type EntityArrayResponseType = HttpResponse<IConfiguracion[]>;

@Injectable({ providedIn: 'root' })
export class ConfiguracionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/configuracions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(configuracion: NewConfiguracion): Observable<EntityResponseType> {
    return this.http.post<IConfiguracion>(this.resourceUrl, configuracion, { observe: 'response' });
  }

  update(configuracion: IConfiguracion): Observable<EntityResponseType> {
    return this.http.put<IConfiguracion>(`${this.resourceUrl}/${this.getConfiguracionIdentifier(configuracion)}`, configuracion, {
      observe: 'response',
    });
  }

  partialUpdate(configuracion: PartialUpdateConfiguracion): Observable<EntityResponseType> {
    return this.http.patch<IConfiguracion>(`${this.resourceUrl}/${this.getConfiguracionIdentifier(configuracion)}`, configuracion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConfiguracion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConfiguracion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getConfiguracionIdentifier(configuracion: Pick<IConfiguracion, 'id'>): number {
    return configuracion.id;
  }

  compareConfiguracion(o1: Pick<IConfiguracion, 'id'> | null, o2: Pick<IConfiguracion, 'id'> | null): boolean {
    return o1 && o2 ? this.getConfiguracionIdentifier(o1) === this.getConfiguracionIdentifier(o2) : o1 === o2;
  }

  addConfiguracionToCollectionIfMissing<Type extends Pick<IConfiguracion, 'id'>>(
    configuracionCollection: Type[],
    ...configuracionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const configuracions: Type[] = configuracionsToCheck.filter(isPresent);
    if (configuracions.length > 0) {
      const configuracionCollectionIdentifiers = configuracionCollection.map(
        configuracionItem => this.getConfiguracionIdentifier(configuracionItem)!
      );
      const configuracionsToAdd = configuracions.filter(configuracionItem => {
        const configuracionIdentifier = this.getConfiguracionIdentifier(configuracionItem);
        if (configuracionCollectionIdentifiers.includes(configuracionIdentifier)) {
          return false;
        }
        configuracionCollectionIdentifiers.push(configuracionIdentifier);
        return true;
      });
      return [...configuracionsToAdd, ...configuracionCollection];
    }
    return configuracionCollection;
  }
}
