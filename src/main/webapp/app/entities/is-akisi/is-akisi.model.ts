import * as dayjs from 'dayjs';
import { IPersonel } from 'app/entities/personel/personel.model';

export interface IIsAkisi {
  id?: number;
  konu?: string | null;
  aciklama?: string | null;
  sonTarih?: dayjs.Dayjs | null;
  personel?: IPersonel | null;
}

export class IsAkisi implements IIsAkisi {
  constructor(
    public id?: number,
    public konu?: string | null,
    public aciklama?: string | null,
    public sonTarih?: dayjs.Dayjs | null,
    public personel?: IPersonel | null
  ) {}
}

export function getIsAkisiIdentifier(isAkisi: IIsAkisi): number | undefined {
  return isAkisi.id;
}
