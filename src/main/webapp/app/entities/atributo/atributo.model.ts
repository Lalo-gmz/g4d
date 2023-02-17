export interface IAtributo {
  id: number;
  nombre?: string | null;
}

export type NewAtributo = Omit<IAtributo, 'id'> & { id: null };
