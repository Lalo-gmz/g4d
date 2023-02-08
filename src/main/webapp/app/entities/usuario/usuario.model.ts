import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { IRol } from 'app/entities/rol/rol.model';

export interface IUsuario {
  id: number;
  nombre?: string | null;
  idGitLab?: string | null;
  tokenIdentificacion?: string | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
  rol?: Pick<IRol, 'id'> | null;
}

export type NewUsuario = Omit<IUsuario, 'id'> & { id: null };
