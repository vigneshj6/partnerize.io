import { IPartner } from 'app/entities/partner/partner.model';
import { ICompany } from 'app/entities/company/company.model';

export interface ICustomer {
  id: number;
  name?: string | null;
  partner?: Pick<IPartner, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
