import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIteracion, NewIteracion } from '../iteracion.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';

export type PartialUpdateIteracion = Partial<IIteracion> & Pick<IIteracion, 'id'>;

type RestOf<T extends IIteracion | NewIteracion> = Omit<T, 'inicio' | 'fin'> & {
  inicio?: string | null;
  fin?: string | null;
};

export type RestIteracion = RestOf<IIteracion>;

export type NewRestIteracion = RestOf<NewIteracion>;

export type PartialUpdateRestIteracion = RestOf<PartialUpdateIteracion>;

export type EntityResponseType = HttpResponse<IIteracion>;
export type EntityArrayResponseType = HttpResponse<IIteracion[]>;

export type EntityResponseTypeF = HttpResponse<IFuncionalidad>;
export type EntityArrayResponseTypeF = HttpResponse<IFuncionalidad[]>;

@Injectable({ providedIn: 'root' })
export class IteracionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/iteracions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(iteracion: NewIteracion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(iteracion);
    return this.http
      .post<RestIteracion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(iteracion: IIteracion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(iteracion);
    return this.http
      .put<RestIteracion>(`${this.resourceUrl}/${this.getIteracionIdentifier(iteracion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(iteracion: PartialUpdateIteracion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(iteracion);
    return this.http
      .patch<RestIteracion>(`${this.resourceUrl}/${this.getIteracionIdentifier(iteracion)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIteracion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  import(file: any, proyectoId: number): Observable<EntityArrayResponseType> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.http.post<any>(`/api/funcionalidades/excelUpdate/proyecto/${proyectoId}`, file);
  }

  exportarExcel(proyectoId: number): any {
    return this.http.get(`/api/proyectos/${proyectoId}/excel`, { responseType: 'blob', headers: { Accept: 'application/vnd.ms-excel' } });
  }

  updateByExcel(file: any, proyectoId: number): Observable<EntityArrayResponseType> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return this.http.post<any>(`/api/funcionalidades/excelUpdate/proyecto/${proyectoId}`, file);
  }

  getPlantilla(proyectoId: number): any {
    return this.http.get(`/api/proyectos/${proyectoId}/PlantillaExcel`, {
      responseType: 'blob',
      headers: { Accept: 'application/vnd.ms-excel' },
    });
  }

  updateFuncionalidadesByIssuesAtGitLab(proyectoId: number): Observable<EntityArrayResponseType> {
    return this.http.get<IFuncionalidad[]>(`/api/funcionalidads/updateProyecto/${proyectoId}`, { observe: 'response' });
  }

  query(id?: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    if (id) {
      return this.http
        .get<RestIteracion[]>(`${this.resourceUrl}/proyecto/${id}`, { params: options, observe: 'response' })
        .pipe(map(res => this.convertResponseArrayFromServer(res)));
    }
    return this.http
      .get<RestIteracion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIteracionIdentifier(iteracion: Pick<IIteracion, 'id'>): number {
    return iteracion.id;
  }

  compareIteracion(o1: Pick<IIteracion, 'id'> | null, o2: Pick<IIteracion, 'id'> | null): boolean {
    return o1 && o2 ? this.getIteracionIdentifier(o1) === this.getIteracionIdentifier(o2) : o1 === o2;
  }

  addIteracionToCollectionIfMissing<Type extends Pick<IIteracion, 'id'>>(
    iteracionCollection: Type[],
    ...iteracionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const iteracions: Type[] = iteracionsToCheck.filter(isPresent);
    if (iteracions.length > 0) {
      const iteracionCollectionIdentifiers = iteracionCollection.map(iteracionItem => this.getIteracionIdentifier(iteracionItem)!);
      const iteracionsToAdd = iteracions.filter(iteracionItem => {
        const iteracionIdentifier = this.getIteracionIdentifier(iteracionItem);
        if (iteracionCollectionIdentifiers.includes(iteracionIdentifier)) {
          return false;
        }
        iteracionCollectionIdentifiers.push(iteracionIdentifier);
        return true;
      });
      return [...iteracionsToAdd, ...iteracionCollection];
    }
    return iteracionCollection;
  }

  protected convertDateFromClient<T extends IIteracion | NewIteracion | PartialUpdateIteracion>(iteracion: T): RestOf<T> {
    return {
      ...iteracion,
      inicio: iteracion.inicio?.format(DATE_FORMAT) ?? null,
      fin: iteracion.fin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restIteracion: RestIteracion): IIteracion {
    return {
      ...restIteracion,
      inicio: restIteracion.inicio ? dayjs(restIteracion.inicio) : undefined,
      fin: restIteracion.fin ? dayjs(restIteracion.fin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIteracion>): HttpResponse<IIteracion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIteracion[]>): HttpResponse<IIteracion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
