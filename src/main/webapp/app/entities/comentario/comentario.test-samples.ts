import dayjs from 'dayjs/esm';

import { IComentario, NewComentario } from './comentario.model';

export const sampleWithRequiredData: IComentario = {
  id: 63345,
  mensaje: 'Extendido synthesize reinvent',
};

export const sampleWithPartialData: IComentario = {
  id: 26999,
  mensaje: 'real-time',
  modificado: dayjs('2023-02-24T06:26'),
};

export const sampleWithFullData: IComentario = {
  id: 70349,
  mensaje: 'Seychelles Parafarmacia',
  creado: dayjs('2023-02-23T19:36'),
  modificado: dayjs('2023-02-23T21:39'),
};

export const sampleWithNewData: NewComentario = {
  mensaje: 'alarm Increible Mascotas',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
