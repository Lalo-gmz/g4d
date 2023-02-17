import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';

export interface IBitacora {
  id: number;
  tabla?: string | null;
  accion?: string | null;
  creado?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
}

export type NewBitacora = Omit<IBitacora, 'id'> & { id: null };
