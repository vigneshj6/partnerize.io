import dayjs from 'dayjs/esm';
import { ICurrency } from 'app/entities/currency/currency.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IPartner } from 'app/entities/partner/partner.model';
import { ICompany } from 'app/entities/company/company.model';

export interface ISale {
  id: number;
  saleId?: string | null;
  invoice?: string | null;
  totalAmount?: number | null;
  tax?: number | null;
  couponCode?: string | null;
  couponAmount?: number | null;
  on?: dayjs.Dayjs | null;
  currency?: Pick<ICurrency, 'id'> | null;
  customer?: Pick<ICustomer, 'id'> | null;
  partner?: Pick<IPartner, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewSale = Omit<ISale, 'id'> & { id: null };
