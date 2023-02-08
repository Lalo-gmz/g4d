import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AtributoFormService, AtributoFormGroup } from './atributo-form.service';
import { IAtributo } from '../atributo.model';
import { AtributoService } from '../service/atributo.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';

@Component({
  selector: 'jhi-atributo-update',
  templateUrl: './atributo-update.component.html',
})
export class AtributoUpdateComponent implements OnInit {
  isSaving = false;
  atributo: IAtributo | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];

  editForm: AtributoFormGroup = this.atributoFormService.createAtributoFormGroup();

  constructor(
    protected atributoService: AtributoService,
    protected atributoFormService: AtributoFormService,
    protected funcionalidadService: FuncionalidadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atributo }) => {
      this.atributo = atributo;
      if (atributo) {
        this.updateForm(atributo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const atributo = this.atributoFormService.getAtributo(this.editForm);
    if (atributo.id !== null) {
      this.subscribeToSaveResponse(this.atributoService.update(atributo));
    } else {
      this.subscribeToSaveResponse(this.atributoService.create(atributo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAtributo>>): void {
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

  protected updateForm(atributo: IAtributo): void {
    this.atributo = atributo;
    this.atributoFormService.resetForm(this.editForm, atributo);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      atributo.funcionalidad
    );
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(funcionalidads, this.atributo?.funcionalidad)
        )
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));
  }
}
