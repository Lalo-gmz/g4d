import { IAtributoFuncionalidad, NewAtributoFuncionalidad } from './atributo-funcionalidad.model';

export const sampleWithRequiredData: IAtributoFuncionalidad = {
  id: 35774,
};

export const sampleWithPartialData: IAtributoFuncionalidad = {
  id: 75011,
  marcado: true,
  valor: 'hard Director',
};

export const sampleWithFullData: IAtributoFuncionalidad = {
  id: 42347,
  marcado: true,
  valor: 'Usabilidad Blanco cliente',
};

export const sampleWithNewData: NewAtributoFuncionalidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
