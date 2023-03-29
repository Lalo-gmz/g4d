export interface IAtributo {
  id: number;
  nombre?: string | null;
  paraGitLab?: boolean | null;
}

export type NewAtributo = Omit<IAtributo, 'id'> & { id: null };
