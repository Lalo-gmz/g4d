import dayjs from 'dayjs/esm';

import { ICaptura, NewCaptura } from './captura.model';

export const sampleWithRequiredData: ICaptura = {
  id: 78304,
};

export const sampleWithPartialData: ICaptura = {
  id: 58748,
  funcionalidades: 'interfaces',
  estatus: 'Avon Jefe capacitor',
  fecha: dayjs('2023-02-24T13:01'),
};

export const sampleWithFullData: ICaptura = {
  id: 52474,
  funcionalidades: 'a Implementación',
  estatus: 'Patatas Rincón Librería',
  fecha: dayjs('2023-02-23T18:39'),
};

export const sampleWithNewData: NewCaptura = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
