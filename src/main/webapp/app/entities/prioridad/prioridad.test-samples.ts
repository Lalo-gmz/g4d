import { IPrioridad, NewPrioridad } from './prioridad.model';

export const sampleWithRequiredData: IPrioridad = {
  id: 48065,
};

export const sampleWithPartialData: IPrioridad = {
  id: 65406,
  nombre: 'Ergonómico',
  prioridadNumerica: 49875,
};

export const sampleWithFullData: IPrioridad = {
  id: 83470,
  nombre: 'Open-source iterate',
  prioridadNumerica: 65416,
};

export const sampleWithNewData: NewPrioridad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
