import dayjs from 'dayjs/esm';
import { IProyecto } from '../proyecto/proyecto.model';

export interface IScript {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  nombreBoton?: string | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
  orden?: number | null;
}

export type NewScript = Omit<IScript, 'id'> & { id: null };
