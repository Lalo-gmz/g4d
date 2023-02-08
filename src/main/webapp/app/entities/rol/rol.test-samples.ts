import { IRol, NewRol } from './rol.model';

export const sampleWithRequiredData: IRol = {
  id: 20267,
};

export const sampleWithPartialData: IRol = {
  id: 47674,
  nombre: 'engineer Acero Interacciones',
};

export const sampleWithFullData: IRol = {
  id: 42851,
  nombre: 'optimize enterprise gestión',
};

export const sampleWithNewData: NewRol = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
