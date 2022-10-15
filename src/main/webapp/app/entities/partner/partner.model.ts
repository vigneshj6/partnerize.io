import { ICard } from 'app/entities/card/card.model';
import { ICompany } from 'app/entities/company/company.model';

export interface IPartner {
  id: number;
  name?: string | null;
  type?: string | null;
  region?: string | null;
  country?: string | null;
  status?: string | null;
  card?: Pick<ICard, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewPartner = Omit<IPartner, 'id'> & { id: null };
