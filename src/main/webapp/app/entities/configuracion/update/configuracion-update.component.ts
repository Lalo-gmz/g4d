import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ConfiguracionFormService, ConfiguracionFormGroup } from './configuracion-form.service';
import { IConfiguracion } from '../configuracion.model';
import { ConfiguracionService } from '../service/configuracion.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';
import { EtiquetaVisual } from 'app/entities/enumerations/etiqueta-visual.model';

@Component({
  selector: 'jhi-configuracion-update',
  templateUrl: './configuracion-update.component.html',
})
export class ConfiguracionUpdateComponent implements OnInit {
  isSaving = false;
  configuracion: IConfiguracion | null = null;
  etiquetaVisualValues = Object.keys(EtiquetaVisual);

  proyectosSharedCollection: IProyecto[] = [];

  editForm: ConfiguracionFormGroup = this.configuracionFormService.createConfiguracionFormGroup();

  constructor(
    protected configuracionService: ConfiguracionService,
    protected configuracionFormService: ConfiguracionFormService,
    protected proyectoService: ProyectoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ configuracion }) => {
      this.configuracion = configuracion;
      if (configuracion) {
        this.updateForm(configuracion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const configuracion = this.configuracionFormService.getConfiguracion(this.editForm);
    if (configuracion.id !== null) {
      this.subscribeToSaveResponse(this.configuracionService.update(configuracion));
    } else {
      this.subscribeToSaveResponse(this.configuracionService.create(configuracion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConfiguracion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(configuracion: IConfiguracion): void {
    this.configuracion = configuracion;
    this.configuracionFormService.resetForm(this.editForm, configuracion);

    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      configuracion.proyecto
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) =>
          this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.configuracion?.proyecto)
        )
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));
  }
}
