import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BitacoraFormService, BitacoraFormGroup } from './bitacora-form.service';
import { IBitacora } from '../bitacora.model';
import { BitacoraService } from '../service/bitacora.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';

@Component({
  selector: 'jhi-bitacora-update',
  templateUrl: './bitacora-update.component.html',
})
export class BitacoraUpdateComponent implements OnInit {
  isSaving = false;
  bitacora: IBitacora | null = null;

  usersSharedCollection: IUser[] = [];
  funcionalidadsSharedCollection: IFuncionalidad[] = [];

  editForm: BitacoraFormGroup = this.bitacoraFormService.createBitacoraFormGroup();

  constructor(
    protected bitacoraService: BitacoraService,
    protected bitacoraFormService: BitacoraFormService,
    protected userService: UserService,
    protected funcionalidadService: FuncionalidadService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bitacora }) => {
      this.bitacora = bitacora;
      if (bitacora) {
        this.updateForm(bitacora);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bitacora = this.bitacoraFormService.getBitacora(this.editForm);
    if (bitacora.id !== null) {
      this.subscribeToSaveResponse(this.bitacoraService.update(bitacora));
    } else {
      this.subscribeToSaveResponse(this.bitacoraService.create(bitacora));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBitacora>>): void {
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

  protected updateForm(bitacora: IBitacora): void {
    this.bitacora = bitacora;
    this.bitacoraFormService.resetForm(this.editForm, bitacora);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, bitacora.user);
    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      bitacora.funcionalidad
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bitacora?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(funcionalidads, this.bitacora?.funcionalidad)
        )
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));
  }
}
