import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITehdit, getTehditIdentifier } from '../tehdit.model';

export type EntityResponseType = HttpResponse<ITehdit>;
export type EntityArrayResponseType = HttpResponse<ITehdit[]>;

@Injectable({ providedIn: 'root' })
export class TehditService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tehdits');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tehdit: ITehdit): Observable<EntityResponseType> {
    return this.http.post<ITehdit>(this.resourceUrl, tehdit, { observe: 'response' });
  }

  update(tehdit: ITehdit): Observable<EntityResponseType> {
    return this.http.put<ITehdit>(`${this.resourceUrl}/${getTehditIdentifier(tehdit) as number}`, tehdit, { observe: 'response' });
  }

  partialUpdate(tehdit: ITehdit): Observable<EntityResponseType> {
    return this.http.patch<ITehdit>(`${this.resourceUrl}/${getTehditIdentifier(tehdit) as number}`, tehdit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITehdit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITehdit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTehditToCollectionIfMissing(tehditCollection: ITehdit[], ...tehditsToCheck: (ITehdit | null | undefined)[]): ITehdit[] {
    const tehdits: ITehdit[] = tehditsToCheck.filter(isPresent);
    if (tehdits.length > 0) {
      const tehditCollectionIdentifiers = tehditCollection.map(tehditItem => getTehditIdentifier(tehditItem)!);
      const tehditsToAdd = tehdits.filter(tehditItem => {
        const tehditIdentifier = getTehditIdentifier(tehditItem);
        if (tehditIdentifier == null || tehditCollectionIdentifiers.includes(tehditIdentifier)) {
          return false;
        }
        tehditCollectionIdentifiers.push(tehditIdentifier);
        return true;
      });
      return [...tehditsToAdd, ...tehditCollection];
    }
    return tehditCollection;
  }
}
