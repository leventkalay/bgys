export interface IBirim {
  id?: number;
  adi?: string;
  soyadi?: string;
  ustBirim?: IBirim | null;
}

export class Birim implements IBirim {
  constructor(public id?: number, public adi?: string, public soyadi?: string, public ustBirim?: IBirim | null) {}
}

export function getBirimIdentifier(birim: IBirim): number | undefined {
  return birim.id;
}
