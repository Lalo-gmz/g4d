import { IRol } from 'app/entities/rol/rol.model';

export interface IPermiso {
  id: number;
  nombre?: string | null;
  rol?: Pick<IRol, 'id'> | null;
}

export type NewPermiso = Omit<IPermiso, 'id'> & { id: null };
