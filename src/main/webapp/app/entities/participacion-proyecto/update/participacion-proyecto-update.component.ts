import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ParticipacionProyectoFormService, ParticipacionProyectoFormGroup } from './participacion-proyecto-form.service';
import { IParticipacionProyecto } from '../participacion-proyecto.model';
import { ParticipacionProyectoService } from '../service/participacion-proyecto.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';

@Component({
  selector: 'jhi-participacion-proyecto-update',
  templateUrl: './participacion-proyecto-update.component.html',
})
export class ParticipacionProyectoUpdateComponent implements OnInit {
  isSaving = false;
  participacionProyecto: IParticipacionProyecto | null = null;

  usersSharedCollection: IUser[] = [];
  proyectosSharedCollection: IProyecto[] = [];
  rolsSharedCollection: IRol[] = [];

  editForm: ParticipacionProyectoFormGroup = this.participacionProyectoFormService.createParticipacionProyectoFormGroup();

  constructor(
    protected participacionProyectoService: ParticipacionProyectoService,
    protected participacionProyectoFormService: ParticipacionProyectoFormService,
    protected userService: UserService,
    protected proyectoService: ProyectoService,
    protected rolService: RolService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

  compareRol = (o1: IRol | null, o2: IRol | null): boolean => this.rolService.compareRol(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ participacionProyecto }) => {
      this.participacionProyecto = participacionProyecto;
      if (participacionProyecto) {
        this.updateForm(participacionProyecto);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const participacionProyecto = this.participacionProyectoFormService.getParticipacionProyecto(this.editForm);
    if (participacionProyecto.id !== null) {
      this.subscribeToSaveResponse(this.participacionProyectoService.update(participacionProyecto));
    } else {
      this.subscribeToSaveResponse(this.participacionProyectoService.create(participacionProyecto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParticipacionProyecto>>): void {
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

  protected updateForm(participacionProyecto: IParticipacionProyecto): void {
    this.participacionProyecto = participacionProyecto;
    this.participacionProyectoFormService.resetForm(this.editForm, participacionProyecto);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      participacionProyecto.user
    );
    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      participacionProyecto.proyecto
    );
    this.rolsSharedCollection = this.rolService.addRolToCollectionIfMissing<IRol>(this.rolsSharedCollection, participacionProyecto.rol);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.participacionProyecto?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) =>
          this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.participacionProyecto?.proyecto)
        )
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));

    this.rolService
      .query()
      .pipe(map((res: HttpResponse<IRol[]>) => res.body ?? []))
      .pipe(map((rols: IRol[]) => this.rolService.addRolToCollectionIfMissing<IRol>(rols, this.participacionProyecto?.rol)))
      .subscribe((rols: IRol[]) => (this.rolsSharedCollection = rols));
  }
}
