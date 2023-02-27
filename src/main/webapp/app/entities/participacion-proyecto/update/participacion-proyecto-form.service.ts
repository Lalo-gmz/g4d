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

type ParticipacionProyectoFormDefaults = Pick<NewParticipacionProyecto, 'id' | 'esAdmin'>;

type ParticipacionProyectoFormGroupContent = {
  id: FormControl<IParticipacionProyecto['id'] | NewParticipacionProyecto['id']>;
  esAdmin: FormControl<IParticipacionProyecto['esAdmin']>;
  usuario: FormControl<IParticipacionProyecto['usuario']>;
  proyecto: FormControl<IParticipacionProyecto['proyecto']>;
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
      esAdmin: new FormControl(participacionProyectoRawValue.esAdmin),
      usuario: new FormControl(participacionProyectoRawValue.usuario),
      proyecto: new FormControl(participacionProyectoRawValue.proyecto),
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
      esAdmin: false,
    };
  }
}
