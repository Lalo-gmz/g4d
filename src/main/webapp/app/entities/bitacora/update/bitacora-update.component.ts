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
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

@Component({
  selector: 'jhi-bitacora-update',
  templateUrl: './bitacora-update.component.html',
})
export class BitacoraUpdateComponent implements OnInit {
  isSaving = false;
  bitacora: IBitacora | null = null;

  usersSharedCollection: IUser[] = [];
  proyectosSharedCollection: IProyecto[] = [];

  editForm: BitacoraFormGroup = this.bitacoraFormService.createBitacoraFormGroup();

  constructor(
    protected bitacoraService: BitacoraService,
    protected bitacoraFormService: BitacoraFormService,
    protected userService: UserService,
    protected proyectoService: ProyectoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

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
    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      bitacora.proyecto
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bitacora?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) =>
          this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.bitacora?.proyecto)
        )
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));
  }
}
