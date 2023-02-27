import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CapturaFormService, CapturaFormGroup } from './captura-form.service';
import { ICaptura } from '../captura.model';
import { CapturaService } from '../service/captura.service';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';
import { IteracionService } from 'app/entities/iteracion/service/iteracion.service';

@Component({
  selector: 'jhi-captura-update',
  templateUrl: './captura-update.component.html',
})
export class CapturaUpdateComponent implements OnInit {
  isSaving = false;
  captura: ICaptura | null = null;

  iteracionsSharedCollection: IIteracion[] = [];

  editForm: CapturaFormGroup = this.capturaFormService.createCapturaFormGroup();

  constructor(
    protected capturaService: CapturaService,
    protected capturaFormService: CapturaFormService,
    protected iteracionService: IteracionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareIteracion = (o1: IIteracion | null, o2: IIteracion | null): boolean => this.iteracionService.compareIteracion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ captura }) => {
      this.captura = captura;
      if (captura) {
        this.updateForm(captura);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const captura = this.capturaFormService.getCaptura(this.editForm);
    if (captura.id !== null) {
      this.subscribeToSaveResponse(this.capturaService.update(captura));
    } else {
      this.subscribeToSaveResponse(this.capturaService.create(captura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaptura>>): void {
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

  protected updateForm(captura: ICaptura): void {
    this.captura = captura;
    this.capturaFormService.resetForm(this.editForm, captura);

    this.iteracionsSharedCollection = this.iteracionService.addIteracionToCollectionIfMissing<IIteracion>(
      this.iteracionsSharedCollection,
      captura.iteracion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.iteracionService
      .query()
      .pipe(map((res: HttpResponse<IIteracion[]>) => res.body ?? []))
      .pipe(
        map((iteracions: IIteracion[]) =>
          this.iteracionService.addIteracionToCollectionIfMissing<IIteracion>(iteracions, this.captura?.iteracion)
        )
      )
      .subscribe((iteracions: IIteracion[]) => (this.iteracionsSharedCollection = iteracions));
  }
}
