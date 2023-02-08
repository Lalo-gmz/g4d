import { IEstatusFuncionalidad, NewEstatusFuncionalidad } from './estatus-funcionalidad.model';

export const sampleWithRequiredData: IEstatusFuncionalidad = {
  id: 6040,
};

export const sampleWithPartialData: IEstatusFuncionalidad = {
  id: 66896,
  prioridad: 47158,
};

export const sampleWithFullData: IEstatusFuncionalidad = {
  id: 80006,
  nombre: 'Sopa Descentralizado',
  prioridad: 50694,
};

export const sampleWithNewData: NewEstatusFuncionalidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
