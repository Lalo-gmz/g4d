import dayjs from 'dayjs/esm';

import { IBitacora, NewBitacora } from './bitacora.model';

export const sampleWithRequiredData: IBitacora = {
  id: 52109,
  tabla: 'Comunidad Acero deliverables',
  accion: 'Unidos Inversor metrics',
  creado: dayjs('2023-02-07T19:33'),
};

export const sampleWithPartialData: IBitacora = {
  id: 78843,
  tabla: 'extensible',
  accion: 'back-end Amarillo',
  creado: dayjs('2023-02-07T13:22'),
};

export const sampleWithFullData: IBitacora = {
  id: 36857,
  tabla: 'EXE Togo Rojo',
  accion: 'Cedi Lev Expandido',
  creado: dayjs('2023-02-07T12:08'),
};

export const sampleWithNewData: NewBitacora = {
  tabla: 'override',
  accion: 'Puente',
  creado: dayjs('2023-02-08T00:56'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
