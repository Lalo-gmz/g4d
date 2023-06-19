import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IProyecto {
  id: number;
  nombre?: string | null;
  idProyectoGitLab?: string | null;
  creado?: dayjs.Dayjs | null;
  modificado?: dayjs.Dayjs | null;
  participantes?: Pick<IUser, 'id'>[] | null;
  enlaceGitLab?: string | null;
}

export type NewProyecto = Omit<IProyecto, 'id'> & { id: null };
