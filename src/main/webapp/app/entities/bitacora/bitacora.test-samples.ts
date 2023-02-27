import dayjs from 'dayjs/esm';

import { IBitacora, NewBitacora } from './bitacora.model';

export const sampleWithRequiredData: IBitacora = {
  id: 52109,
  accion: 'Comunidad Acero deliverables',
  creado: dayjs('2023-02-24T00:52'),
};

export const sampleWithPartialData: IBitacora = {
  id: 54864,
  accion: 'Agente Práctico Dinar',
  creado: dayjs('2023-02-24T16:04'),
};

export const sampleWithFullData: IBitacora = {
  id: 36667,
  accion: 'copying Madera',
  creado: dayjs('2023-02-24T01:13'),
};

export const sampleWithNewData: NewBitacora = {
  accion: 'Berkshire',
  creado: dayjs('2023-02-23T20:12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
