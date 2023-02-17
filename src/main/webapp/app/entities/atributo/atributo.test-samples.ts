import { IAtributo, NewAtributo } from './atributo.model';

export const sampleWithRequiredData: IAtributo = {
  id: 18874,
};

export const sampleWithPartialData: IAtributo = {
  id: 38506,
  nombre: 'Increible',
};

export const sampleWithFullData: IAtributo = {
  id: 43211,
  nombre: 'Seguridada Murcia',
};

export const sampleWithNewData: NewAtributo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
