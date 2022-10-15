import dayjs from 'dayjs/esm';

import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

import { IPartnerPayment, NewPartnerPayment } from './partner-payment.model';

export const sampleWithRequiredData: IPartnerPayment = {
  id: 62380,
};

export const sampleWithPartialData: IPartnerPayment = {
  id: 57720,
  status: PaymentStatus['WAIT_FOR_RESPONSE'],
  reason: 'Concrete Automotive encompassing',
};

export const sampleWithFullData: IPartnerPayment = {
  id: 64445,
  invoice: 'content system',
  totalAmount: 89350,
  tax: 64089,
  on: dayjs('2022-10-15T06:28'),
  status: PaymentStatus['FAILED'],
  reason: 'Fresh Florida XML',
};

export const sampleWithNewData: NewPartnerPayment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
