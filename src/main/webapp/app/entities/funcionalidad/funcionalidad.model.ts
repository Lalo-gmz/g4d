import dayjs from 'dayjs/esm';
import { IEstatusFuncionalidad } from 'app/entities/estatus-funcionalidad/estatus-funcionalidad.model';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';

export interface IFuncionalidad {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  urlGitLab?: string | null;
  fechaEntrega?: dayjs.Dayjs | null;
  creado?: dayjs.Dayjs | null;
  modificado?: dayjs.Dayjs | null;
  estatusFuncionalidad?: IEstatusFuncionalidad | null;
  iteracion?: Pick<IIteracion, 'id'> | null;
}

export type NewFuncionalidad = Omit<IFuncionalidad, 'id'> & { id: null };
