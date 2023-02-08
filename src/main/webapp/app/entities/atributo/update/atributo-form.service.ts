import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAtributo, NewAtributo } from '../atributo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAtributo for edit and NewAtributoFormGroupInput for create.
 */
type AtributoFormGroupInput = IAtributo | PartialWithRequiredKeyOf<NewAtributo>;

type AtributoFormDefaults = Pick<NewAtributo, 'id' | 'marcado' | 'auxiliar'>;

type AtributoFormGroupContent = {
  id: FormControl<IAtributo['id'] | NewAtributo['id']>;
  nombre: FormControl<IAtributo['nombre']>;
  marcado: FormControl<IAtributo['marcado']>;
  auxiliar: FormControl<IAtributo['auxiliar']>;
  funcionalidad: FormControl<IAtributo['funcionalidad']>;
};

export type AtributoFormGroup = FormGroup<AtributoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AtributoFormService {
  createAtributoFormGroup(atributo: AtributoFormGroupInput = { id: null }): AtributoFormGroup {
    const atributoRawValue = {
      ...this.getFormDefaults(),
      ...atributo,
    };
    return new FormGroup<AtributoFormGroupContent>({
      id: new FormControl(
        { value: atributoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(atributoRawValue.nombre),
      marcado: new FormControl(atributoRawValue.marcado),
      auxiliar: new FormControl(atributoRawValue.auxiliar),
      funcionalidad: new FormControl(atributoRawValue.funcionalidad),
    });
  }

  getAtributo(form: AtributoFormGroup): IAtributo | NewAtributo {
    return form.getRawValue() as IAtributo | NewAtributo;
  }

  resetForm(form: AtributoFormGroup, atributo: AtributoFormGroupInput): void {
    const atributoRawValue = { ...this.getFormDefaults(), ...atributo };
    form.reset(
      {
        ...atributoRawValue,
        id: { value: atributoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AtributoFormDefaults {
    return {
      id: null,
      marcado: false,
      auxiliar: false,
    };
  }
}
