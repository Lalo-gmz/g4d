import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';

export interface IAtributo {
  id: number;
  nombre?: string | null;
  marcado?: boolean | null;
  auxiliar?: boolean | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
}

export type NewAtributo = Omit<IAtributo, 'id'> & { id: null };
