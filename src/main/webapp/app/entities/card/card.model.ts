export interface ICard {
  id: number;
  token?: string | null;
  auth?: string | null;
  key?: string | null;
  account?: string | null;
}

export type NewCard = Omit<ICard, 'id'> & { id: null };
