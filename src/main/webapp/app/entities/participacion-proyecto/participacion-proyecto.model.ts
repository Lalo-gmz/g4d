import { IUser } from 'app/entities/user/user.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { IRol } from 'app/entities/rol/rol.model';

export interface IParticipacionProyecto {
  id: number;
  user?: Pick<IUser, 'id'> | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
  rol?: Pick<IRol, 'id'> | null;
}

export type NewParticipacionProyecto = Omit<IParticipacionProyecto, 'id'> & { id: null };
