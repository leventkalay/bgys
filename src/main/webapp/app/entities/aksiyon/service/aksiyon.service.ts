import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAksiyon, getAksiyonIdentifier } from '../aksiyon.model';

export type EntityResponseType = HttpResponse<IAksiyon>;
export type EntityArrayResponseType = HttpResponse<IAksiyon[]>;

@Injectable({ providedIn: 'root' })
export class AksiyonService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/aksiyons');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(aksiyon: IAksiyon): Observable<EntityResponseType> {
    return this.http.post<IAksiyon>(this.resourceUrl, aksiyon, { observe: 'response' });
  }

  update(aksiyon: IAksiyon): Observable<EntityResponseType> {
    return this.http.put<IAksiyon>(`${this.resourceUrl}/${getAksiyonIdentifier(aksiyon) as number}`, aksiyon, { observe: 'response' });
  }

  partialUpdate(aksiyon: IAksiyon): Observable<EntityResponseType> {
    return this.http.patch<IAksiyon>(`${this.resourceUrl}/${getAksiyonIdentifier(aksiyon) as number}`, aksiyon, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAksiyon>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAksiyon[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAksiyonToCollectionIfMissing(aksiyonCollection: IAksiyon[], ...aksiyonsToCheck: (IAksiyon | null | undefined)[]): IAksiyon[] {
    const aksiyons: IAksiyon[] = aksiyonsToCheck.filter(isPresent);
    if (aksiyons.length > 0) {
      const aksiyonCollectionIdentifiers = aksiyonCollection.map(aksiyonItem => getAksiyonIdentifier(aksiyonItem)!);
      const aksiyonsToAdd = aksiyons.filter(aksiyonItem => {
        const aksiyonIdentifier = getAksiyonIdentifier(aksiyonItem);
        if (aksiyonIdentifier == null || aksiyonCollectionIdentifiers.includes(aksiyonIdentifier)) {
          return false;
        }
        aksiyonCollectionIdentifiers.push(aksiyonIdentifier);
        return true;
      });
      return [...aksiyonsToAdd, ...aksiyonCollection];
    }
    return aksiyonCollection;
  }
}
