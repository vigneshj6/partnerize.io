import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 32440,
};

export const sampleWithPartialData: ICompany = {
  id: 56879,
  region: 'zero Hawaii Handmade',
  status: 'Granite',
};

export const sampleWithFullData: ICompany = {
  id: 69135,
  name: 'Belize Estate',
  region: 'Devolved',
  country: 'Falkland Islands (Malvinas)',
  status: 'Chips Ford',
};

export const sampleWithNewData: NewCompany = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
