import { IRol, NewRol } from './rol.model';

export const sampleWithRequiredData: IRol = {
  id: 20267,
  nombre: 'Senda calculating',
};

export const sampleWithPartialData: IRol = {
  id: 138,
  nombre: 'Interacciones metrics',
};

export const sampleWithFullData: IRol = {
  id: 8111,
  nombre: 'enterprise gestión',
};

export const sampleWithNewData: NewRol = {
  nombre: 'parse',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
