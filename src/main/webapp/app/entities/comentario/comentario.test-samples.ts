import dayjs from 'dayjs/esm';

import { IComentario, NewComentario } from './comentario.model';

export const sampleWithRequiredData: IComentario = {
  id: 63345,
  mensaje: 'Extendido synthesize reinvent',
};

export const sampleWithPartialData: IComentario = {
  id: 26999,
  mensaje: 'real-time',
  modificado: dayjs('2023-02-15T06:56'),
};

export const sampleWithFullData: IComentario = {
  id: 70349,
  mensaje: 'Seychelles Parafarmacia',
  creado: dayjs('2023-02-14T20:06'),
  modificado: dayjs('2023-02-14T22:10'),
};

export const sampleWithNewData: NewComentario = {
  mensaje: 'alarm Increible Mascotas',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
