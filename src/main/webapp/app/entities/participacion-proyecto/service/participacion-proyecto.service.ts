import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParticipacionProyecto, NewParticipacionProyecto } from '../participacion-proyecto.model';

export type PartialUpdateParticipacionProyecto = Partial<IParticipacionProyecto> & Pick<IParticipacionProyecto, 'id'>;

export type EntityResponseType = HttpResponse<IParticipacionProyecto>;
export type EntityArrayResponseType = HttpResponse<IParticipacionProyecto[]>;

@Injectable({ providedIn: 'root' })
export class ParticipacionProyectoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/participacion-proyectos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(participacionProyecto: NewParticipacionProyecto): Observable<EntityResponseType> {
    return this.http.post<IParticipacionProyecto>(this.resourceUrl, participacionProyecto, { observe: 'response' });
  }

  createAll(participacionProyecto: NewParticipacionProyecto[]): Observable<EntityArrayResponseType> {
    return this.http.post<IParticipacionProyecto[]>(`${this.resourceUrl}/all`, participacionProyecto, { observe: 'response' });
  }

  update(participacionProyecto: IParticipacionProyecto): Observable<EntityResponseType> {
    return this.http.put<IParticipacionProyecto>(
      `${this.resourceUrl}/${this.getParticipacionProyectoIdentifier(participacionProyecto)}`,
      participacionProyecto,
      { observe: 'response' }
    );
  }

  partialUpdate(participacionProyecto: PartialUpdateParticipacionProyecto): Observable<EntityResponseType> {
    return this.http.patch<IParticipacionProyecto>(
      `${this.resourceUrl}/${this.getParticipacionProyectoIdentifier(participacionProyecto)}`,
      participacionProyecto,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParticipacionProyecto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParticipacionProyecto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  findByProyecto(id: number): Observable<EntityArrayResponseType> {
    return this.http.get<IParticipacionProyecto[]>(`${this.resourceUrl}/proyecto/${id}`, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteAll(registrosAEliminar: IParticipacionProyecto[]): Observable<HttpResponse<{}>> {
    const ids = registrosAEliminar.map(registro => registro.id);

    return this.http.delete(`${this.resourceUrl}?ids=${ids.join(',')}`, { observe: 'response' });
  }

  getParticipacionProyectoIdentifier(participacionProyecto: Pick<IParticipacionProyecto, 'id'>): number {
    return participacionProyecto.id;
  }

  compareParticipacionProyecto(o1: Pick<IParticipacionProyecto, 'id'> | null, o2: Pick<IParticipacionProyecto, 'id'> | null): boolean {
    return o1 && o2 ? this.getParticipacionProyectoIdentifier(o1) === this.getParticipacionProyectoIdentifier(o2) : o1 === o2;
  }

  addParticipacionProyectoToCollectionIfMissing<Type extends Pick<IParticipacionProyecto, 'id'>>(
    participacionProyectoCollection: Type[],
    ...participacionProyectosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const participacionProyectos: Type[] = participacionProyectosToCheck.filter(isPresent);
    if (participacionProyectos.length > 0) {
      const participacionProyectoCollectionIdentifiers = participacionProyectoCollection.map(
        participacionProyectoItem => this.getParticipacionProyectoIdentifier(participacionProyectoItem)!
      );
      const participacionProyectosToAdd = participacionProyectos.filter(participacionProyectoItem => {
        const participacionProyectoIdentifier = this.getParticipacionProyectoIdentifier(participacionProyectoItem);
        if (participacionProyectoCollectionIdentifiers.includes(participacionProyectoIdentifier)) {
          return false;
        }
        participacionProyectoCollectionIdentifiers.push(participacionProyectoIdentifier);
        return true;
      });
      return [...participacionProyectosToAdd, ...participacionProyectoCollection];
    }
    return participacionProyectoCollection;
  }
}
