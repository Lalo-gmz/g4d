import { IUser } from 'app/entities/user/user.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';

export interface IParticipacionProyecto {
  id: number;
  esAdmin?: boolean | null;
  usuario?: IUser | null;
  proyecto?: IProyecto | null;
}

export type NewParticipacionProyecto = Omit<IParticipacionProyecto, 'id'> & { id: null };
