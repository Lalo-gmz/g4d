import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AtributoFormService, AtributoFormGroup } from './atributo-form.service';
import { IAtributo } from '../atributo.model';
import { AtributoService } from '../service/atributo.service';

@Component({
  selector: 'jhi-atributo-update',
  templateUrl: './atributo-update.component.html',
})
export class AtributoUpdateComponent implements OnInit {
  isSaving = false;
  atributo: IAtributo | null = null;

  editForm: AtributoFormGroup = this.atributoFormService.createAtributoFormGroup();

  constructor(
    protected atributoService: AtributoService,
    protected atributoFormService: AtributoFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ atributo }) => {
      this.atributo = atributo;
      if (atributo) {
        this.updateForm(atributo);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const atributo = this.atributoFormService.getAtributo(this.editForm);
    if (atributo.id !== null) {
      this.subscribeToSaveResponse(this.atributoService.update(atributo));
    } else {
      this.subscribeToSaveResponse(this.atributoService.create(atributo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAtributo>>): void {
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

  protected updateForm(atributo: IAtributo): void {
    this.atributo = atributo;
    this.atributoFormService.resetForm(this.editForm, atributo);
  }
}
