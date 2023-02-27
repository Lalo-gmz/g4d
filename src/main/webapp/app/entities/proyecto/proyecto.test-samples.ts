import dayjs from 'dayjs/esm';

import { IProyecto, NewProyecto } from './proyecto.model';

export const sampleWithRequiredData: IProyecto = {
  id: 99891,
  idProyectoGitLab: 'overriding',
};

export const sampleWithPartialData: IProyecto = {
  id: 44689,
  idProyectoGitLab: 'circuit Lugar Increible',
  modificado: dayjs('2023-02-24T16:18'),
};

export const sampleWithFullData: IProyecto = {
  id: 89357,
  nombre: 'Buckinghamshire auxiliary',
  idProyectoGitLab: 'invoice',
  creado: dayjs('2023-02-24T11:21'),
  modificado: dayjs('2023-02-23T17:40'),
};

export const sampleWithNewData: NewProyecto = {
  idProyectoGitLab: 'éxito',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
