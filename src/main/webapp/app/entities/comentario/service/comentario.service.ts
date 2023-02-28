import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComentario, NewComentario } from '../comentario.model';
import { User } from 'app/admin/user-management/user-management.model';

export type PartialUpdateComentario = Partial<IComentario> & Pick<IComentario, 'id'>;

type RestOf<T extends IComentario | NewComentario> = Omit<T, 'creado' | 'modificado'> & {
  creado?: string | null;
  modificado?: string | null;
  user?: User | null;
};

export type RestComentario = RestOf<IComentario>;

export type NewRestComentario = RestOf<NewComentario>;

export type PartialUpdateRestComentario = RestOf<PartialUpdateComentario>;

export type EntityResponseType = HttpResponse<IComentario>;
export type EntityArrayResponseType = HttpResponse<IComentario[]>;

@Injectable({ providedIn: 'root' })
export class ComentarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/comentarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(comentario: NewComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .post<RestComentario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createByFuncidAndUserId(comentario: NewComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .post<RestComentario>(`${this.resourceUrl}-funcionalidad`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(comentario: IComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .put<RestComentario>(`${this.resourceUrl}/${this.getComentarioIdentifier(comentario)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(comentario: PartialUpdateComentario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comentario);
    return this.http
      .patch<RestComentario>(`${this.resourceUrl}/${this.getComentarioIdentifier(comentario)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestComentario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestComentario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  findByFuncId(id: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestComentario[]>(`${this.resourceUrl}/funcionalidad/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getComentarioIdentifier(comentario: Pick<IComentario, 'id'>): number {
    return comentario.id;
  }

  compareComentario(o1: Pick<IComentario, 'id'> | null, o2: Pick<IComentario, 'id'> | null): boolean {
    return o1 && o2 ? this.getComentarioIdentifier(o1) === this.getComentarioIdentifier(o2) : o1 === o2;
  }

  addComentarioToCollectionIfMissing<Type extends Pick<IComentario, 'id'>>(
    comentarioCollection: Type[],
    ...comentariosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const comentarios: Type[] = comentariosToCheck.filter(isPresent);
    if (comentarios.length > 0) {
      const comentarioCollectionIdentifiers = comentarioCollection.map(comentarioItem => this.getComentarioIdentifier(comentarioItem)!);
      const comentariosToAdd = comentarios.filter(comentarioItem => {
        const comentarioIdentifier = this.getComentarioIdentifier(comentarioItem);
        if (comentarioCollectionIdentifiers.includes(comentarioIdentifier)) {
          return false;
        }
        comentarioCollectionIdentifiers.push(comentarioIdentifier);
        return true;
      });
      return [...comentariosToAdd, ...comentarioCollection];
    }
    return comentarioCollection;
  }

  protected convertDateFromClient<T extends IComentario | NewComentario | PartialUpdateComentario>(comentario: T): RestOf<T> {
    return {
      ...comentario,
      creado: comentario.creado?.toJSON() ?? null,
      modificado: comentario.modificado?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restComentario: RestComentario): IComentario {
    return {
      ...restComentario,
      creado: restComentario.creado ? dayjs(restComentario.creado) : undefined,
      modificado: restComentario.modificado ? dayjs(restComentario.modificado) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestComentario>): HttpResponse<IComentario> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestComentario[]>): HttpResponse<IComentario[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
