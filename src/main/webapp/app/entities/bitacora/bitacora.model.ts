import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';

export interface IBitacora {
  id: number;
  accion?: string | null;
  creado?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
}

export type NewBitacora = Omit<IBitacora, 'id'> & { id: null };
