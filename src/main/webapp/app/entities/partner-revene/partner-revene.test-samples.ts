import { IPartnerRevene, NewPartnerRevene } from './partner-revene.model';

export const sampleWithRequiredData: IPartnerRevene = {
  id: 71908,
};

export const sampleWithPartialData: IPartnerRevene = {
  id: 89825,
};

export const sampleWithFullData: IPartnerRevene = {
  id: 6902,
};

export const sampleWithNewData: NewPartnerRevene = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
