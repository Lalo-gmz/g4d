import dayjs from 'dayjs/esm';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { IUser } from 'app/entities/user/user.model';

export interface IComentario {
  id: number;
  mensaje?: string | null;
  creado?: dayjs.Dayjs | null;
  modificado?: dayjs.Dayjs | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewComentario = Omit<IComentario, 'id'> & { id: null };
