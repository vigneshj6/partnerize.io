import { ICurrency, NewCurrency } from './currency.model';

export const sampleWithRequiredData: ICurrency = {
  id: 10224,
};

export const sampleWithPartialData: ICurrency = {
  id: 27384,
  name: 'open-source',
  code: 'invoice niches Cotton',
};

export const sampleWithFullData: ICurrency = {
  id: 34264,
  name: 'compressing Hat',
  code: 'Borders Azerbaijanian',
};

export const sampleWithNewData: NewCurrency = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
