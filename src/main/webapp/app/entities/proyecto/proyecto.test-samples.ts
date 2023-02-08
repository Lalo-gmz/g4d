import { IProyecto, NewProyecto } from './proyecto.model';

export const sampleWithRequiredData: IProyecto = {
  id: 99891,
  idProyectoGitLab: 'overriding',
};

export const sampleWithPartialData: IProyecto = {
  id: 23768,
  idProyectoGitLab: 'Ronda Gerente Paseo',
};

export const sampleWithFullData: IProyecto = {
  id: 30950,
  nombre: 'Islas Cliente Unit',
  idProyectoGitLab: 'Avon',
};

export const sampleWithNewData: NewProyecto = {
  idProyectoGitLab: 'Chipre Marroquinería Bedfordshire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
