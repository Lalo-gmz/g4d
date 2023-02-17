import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IComentario, NewComentario } from '../comentario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IComentario for edit and NewComentarioFormGroupInput for create.
 */
type ComentarioFormGroupInput = IComentario | PartialWithRequiredKeyOf<NewComentario>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IComentario | NewComentario> = Omit<T, 'creado' | 'modificado'> & {
  creado?: string | null;
  modificado?: string | null;
};

type ComentarioFormRawValue = FormValueOf<IComentario>;

type NewComentarioFormRawValue = FormValueOf<NewComentario>;

type ComentarioFormDefaults = Pick<NewComentario, 'id' | 'creado' | 'modificado'>;

type ComentarioFormGroupContent = {
  id: FormControl<ComentarioFormRawValue['id'] | NewComentario['id']>;
  mensaje: FormControl<ComentarioFormRawValue['mensaje']>;
  creado: FormControl<ComentarioFormRawValue['creado']>;
  modificado: FormControl<ComentarioFormRawValue['modificado']>;
  funcionalidad: FormControl<ComentarioFormRawValue['funcionalidad']>;
  user: FormControl<ComentarioFormRawValue['user']>;
};

export type ComentarioFormGroup = FormGroup<ComentarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ComentarioFormService {
  createComentarioFormGroup(comentario: ComentarioFormGroupInput = { id: null }): ComentarioFormGroup {
    const comentarioRawValue = this.convertComentarioToComentarioRawValue({
      ...this.getFormDefaults(),
      ...comentario,
    });
    return new FormGroup<ComentarioFormGroupContent>({
      id: new FormControl(
        { value: comentarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      mensaje: new FormControl(comentarioRawValue.mensaje, {
        validators: [Validators.required],
      }),
      creado: new FormControl(comentarioRawValue.creado),
      modificado: new FormControl(comentarioRawValue.modificado),
      funcionalidad: new FormControl(comentarioRawValue.funcionalidad),
      user: new FormControl(comentarioRawValue.user),
    });
  }

  getComentario(form: ComentarioFormGroup): IComentario | NewComentario {
    return this.convertComentarioRawValueToComentario(form.getRawValue() as ComentarioFormRawValue | NewComentarioFormRawValue);
  }

  resetForm(form: ComentarioFormGroup, comentario: ComentarioFormGroupInput): void {
    const comentarioRawValue = this.convertComentarioToComentarioRawValue({ ...this.getFormDefaults(), ...comentario });
    form.reset(
      {
        ...comentarioRawValue,
        id: { value: comentarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ComentarioFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creado: currentTime,
      modificado: currentTime,
    };
  }

  private convertComentarioRawValueToComentario(
    rawComentario: ComentarioFormRawValue | NewComentarioFormRawValue
  ): IComentario | NewComentario {
    return {
      ...rawComentario,
      creado: dayjs(rawComentario.creado, DATE_TIME_FORMAT),
      modificado: dayjs(rawComentario.modificado, DATE_TIME_FORMAT),
    };
  }

  private convertComentarioToComentarioRawValue(
    comentario: IComentario | (Partial<NewComentario> & ComentarioFormDefaults)
  ): ComentarioFormRawValue | PartialWithRequiredKeyOf<NewComentarioFormRawValue> {
    return {
      ...comentario,
      creado: comentario.creado ? comentario.creado.format(DATE_TIME_FORMAT) : undefined,
      modificado: comentario.modificado ? comentario.modificado.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
