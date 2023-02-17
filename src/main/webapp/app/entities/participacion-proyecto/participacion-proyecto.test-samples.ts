import { IParticipacionProyecto, NewParticipacionProyecto } from './participacion-proyecto.model';

export const sampleWithRequiredData: IParticipacionProyecto = {
  id: 32071,
};

export const sampleWithPartialData: IParticipacionProyecto = {
  id: 29913,
};

export const sampleWithFullData: IParticipacionProyecto = {
  id: 79016,
};

export const sampleWithNewData: NewParticipacionProyecto = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
