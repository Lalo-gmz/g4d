import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BitacoraFormService, BitacoraFormGroup } from './bitacora-form.service';
import { IBitacora } from '../bitacora.model';
import { BitacoraService } from '../service/bitacora.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

@Component({
  selector: 'jhi-bitacora-update',
  templateUrl: './bitacora-update.component.html',
})
export class BitacoraUpdateComponent implements OnInit {
  isSaving = false;
  bitacora: IBitacora | null = null;

  usuariosSharedCollection: IUsuario[] = [];
  proyectosSharedCollection: IProyecto[] = [];

  editForm: BitacoraFormGroup = this.bitacoraFormService.createBitacoraFormGroup();

  constructor(
    protected bitacoraService: BitacoraService,
    protected bitacoraFormService: BitacoraFormService,
    protected usuarioService: UsuarioService,
    protected proyectoService: ProyectoService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUsuario = (o1: IUsuario | null, o2: IUsuario | null): boolean => this.usuarioService.compareUsuario(o1, o2);

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

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(
      this.usuariosSharedCollection,
      bitacora.usuario
    );
    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      bitacora.proyecto
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, this.bitacora?.usuario)))
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

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
