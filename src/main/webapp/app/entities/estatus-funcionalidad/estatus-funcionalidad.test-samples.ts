import { IEstatusFuncionalidad, NewEstatusFuncionalidad } from './estatus-funcionalidad.model';

export const sampleWithRequiredData: IEstatusFuncionalidad = {
  id: 6040,
};

export const sampleWithPartialData: IEstatusFuncionalidad = {
  id: 96372,
};

export const sampleWithFullData: IEstatusFuncionalidad = {
  id: 66896,
  nombre: 'online Castilla-La',
};

export const sampleWithNewData: NewEstatusFuncionalidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
