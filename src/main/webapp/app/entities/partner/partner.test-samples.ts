import { IPartner, NewPartner } from './partner.model';

export const sampleWithRequiredData: IPartner = {
  id: 86899,
};

export const sampleWithPartialData: IPartner = {
  id: 70191,
  name: 'Plaza cyan customized',
  region: 'encompassing 1080p product',
};

export const sampleWithFullData: IPartner = {
  id: 38777,
  name: 'Cross-group Cotton XSS',
  type: 'connecting lavender',
  region: 'Chair',
  country: 'Madagascar',
  status: 'Wooden',
};

export const sampleWithNewData: NewPartner = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
