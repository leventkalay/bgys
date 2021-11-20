import { IVarlikKategorisi } from 'app/entities/varlik-kategorisi/varlik-kategorisi.model';
import { ISurec } from 'app/entities/surec/surec.model';
import { IPersonel } from 'app/entities/personel/personel.model';
import { IRisk } from 'app/entities/risk/risk.model';
import { Seviye } from 'app/entities/enumerations/seviye.model';
import { Siniflandirma } from 'app/entities/enumerations/siniflandirma.model';
import { Onay } from 'app/entities/enumerations/onay.model';
import { Durumu } from 'app/entities/enumerations/durumu.model';

export interface IVarlik {
  id?: number;
  adi?: string;
  yeri?: string | null;
  aciklama?: string | null;
  gizlilik?: Seviye;
  butunluk?: Seviye;
  erisebilirlik?: Seviye;
  siniflandirma?: Siniflandirma | null;
  onayDurumu?: Onay | null;
  durumu?: Durumu | null;
  kategoriRiskleri?: boolean | null;
  kategori?: IVarlikKategorisi | null;
  surec?: ISurec | null;
  ilkSahibi?: IPersonel | null;
  ikinciSahibi?: IPersonel | null;
  risks?: IRisk[] | null;
}

export class Varlik implements IVarlik {
  constructor(
    public id?: number,
    public adi?: string,
    public yeri?: string | null,
    public aciklama?: string | null,
    public gizlilik?: Seviye,
    public butunluk?: Seviye,
    public erisebilirlik?: Seviye,
    public siniflandirma?: Siniflandirma | null,
    public onayDurumu?: Onay | null,
    public durumu?: Durumu | null,
    public kategoriRiskleri?: boolean | null,
    public kategori?: IVarlikKategorisi | null,
    public surec?: ISurec | null,
    public ilkSahibi?: IPersonel | null,
    public ikinciSahibi?: IPersonel | null,
    public risks?: IRisk[] | null
  ) {
    this.kategoriRiskleri = this.kategoriRiskleri ?? false;
  }
}

export function getVarlikIdentifier(varlik: IVarlik): number | undefined {
  return varlik.id;
}
