import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ComentarioFormService, ComentarioFormGroup } from './comentario-form.service';
import { IComentario } from '../comentario.model';
import { ComentarioService } from '../service/comentario.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-comentario-update',
  templateUrl: './comentario-update.component.html',
})
export class ComentarioUpdateComponent implements OnInit {
  isSaving = false;
  comentario: IComentario | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: ComentarioFormGroup = this.comentarioFormService.createComentarioFormGroup();

  constructor(
    protected comentarioService: ComentarioService,
    protected comentarioFormService: ComentarioFormService,
    protected funcionalidadService: FuncionalidadService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comentario }) => {
      this.comentario = comentario;
      if (comentario) {
        this.updateForm(comentario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comentario = this.comentarioFormService.getComentario(this.editForm);
    if (comentario.id !== null) {
      this.subscribeToSaveResponse(this.comentarioService.update(comentario));
    } else {
      this.subscribeToSaveResponse(this.comentarioService.create(comentario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComentario>>): void {
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

  protected updateForm(comentario: IComentario): void {
    this.comentario = comentario;
    this.comentarioFormService.resetForm(this.editForm, comentario);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      comentario.funcionalidad
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, comentario.user);
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(funcionalidads, this.comentario?.funcionalidad)
        )
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.comentario?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
