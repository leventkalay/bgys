import { IVarlik } from 'app/entities/varlik/varlik.model';
import { Onay } from 'app/entities/enumerations/onay.model';

export interface IRisk {
  id?: number;
  adi?: string;
  aciklama?: string | null;
  onayDurumu?: Onay | null;
  varliks?: IVarlik[] | null;
}

export class Risk implements IRisk {
  constructor(
    public id?: number,
    public adi?: string,
    public aciklama?: string | null,
    public onayDurumu?: Onay | null,
    public varliks?: IVarlik[] | null
  ) {}
}

export function getRiskIdentifier(risk: IRisk): number | undefined {
  return risk.id;
}
