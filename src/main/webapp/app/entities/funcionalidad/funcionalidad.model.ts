import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IEstatusFuncionalidad } from 'app/entities/estatus-funcionalidad/estatus-funcionalidad.model';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';
import { IPrioridad } from 'app/entities/prioridad/prioridad.model';

export interface IFuncionalidad {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  urlGitLab?: string | null;
  fechaEntrega?: dayjs.Dayjs | null;
  creado?: dayjs.Dayjs | null;
  modificado?: dayjs.Dayjs | null;
  users?: IUser[] | null;
  // estatusFuncionalidad?: Pick<IEstatusFuncionalidad, 'id'> | null;
  estatusFuncionalidad?: IEstatusFuncionalidad | null;
  iteracion?: IIteracion | null;
  prioridad?: IPrioridad | null;
}

export type NewFuncionalidad = Omit<IFuncionalidad, 'id'> & { id: null };
