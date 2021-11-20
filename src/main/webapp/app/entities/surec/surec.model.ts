export interface ISurec {
  id?: number;
  adi?: string | null;
  aciklama?: string | null;
}

export class Surec implements ISurec {
  constructor(public id?: number, public adi?: string | null, public aciklama?: string | null) {}
}

export function getSurecIdentifier(surec: ISurec): number | undefined {
  return surec.id;
}
