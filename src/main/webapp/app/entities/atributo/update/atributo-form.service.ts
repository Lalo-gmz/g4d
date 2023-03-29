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

type AtributoFormDefaults = Pick<NewAtributo, 'id'>;

type AtributoFormGroupContent = {
  id: FormControl<IAtributo['id'] | NewAtributo['id']>;
  nombre: FormControl<IAtributo['nombre']>;
  paraGitLab: FormControl<IAtributo['paraGitLab']>;
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
      paraGitLab: new FormControl(atributoRawValue.paraGitLab),
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
    };
  }
}
