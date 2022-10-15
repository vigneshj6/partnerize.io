import dayjs from 'dayjs/esm';
import { IPartnerPayment } from 'app/entities/partner-payment/partner-payment.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface IPaymentEvent {
  id: number;
  status?: PaymentStatus | null;
  reason?: string | null;
  on?: dayjs.Dayjs | null;
  totalAmount?: number | null;
  invoice?: string | null;
  partnerPayment?: Pick<IPartnerPayment, 'id'> | null;
}

export type NewPaymentEvent = Omit<IPaymentEvent, 'id'> & { id: null };
