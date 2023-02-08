import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 19585,
  nombre: 'cross-platform',
  idGitLab: 'intranet Cuentas Berkshire',
};

export const sampleWithPartialData: IUsuario = {
  id: 87653,
  nombre: 'auxiliary a Oficial',
  idGitLab: 'Bielorusia',
};

export const sampleWithFullData: IUsuario = {
  id: 61003,
  nombre: 'e-business',
  idGitLab: 'arquitectura orquestar Implementado',
  tokenIdentificacion: 'East IB Dollar',
};

export const sampleWithNewData: NewUsuario = {
  nombre: 'middleware',
  idGitLab: 'Senior Verde',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
