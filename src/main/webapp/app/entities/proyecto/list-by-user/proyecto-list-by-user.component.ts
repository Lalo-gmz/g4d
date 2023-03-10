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
