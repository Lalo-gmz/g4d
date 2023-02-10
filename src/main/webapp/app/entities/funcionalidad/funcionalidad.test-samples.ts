import dayjs from 'dayjs/esm';

import { IFuncionalidad, NewFuncionalidad } from './funcionalidad.model';

export const sampleWithRequiredData: IFuncionalidad = {
  id: 43878,
};

export const sampleWithPartialData: IFuncionalidad = {
  id: 72695,
  urlGitLab: 'extend Card',
  fechaEntrega: dayjs('2023-02-08'),
  modificado: dayjs('2023-02-08T23:12'),
};

export const sampleWithFullData: IFuncionalidad = {
  id: 55122,
  nombre: 'override',
  descripcion: 'Funcionario',
  urlGitLab: 'Adelante',
  fechaEntrega: dayjs('2023-02-08'),
  creado: dayjs('2023-02-09T04:20'),
  modificado: dayjs('2023-02-08T13:16'),
};

export const sampleWithNewData: NewFuncionalidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
