import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PermisoFormService, PermisoFormGroup } from './permiso-form.service';
import { IPermiso } from '../permiso.model';
import { PermisoService } from '../service/permiso.service';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';

@Component({
  selector: 'jhi-permiso-update',
  templateUrl: './permiso-update.component.html',
})
export class PermisoUpdateComponent implements OnInit {
  isSaving = false;
  permiso: IPermiso | null = null;

  rolsSharedCollection: IRol[] = [];

  editForm: PermisoFormGroup = this.permisoFormService.createPermisoFormGroup();

  constructor(
    protected permisoService: PermisoService,
    protected permisoFormService: PermisoFormService,
    protected rolService: RolService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareRol = (o1: IRol | null, o2: IRol | null): boolean => this.rolService.compareRol(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permiso }) => {
      this.permiso = permiso;
      if (permiso) {
        this.updateForm(permiso);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const permiso = this.permisoFormService.getPermiso(this.editForm);
    if (permiso.id !== null) {
      this.subscribeToSaveResponse(this.permisoService.update(permiso));
    } else {
      this.subscribeToSaveResponse(this.permisoService.create(permiso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPermiso>>): void {
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

  protected updateForm(permiso: IPermiso): void {
    this.permiso = permiso;
    this.permisoFormService.resetForm(this.editForm, permiso);

    this.rolsSharedCollection = this.rolService.addRolToCollectionIfMissing<IRol>(this.rolsSharedCollection, permiso.rol);
  }

  protected loadRelationshipsOptions(): void {
    this.rolService
      .query()
      .pipe(map((res: HttpResponse<IRol[]>) => res.body ?? []))
      .pipe(map((rols: IRol[]) => this.rolService.addRolToCollectionIfMissing<IRol>(rols, this.permiso?.rol)))
      .subscribe((rols: IRol[]) => (this.rolsSharedCollection = rols));
  }
}
