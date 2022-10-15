export interface IPartnerRevene {
  id: number;
}

export type NewPartnerRevene = Omit<IPartnerRevene, 'id'> & { id: null };
