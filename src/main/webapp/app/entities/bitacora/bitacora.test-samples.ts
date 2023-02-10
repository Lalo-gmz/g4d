import dayjs from 'dayjs/esm';

import { IBitacora, NewBitacora } from './bitacora.model';

export const sampleWithRequiredData: IBitacora = {
  id: 52109,
  tabla: 'Comunidad Acero deliverables',
  accion: 'Unidos Inversor metrics',
  creado: dayjs('2023-02-08T23:41'),
};

export const sampleWithPartialData: IBitacora = {
  id: 78843,
  tabla: 'extensible',
  accion: 'back-end Amarillo',
  creado: dayjs('2023-02-08T17:30'),
};

export const sampleWithFullData: IBitacora = {
  id: 36857,
  tabla: 'EXE Togo Rojo',
  accion: 'Cedi Lev Expandido',
  creado: dayjs('2023-02-08T16:16'),
};

export const sampleWithNewData: NewBitacora = {
  tabla: 'override',
  accion: 'Puente',
  creado: dayjs('2023-02-09T05:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
