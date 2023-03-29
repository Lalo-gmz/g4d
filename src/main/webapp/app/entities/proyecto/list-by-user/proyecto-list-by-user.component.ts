import { Component, OnInit } from '@angular/core';
import { IParticipacionProyecto } from 'app/entities/participacion-proyecto/participacion-proyecto.model';
import { IProyecto } from '../proyecto.model';
import { EntityArrayResponseType, ProyectoService } from '../service/proyecto.service';

@Component({
  selector: 'jhi-proyecto-by-user',
  templateUrl: './proyecto.component.html',
})
export class ProyectoListByUserComponent implements OnInit {
  participaciones?: IParticipacionProyecto[];
  proyectos?: IProyecto[] = [];
  GITURL?: string = 'https://gitlab.com/g4dadmin/';

  constructor(protected proyectoService: ProyectoService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.proyectoService.findAllByUser().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
        if (this.participaciones)
          this.participaciones.forEach(e => {
            if (e.proyecto) this.addProyecto(e.proyecto);
          });
        console.log(this.proyectos);
      },
    });
  }

  convertToSlugURL(text: string) {
    return this.GITURL + text.toLowerCase().replace(/\s+/g, '-');
  }

  // asignar la función a una propiedad del componente
  slugify = this.convertToSlugURL.bind(this);

  addProyecto(p: IProyecto) {
    this.proyectos?.push(p);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.participaciones = this.fillComponentAttributesFromResponseBody(response.body);
  }

  protected fillComponentAttributesFromResponseBody(data: IParticipacionProyecto[] | null): IParticipacionProyecto[] {
    return data ?? [];
  }
}
