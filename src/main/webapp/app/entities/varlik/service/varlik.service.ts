import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVarlik, getVarlikIdentifier } from '../varlik.model';

export type EntityResponseType = HttpResponse<IVarlik>;
export type EntityArrayResponseType = HttpResponse<IVarlik[]>;

@Injectable({ providedIn: 'root' })
export class VarlikService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/varliks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(varlik: IVarlik): Observable<EntityResponseType> {
    return this.http.post<IVarlik>(this.resourceUrl, varlik, { observe: 'response' });
  }

  update(varlik: IVarlik): Observable<EntityResponseType> {
    return this.http.put<IVarlik>(`${this.resourceUrl}/${getVarlikIdentifier(varlik) as number}`, varlik, { observe: 'response' });
  }

  partialUpdate(varlik: IVarlik): Observable<EntityResponseType> {
    return this.http.patch<IVarlik>(`${this.resourceUrl}/${getVarlikIdentifier(varlik) as number}`, varlik, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVarlik>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVarlik[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVarlikToCollectionIfMissing(varlikCollection: IVarlik[], ...varliksToCheck: (IVarlik | null | undefined)[]): IVarlik[] {
    const varliks: IVarlik[] = varliksToCheck.filter(isPresent);
    if (varliks.length > 0) {
      const varlikCollectionIdentifiers = varlikCollection.map(varlikItem => getVarlikIdentifier(varlikItem)!);
      const varliksToAdd = varliks.filter(varlikItem => {
        const varlikIdentifier = getVarlikIdentifier(varlikItem);
        if (varlikIdentifier == null || varlikCollectionIdentifiers.includes(varlikIdentifier)) {
          return false;
        }
        varlikCollectionIdentifiers.push(varlikIdentifier);
        return true;
      });
      return [...varliksToAdd, ...varlikCollection];
    }
    return varlikCollection;
  }
}
