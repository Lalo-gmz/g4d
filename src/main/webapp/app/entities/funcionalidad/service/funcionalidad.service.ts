import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFuncionalidad, NewFuncionalidad } from '../funcionalidad.model';

export type PartialUpdateFuncionalidad = Partial<IFuncionalidad> & Pick<IFuncionalidad, 'id'>;

type RestOf<T extends IFuncionalidad | NewFuncionalidad> = Omit<T, 'fechaEntrega' | 'creado' | 'modificado'> & {
  fechaEntrega?: string | null;
  creado?: string | null;
  modificado?: string | null;
};

export type RestFuncionalidad = RestOf<IFuncionalidad>;

export type NewRestFuncionalidad = RestOf<NewFuncionalidad>;

export type PartialUpdateRestFuncionalidad = RestOf<PartialUpdateFuncionalidad>;

export type EntityResponseType = HttpResponse<IFuncionalidad>;
export type EntityArrayResponseType = HttpResponse<IFuncionalidad[]>;

@Injectable({ providedIn: 'root' })
export class FuncionalidadService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/funcionalidads');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(funcionalidad: NewFuncionalidad): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionalidad);
    return this.http
      .post<RestFuncionalidad>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(funcionalidad: IFuncionalidad): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionalidad);
    return this.http
      .put<RestFuncionalidad>(`${this.resourceUrl}/${this.getFuncionalidadIdentifier(funcionalidad)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(funcionalidad: PartialUpdateFuncionalidad): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(funcionalidad);
    return this.http
      .patch<RestFuncionalidad>(`${this.resourceUrl}/${this.getFuncionalidadIdentifier(funcionalidad)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFuncionalidad>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFuncionalidad[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFuncionalidadIdentifier(funcionalidad: Pick<IFuncionalidad, 'id'>): number {
    return funcionalidad.id;
  }

  compareFuncionalidad(o1: Pick<IFuncionalidad, 'id'> | null, o2: Pick<IFuncionalidad, 'id'> | null): boolean {
    return o1 && o2 ? this.getFuncionalidadIdentifier(o1) === this.getFuncionalidadIdentifier(o2) : o1 === o2;
  }

  addFuncionalidadToCollectionIfMissing<Type extends Pick<IFuncionalidad, 'id'>>(
    funcionalidadCollection: Type[],
    ...funcionalidadsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const funcionalidads: Type[] = funcionalidadsToCheck.filter(isPresent);
    if (funcionalidads.length > 0) {
      const funcionalidadCollectionIdentifiers = funcionalidadCollection.map(
        funcionalidadItem => this.getFuncionalidadIdentifier(funcionalidadItem)!
      );
      const funcionalidadsToAdd = funcionalidads.filter(funcionalidadItem => {
        const funcionalidadIdentifier = this.getFuncionalidadIdentifier(funcionalidadItem);
        if (funcionalidadCollectionIdentifiers.includes(funcionalidadIdentifier)) {
          return false;
        }
        funcionalidadCollectionIdentifiers.push(funcionalidadIdentifier);
        return true;
      });
      return [...funcionalidadsToAdd, ...funcionalidadCollection];
    }
    return funcionalidadCollection;
  }

  protected convertDateFromClient<T extends IFuncionalidad | NewFuncionalidad | PartialUpdateFuncionalidad>(funcionalidad: T): RestOf<T> {
    return {
      ...funcionalidad,
      fechaEntrega: funcionalidad.fechaEntrega?.format(DATE_FORMAT) ?? null,
      creado: funcionalidad.creado?.toJSON() ?? null,
      modificado: funcionalidad.modificado?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFuncionalidad: RestFuncionalidad): IFuncionalidad {
    return {
      ...restFuncionalidad,
      fechaEntrega: restFuncionalidad.fechaEntrega ? dayjs(restFuncionalidad.fechaEntrega) : undefined,
      creado: restFuncionalidad.creado ? dayjs(restFuncionalidad.creado) : undefined,
      modificado: restFuncionalidad.modificado ? dayjs(restFuncionalidad.modificado) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFuncionalidad>): HttpResponse<IFuncionalidad> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFuncionalidad[]>): HttpResponse<IFuncionalidad[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
