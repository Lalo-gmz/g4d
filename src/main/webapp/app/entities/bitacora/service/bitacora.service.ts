import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBitacora, NewBitacora } from '../bitacora.model';

export type PartialUpdateBitacora = Partial<IBitacora> & Pick<IBitacora, 'id'>;

type RestOf<T extends IBitacora | NewBitacora> = Omit<T, 'creado'> & {
  creado?: string | null;
};

export type RestBitacora = RestOf<IBitacora>;

export type NewRestBitacora = RestOf<NewBitacora>;

export type PartialUpdateRestBitacora = RestOf<PartialUpdateBitacora>;

export type EntityResponseType = HttpResponse<IBitacora>;
export type EntityArrayResponseType = HttpResponse<IBitacora[]>;

@Injectable({ providedIn: 'root' })
export class BitacoraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bitacoras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bitacora: NewBitacora): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bitacora);
    return this.http
      .post<RestBitacora>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bitacora: IBitacora): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bitacora);
    return this.http
      .put<RestBitacora>(`${this.resourceUrl}/${this.getBitacoraIdentifier(bitacora)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bitacora: PartialUpdateBitacora): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bitacora);
    return this.http
      .patch<RestBitacora>(`${this.resourceUrl}/${this.getBitacoraIdentifier(bitacora)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBitacora>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBitacora[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBitacoraIdentifier(bitacora: Pick<IBitacora, 'id'>): number {
    return bitacora.id;
  }

  compareBitacora(o1: Pick<IBitacora, 'id'> | null, o2: Pick<IBitacora, 'id'> | null): boolean {
    return o1 && o2 ? this.getBitacoraIdentifier(o1) === this.getBitacoraIdentifier(o2) : o1 === o2;
  }

  addBitacoraToCollectionIfMissing<Type extends Pick<IBitacora, 'id'>>(
    bitacoraCollection: Type[],
    ...bitacorasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bitacoras: Type[] = bitacorasToCheck.filter(isPresent);
    if (bitacoras.length > 0) {
      const bitacoraCollectionIdentifiers = bitacoraCollection.map(bitacoraItem => this.getBitacoraIdentifier(bitacoraItem)!);
      const bitacorasToAdd = bitacoras.filter(bitacoraItem => {
        const bitacoraIdentifier = this.getBitacoraIdentifier(bitacoraItem);
        if (bitacoraCollectionIdentifiers.includes(bitacoraIdentifier)) {
          return false;
        }
        bitacoraCollectionIdentifiers.push(bitacoraIdentifier);
        return true;
      });
      return [...bitacorasToAdd, ...bitacoraCollection];
    }
    return bitacoraCollection;
  }

  protected convertDateFromClient<T extends IBitacora | NewBitacora | PartialUpdateBitacora>(bitacora: T): RestOf<T> {
    return {
      ...bitacora,
      creado: bitacora.creado?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBitacora: RestBitacora): IBitacora {
    return {
      ...restBitacora,
      creado: restBitacora.creado ? dayjs(restBitacora.creado) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBitacora>): HttpResponse<IBitacora> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBitacora[]>): HttpResponse<IBitacora[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
