import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIteracion, NewIteracion } from '../iteracion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIteracion for edit and NewIteracionFormGroupInput for create.
 */
type IteracionFormGroupInput = IIteracion | PartialWithRequiredKeyOf<NewIteracion>;

type IteracionFormDefaults = Pick<NewIteracion, 'id'>;

type IteracionFormGroupContent = {
  id: FormControl<IIteracion['id'] | NewIteracion['id']>;
  nombre: FormControl<IIteracion['nombre']>;
  inicio: FormControl<IIteracion['inicio']>;
  fin: FormControl<IIteracion['fin']>;
  proyecto: FormControl<IIteracion['proyecto']>;
};

export type IteracionFormGroup = FormGroup<IteracionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IteracionFormService {
  createIteracionFormGroup(iteracion: IteracionFormGroupInput = { id: null }): IteracionFormGroup {
    const iteracionRawValue = {
      ...this.getFormDefaults(),
      ...iteracion,
    };
    return new FormGroup<IteracionFormGroupContent>({
      id: new FormControl(
        { value: iteracionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(iteracionRawValue.nombre, {
        validators: [Validators.required],
      }),
      inicio: new FormControl(iteracionRawValue.inicio),
      fin: new FormControl(iteracionRawValue.fin),
      proyecto: new FormControl(iteracionRawValue.proyecto),
    });
  }

  getIteracion(form: IteracionFormGroup): IIteracion | NewIteracion {
    return form.getRawValue() as IIteracion | NewIteracion;
  }

  resetForm(form: IteracionFormGroup, iteracion: IteracionFormGroupInput): void {
    const iteracionRawValue = { ...this.getFormDefaults(), ...iteracion };
    form.reset(
      {
        ...iteracionRawValue,
        id: { value: iteracionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IteracionFormDefaults {
    return {
      id: null,
    };
  }
}
