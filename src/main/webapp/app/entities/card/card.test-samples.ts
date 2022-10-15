import { ICard, NewCard } from './card.model';

export const sampleWithRequiredData: ICard = {
  id: 57013,
};

export const sampleWithPartialData: ICard = {
  id: 78922,
  token: 'Refined',
  auth: 'Legacy',
  key: 'Avon Money',
  account: '06465154',
};

export const sampleWithFullData: ICard = {
  id: 1853,
  token: 'Republic)',
  auth: 'Movies next-generation Dynamic',
  key: 'Reduced',
  account: '98222817',
};

export const sampleWithNewData: NewCard = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
