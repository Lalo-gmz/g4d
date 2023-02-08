import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { EstatusFuncionalidadFormService, EstatusFuncionalidadFormGroup } from './estatus-funcionalidad-form.service';
import { IEstatusFuncionalidad } from '../estatus-funcionalidad.model';
import { EstatusFuncionalidadService } from '../service/estatus-funcionalidad.service';

@Component({
  selector: 'jhi-estatus-funcionalidad-update',
  templateUrl: './estatus-funcionalidad-update.component.html',
})
export class EstatusFuncionalidadUpdateComponent implements OnInit {
  isSaving = false;
  estatusFuncionalidad: IEstatusFuncionalidad | null = null;

  editForm: EstatusFuncionalidadFormGroup = this.estatusFuncionalidadFormService.createEstatusFuncionalidadFormGroup();

  constructor(
    protected estatusFuncionalidadService: EstatusFuncionalidadService,
    protected estatusFuncionalidadFormService: EstatusFuncionalidadFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ estatusFuncionalidad }) => {
      this.estatusFuncionalidad = estatusFuncionalidad;
      if (estatusFuncionalidad) {
        this.updateForm(estatusFuncionalidad);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const estatusFuncionalidad = this.estatusFuncionalidadFormService.getEstatusFuncionalidad(this.editForm);
    if (estatusFuncionalidad.id !== null) {
      this.subscribeToSaveResponse(this.estatusFuncionalidadService.update(estatusFuncionalidad));
    } else {
      this.subscribeToSaveResponse(this.estatusFuncionalidadService.create(estatusFuncionalidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEstatusFuncionalidad>>): void {
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

  protected updateForm(estatusFuncionalidad: IEstatusFuncionalidad): void {
    this.estatusFuncionalidad = estatusFuncionalidad;
    this.estatusFuncionalidadFormService.resetForm(this.editForm, estatusFuncionalidad);
  }
}
