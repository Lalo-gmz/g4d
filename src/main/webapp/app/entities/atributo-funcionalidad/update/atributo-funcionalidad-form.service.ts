import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAtributoFuncionalidad, NewAtributoFuncionalidad } from '../atributo-funcionalidad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAtributoFuncionalidad for edit and NewAtributoFuncionalidadFormGroupInput for create.
 */
type AtributoFuncionalidadFormGroupInput = IAtributoFuncionalidad | PartialWithRequiredKeyOf<NewAtributoFuncionalidad>;

type AtributoFuncionalidadFormDefaults = Pick<NewAtributoFuncionalidad, 'id' | 'marcado'>;

type AtributoFuncionalidadFormGroupContent = {
  id: FormControl<IAtributoFuncionalidad['id'] | NewAtributoFuncionalidad['id']>;
  marcado: FormControl<IAtributoFuncionalidad['marcado']>;
  valor: FormControl<IAtributoFuncionalidad['valor']>;
  funcionalidad: FormControl<IAtributoFuncionalidad['funcionalidad']>;
  atributo: FormControl<IAtributoFuncionalidad['atributo']>;
  paraGitLab: FormControl<IAtributoFuncionalidad['paraGitLab']>;
};

export type AtributoFuncionalidadFormGroup = FormGroup<AtributoFuncionalidadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AtributoFuncionalidadFormService {
  createAtributoFuncionalidadFormGroup(
    atributoFuncionalidad: AtributoFuncionalidadFormGroupInput = { id: null }
  ): AtributoFuncionalidadFormGroup {
    const atributoFuncionalidadRawValue = {
      ...this.getFormDefaults(),
      ...atributoFuncionalidad,
    };
    return new FormGroup<AtributoFuncionalidadFormGroupContent>({
      id: new FormControl(
        { value: atributoFuncionalidadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      marcado: new FormControl(atributoFuncionalidadRawValue.marcado),
      valor: new FormControl(atributoFuncionalidadRawValue.valor),
      funcionalidad: new FormControl(atributoFuncionalidadRawValue.funcionalidad),
      atributo: new FormControl(atributoFuncionalidadRawValue.atributo),
      paraGitLab: new FormControl(atributoFuncionalidadRawValue.paraGitLab),
    });
  }

  getAtributoFuncionalidad(form: AtributoFuncionalidadFormGroup): IAtributoFuncionalidad | NewAtributoFuncionalidad {
    return form.getRawValue() as IAtributoFuncionalidad | NewAtributoFuncionalidad;
  }

  resetForm(form: AtributoFuncionalidadFormGroup, atributoFuncionalidad: AtributoFuncionalidadFormGroupInput): void {
    const atributoFuncionalidadRawValue = { ...this.getFormDefaults(), ...atributoFuncionalidad };
    form.reset(
      {
        ...atributoFuncionalidadRawValue,
        id: { value: atributoFuncionalidadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AtributoFuncionalidadFormDefaults {
    return {
      id: null,
      marcado: false,
    };
  }
}
