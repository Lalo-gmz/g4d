import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRol, NewRol } from '../rol.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRol for edit and NewRolFormGroupInput for create.
 */
type RolFormGroupInput = IRol | PartialWithRequiredKeyOf<NewRol>;

type RolFormDefaults = Pick<NewRol, 'id'>;

type RolFormGroupContent = {
  id: FormControl<IRol['id'] | NewRol['id']>;
  nombre: FormControl<IRol['nombre']>;
  proyecto: FormControl<IRol['proyecto']>;
};

export type RolFormGroup = FormGroup<RolFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RolFormService {
  createRolFormGroup(rol: RolFormGroupInput = { id: null }): RolFormGroup {
    const rolRawValue = {
      ...this.getFormDefaults(),
      ...rol,
    };
    return new FormGroup<RolFormGroupContent>({
      id: new FormControl(
        { value: rolRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(rolRawValue.nombre),
      proyecto: new FormControl(rolRawValue.proyecto),
    });
  }

  getRol(form: RolFormGroup): IRol | NewRol {
    return form.getRawValue() as IRol | NewRol;
  }

  resetForm(form: RolFormGroup, rol: RolFormGroupInput): void {
    const rolRawValue = { ...this.getFormDefaults(), ...rol };
    form.reset(
      {
        ...rolRawValue,
        id: { value: rolRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RolFormDefaults {
    return {
      id: null,
    };
  }
}
