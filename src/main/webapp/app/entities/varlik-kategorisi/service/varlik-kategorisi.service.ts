import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVarlikKategorisi, getVarlikKategorisiIdentifier } from '../varlik-kategorisi.model';

export type EntityResponseType = HttpResponse<IVarlikKategorisi>;
export type EntityArrayResponseType = HttpResponse<IVarlikKategorisi[]>;

@Injectable({ providedIn: 'root' })
export class VarlikKategorisiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/varlik-kategorisis');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(varlikKategorisi: IVarlikKategorisi): Observable<EntityResponseType> {
    return this.http.post<IVarlikKategorisi>(this.resourceUrl, varlikKategorisi, { observe: 'response' });
  }

  update(varlikKategorisi: IVarlikKategorisi): Observable<EntityResponseType> {
    return this.http.put<IVarlikKategorisi>(
      `${this.resourceUrl}/${getVarlikKategorisiIdentifier(varlikKategorisi) as number}`,
      varlikKategorisi,
      { observe: 'response' }
    );
  }

  partialUpdate(varlikKategorisi: IVarlikKategorisi): Observable<EntityResponseType> {
    return this.http.patch<IVarlikKategorisi>(
      `${this.resourceUrl}/${getVarlikKategorisiIdentifier(varlikKategorisi) as number}`,
      varlikKategorisi,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVarlikKategorisi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVarlikKategorisi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVarlikKategorisiToCollectionIfMissing(
    varlikKategorisiCollection: IVarlikKategorisi[],
    ...varlikKategorisisToCheck: (IVarlikKategorisi | null | undefined)[]
  ): IVarlikKategorisi[] {
    const varlikKategorisis: IVarlikKategorisi[] = varlikKategorisisToCheck.filter(isPresent);
    if (varlikKategorisis.length > 0) {
      const varlikKategorisiCollectionIdentifiers = varlikKategorisiCollection.map(
        varlikKategorisiItem => getVarlikKategorisiIdentifier(varlikKategorisiItem)!
      );
      const varlikKategorisisToAdd = varlikKategorisis.filter(varlikKategorisiItem => {
        const varlikKategorisiIdentifier = getVarlikKategorisiIdentifier(varlikKategorisiItem);
        if (varlikKategorisiIdentifier == null || varlikKategorisiCollectionIdentifiers.includes(varlikKategorisiIdentifier)) {
          return false;
        }
        varlikKategorisiCollectionIdentifiers.push(varlikKategorisiIdentifier);
        return true;
      });
      return [...varlikKategorisisToAdd, ...varlikKategorisiCollection];
    }
    return varlikKategorisiCollection;
  }
}
