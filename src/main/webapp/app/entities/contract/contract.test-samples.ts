import dayjs from 'dayjs/esm';

import { ContractType } from 'app/entities/enumerations/contract-type.model';
import { BillingCycle } from 'app/entities/enumerations/billing-cycle.model';

import { IContract, NewContract } from './contract.model';

export const sampleWithRequiredData: IContract = {
  id: 90219,
};

export const sampleWithPartialData: IContract = {
  id: 93963,
  name: 'Kuwait',
  startAt: dayjs('2022-10-15T15:28'),
  billingCycle: BillingCycle['YEAR'],
};

export const sampleWithFullData: IContract = {
  id: 2093,
  name: 'parsing',
  type: ContractType['COMMISSION_ON_REVENUE'],
  startAt: dayjs('2022-10-15T06:20'),
  endAt: dayjs('2022-10-15T04:04'),
  billingCycle: BillingCycle['WEEK'],
  fixedRate: 16324,
  commisionPercent: 48698,
  commisionRate: 57955,
};

export const sampleWithNewData: NewContract = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
