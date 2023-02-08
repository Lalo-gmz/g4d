export interface IEstatusFuncionalidad {
  id: number;
  nombre?: string | null;
  prioridad?: number | null;
}

export type NewEstatusFuncionalidad = Omit<IEstatusFuncionalidad, 'id'> & { id: null };
