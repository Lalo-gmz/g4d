import { EtiquetaVisual } from 'app/entities/enumerations/etiqueta-visual.model';

import { IConfiguracion, NewConfiguracion } from './configuracion.model';

export const sampleWithRequiredData: IConfiguracion = {
  id: 91478,
};

export const sampleWithPartialData: IConfiguracion = {
  id: 11719,
  clave: EtiquetaVisual['FUNCIONALIDAD'],
};

export const sampleWithFullData: IConfiguracion = {
  id: 64680,
  clave: EtiquetaVisual['ATRIBUTOS'],
  valor: 'parsing Gorro Loan',
};

export const sampleWithNewData: NewConfiguracion = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
