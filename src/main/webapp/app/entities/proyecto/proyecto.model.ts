export interface IProyecto {
  id: number;
  nombre?: string | null;
  idProyectoGitLab?: string | null;
}

export type NewProyecto = Omit<IProyecto, 'id'> & { id: null };
