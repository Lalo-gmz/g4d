import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFuncionalidad, NewFuncionalidad } from '../funcionalidad.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFuncionalidad for edit and NewFuncionalidadFormGroupInput for create.
 */
type FuncionalidadFormGroupInput = IFuncionalidad | PartialWithRequiredKeyOf<NewFuncionalidad>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFuncionalidad | NewFuncionalidad> = Omit<T, 'creado' | 'modificado'> & {
  creado?: string | null;
  modificado?: string | null;
};

type FuncionalidadFormRawValue = FormValueOf<IFuncionalidad>;

type NewFuncionalidadFormRawValue = FormValueOf<NewFuncionalidad>;

type FuncionalidadFormDefaults = Pick<NewFuncionalidad, 'id' | 'creado' | 'modificado' | 'users'>;

type FuncionalidadFormGroupContent = {
  id: FormControl<FuncionalidadFormRawValue['id'] | NewFuncionalidad['id']>;
  nombre: FormControl<FuncionalidadFormRawValue['nombre']>;
  descripcion: FormControl<FuncionalidadFormRawValue['descripcion']>;
  urlGitLab: FormControl<FuncionalidadFormRawValue['urlGitLab']>;
  creado: FormControl<FuncionalidadFormRawValue['creado']>;
  modificado: FormControl<FuncionalidadFormRawValue['modificado']>;
  users: FormControl<FuncionalidadFormRawValue['users']>;
  estatusFuncionalidad: FormControl<FuncionalidadFormRawValue['estatusFuncionalidad']>;
  iteracion: FormControl<FuncionalidadFormRawValue['iteracion']>;
  prioridad: FormControl<FuncionalidadFormRawValue['prioridad']>;
};

export type FuncionalidadFormGroup = FormGroup<FuncionalidadFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FuncionalidadFormService {
  createFuncionalidadFormGroup(funcionalidad: FuncionalidadFormGroupInput = { id: null }): FuncionalidadFormGroup {
    const funcionalidadRawValue = this.convertFuncionalidadToFuncionalidadRawValue({
      ...this.getFormDefaults(),
      ...funcionalidad,
    });
    return new FormGroup<FuncionalidadFormGroupContent>({
      id: new FormControl(
        { value: funcionalidadRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(funcionalidadRawValue.nombre),
      descripcion: new FormControl(funcionalidadRawValue.descripcion),
      urlGitLab: new FormControl(funcionalidadRawValue.urlGitLab),
      creado: new FormControl(funcionalidadRawValue.creado),
      modificado: new FormControl(funcionalidadRawValue.modificado),
      users: new FormControl(funcionalidadRawValue.users ?? []),
      estatusFuncionalidad: new FormControl(funcionalidadRawValue.estatusFuncionalidad),
      iteracion: new FormControl(funcionalidadRawValue.iteracion),
      prioridad: new FormControl(funcionalidadRawValue.prioridad),
    });
  }

  getFuncionalidad(form: FuncionalidadFormGroup): IFuncionalidad | NewFuncionalidad {
    return this.convertFuncionalidadRawValueToFuncionalidad(form.getRawValue() as FuncionalidadFormRawValue | NewFuncionalidadFormRawValue);
  }

  resetForm(form: FuncionalidadFormGroup, funcionalidad: FuncionalidadFormGroupInput): void {
    const funcionalidadRawValue = this.convertFuncionalidadToFuncionalidadRawValue({ ...this.getFormDefaults(), ...funcionalidad });
    form.reset(
      {
        ...funcionalidadRawValue,
        id: { value: funcionalidadRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FuncionalidadFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creado: currentTime,
      modificado: currentTime,
      users: [],
    };
  }

  private convertFuncionalidadRawValueToFuncionalidad(
    rawFuncionalidad: FuncionalidadFormRawValue | NewFuncionalidadFormRawValue
  ): IFuncionalidad | NewFuncionalidad {
    return {
      ...rawFuncionalidad,
      creado: dayjs(rawFuncionalidad.creado, DATE_TIME_FORMAT),
      modificado: dayjs(rawFuncionalidad.modificado, DATE_TIME_FORMAT),
    };
  }

  private convertFuncionalidadToFuncionalidadRawValue(
    funcionalidad: IFuncionalidad | (Partial<NewFuncionalidad> & FuncionalidadFormDefaults)
  ): FuncionalidadFormRawValue | PartialWithRequiredKeyOf<NewFuncionalidadFormRawValue> {
    return {
      ...funcionalidad,
      creado: funcionalidad.creado ? funcionalidad.creado.format(DATE_TIME_FORMAT) : undefined,
      modificado: funcionalidad.modificado ? funcionalidad.modificado.format(DATE_TIME_FORMAT) : undefined,
      users: funcionalidad.users ?? [],
    };
  }
}
