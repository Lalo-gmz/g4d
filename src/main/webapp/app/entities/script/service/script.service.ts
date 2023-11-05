import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IScript, NewScript } from '../script.model';
import { ICaptura } from '../captura.model';

export type PartialUpdateScript = Partial<IScript> & Pick<IScript, 'id'>;

type RestOf<T extends IScript | NewScript> = Omit<T, 'creado'> & {
  creado?: string | null;
};

export type RestScript = RestOf<IScript>;

export type NewRestScript = RestOf<IScript>;

export type PartialUpdateRestScript = RestOf<PartialUpdateScript>;

export type EntityResponseType = HttpResponse<IScript>;
export type EntityArrayResponseType = HttpResponse<IScript[]>;
export type EntityArrayResponseTypeCaptura = HttpResponse<ICaptura[]>;

@Injectable({ providedIn: 'root' })
export class ScriptService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scripts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(script: NewScript): Observable<EntityResponseType> {
    return this.http.post<IScript>(this.resourceUrl, script, { observe: 'response' });
  }

  update(script: IScript): Observable<EntityResponseType> {
    return this.http.put<IScript>(`${this.resourceUrl}/${this.getScriptIdentifier(script)}`, script, {
      observe: 'response',
    });
  }

  partialUpdate(script: PartialUpdateScript): Observable<EntityResponseType> {
    return this.http.patch<IScript>(`${this.resourceUrl}/${this.getScriptIdentifier(script)}`, script, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IScript>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IScript[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  findAllByProyectoId(req?: any, id?: number): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IScript[]>(`${this.resourceUrl}/proyecto/${id ?? 0}`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getScriptIdentifier(script: Pick<IScript, 'id'>): number {
    return script.id;
  }

  compareConfiguracion(o1: Pick<IScript, 'id'> | null, o2: Pick<IScript, 'id'> | null): boolean {
    return o1 && o2 ? this.getScriptIdentifier(o1) === this.getScriptIdentifier(o2) : o1 === o2;
  }

  addConfiguracionToCollectionIfMissing<Type extends Pick<IScript, 'id'>>(
    configuracionCollection: Type[],
    ...configuracionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scripts: Type[] = configuracionsToCheck.filter(isPresent);
    if (scripts.length > 0) {
      const configuracionCollectionIdentifiers = configuracionCollection.map(
        configuracionItem => this.getScriptIdentifier(configuracionItem)!
      );
      const configuracionsToAdd = scripts.filter(configuracionItem => {
        const configuracionIdentifier = this.getScriptIdentifier(configuracionItem);
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

  //captura service

  findByProyect(id: number): Observable<EntityArrayResponseTypeCaptura> {
    const options = createRequestOption();
    return this.http.get<ICaptura[]>(`api/capturas/funcionalidades/proyecto/${id}`, { params: options, observe: 'response' });
  }
}
