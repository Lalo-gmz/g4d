import dayjs from 'dayjs/esm';
import { IIteracion } from 'app/entities/iteracion/iteracion.model';

export interface ICaptura {
  id: number;
  funcionalidades?: string | null;
  estatus?: string | null;
  fecha?: dayjs.Dayjs | null;
  iteracion?: Pick<IIteracion, 'id'> | null;
}

export type NewCaptura = Omit<ICaptura, 'id'> & { id: null };
