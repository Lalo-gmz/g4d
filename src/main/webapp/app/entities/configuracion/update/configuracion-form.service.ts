import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IConfiguracion, NewConfiguracion } from '../configuracion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IConfiguracion for edit and NewConfiguracionFormGroupInput for create.
 */
type ConfiguracionFormGroupInput = IConfiguracion | PartialWithRequiredKeyOf<NewConfiguracion>;

type ConfiguracionFormDefaults = Pick<NewConfiguracion, 'id'>;

type ConfiguracionFormGroupContent = {
  id: FormControl<IConfiguracion['id'] | NewConfiguracion['id']>;
  clave: FormControl<IConfiguracion['clave']>;
  valor: FormControl<IConfiguracion['valor']>;
  proyecto: FormControl<IConfiguracion['proyecto']>;
};

export type ConfiguracionFormGroup = FormGroup<ConfiguracionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ConfiguracionFormService {
  createConfiguracionFormGroup(configuracion: ConfiguracionFormGroupInput = { id: null }): ConfiguracionFormGroup {
    const configuracionRawValue = {
      ...this.getFormDefaults(),
      ...configuracion,
    };
    return new FormGroup<ConfiguracionFormGroupContent>({
      id: new FormControl(
        { value: configuracionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      clave: new FormControl(configuracionRawValue.clave),
      valor: new FormControl(configuracionRawValue.valor),
      proyecto: new FormControl(configuracionRawValue.proyecto),
    });
  }

  getConfiguracion(form: ConfiguracionFormGroup): IConfiguracion | NewConfiguracion {
    return form.getRawValue() as IConfiguracion | NewConfiguracion;
  }

  resetForm(form: ConfiguracionFormGroup, configuracion: ConfiguracionFormGroupInput): void {
    const configuracionRawValue = { ...this.getFormDefaults(), ...configuracion };
    form.reset(
      {
        ...configuracionRawValue,
        id: { value: configuracionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ConfiguracionFormDefaults {
    return {
      id: null,
    };
  }
}
