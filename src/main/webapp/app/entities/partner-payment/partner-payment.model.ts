import dayjs from 'dayjs/esm';
import { IContract } from 'app/entities/contract/contract.model';
import { IPartner } from 'app/entities/partner/partner.model';
import { ICompany } from 'app/entities/company/company.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IPartnerPayment {
  id: number;
  invoice?: string | null;
  totalAmount?: number | null;
  tax?: number | null;
  on?: dayjs.Dayjs | null;
  status?: PaymentStatus | null;
  reason?: string | null;
  contract?: Pick<IContract, 'id'> | null;
  partner?: Pick<IPartner, 'id'> | null;
  company?: Pick<ICompany, 'id'> | null;
}

export type NewPartnerPayment = Omit<IPartnerPayment, 'id'> & { id: null };
