export interface ITehditKategorisi {
  id?: number;
  adi?: string | null;
  aciklama?: string | null;
}

export class TehditKategorisi implements ITehditKategorisi {
  constructor(public id?: number, public adi?: string | null, public aciklama?: string | null) {}
}

export function getTehditKategorisiIdentifier(tehditKategorisi: ITehditKategorisi): number | undefined {
  return tehditKategorisi.id;
}
