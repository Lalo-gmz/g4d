import dayjs from 'dayjs/esm';

import { IFuncionalidad, NewFuncionalidad } from './funcionalidad.model';

export const sampleWithRequiredData: IFuncionalidad = {
  id: 43878,
};

export const sampleWithPartialData: IFuncionalidad = {
  id: 72695,
  urlGitLab: 'extend Card',
  fechaEntrega: dayjs('2023-02-24'),
  modificado: dayjs('2023-02-24T11:08'),
};

export const sampleWithFullData: IFuncionalidad = {
  id: 55122,
  nombre: 'override',
  descripcion: 'Funcionario',
  urlGitLab: 'Adelante',
  fechaEntrega: dayjs('2023-02-23'),
  creado: dayjs('2023-02-24T16:17'),
  modificado: dayjs('2023-02-24T01:12'),
};

export const sampleWithNewData: NewFuncionalidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
