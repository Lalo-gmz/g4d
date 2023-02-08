import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EtiquetaFormService, EtiquetaFormGroup } from './etiqueta-form.service';
import { IEtiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';

@Component({
  selector: 'jhi-etiqueta-update',
  templateUrl: './etiqueta-update.component.html',
})
export class EtiquetaUpdateComponent implements OnInit {
  isSaving = false;
  etiqueta: IEtiqueta | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];

  editForm: EtiquetaFormGroup = this.etiquetaFormService.createEtiquetaFormGroup();

  constructor(
    protected etiquetaService: EtiquetaService,
    protected etiquetaFormService: EtiquetaFormService,
    protected funcionalidadService: FuncionalidadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etiqueta }) => {
      this.etiqueta = etiqueta;
      if (etiqueta) {
        this.updateForm(etiqueta);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etiqueta = this.etiquetaFormService.getEtiqueta(this.editForm);
    if (etiqueta.id !== null) {
      this.subscribeToSaveResponse(this.etiquetaService.update(etiqueta));
    } else {
      this.subscribeToSaveResponse(this.etiquetaService.create(etiqueta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtiqueta>>): void {
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

  protected updateForm(etiqueta: IEtiqueta): void {
    this.etiqueta = etiqueta;
    this.etiquetaFormService.resetForm(this.editForm, etiqueta);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      etiqueta.funcionalidad
    );
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(funcionalidads, this.etiqueta?.funcionalidad)
        )
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));
  }
}
