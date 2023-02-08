import dayjs from 'dayjs/esm';
import { IFuncionalidad } from 'app/entities/funcionalidad/funcionalidad.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IComentario {
  id: number;
  mensaje?: string | null;
  creado?: dayjs.Dayjs | null;
  modificado?: dayjs.Dayjs | null;
  funcionalidad?: Pick<IFuncionalidad, 'id'> | null;
  usuario?: Pick<IUsuario, 'id'> | null;
}

export type NewComentario = Omit<IComentario, 'id'> & { id: null };
