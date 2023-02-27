import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProyecto, NewProyecto } from '../proyecto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProyecto for edit and NewProyectoFormGroupInput for create.
 */
type ProyectoFormGroupInput = IProyecto | PartialWithRequiredKeyOf<NewProyecto>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProyecto | NewProyecto> = Omit<T, 'creado' | 'modificado'> & {
  creado?: string | null;
  modificado?: string | null;
};

type ProyectoFormRawValue = FormValueOf<IProyecto>;

type NewProyectoFormRawValue = FormValueOf<NewProyecto>;

type ProyectoFormDefaults = Pick<NewProyecto, 'id' | 'creado' | 'modificado' | 'participantes'>;

type ProyectoFormGroupContent = {
  id: FormControl<ProyectoFormRawValue['id'] | NewProyecto['id']>;
  nombre: FormControl<ProyectoFormRawValue['nombre']>;
  idProyectoGitLab: FormControl<ProyectoFormRawValue['idProyectoGitLab']>;
  creado: FormControl<ProyectoFormRawValue['creado']>;
  modificado: FormControl<ProyectoFormRawValue['modificado']>;
  participantes: FormControl<ProyectoFormRawValue['participantes']>;
};

export type ProyectoFormGroup = FormGroup<ProyectoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProyectoFormService {
  createProyectoFormGroup(proyecto: ProyectoFormGroupInput = { id: null }): ProyectoFormGroup {
    const proyectoRawValue = this.convertProyectoToProyectoRawValue({
      ...this.getFormDefaults(),
      ...proyecto,
    });
    return new FormGroup<ProyectoFormGroupContent>({
      id: new FormControl(
        { value: proyectoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(proyectoRawValue.nombre),
      idProyectoGitLab: new FormControl(proyectoRawValue.idProyectoGitLab, {
        validators: [Validators.required],
      }),
      creado: new FormControl(proyectoRawValue.creado),
      modificado: new FormControl(proyectoRawValue.modificado),
      participantes: new FormControl(proyectoRawValue.participantes ?? []),
    });
  }

  getProyecto(form: ProyectoFormGroup): IProyecto | NewProyecto {
    return this.convertProyectoRawValueToProyecto(form.getRawValue() as ProyectoFormRawValue | NewProyectoFormRawValue);
  }

  resetForm(form: ProyectoFormGroup, proyecto: ProyectoFormGroupInput): void {
    const proyectoRawValue = this.convertProyectoToProyectoRawValue({ ...this.getFormDefaults(), ...proyecto });
    form.reset(
      {
        ...proyectoRawValue,
        id: { value: proyectoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProyectoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creado: currentTime,
      modificado: currentTime,
      participantes: [],
    };
  }

  private convertProyectoRawValueToProyecto(rawProyecto: ProyectoFormRawValue | NewProyectoFormRawValue): IProyecto | NewProyecto {
    return {
      ...rawProyecto,
      creado: dayjs(rawProyecto.creado, DATE_TIME_FORMAT),
      modificado: dayjs(rawProyecto.modificado, DATE_TIME_FORMAT),
    };
  }

  private convertProyectoToProyectoRawValue(
    proyecto: IProyecto | (Partial<NewProyecto> & ProyectoFormDefaults)
  ): ProyectoFormRawValue | PartialWithRequiredKeyOf<NewProyectoFormRawValue> {
    return {
      ...proyecto,
      creado: proyecto.creado ? proyecto.creado.format(DATE_TIME_FORMAT) : undefined,
      modificado: proyecto.modificado ? proyecto.modificado.format(DATE_TIME_FORMAT) : undefined,
      participantes: proyecto.participantes ?? [],
    };
  }
}
