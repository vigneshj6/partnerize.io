import { ICard } from 'app/entities/card/card.model';

export interface ICompany {
  id: number;
  name?: string | null;
  region?: string | null;
  country?: string | null;
  status?: string | null;
  card?: Pick<ICard, 'id'> | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
