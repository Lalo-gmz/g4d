import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IScript, NewScript } from '../script.model';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScript for edit and NewConfiguracionFormGroupInput for create.
 */
type ScriptFormGroupInput = IScript | PartialWithRequiredKeyOf<NewScript>;

type ScriptFormDefaults = Pick<NewScript, 'id'>;

type ScriptFormGroupContent = {
  id: FormControl<IScript['id'] | NewScript['id']>;
  nombre: FormControl<IScript['nombre']>;
  nombreBoton: FormControl<IScript['nombreBoton']>;
  descripcion: FormControl<IScript['descripcion']>;
  proyecto: FormControl<IIteracion['proyecto']>;
  orden: FormControl<IScript['orden']>;
};

export type ScriptFormGroup = FormGroup<ScriptFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScriptFormService {
  createScriptFormGroup(configuracion: ScriptFormGroupInput = { id: null }): ScriptFormGroup {
    const scriptRawValue = {
      ...this.getFormDefaults(),
      ...configuracion,
    };
    return new FormGroup<ScriptFormGroupContent>({
      id: new FormControl(
        { value: scriptRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(scriptRawValue.nombre),
      descripcion: new FormControl(scriptRawValue.descripcion),
      nombreBoton: new FormControl(scriptRawValue.nombreBoton),
      orden: new FormControl(scriptRawValue.orden),
      proyecto: new FormControl(scriptRawValue.proyecto),
    });
  }

  getScript(form: ScriptFormGroup): IScript | NewScript {
    return form.getRawValue() as IScript | NewScript;
  }

  resetForm(form: ScriptFormGroup, configuracion: ScriptFormGroupInput): void {
    const scriptRawValue = { ...this.getFormDefaults(), ...configuracion };
    form.reset(
      {
        ...scriptRawValue,
        id: { value: scriptRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ScriptFormDefaults {
    return {
      id: null,
    };
  }
}
