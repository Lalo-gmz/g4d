import { IProyecto } from 'app/entities/proyecto/proyecto.model';

export interface IRol {
  id: number;
  nombre?: string | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
}

export type NewRol = Omit<IRol, 'id'> & { id: null };
