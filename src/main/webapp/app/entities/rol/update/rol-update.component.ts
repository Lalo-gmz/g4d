import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { RolFormService, RolFormGroup } from './rol-form.service';
import { IRol } from '../rol.model';
import { RolService } from '../service/rol.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

@Component({
  selector: 'jhi-rol-update',
  templateUrl: './rol-update.component.html',
})
export class RolUpdateComponent implements OnInit {
  isSaving = false;
  rol: IRol | null = null;

  proyectosSharedCollection: IProyecto[] = [];

  editForm: RolFormGroup = this.rolFormService.createRolFormGroup();

  constructor(
    protected rolService: RolService,
    protected rolFormService: RolFormService,
    protected proyectoService: ProyectoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rol }) => {
      this.rol = rol;
      if (rol) {
        this.updateForm(rol);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rol = this.rolFormService.getRol(this.editForm);
    if (rol.id !== null) {
      this.subscribeToSaveResponse(this.rolService.update(rol));
    } else {
      this.subscribeToSaveResponse(this.rolService.create(rol));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRol>>): void {
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

  protected updateForm(rol: IRol): void {
    this.rol = rol;
    this.rolFormService.resetForm(this.editForm, rol);

    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      rol.proyecto
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) => this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.rol?.proyecto))
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));
  }
}
