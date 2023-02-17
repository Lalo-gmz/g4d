import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { IAtributo } from 'app/entities/atributo/atributo.model';

export interface IAtributoFuncionalidad {
  id: number;
  marcado?: boolean | null;
  valor?: string | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
  atributo?: Pick<IAtributo, 'id'> | null;
}

export type NewAtributoFuncionalidad = Omit<IAtributoFuncionalidad, 'id'> & { id: null };
