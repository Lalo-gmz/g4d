import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IParticipacionProyecto, NewParticipacionProyecto } from '../participacion-proyecto.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParticipacionProyecto for edit and NewParticipacionProyectoFormGroupInput for create.
 */
type ParticipacionProyectoFormGroupInput = IParticipacionProyecto | PartialWithRequiredKeyOf<NewParticipacionProyecto>;

type ParticipacionProyectoFormDefaults = Pick<NewParticipacionProyecto, 'id'>;

type ParticipacionProyectoFormGroupContent = {
  id: FormControl<IParticipacionProyecto['id'] | NewParticipacionProyecto['id']>;
  user: FormControl<IParticipacionProyecto['user']>;
  proyecto: FormControl<IParticipacionProyecto['proyecto']>;
  rol: FormControl<IParticipacionProyecto['rol']>;
};

export type ParticipacionProyectoFormGroup = FormGroup<ParticipacionProyectoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParticipacionProyectoFormService {
  createParticipacionProyectoFormGroup(
    participacionProyecto: ParticipacionProyectoFormGroupInput = { id: null }
  ): ParticipacionProyectoFormGroup {
    const participacionProyectoRawValue = {
      ...this.getFormDefaults(),
      ...participacionProyecto,
    };
    return new FormGroup<ParticipacionProyectoFormGroupContent>({
      id: new FormControl(
        { value: participacionProyectoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      user: new FormControl(participacionProyectoRawValue.user),
      proyecto: new FormControl(participacionProyectoRawValue.proyecto),
      rol: new FormControl(participacionProyectoRawValue.rol),
    });
  }

  getParticipacionProyecto(form: ParticipacionProyectoFormGroup): IParticipacionProyecto | NewParticipacionProyecto {
    return form.getRawValue() as IParticipacionProyecto | NewParticipacionProyecto;
  }

  resetForm(form: ParticipacionProyectoFormGroup, participacionProyecto: ParticipacionProyectoFormGroupInput): void {
    const participacionProyectoRawValue = { ...this.getFormDefaults(), ...participacionProyecto };
    form.reset(
      {
        ...participacionProyectoRawValue,
        id: { value: participacionProyectoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ParticipacionProyectoFormDefaults {
    return {
      id: null,
    };
  }
}
