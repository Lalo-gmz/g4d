import { IProyecto } from 'app/entities/proyecto/proyecto.model';
import { EtiquetaVisual } from 'app/entities/enumerations/etiqueta-visual.model';

export interface IConfiguracion {
  id: number;
  clave?: EtiquetaVisual | null;
  valor?: string | null;
  proyecto?: Pick<IProyecto, 'id'> | null;
}

export type NewConfiguracion = Omit<IConfiguracion, 'id'> & { id: null };
