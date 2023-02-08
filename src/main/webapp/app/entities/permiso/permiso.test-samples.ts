import { IPermiso, NewPermiso } from './permiso.model';

export const sampleWithRequiredData: IPermiso = {
  id: 77515,
};

export const sampleWithPartialData: IPermiso = {
  id: 90485,
};

export const sampleWithFullData: IPermiso = {
  id: 38747,
  nombre: 'Camiseta',
};

export const sampleWithNewData: NewPermiso = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
