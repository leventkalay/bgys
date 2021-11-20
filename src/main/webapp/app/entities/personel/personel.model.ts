import { IUser } from 'app/entities/user/user.model';
import { IBirim } from 'app/entities/birim/birim.model';

export interface IPersonel {
  id?: number;
  adi?: string;
  soyadi?: string;
  internalUser?: IUser | null;
  birim?: IBirim | null;
}

export class Personel implements IPersonel {
  constructor(
    public id?: number,
    public adi?: string,
    public soyadi?: string,
    public internalUser?: IUser | null,
    public birim?: IBirim | null
  ) {}
}

export function getPersonelIdentifier(personel: IPersonel): number | undefined {
  return personel.id;
}
