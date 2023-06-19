import dayjs from 'dayjs/esm';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { IProyecto } from '../proyecto/proyecto.model';

export interface ICaptura {
  id: number;
  fecha?: dayjs.Dayjs | null;
  proyecto?: IProyecto | null;
  funcionalidadList?: IFuncionalidad | null;
}

export type NewCaptura = Omit<ICaptura, 'id'> & { id: null };
