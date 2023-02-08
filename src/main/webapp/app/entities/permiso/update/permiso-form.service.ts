import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPermiso, NewPermiso } from '../permiso.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPermiso for edit and NewPermisoFormGroupInput for create.
 */
type PermisoFormGroupInput = IPermiso | PartialWithRequiredKeyOf<NewPermiso>;

type PermisoFormDefaults = Pick<NewPermiso, 'id'>;

type PermisoFormGroupContent = {
  id: FormControl<IPermiso['id'] | NewPermiso['id']>;
  nombre: FormControl<IPermiso['nombre']>;
  rol: FormControl<IPermiso['rol']>;
};

export type PermisoFormGroup = FormGroup<PermisoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PermisoFormService {
  createPermisoFormGroup(permiso: PermisoFormGroupInput = { id: null }): PermisoFormGroup {
    const permisoRawValue = {
      ...this.getFormDefaults(),
      ...permiso,
    };
    return new FormGroup<PermisoFormGroupContent>({
      id: new FormControl(
        { value: permisoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(permisoRawValue.nombre),
      rol: new FormControl(permisoRawValue.rol),
    });
  }

  getPermiso(form: PermisoFormGroup): IPermiso | NewPermiso {
    return form.getRawValue() as IPermiso | NewPermiso;
  }

  resetForm(form: PermisoFormGroup, permiso: PermisoFormGroupInput): void {
    const permisoRawValue = { ...this.getFormDefaults(), ...permiso };
    form.reset(
      {
        ...permisoRawValue,
        id: { value: permisoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PermisoFormDefaults {
    return {
      id: null,
    };
  }
}
