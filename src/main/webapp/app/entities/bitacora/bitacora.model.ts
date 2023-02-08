import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IProyecto } from 'app/entities/proyecto/proyecto.model';

export interface IBitacora {
  id: number;
  tabla?: string | null;
  accion?: string | null;
  creado?: dayjs.Dayjs | null;
  usuario?: Pick<IUsuario, 'id'> | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
}

export type NewBitacora = Omit<IBitacora, 'id'> & { id: null };
