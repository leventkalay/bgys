import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBirim, getBirimIdentifier } from '../birim.model';

export type EntityResponseType = HttpResponse<IBirim>;
export type EntityArrayResponseType = HttpResponse<IBirim[]>;

@Injectable({ providedIn: 'root' })
export class BirimService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/birims');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(birim: IBirim): Observable<EntityResponseType> {
    return this.http.post<IBirim>(this.resourceUrl, birim, { observe: 'response' });
  }

  update(birim: IBirim): Observable<EntityResponseType> {
    return this.http.put<IBirim>(`${this.resourceUrl}/${getBirimIdentifier(birim) as number}`, birim, { observe: 'response' });
  }

  partialUpdate(birim: IBirim): Observable<EntityResponseType> {
    return this.http.patch<IBirim>(`${this.resourceUrl}/${getBirimIdentifier(birim) as number}`, birim, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBirim>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBirim[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBirimToCollectionIfMissing(birimCollection: IBirim[], ...birimsToCheck: (IBirim | null | undefined)[]): IBirim[] {
    const birims: IBirim[] = birimsToCheck.filter(isPresent);
    if (birims.length > 0) {
      const birimCollectionIdentifiers = birimCollection.map(birimItem => getBirimIdentifier(birimItem)!);
      const birimsToAdd = birims.filter(birimItem => {
        const birimIdentifier = getBirimIdentifier(birimItem);
        if (birimIdentifier == null || birimCollectionIdentifiers.includes(birimIdentifier)) {
          return false;
        }
        birimCollectionIdentifiers.push(birimIdentifier);
        return true;
      });
      return [...birimsToAdd, ...birimCollection];
    }
    return birimCollection;
  }
}
