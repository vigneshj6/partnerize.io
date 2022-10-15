import dayjs from 'dayjs/esm';

import { ISale, NewSale } from './sale.model';

export const sampleWithRequiredData: ISale = {
  id: 89826,
};

export const sampleWithPartialData: ISale = {
  id: 38856,
  invoice: 'withdrawal SDD Drive',
  tax: 76184,
  couponAmount: 81657,
  on: dayjs('2022-10-15T20:06'),
};

export const sampleWithFullData: ISale = {
  id: 21767,
  saleId: 'deposit Sharable',
  invoice: 'Island envisioneer',
  totalAmount: 90040,
  tax: 49567,
  couponCode: 'azure Sleek matrix',
  couponAmount: 16878,
  on: dayjs('2022-10-14T22:24'),
};

export const sampleWithNewData: NewSale = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
