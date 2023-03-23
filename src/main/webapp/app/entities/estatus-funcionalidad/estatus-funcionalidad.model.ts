export interface IEstatusFuncionalidad {
  id: number;
  nombre?: string | null;
  orden?: number | null;
}

export type NewEstatusFuncionalidad = Omit<IEstatusFuncionalidad, 'id'> & { id: null };
