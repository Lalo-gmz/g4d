export interface IRol {
  id: number;
  nombre?: string | null;
}

export type NewRol = Omit<IRol, 'id'> & { id: null };
