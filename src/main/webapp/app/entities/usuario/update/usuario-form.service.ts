import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUsuario, NewUsuario } from '../usuario.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUsuario for edit and NewUsuarioFormGroupInput for create.
 */
type UsuarioFormGroupInput = IUsuario | PartialWithRequiredKeyOf<NewUsuario>;

type UsuarioFormDefaults = Pick<NewUsuario, 'id'>;

type UsuarioFormGroupContent = {
  id: FormControl<IUsuario['id'] | NewUsuario['id']>;
  nombre: FormControl<IUsuario['nombre']>;
  idGitLab: FormControl<IUsuario['idGitLab']>;
  tokenIdentificacion: FormControl<IUsuario['tokenIdentificacion']>;
  funcionalidad: FormControl<IUsuario['funcionalidad']>;
  proyecto: FormControl<IUsuario['proyecto']>;
  rol: FormControl<IUsuario['rol']>;
};

export type UsuarioFormGroup = FormGroup<UsuarioFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UsuarioFormService {
  createUsuarioFormGroup(usuario: UsuarioFormGroupInput = { id: null }): UsuarioFormGroup {
    const usuarioRawValue = {
      ...this.getFormDefaults(),
      ...usuario,
    };
    return new FormGroup<UsuarioFormGroupContent>({
      id: new FormControl(
        { value: usuarioRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(usuarioRawValue.nombre, {
        validators: [Validators.required],
      }),
      idGitLab: new FormControl(usuarioRawValue.idGitLab, {
        validators: [Validators.required],
      }),
      tokenIdentificacion: new FormControl(usuarioRawValue.tokenIdentificacion),
      funcionalidad: new FormControl(usuarioRawValue.funcionalidad),
      proyecto: new FormControl(usuarioRawValue.proyecto),
      rol: new FormControl(usuarioRawValue.rol),
    });
  }

  getUsuario(form: UsuarioFormGroup): IUsuario | NewUsuario {
    return form.getRawValue() as IUsuario | NewUsuario;
  }

  resetForm(form: UsuarioFormGroup, usuario: UsuarioFormGroupInput): void {
    const usuarioRawValue = { ...this.getFormDefaults(), ...usuario };
    form.reset(
      {
        ...usuarioRawValue,
        id: { value: usuarioRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UsuarioFormDefaults {
    return {
      id: null,
    };
  }
}
