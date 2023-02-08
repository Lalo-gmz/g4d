import { IEtiqueta, NewEtiqueta } from './etiqueta.model';

export const sampleWithRequiredData: IEtiqueta = {
  id: 4330,
  nombre: 'Sabroso',
};

export const sampleWithPartialData: IEtiqueta = {
  id: 52864,
  nombre: 'withdrawal Cataluña',
  prioridad: 70638,
};

export const sampleWithFullData: IEtiqueta = {
  id: 69775,
  nombre: 'holística',
  prioridad: 82857,
};

export const sampleWithNewData: NewEtiqueta = {
  nombre: 'Home',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
