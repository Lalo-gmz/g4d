import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FuncionalidadFormService, FuncionalidadFormGroup } from './funcionalidad-form.service';
import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadService } from '../service/funcionalidad.service';
import { IEstatusFuncionalidad } from 'app/entities/estatus-funcionalidad/estatus-funcionalidad.model';
import { EstatusFuncionalidadService } from 'app/entities/estatus-funcionalidad/service/estatus-funcionalidad.service';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';
import { IteracionService } from 'app/entities/iteracion/service/iteracion.service';

@Component({
  selector: 'jhi-funcionalidad-update',
  templateUrl: './funcionalidad-update.component.html',
})
export class FuncionalidadUpdateComponent implements OnInit {
  isSaving = false;
  funcionalidad: IFuncionalidad | null = null;

  estatusFuncionalidadsSharedCollection: IEstatusFuncionalidad[] = [];
  iteracionsSharedCollection: IIteracion[] = [];

  editForm: FuncionalidadFormGroup = this.funcionalidadFormService.createFuncionalidadFormGroup();

  constructor(
    protected funcionalidadService: FuncionalidadService,
    protected funcionalidadFormService: FuncionalidadFormService,
    protected estatusFuncionalidadService: EstatusFuncionalidadService,
    protected iteracionService: IteracionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEstatusFuncionalidad = (o1: IEstatusFuncionalidad | null, o2: IEstatusFuncionalidad | null): boolean =>
    this.estatusFuncionalidadService.compareEstatusFuncionalidad(o1, o2);

  compareIteracion = (o1: IIteracion | null, o2: IIteracion | null): boolean => this.iteracionService.compareIteracion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionalidad }) => {
      this.funcionalidad = funcionalidad;
      if (funcionalidad) {
        this.updateForm(funcionalidad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const funcionalidad = this.funcionalidadFormService.getFuncionalidad(this.editForm);
    if (funcionalidad.id !== null) {
      this.subscribeToSaveResponse(this.funcionalidadService.update(funcionalidad));
    } else {
      this.subscribeToSaveResponse(this.funcionalidadService.create(funcionalidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFuncionalidad>>): void {
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

  protected updateForm(funcionalidad: IFuncionalidad): void {
    this.funcionalidad = funcionalidad;
    this.funcionalidadFormService.resetForm(this.editForm, funcionalidad);

    this.estatusFuncionalidadsSharedCollection =
      this.estatusFuncionalidadService.addEstatusFuncionalidadToCollectionIfMissing<IEstatusFuncionalidad>(
        this.estatusFuncionalidadsSharedCollection,
        funcionalidad.estatusFuncionalidad
      );
    this.iteracionsSharedCollection = this.iteracionService.addIteracionToCollectionIfMissing<IIteracion>(
      this.iteracionsSharedCollection,
      funcionalidad.iteracion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.estatusFuncionalidadService
      .query()
      .pipe(map((res: HttpResponse<IEstatusFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((estatusFuncionalidads: IEstatusFuncionalidad[]) =>
          this.estatusFuncionalidadService.addEstatusFuncionalidadToCollectionIfMissing<IEstatusFuncionalidad>(
            estatusFuncionalidads,
            this.funcionalidad?.estatusFuncionalidad
          )
        )
      )
      .subscribe((estatusFuncionalidads: IEstatusFuncionalidad[]) => (this.estatusFuncionalidadsSharedCollection = estatusFuncionalidads));

    this.iteracionService
      .query()
      .pipe(map((res: HttpResponse<IIteracion[]>) => res.body ?? []))
      .pipe(
        map((iteracions: IIteracion[]) =>
          this.iteracionService.addIteracionToCollectionIfMissing<IIteracion>(iteracions, this.funcionalidad?.iteracion)
        )
      )
      .subscribe((iteracions: IIteracion[]) => (this.iteracionsSharedCollection = iteracions));
  }
}
