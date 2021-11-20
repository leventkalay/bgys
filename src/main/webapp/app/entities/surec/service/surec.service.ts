import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISurec, getSurecIdentifier } from '../surec.model';

export type EntityResponseType = HttpResponse<ISurec>;
export type EntityArrayResponseType = HttpResponse<ISurec[]>;

@Injectable({ providedIn: 'root' })
export class SurecService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/surecs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(surec: ISurec): Observable<EntityResponseType> {
    return this.http.post<ISurec>(this.resourceUrl, surec, { observe: 'response' });
  }

  update(surec: ISurec): Observable<EntityResponseType> {
    return this.http.put<ISurec>(`${this.resourceUrl}/${getSurecIdentifier(surec) as number}`, surec, { observe: 'response' });
  }

  partialUpdate(surec: ISurec): Observable<EntityResponseType> {
    return this.http.patch<ISurec>(`${this.resourceUrl}/${getSurecIdentifier(surec) as number}`, surec, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISurec>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISurec[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSurecToCollectionIfMissing(surecCollection: ISurec[], ...surecsToCheck: (ISurec | null | undefined)[]): ISurec[] {
    const surecs: ISurec[] = surecsToCheck.filter(isPresent);
    if (surecs.length > 0) {
      const surecCollectionIdentifiers = surecCollection.map(surecItem => getSurecIdentifier(surecItem)!);
      const surecsToAdd = surecs.filter(surecItem => {
        const surecIdentifier = getSurecIdentifier(surecItem);
        if (surecIdentifier == null || surecCollectionIdentifiers.includes(surecIdentifier)) {
          return false;
        }
        surecCollectionIdentifiers.push(surecIdentifier);
        return true;
      });
      return [...surecsToAdd, ...surecCollection];
    }
    return surecCollection;
  }
}
