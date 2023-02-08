import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UsuarioFormService, UsuarioFormGroup } from './usuario-form.service';
import { IUsuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { FuncionalidadService } from 'app/entities/funcionalidad/service/funcionalidad.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';
import { IRol } from 'app/entities/rol/rol.model';
import { RolService } from 'app/entities/rol/service/rol.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;
  usuario: IUsuario | null = null;

  funcionalidadsSharedCollection: IFuncionalidad[] = [];
  proyectosSharedCollection: IProyecto[] = [];
  rolsSharedCollection: IRol[] = [];

  editForm: UsuarioFormGroup = this.usuarioFormService.createUsuarioFormGroup();

  constructor(
    protected usuarioService: UsuarioService,
    protected usuarioFormService: UsuarioFormService,
    protected funcionalidadService: FuncionalidadService,
    protected proyectoService: ProyectoService,
    protected rolService: RolService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFuncionalidad = (o1: IFuncionalidad | null, o2: IFuncionalidad | null): boolean =>
    this.funcionalidadService.compareFuncionalidad(o1, o2);

  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

  compareRol = (o1: IRol | null, o2: IRol | null): boolean => this.rolService.compareRol(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.usuario = usuario;
      if (usuario) {
        this.updateForm(usuario);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.usuarioFormService.getUsuario(this.editForm);
    if (usuario.id !== null) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.usuario = usuario;
    this.usuarioFormService.resetForm(this.editForm, usuario);

    this.funcionalidadsSharedCollection = this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(
      this.funcionalidadsSharedCollection,
      usuario.funcionalidad
    );
    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      usuario.proyecto
    );
    this.rolsSharedCollection = this.rolService.addRolToCollectionIfMissing<IRol>(this.rolsSharedCollection, usuario.rol);
  }

  protected loadRelationshipsOptions(): void {
    this.funcionalidadService
      .query()
      .pipe(map((res: HttpResponse<IFuncionalidad[]>) => res.body ?? []))
      .pipe(
        map((funcionalidads: IFuncionalidad[]) =>
          this.funcionalidadService.addFuncionalidadToCollectionIfMissing<IFuncionalidad>(funcionalidads, this.usuario?.funcionalidad)
        )
      )
      .subscribe((funcionalidads: IFuncionalidad[]) => (this.funcionalidadsSharedCollection = funcionalidads));

    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) => this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.usuario?.proyecto))
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));

    this.rolService
      .query()
      .pipe(map((res: HttpResponse<IRol[]>) => res.body ?? []))
      .pipe(map((rols: IRol[]) => this.rolService.addRolToCollectionIfMissing<IRol>(rols, this.usuario?.rol)))
      .subscribe((rols: IRol[]) => (this.rolsSharedCollection = rols));
  }
}
