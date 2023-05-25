import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FuncionalidadFormService, FuncionalidadFormGroup } from './funcionalidad-form.service';
import { IFuncionalidad } from '../funcionalidad.model';
import { FuncionalidadService } from '../service/funcionalidad.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEstatusFuncionalidad } from 'app/entities/estatus-funcionalidad/estatus-funcionalidad.model';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';
import { IteracionService } from 'app/entities/iteracion/service/iteracion.service';

@Component({
  selector: 'jhi-funcionalidad-update',
  templateUrl: './funcionalidad-update.component.html',
})
export class FuncionalidadUpdateComponent implements OnInit {
  isSaving = false;
  funcionalidad: IFuncionalidad | null = null;

  iteracionId: number;

  usersSharedCollection: IUser[] = [];
  estatusFuncionalidadsSharedCollection: IEstatusFuncionalidad[] = [];
  iteracionsSharedCollection: IIteracion[] = [];

  editForm: FuncionalidadFormGroup = this.funcionalidadFormService.createFuncionalidadFormGroup();

  constructor(
    protected funcionalidadService: FuncionalidadService,
    protected funcionalidadFormService: FuncionalidadFormService,
    protected userService: UserService,
    protected iteracionService: IteracionService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.iteracionId = this.activatedRoute.snapshot.params['iteracionId'];
  }

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareIteracion = (o1: IIteracion | null, o2: IIteracion | null): boolean => this.iteracionService.compareIteracion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ funcionalidad }) => {
      this.funcionalidad = funcionalidad;
      if (funcionalidad) {
        this.updateForm(funcionalidad);
      }

      this.loadRelationshipsOptions();
    });

    //recuperar el proyecto de donde viene
    this.iteracionService.find(this.iteracionId).subscribe({
      next: res => {
        console.log({ res });
        console.log(this.editForm);
        this.editForm.get('iteracion')?.setValue(res.body);
        this.editForm.get('iteracion')?.disable();
      },
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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      ...(funcionalidad.users ?? [])
    );

    this.iteracionsSharedCollection = this.iteracionService.addIteracionToCollectionIfMissing<IIteracion>(
      this.iteracionsSharedCollection,
      funcionalidad.iteracion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.funcionalidad?.users ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

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
