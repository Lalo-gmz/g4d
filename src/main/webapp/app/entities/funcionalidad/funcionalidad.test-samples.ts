import dayjs from 'dayjs/esm';

import { IFuncionalidad, NewFuncionalidad } from './funcionalidad.model';

export const sampleWithRequiredData: IFuncionalidad = {
  id: 43878,
};

export const sampleWithPartialData: IFuncionalidad = {
  id: 72695,
  urlGitLab: 'extend Card',
  fechaEntrega: dayjs('2023-02-07'),
  modificado: dayjs('2023-02-07T19:03'),
};

export const sampleWithFullData: IFuncionalidad = {
  id: 55122,
  nombre: 'override',
  descripcion: 'Funcionario',
  urlGitLab: 'Adelante',
  fechaEntrega: dayjs('2023-02-07'),
  creado: dayjs('2023-02-08T00:12'),
  modificado: dayjs('2023-02-07T09:07'),
};

export const sampleWithNewData: NewFuncionalidad = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
