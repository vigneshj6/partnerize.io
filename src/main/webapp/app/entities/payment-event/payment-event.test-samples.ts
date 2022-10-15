import dayjs from 'dayjs/esm';

import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

import { IPaymentEvent, NewPaymentEvent } from './payment-event.model';

export const sampleWithRequiredData: IPaymentEvent = {
  id: 38249,
};

export const sampleWithPartialData: IPaymentEvent = {
  id: 96859,
  status: PaymentStatus['CREATED'],
  reason: 'line',
  invoice: 'Chips Fresh',
};

export const sampleWithFullData: IPaymentEvent = {
  id: 86060,
  status: PaymentStatus['COMPLETED'],
  reason: 'bypassing',
  on: dayjs('2022-10-15T13:17'),
  totalAmount: 18345,
  invoice: 'Clothing',
};

export const sampleWithNewData: NewPaymentEvent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
