export interface ICurrency {
  id: number;
  name?: string | null;
  code?: string | null;
}

export type NewCurrency = Omit<ICurrency, 'id'> & { id: null };
