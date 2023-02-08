import dayjs from 'dayjs/esm';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';

export interface IIteracion {
  id: number;
  nombre?: string | null;
  inicio?: dayjs.Dayjs | null;
  fin?: dayjs.Dayjs | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
}

export type NewIteracion = Omit<IIteracion, 'id'> & { id: null };
