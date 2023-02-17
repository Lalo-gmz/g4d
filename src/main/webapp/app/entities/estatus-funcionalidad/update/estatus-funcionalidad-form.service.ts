import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEstatusFuncionalidad, NewEstatusFuncionalidad } from '../estatus-funcionalidad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEstatusFuncionalidad for edit and NewEstatusFuncionalidadFormGroupInput for create.
 */
type EstatusFuncionalidadFormGroupInput = IEstatusFuncionalidad | PartialWithRequiredKeyOf<NewEstatusFuncionalidad>;

type EstatusFuncionalidadFormDefaults = Pick<NewEstatusFuncionalidad, 'id'>;

type EstatusFuncionalidadFormGroupContent = {
  id: FormControl<IEstatusFuncionalidad['id'] | NewEstatusFuncionalidad['id']>;
  nombre: FormControl<IEstatusFuncionalidad['nombre']>;
};

export type EstatusFuncionalidadFormGroup = FormGroup<EstatusFuncionalidadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EstatusFuncionalidadFormService {
  createEstatusFuncionalidadFormGroup(
    estatusFuncionalidad: EstatusFuncionalidadFormGroupInput = { id: null }
  ): EstatusFuncionalidadFormGroup {
    const estatusFuncionalidadRawValue = {
      ...this.getFormDefaults(),
      ...estatusFuncionalidad,
    };
    return new FormGroup<EstatusFuncionalidadFormGroupContent>({
      id: new FormControl(
        { value: estatusFuncionalidadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(estatusFuncionalidadRawValue.nombre),
    });
  }

  getEstatusFuncionalidad(form: EstatusFuncionalidadFormGroup): IEstatusFuncionalidad | NewEstatusFuncionalidad {
    return form.getRawValue() as IEstatusFuncionalidad | NewEstatusFuncionalidad;
  }

  resetForm(form: EstatusFuncionalidadFormGroup, estatusFuncionalidad: EstatusFuncionalidadFormGroupInput): void {
    const estatusFuncionalidadRawValue = { ...this.getFormDefaults(), ...estatusFuncionalidad };
    form.reset(
      {
        ...estatusFuncionalidadRawValue,
        id: { value: estatusFuncionalidadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EstatusFuncionalidadFormDefaults {
    return {
      id: null,
    };
  }
}
