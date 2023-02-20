import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';

export interface IEtiqueta {
  id: number;
  nombre?: string | null;
  color?: string | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
  contrasteColor?: string | '#ffffff';
}

export type NewEtiqueta = Omit<IEtiqueta, 'id'> & { id: null };
