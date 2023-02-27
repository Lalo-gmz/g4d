import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProyecto, NewProyecto } from '../proyecto.model';

export type PartialUpdateProyecto = Partial<IProyecto> & Pick<IProyecto, 'id'>;

type RestOf<T extends IProyecto | NewProyecto> = Omit<T, 'creado' | 'modificado'> & {
  creado?: string | null;
  modificado?: string | null;
};

export type RestProyecto = RestOf<IProyecto>;

export type NewRestProyecto = RestOf<NewProyecto>;

export type PartialUpdateRestProyecto = RestOf<PartialUpdateProyecto>;

export type EntityResponseType = HttpResponse<IProyecto>;
export type EntityArrayResponseType = HttpResponse<IProyecto[]>;

@Injectable({ providedIn: 'root' })
export class ProyectoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proyectos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proyecto: NewProyecto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proyecto);
    return this.http
      .post<RestProyecto>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(proyecto: IProyecto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proyecto);
    return this.http
      .put<RestProyecto>(`${this.resourceUrl}/${this.getProyectoIdentifier(proyecto)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(proyecto: PartialUpdateProyecto): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proyecto);
    return this.http
      .patch<RestProyecto>(`${this.resourceUrl}/${this.getProyectoIdentifier(proyecto)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProyecto>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProyecto[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProyectoIdentifier(proyecto: Pick<IProyecto, 'id'>): number {
    return proyecto.id;
  }

  compareProyecto(o1: Pick<IProyecto, 'id'> | null, o2: Pick<IProyecto, 'id'> | null): boolean {
    return o1 && o2 ? this.getProyectoIdentifier(o1) === this.getProyectoIdentifier(o2) : o1 === o2;
  }

  addProyectoToCollectionIfMissing<Type extends Pick<IProyecto, 'id'>>(
    proyectoCollection: Type[],
    ...proyectosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const proyectos: Type[] = proyectosToCheck.filter(isPresent);
    if (proyectos.length > 0) {
      const proyectoCollectionIdentifiers = proyectoCollection.map(proyectoItem => this.getProyectoIdentifier(proyectoItem)!);
      const proyectosToAdd = proyectos.filter(proyectoItem => {
        const proyectoIdentifier = this.getProyectoIdentifier(proyectoItem);
        if (proyectoCollectionIdentifiers.includes(proyectoIdentifier)) {
          return false;
        }
        proyectoCollectionIdentifiers.push(proyectoIdentifier);
        return true;
      });
      return [...proyectosToAdd, ...proyectoCollection];
    }
    return proyectoCollection;
  }

  protected convertDateFromClient<T extends IProyecto | NewProyecto | PartialUpdateProyecto>(proyecto: T): RestOf<T> {
    return {
      ...proyecto,
      creado: proyecto.creado?.toJSON() ?? null,
      modificado: proyecto.modificado?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProyecto: RestProyecto): IProyecto {
    return {
      ...restProyecto,
      creado: restProyecto.creado ? dayjs(restProyecto.creado) : undefined,
      modificado: restProyecto.modificado ? dayjs(restProyecto.modificado) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProyecto>): HttpResponse<IProyecto> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProyecto[]>): HttpResponse<IProyecto[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
