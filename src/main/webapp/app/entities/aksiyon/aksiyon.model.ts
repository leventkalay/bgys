import { Onay } from 'app/entities/enumerations/onay.model';

export interface IAksiyon {
  id?: number;
  adi?: string;
  aciklama?: string | null;
  onayDurumu?: Onay | null;
}

export class Aksiyon implements IAksiyon {
  constructor(public id?: number, public adi?: string, public aciklama?: string | null, public onayDurumu?: Onay | null) {}
}

export function getAksiyonIdentifier(aksiyon: IAksiyon): number | undefined {
  return aksiyon.id;
}
