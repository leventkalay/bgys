import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonel, getPersonelIdentifier } from '../personel.model';

export type EntityResponseType = HttpResponse<IPersonel>;
export type EntityArrayResponseType = HttpResponse<IPersonel[]>;

@Injectable({ providedIn: 'root' })
export class PersonelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personel: IPersonel): Observable<EntityResponseType> {
    return this.http.post<IPersonel>(this.resourceUrl, personel, { observe: 'response' });
  }

  update(personel: IPersonel): Observable<EntityResponseType> {
    return this.http.put<IPersonel>(`${this.resourceUrl}/${getPersonelIdentifier(personel) as number}`, personel, { observe: 'response' });
  }

  partialUpdate(personel: IPersonel): Observable<EntityResponseType> {
    return this.http.patch<IPersonel>(`${this.resourceUrl}/${getPersonelIdentifier(personel) as number}`, personel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonelToCollectionIfMissing(personelCollection: IPersonel[], ...personelsToCheck: (IPersonel | null | undefined)[]): IPersonel[] {
    const personels: IPersonel[] = personelsToCheck.filter(isPresent);
    if (personels.length > 0) {
      const personelCollectionIdentifiers = personelCollection.map(personelItem => getPersonelIdentifier(personelItem)!);
      const personelsToAdd = personels.filter(personelItem => {
        const personelIdentifier = getPersonelIdentifier(personelItem);
        if (personelIdentifier == null || personelCollectionIdentifiers.includes(personelIdentifier)) {
          return false;
        }
        personelCollectionIdentifiers.push(personelIdentifier);
        return true;
      });
      return [...personelsToAdd, ...personelCollection];
    }
    return personelCollection;
  }
}
