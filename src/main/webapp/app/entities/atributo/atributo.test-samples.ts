import { IAtributo, NewAtributo } from './atributo.model';

export const sampleWithRequiredData: IAtributo = {
  id: 18874,
};

export const sampleWithPartialData: IAtributo = {
  id: 9460,
  nombre: 'alto',
};

export const sampleWithFullData: IAtributo = {
  id: 95872,
  nombre: 'Metical',
  marcado: true,
  auxiliar: true,
};

export const sampleWithNewData: NewAtributo = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
