import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBitacora, NewBitacora } from '../bitacora.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBitacora for edit and NewBitacoraFormGroupInput for create.
 */
type BitacoraFormGroupInput = IBitacora | PartialWithRequiredKeyOf<NewBitacora>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBitacora | NewBitacora> = Omit<T, 'creado'> & {
  creado?: string | null;
};

type BitacoraFormRawValue = FormValueOf<IBitacora>;

type NewBitacoraFormRawValue = FormValueOf<NewBitacora>;

type BitacoraFormDefaults = Pick<NewBitacora, 'id' | 'creado'>;

type BitacoraFormGroupContent = {
  id: FormControl<BitacoraFormRawValue['id'] | NewBitacora['id']>;
  tabla: FormControl<BitacoraFormRawValue['tabla']>;
  accion: FormControl<BitacoraFormRawValue['accion']>;
  creado: FormControl<BitacoraFormRawValue['creado']>;
  usuario: FormControl<BitacoraFormRawValue['usuario']>;
  proyecto: FormControl<BitacoraFormRawValue['proyecto']>;
};

export type BitacoraFormGroup = FormGroup<BitacoraFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BitacoraFormService {
  createBitacoraFormGroup(bitacora: BitacoraFormGroupInput = { id: null }): BitacoraFormGroup {
    const bitacoraRawValue = this.convertBitacoraToBitacoraRawValue({
      ...this.getFormDefaults(),
      ...bitacora,
    });
    return new FormGroup<BitacoraFormGroupContent>({
      id: new FormControl(
        { value: bitacoraRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      tabla: new FormControl(bitacoraRawValue.tabla, {
        validators: [Validators.required],
      }),
      accion: new FormControl(bitacoraRawValue.accion, {
        validators: [Validators.required],
      }),
      creado: new FormControl(bitacoraRawValue.creado, {
        validators: [Validators.required],
      }),
      usuario: new FormControl(bitacoraRawValue.usuario),
      proyecto: new FormControl(bitacoraRawValue.proyecto),
    });
  }

  getBitacora(form: BitacoraFormGroup): IBitacora | NewBitacora {
    return this.convertBitacoraRawValueToBitacora(form.getRawValue() as BitacoraFormRawValue | NewBitacoraFormRawValue);
  }

  resetForm(form: BitacoraFormGroup, bitacora: BitacoraFormGroupInput): void {
    const bitacoraRawValue = this.convertBitacoraToBitacoraRawValue({ ...this.getFormDefaults(), ...bitacora });
    form.reset(
      {
        ...bitacoraRawValue,
        id: { value: bitacoraRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BitacoraFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creado: currentTime,
    };
  }

  private convertBitacoraRawValueToBitacora(rawBitacora: BitacoraFormRawValue | NewBitacoraFormRawValue): IBitacora | NewBitacora {
    return {
      ...rawBitacora,
      creado: dayjs(rawBitacora.creado, DATE_TIME_FORMAT),
    };
  }

  private convertBitacoraToBitacoraRawValue(
    bitacora: IBitacora | (Partial<NewBitacora> & BitacoraFormDefaults)
  ): BitacoraFormRawValue | PartialWithRequiredKeyOf<NewBitacoraFormRawValue> {
    return {
      ...bitacora,
      creado: bitacora.creado ? bitacora.creado.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
