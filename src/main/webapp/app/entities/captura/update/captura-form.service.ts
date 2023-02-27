import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICaptura, NewCaptura } from '../captura.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICaptura for edit and NewCapturaFormGroupInput for create.
 */
type CapturaFormGroupInput = ICaptura | PartialWithRequiredKeyOf<NewCaptura>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICaptura | NewCaptura> = Omit<T, 'fecha'> & {
  fecha?: string | null;
};

type CapturaFormRawValue = FormValueOf<ICaptura>;

type NewCapturaFormRawValue = FormValueOf<NewCaptura>;

type CapturaFormDefaults = Pick<NewCaptura, 'id' | 'fecha'>;

type CapturaFormGroupContent = {
  id: FormControl<CapturaFormRawValue['id'] | NewCaptura['id']>;
  funcionalidades: FormControl<CapturaFormRawValue['funcionalidades']>;
  estatus: FormControl<CapturaFormRawValue['estatus']>;
  fecha: FormControl<CapturaFormRawValue['fecha']>;
  iteracion: FormControl<CapturaFormRawValue['iteracion']>;
};

export type CapturaFormGroup = FormGroup<CapturaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CapturaFormService {
  createCapturaFormGroup(captura: CapturaFormGroupInput = { id: null }): CapturaFormGroup {
    const capturaRawValue = this.convertCapturaToCapturaRawValue({
      ...this.getFormDefaults(),
      ...captura,
    });
    return new FormGroup<CapturaFormGroupContent>({
      id: new FormControl(
        { value: capturaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      funcionalidades: new FormControl(capturaRawValue.funcionalidades),
      estatus: new FormControl(capturaRawValue.estatus),
      fecha: new FormControl(capturaRawValue.fecha),
      iteracion: new FormControl(capturaRawValue.iteracion),
    });
  }

  getCaptura(form: CapturaFormGroup): ICaptura | NewCaptura {
    return this.convertCapturaRawValueToCaptura(form.getRawValue() as CapturaFormRawValue | NewCapturaFormRawValue);
  }

  resetForm(form: CapturaFormGroup, captura: CapturaFormGroupInput): void {
    const capturaRawValue = this.convertCapturaToCapturaRawValue({ ...this.getFormDefaults(), ...captura });
    form.reset(
      {
        ...capturaRawValue,
        id: { value: capturaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CapturaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fecha: currentTime,
    };
  }

  private convertCapturaRawValueToCaptura(rawCaptura: CapturaFormRawValue | NewCapturaFormRawValue): ICaptura | NewCaptura {
    return {
      ...rawCaptura,
      fecha: dayjs(rawCaptura.fecha, DATE_TIME_FORMAT),
    };
  }

  private convertCapturaToCapturaRawValue(
    captura: ICaptura | (Partial<NewCaptura> & CapturaFormDefaults)
  ): CapturaFormRawValue | PartialWithRequiredKeyOf<NewCapturaFormRawValue> {
    return {
      ...captura,
      fecha: captura.fecha ? captura.fecha.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
