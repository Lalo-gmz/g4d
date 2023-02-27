import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProyectoFormService, ProyectoFormGroup } from './proyecto-form.service';
import { IProyecto } from '../proyecto.model';
import { ProyectoService } from '../service/proyecto.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-proyecto-update',
  templateUrl: './proyecto-update.component.html',
})
export class ProyectoUpdateComponent implements OnInit {
  isSaving = false;
  proyecto: IProyecto | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: ProyectoFormGroup = this.proyectoFormService.createProyectoFormGroup();

  constructor(
    protected proyectoService: ProyectoService,
    protected proyectoFormService: ProyectoFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proyecto }) => {
      this.proyecto = proyecto;
      if (proyecto) {
        this.updateForm(proyecto);
      }

      this.loadRelationshipsOptions();
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

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      ...(proyecto.participantes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, ...(this.proyecto?.participantes ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
