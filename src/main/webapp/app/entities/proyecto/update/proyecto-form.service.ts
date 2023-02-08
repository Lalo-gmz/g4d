import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type ProyectoFormDefaults = Pick<NewProyecto, 'id'>;

type ProyectoFormGroupContent = {
  id: FormControl<IProyecto['id'] | NewProyecto['id']>;
  nombre: FormControl<IProyecto['nombre']>;
  idProyectoGitLab: FormControl<IProyecto['idProyectoGitLab']>;
};

export type ProyectoFormGroup = FormGroup<ProyectoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProyectoFormService {
  createProyectoFormGroup(proyecto: ProyectoFormGroupInput = { id: null }): ProyectoFormGroup {
    const proyectoRawValue = {
      ...this.getFormDefaults(),
      ...proyecto,
    };
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
    });
  }

  getProyecto(form: ProyectoFormGroup): IProyecto | NewProyecto {
    return form.getRawValue() as IProyecto | NewProyecto;
  }

  resetForm(form: ProyectoFormGroup, proyecto: ProyectoFormGroupInput): void {
    const proyectoRawValue = { ...this.getFormDefaults(), ...proyecto };
    form.reset(
      {
        ...proyectoRawValue,
        id: { value: proyectoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProyectoFormDefaults {
    return {
      id: null,
    };
  }
}
