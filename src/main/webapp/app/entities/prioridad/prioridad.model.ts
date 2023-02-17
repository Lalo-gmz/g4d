export interface IPrioridad {
  id: number;
  nombre?: string | null;
  prioridadNumerica?: number | null;
}

export type NewPrioridad = Omit<IPrioridad, 'id'> & { id: null };
