import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { RolFormService, RolFormGroup } from './rol-form.service';
import { IRol } from '../rol.model';
import { RolService } from '../service/rol.service';

@Component({
  selector: 'jhi-rol-update',
  templateUrl: './rol-update.component.html',
})
export class RolUpdateComponent implements OnInit {
  isSaving = false;
  rol: IRol | null = null;

  editForm: RolFormGroup = this.rolFormService.createRolFormGroup();

  constructor(protected rolService: RolService, protected rolFormService: RolFormService, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rol }) => {
      this.rol = rol;
      if (rol) {
        this.updateForm(rol);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rol = this.rolFormService.getRol(this.editForm);
    if (rol.id !== null) {
      this.subscribeToSaveResponse(this.rolService.update(rol));
    } else {
      this.subscribeToSaveResponse(this.rolService.create(rol));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRol>>): void {
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

  protected updateForm(rol: IRol): void {
    this.rol = rol;
    this.rolFormService.resetForm(this.editForm, rol);
  }
}
