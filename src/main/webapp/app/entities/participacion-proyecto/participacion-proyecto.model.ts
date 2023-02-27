import { IUser } from 'app/entities/user/user.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';

export interface IParticipacionProyecto {
  id: number;
  esAdmin?: boolean | null;
  usuario?: Pick<IUser, 'id'> | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
}

export type NewParticipacionProyecto = Omit<IParticipacionProyecto, 'id'> & { id: null };
