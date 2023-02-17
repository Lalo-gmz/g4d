import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PrioridadFormService, PrioridadFormGroup } from './prioridad-form.service';
import { IPrioridad } from '../prioridad.model';
import { PrioridadService } from '../service/prioridad.service';

@Component({
  selector: 'jhi-prioridad-update',
  templateUrl: './prioridad-update.component.html',
})
export class PrioridadUpdateComponent implements OnInit {
  isSaving = false;
  prioridad: IPrioridad | null = null;

  editForm: PrioridadFormGroup = this.prioridadFormService.createPrioridadFormGroup();

  constructor(
    protected prioridadService: PrioridadService,
    protected prioridadFormService: PrioridadFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prioridad }) => {
      this.prioridad = prioridad;
      if (prioridad) {
        this.updateForm(prioridad);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prioridad = this.prioridadFormService.getPrioridad(this.editForm);
    if (prioridad.id !== null) {
      this.subscribeToSaveResponse(this.prioridadService.update(prioridad));
    } else {
      this.subscribeToSaveResponse(this.prioridadService.create(prioridad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrioridad>>): void {
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

  protected updateForm(prioridad: IPrioridad): void {
    this.prioridad = prioridad;
    this.prioridadFormService.resetForm(this.editForm, prioridad);
  }
}
