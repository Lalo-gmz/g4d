import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AtributoFuncionalidadFormService, AtributoFuncionalidadFormGroup } from './atributo-funcionalidad-form.service';
import { IAtributoFuncionalidad } from '../atributo-funcionalidad.model';
import { AtributoFuncionalidadService } from '../service/atributo-funcionalidad.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IAtributo } from 'app/entities/atributo/atributo.model';
import { AtributoService } from 'app/entities/atributo/service/atributo.service';

@Component({
  selector: 'jhi-atributo-funcionalidad-update',
  templateUrl: './atributo-funcionalidad-update.component.html',
})
export class AtributoFuncionalidadUpdateComponent implements OnInit {
  isSaving = false;
  atributoFuncionalidad: IAtributoFuncionalidad | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];
  atributosSharedCollection: IAtributo[] = [];

  editForm: AtributoFuncionalidadFormGroup = this.atributoFuncionalidadFormService.createAtributoFuncionalidadFormGroup();

  funcionalidadId: number = 0;

  constructor(
    protected atributoFuncionalidadService: AtributoFuncionalidadService,
    protected atributoFuncionalidadFormService: AtributoFuncionalidadFormService,
    protected funcionalidadService: FuncionalidadService,
    protected atributoService: AtributoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  compareAtributo = (o1: IAtributo | null, o2: IAtributo | null): boolean => this.atributoService.compareAtributo(o1, o2);

  ngOnInit(): void {
    this.funcionalidadId = parseInt(this.activatedRoute.snapshot.params['funcionalidadId']);

    this.activatedRoute.data.subscribe(({ atributoFuncionalidad }) => {
      this.atributoFuncionalidad = atributoFuncionalidad;
      if (atributoFuncionalidad) {
        this.updateForm(atributoFuncionalidad);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    let atributoFuncionalidad = this.atributoFuncionalidadFormService.getAtributoFuncionalidad(this.editForm);

    const funcionalidadEcontrada = this.funcionalidadsSharedCollection.find(func => func.id === this.funcionalidadId);

    atributoFuncionalidad.funcionalidad = funcionalidadEcontrada;

    console.log('Atributo: ', atributoFuncionalidad);

    if (atributoFuncionalidad.id !== null) {
      this.subscribeToSaveResponse(this.atributoFuncionalidadService.update(atributoFuncionalidad));
    } else {
      this.subscribeToSaveResponse(this.atributoFuncionalidadService.create(atributoFuncionalidad));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAtributoFuncionalidad>>): void {
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

  protected updateForm(atributoFuncionalidad: IAtributoFuncionalidad): void {
    this.atributoFuncionalidad = atributoFuncionalidad;
    this.atributoFuncionalidadFormService.resetForm(this.editForm, atributoFuncionalidad);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      atributoFuncionalidad.funcionalidad
    );
    this.atributosSharedCollection = this.atributoService.addAtributoToCollectionIfMissing<IAtributo>(
      this.atributosSharedCollection,
      atributoFuncionalidad.atributo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
            funcionalidads,
            this.atributoFuncionalidad?.funcionalidad
          )
        )
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));

    this.atributoService
      .query()
      .pipe(map((res: HttpResponse<IAtributo[]>) => res.body ?? []))
      .pipe(
        map((atributos: IAtributo[]) =>
          this.atributoService.addAtributoToCollectionIfMissing<IAtributo>(atributos, this.atributoFuncionalidad?.atributo)
        )
      )
      .subscribe((atributos: IAtributo[]) => (this.atributosSharedCollection = atributos));
  }
}
