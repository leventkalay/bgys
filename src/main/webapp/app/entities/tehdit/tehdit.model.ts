import { ITehditKategorisi } from 'app/entities/tehdit-kategorisi/tehdit-kategorisi.model';
import { Onay } from 'app/entities/enumerations/onay.model';

export interface ITehdit {
  id?: number;
  adi?: string;
  aciklama?: string | null;
  onayDurumu?: Onay | null;
  kategori?: ITehditKategorisi | null;
}

export class Tehdit implements ITehdit {
  constructor(
    public id?: number,
    public adi?: string,
    public aciklama?: string | null,
    public onayDurumu?: Onay | null,
    public kategori?: ITehditKategorisi | null
  ) {}
}

export function getTehditIdentifier(tehdit: ITehdit): number | undefined {
  return tehdit.id;
}
