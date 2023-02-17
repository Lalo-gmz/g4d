import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPrioridad, NewPrioridad } from '../prioridad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrioridad for edit and NewPrioridadFormGroupInput for create.
 */
type PrioridadFormGroupInput = IPrioridad | PartialWithRequiredKeyOf<NewPrioridad>;

type PrioridadFormDefaults = Pick<NewPrioridad, 'id'>;

type PrioridadFormGroupContent = {
  id: FormControl<IPrioridad['id'] | NewPrioridad['id']>;
  nombre: FormControl<IPrioridad['nombre']>;
  prioridadNumerica: FormControl<IPrioridad['prioridadNumerica']>;
};

export type PrioridadFormGroup = FormGroup<PrioridadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PrioridadFormService {
  createPrioridadFormGroup(prioridad: PrioridadFormGroupInput = { id: null }): PrioridadFormGroup {
    const prioridadRawValue = {
      ...this.getFormDefaults(),
      ...prioridad,
    };
    return new FormGroup<PrioridadFormGroupContent>({
      id: new FormControl(
        { value: prioridadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(prioridadRawValue.nombre),
      prioridadNumerica: new FormControl(prioridadRawValue.prioridadNumerica),
    });
  }

  getPrioridad(form: PrioridadFormGroup): IPrioridad | NewPrioridad {
    return form.getRawValue() as IPrioridad | NewPrioridad;
  }

  resetForm(form: PrioridadFormGroup, prioridad: PrioridadFormGroupInput): void {
    const prioridadRawValue = { ...this.getFormDefaults(), ...prioridad };
    form.reset(
      {
        ...prioridadRawValue,
        id: { value: prioridadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PrioridadFormDefaults {
    return {
      id: null,
    };
  }
}
