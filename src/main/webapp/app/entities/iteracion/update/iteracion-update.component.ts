import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IteracionFormService, IteracionFormGroup } from './iteracion-form.service';
import { IIteracion } from '../iteracion.model';
import { IteracionService } from '../service/iteracion.service';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { ProyectoService } from 'app/entities/proyecto/service/proyecto.service';

@Component({
  selector: 'jhi-iteracion-update',
  templateUrl: './iteracion-update.component.html',
})
export class IteracionUpdateComponent implements OnInit {
  isSaving = false;
  iteracion: IIteracion | null = null;
  isChecked: boolean = false;

  proyectoId: number;

  proyectosSharedCollection: IProyecto[] = [];

  editForm: IteracionFormGroup = this.iteracionFormService.createIteracionFormGroup();

  constructor(
    protected iteracionService: IteracionService,
    protected iteracionFormService: IteracionFormService,
    protected proyectoService: ProyectoService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.proyectoId = this.activatedRoute.snapshot.params['proyectoId'];
  }

  compareProyecto = (o1: IProyecto | null, o2: IProyecto | null): boolean => this.proyectoService.compareProyecto(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ iteracion }) => {
      this.iteracion = iteracion;
      if (iteracion) {
        this.updateForm(iteracion);
      }

      this.loadRelationshipsOptions();
    });

    //recuperar el proyecto de donde viene
    if (this.proyectoId) {
      this.proyectoService.find(this.proyectoId).subscribe({
        next: res => {
          console.log({ res });
          this.editForm.get('proyecto')?.setValue(res.body);
          this.editForm.get('proyecto')?.disable();
        },
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const iteracion = this.iteracionFormService.getIteracion(this.editForm);
    if (iteracion.id !== null) {
      this.subscribeToSaveResponse(this.iteracionService.update(iteracion));
    } else {
      this.subscribeToSaveResponse(this.iteracionService.create(iteracion));
    }
  }

  onCheckboxChange() {
    this.isChecked = !this.isChecked;
    console.log(this.isChecked);
    if (this.isChecked) {
      this.editForm.get('idGitLab')?.setValue('');
      this.editForm.get('idGitLab')?.disable();
    } else {
      this.editForm.get('idGitLab')?.setValue('');
      this.editForm.get('idGitLab')?.enable();
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIteracion>>): void {
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

  protected updateForm(iteracion: IIteracion): void {
    this.iteracion = iteracion;
    this.iteracionFormService.resetForm(this.editForm, iteracion);

    this.proyectosSharedCollection = this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(
      this.proyectosSharedCollection,
      iteracion.proyecto
    );
  }

  protected loadRelationshipsOptions(): void {
    this.proyectoService
      .query()
      .pipe(map((res: HttpResponse<IProyecto[]>) => res.body ?? []))
      .pipe(
        map((proyectos: IProyecto[]) =>
          this.proyectoService.addProyectoToCollectionIfMissing<IProyecto>(proyectos, this.iteracion?.proyecto)
        )
      )
      .subscribe((proyectos: IProyecto[]) => (this.proyectosSharedCollection = proyectos));
  }
}
