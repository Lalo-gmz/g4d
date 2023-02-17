export interface IEstatusFuncionalidad {
  id: number;
  nombre?: string | null;
}

export type NewEstatusFuncionalidad = Omit<IEstatusFuncionalidad, 'id'> & { id: null };
