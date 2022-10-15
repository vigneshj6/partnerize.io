import dayjs from 'dayjs/esm';
import { ICurrency } from 'app/entities/currency/currency.model';
import { IPartner } from 'app/entities/partner/partner.model';
import { ICompany } from 'app/entities/company/company.model';
import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { BillingCycle } from 'app/entities/enumerations/billing-cycle.model';

export interface IContract {
  id: number;
  name?: string | null;
  type?: ContractType | null;
  startAt?: dayjs.Dayjs | null;
  endAt?: dayjs.Dayjs | null;
  billingCycle?: BillingCycle | null;
  fixedRate?: number | null;
  commisionPercent?: number | null;
  commisionRate?: number | null;
  currency?: Pick<ICurrency, 'id'> | null;
  partner?: Pick<IPartner, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewContract = Omit<IContract, 'id'> & { id: null };
