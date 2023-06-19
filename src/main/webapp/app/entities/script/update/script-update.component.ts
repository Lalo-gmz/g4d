import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ScriptFormService, ScriptFormGroup } from './script-form.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { ScriptService } from '../service/script.service';
import { IScript } from '../script.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

@Component({
  selector: 'jhi-script-update',
  templateUrl: './script-update.component.html',
})
export class ScriptUpdateComponent implements OnInit {
  isSaving = false;
  script: IScript | null = null;

  usersSharedCollection: IUser[] = [];
  funcionalidadsSharedCollection: IFuncionalidad[] = [];
  proyectosSharedCollection: IProyecto[] = [];

  editForm: ScriptFormGroup = this.scriptFormService.createScriptFormGroup();

  constructor(
    protected scriptService: ScriptService,
    protected scriptFormService: ScriptFormService,
    protected userService: UserService,
    protected funcionalidadService: FuncionalidadService,
    protected activatedRoute: ActivatedRoute,
    protected proyectoService: ProyectoService
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);
  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ script }) => {
      this.script = script;
      if (script) {
        this.updateForm(script);
      }
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const script = this.scriptFormService.getScript(this.editForm);

    if (script.id !== null) {
      this.subscribeToSaveResponse(this.scriptService.update(script));
    } else {
      this.subscribeToSaveResponse(this.scriptService.create(script));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScript>>): void {
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

  protected updateForm(script: IScript): void {
    this.script = script;
    this.scriptFormService.resetForm(this.editForm, script);
  }

  protected loadRelationshipsOptions(): void {
    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) => this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.script?.proyecto))
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));
  }
}
