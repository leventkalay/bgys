import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITehditKategorisi, getTehditKategorisiIdentifier } from '../tehdit-kategorisi.model';

export type EntityResponseType = HttpResponse<ITehditKategorisi>;
export type EntityArrayResponseType = HttpResponse<ITehditKategorisi[]>;

@Injectable({ providedIn: 'root' })
export class TehditKategorisiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tehdit-kategorisis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tehditKategorisi: ITehditKategorisi): Observable<EntityResponseType> {
    return this.http.post<ITehditKategorisi>(this.resourceUrl, tehditKategorisi, { observe: 'response' });
  }

  update(tehditKategorisi: ITehditKategorisi): Observable<EntityResponseType> {
    return this.http.put<ITehditKategorisi>(
      `${this.resourceUrl}/${getTehditKategorisiIdentifier(tehditKategorisi) as number}`,
      tehditKategorisi,
      { observe: 'response' }
    );
  }

  partialUpdate(tehditKategorisi: ITehditKategorisi): Observable<EntityResponseType> {
    return this.http.patch<ITehditKategorisi>(
      `${this.resourceUrl}/${getTehditKategorisiIdentifier(tehditKategorisi) as number}`,
      tehditKategorisi,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITehditKategorisi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITehditKategorisi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTehditKategorisiToCollectionIfMissing(
    tehditKategorisiCollection: ITehditKategorisi[],
    ...tehditKategorisisToCheck: (ITehditKategorisi | null | undefined)[]
  ): ITehditKategorisi[] {
    const tehditKategorisis: ITehditKategorisi[] = tehditKategorisisToCheck.filter(isPresent);
    if (tehditKategorisis.length > 0) {
      const tehditKategorisiCollectionIdentifiers = tehditKategorisiCollection.map(
        tehditKategorisiItem => getTehditKategorisiIdentifier(tehditKategorisiItem)!
      );
      const tehditKategorisisToAdd = tehditKategorisis.filter(tehditKategorisiItem => {
        const tehditKategorisiIdentifier = getTehditKategorisiIdentifier(tehditKategorisiItem);
        if (tehditKategorisiIdentifier == null || tehditKategorisiCollectionIdentifiers.includes(tehditKategorisiIdentifier)) {
          return false;
        }
        tehditKategorisiCollectionIdentifiers.push(tehditKategorisiIdentifier);
        return true;
      });
      return [...tehditKategorisisToAdd, ...tehditKategorisiCollection];
    }
    return tehditKategorisiCollection;
  }
}
