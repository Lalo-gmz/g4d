import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICaptura, NewCaptura } from '../captura.model';

export type PartialUpdateCaptura = Partial<ICaptura> & Pick<ICaptura, 'id'>;

type RestOf<T extends ICaptura | NewCaptura> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

export type RestCaptura = RestOf<ICaptura>;

export type NewRestCaptura = RestOf<NewCaptura>;

export type PartialUpdateRestCaptura = RestOf<PartialUpdateCaptura>;

export type EntityResponseType = HttpResponse<ICaptura>;
export type EntityArrayResponseType = HttpResponse<ICaptura[]>;

@Injectable({ providedIn: 'root' })
export class CapturaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/capturas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(captura: NewCaptura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(captura);
    return this.http
      .post<RestCaptura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(captura: ICaptura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(captura);
    return this.http
      .put<RestCaptura>(`${this.resourceUrl}/${this.getCapturaIdentifier(captura)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(captura: PartialUpdateCaptura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(captura);
    return this.http
      .patch<RestCaptura>(`${this.resourceUrl}/${this.getCapturaIdentifier(captura)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCaptura>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCaptura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCapturaIdentifier(captura: Pick<ICaptura, 'id'>): number {
    return captura.id;
  }

  compareCaptura(o1: Pick<ICaptura, 'id'> | null, o2: Pick<ICaptura, 'id'> | null): boolean {
    return o1 && o2 ? this.getCapturaIdentifier(o1) === this.getCapturaIdentifier(o2) : o1 === o2;
  }

  addCapturaToCollectionIfMissing<Type extends Pick<ICaptura, 'id'>>(
    capturaCollection: Type[],
    ...capturasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const capturas: Type[] = capturasToCheck.filter(isPresent);
    if (capturas.length > 0) {
      const capturaCollectionIdentifiers = capturaCollection.map(capturaItem => this.getCapturaIdentifier(capturaItem)!);
      const capturasToAdd = capturas.filter(capturaItem => {
        const capturaIdentifier = this.getCapturaIdentifier(capturaItem);
        if (capturaCollectionIdentifiers.includes(capturaIdentifier)) {
          return false;
        }
        capturaCollectionIdentifiers.push(capturaIdentifier);
        return true;
      });
      return [...capturasToAdd, ...capturaCollection];
    }
    return capturaCollection;
  }

  protected convertDateFromClient<T extends ICaptura | NewCaptura | PartialUpdateCaptura>(captura: T): RestOf<T> {
    return {
      ...captura,
      fecha: captura.fecha?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCaptura: RestCaptura): ICaptura {
    return {
      ...restCaptura,
      fecha: restCaptura.fecha ? dayjs(restCaptura.fecha) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCaptura>): HttpResponse<ICaptura> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCaptura[]>): HttpResponse<ICaptura[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
