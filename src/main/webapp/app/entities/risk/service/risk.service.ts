import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRisk, getRiskIdentifier } from '../risk.model';

export type EntityResponseType = HttpResponse<IRisk>;
export type EntityArrayResponseType = HttpResponse<IRisk[]>;

@Injectable({ providedIn: 'root' })
export class RiskService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/risks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(risk: IRisk): Observable<EntityResponseType> {
    return this.http.post<IRisk>(this.resourceUrl, risk, { observe: 'response' });
  }

  update(risk: IRisk): Observable<EntityResponseType> {
    return this.http.put<IRisk>(`${this.resourceUrl}/${getRiskIdentifier(risk) as number}`, risk, { observe: 'response' });
  }

  partialUpdate(risk: IRisk): Observable<EntityResponseType> {
    return this.http.patch<IRisk>(`${this.resourceUrl}/${getRiskIdentifier(risk) as number}`, risk, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRisk>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRisk[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  query2(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRisk[]>(this.resourceUrl + '/findByOnayDurumu', { params: options, observe: 'response' });
  }
  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRiskToCollectionIfMissing(riskCollection: IRisk[], ...risksToCheck: (IRisk | null | undefined)[]): IRisk[] {
    const risks: IRisk[] = risksToCheck.filter(isPresent);
    if (risks.length > 0) {
      const riskCollectionIdentifiers = riskCollection.map(riskItem => getRiskIdentifier(riskItem)!);
      const risksToAdd = risks.filter(riskItem => {
        const riskIdentifier = getRiskIdentifier(riskItem);
        if (riskIdentifier == null || riskCollectionIdentifiers.includes(riskIdentifier)) {
          return false;
        }
        riskCollectionIdentifiers.push(riskIdentifier);
        return true;
      });
      return [...risksToAdd, ...riskCollection];
    }
    return riskCollection;
  }
}
