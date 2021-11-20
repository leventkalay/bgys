export interface IVarlikKategorisi {
  id?: number;
  adi?: string | null;
  aciklama?: string | null;
}

export class VarlikKategorisi implements IVarlikKategorisi {
  constructor(public id?: number, public adi?: string | null, public aciklama?: string | null) {}
}

export function getVarlikKategorisiIdentifier(varlikKategorisi: IVarlikKategorisi): number | undefined {
  return varlikKategorisi.id;
}
