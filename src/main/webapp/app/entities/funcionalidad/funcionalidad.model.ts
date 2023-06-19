import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';

export interface IFuncionalidad {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  urlGitLab?: string | null;
  enlaceGitLab?: string | null;
  // fechaEntrega?: dayjs.Dayjs | null;
  creado?: dayjs.Dayjs | null;
  modificado?: dayjs.Dayjs | null;
  users?: IUser[] | null;
  estatusFuncionalidad?: string | null;
  iteracion?: IIteracion | null;
  prioridad?: string | null;
  // etiquetas?: IEtiqueta[] | null;
}

export type NewFuncionalidad = Omit<IFuncionalidad, 'id'> & { id: null };
