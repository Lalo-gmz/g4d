import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProyectoFormService, ProyectoFormGroup } from './proyecto-form.service';
import { IProyecto } from '../proyecto.model';
import { ProyectoService } from '../service/proyecto.service';

@Component({
  selector: 'jhi-proyecto-update',
  templateUrl: './proyecto-update.component.html',
})
export class ProyectoUpdateComponent implements OnInit {
  isSaving = false;
  proyecto: IProyecto | null = null;

  editForm: ProyectoFormGroup = this.proyectoFormService.createProyectoFormGroup();

  constructor(
    protected proyectoService: ProyectoService,
    protected proyectoFormService: ProyectoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyecto }) => {
      this.proyecto = proyecto;
      if (proyecto) {
        this.updateForm(proyecto);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proyecto = this.proyectoFormService.getProyecto(this.editForm);
    if (proyecto.id !== null) {
      this.subscribeToSaveResponse(this.proyectoService.update(proyecto));
    } else {
      this.subscribeToSaveResponse(this.proyectoService.create(proyecto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProyecto>>): void {
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

  protected updateForm(proyecto: IProyecto): void {
    this.proyecto = proyecto;
    this.proyectoFormService.resetForm(this.editForm, proyecto);
  }
}
