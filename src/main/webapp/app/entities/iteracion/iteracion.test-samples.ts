import dayjs from 'dayjs/esm';

import { IIteracion, NewIteracion } from './iteracion.model';

export const sampleWithRequiredData: IIteracion = {
  id: 472,
  nombre: 'Acero wireless',
};

export const sampleWithPartialData: IIteracion = {
  id: 18694,
  nombre: 'Gorro',
  inicio: dayjs('2023-02-08'),
  fin: dayjs('2023-02-07'),
};

export const sampleWithFullData: IIteracion = {
  id: 30504,
  nombre: 'Tajikistan Nacional',
  inicio: dayjs('2023-02-07'),
  fin: dayjs('2023-02-07'),
};

export const sampleWithNewData: NewIteracion = {
  nombre: 'Parafarmacia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
